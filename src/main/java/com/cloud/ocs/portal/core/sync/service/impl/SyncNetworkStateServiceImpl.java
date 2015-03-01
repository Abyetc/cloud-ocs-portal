package com.cloud.ocs.portal.core.sync.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloud.ocs.portal.common.bean.CityNetwork;
import com.cloud.ocs.portal.common.cs.CloudStackApiRequest;
import com.cloud.ocs.portal.common.dao.CityNetworkDao;
import com.cloud.ocs.portal.core.business.constant.NetworkState;
import com.cloud.ocs.portal.core.business.dto.PublicIpDto;
import com.cloud.ocs.portal.core.sync.constant.SyncApiName;
import com.cloud.ocs.portal.core.sync.service.SyncNetworkStateService;
import com.cloud.ocs.portal.properties.OcsVmProperties;
import com.cloud.ocs.portal.utils.cs.CloudStackApiSignatureUtil;
import com.cloud.ocs.portal.utils.http.HttpRequestSender;

/**
 * 调用CloudStack API将network状态同步到本地数据库的Service实现类
 *  
 * @author Wang Chao
 *
 * @date 2015-1-5 下午9:28:43
 *
 */
@Transactional(value="portal_em")
@Service
public class SyncNetworkStateServiceImpl implements SyncNetworkStateService{
	
	@Resource
	private CityNetworkDao cityNetworkDao; 

	public void syncNetworkState() {
		List<CityNetwork> networkListFromCs = this.getNetworkListFromCs();
		
		for (CityNetwork cityNetworkFromCs : networkListFromCs) {
			CityNetwork cityNetworkFromLocalDB = cityNetworkDao.findCityNetworkByNetworkId(cityNetworkFromCs.getNetworkId());
			//若本地数据库中无该网络，则跳过不同步
			if ( cityNetworkFromLocalDB == null) {
				continue;
			}
			cityNetworkFromCs.setId(cityNetworkFromLocalDB.getId());
			cityNetworkFromCs.setCityId(cityNetworkFromLocalDB.getCityId());
			cityNetworkFromCs.setRealmName(cityNetworkFromLocalDB.getRealmName());
			cityNetworkFromCs.setCreated(cityNetworkFromLocalDB.getCreated());
			cityNetworkDao.merge(cityNetworkFromCs);
		}
	}
	
	/**
	 * 通过CloudStack API获得network列表
	 * @return
	 */
	private List<CityNetwork> getNetworkListFromCs() {
		CloudStackApiRequest request = new CloudStackApiRequest(SyncApiName.SYNC_API_LIST_NETWORKS);
		request.addRequestParams("type", "Isolated");
		CloudStackApiSignatureUtil.generateSignature(request);
		String requestUrl = request.generateRequestURL();
		String response = HttpRequestSender.sendGetRequest(requestUrl);
		
		List<CityNetwork> result = new ArrayList<CityNetwork>();
		
		if (response != null) {
			JSONObject responseJsonObj = new JSONObject(response);
			JSONObject networksListJsonObj = responseJsonObj.getJSONObject("listnetworksresponse");
			if (networksListJsonObj.has("network")) {
				JSONArray networksJsonArrayObj = networksListJsonObj.getJSONArray("network");
				if (networksJsonArrayObj != null) {
					for (int i = 0; i < networksJsonArrayObj.length(); i++) {
						JSONObject jsonObj = (JSONObject)networksJsonArrayObj.get(i);
						CityNetwork network = new CityNetwork();
						network.setNetworkId(jsonObj.getString("id"));
						network.setNetworkName(jsonObj.getString("name"));
						network.setZoneId(jsonObj.getString("zoneid"));
						network.setNetworkOfferingId(jsonObj.getString("networkofferingid"));
						network.setServicePort(OcsVmProperties.getOcsVmEngineServicePort());
						if (jsonObj.has("vlan")) {
							network.setVlan(jsonObj.getString("vlan"));
						}
						network.setNetworkState(NetworkState.getCode(jsonObj.getString("state")));
						
						//network state为:Implemented,获取public Ip
						if (network.getNetworkState() == 3) {
							PublicIpDto publicIpDto = this.getPublicSourceNatIp(network.getNetworkId());
							if (publicIpDto != null) {
								network.setPublicIpId(publicIpDto.getPublicIpId());
								network.setPublicIp(publicIpDto.getPublicIpAddress());
							}
						}
						result.add(network);
					}
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 获取该network的source nat public IP并作为该network的公共服务IP
	 * @param networkId
	 * @return
	 */
	private PublicIpDto getPublicSourceNatIp(String networkId) {
		CloudStackApiRequest request = new CloudStackApiRequest(SyncApiName.SYNC_API_LIST_PUBLIC_IP);
		request.addRequestParams("associatednetworkid", networkId);
		request.addRequestParams("forvirtualnetwork", "true");
		request.addRequestParams("listall", "true");
		CloudStackApiSignatureUtil.generateSignature(request);
		String requestUrl = request.generateRequestURL();
		String response = HttpRequestSender.sendGetRequest(requestUrl);
		
		PublicIpDto result = null;
		
		if (response != null) {
			JSONObject responseJsonObj = new JSONObject(response);
			JSONObject publicIpsListJsonObj = responseJsonObj.getJSONObject("listpublicipaddressesresponse");
			if (publicIpsListJsonObj.has("publicipaddress")) {
				JSONArray publicIpsJsonArrayObj = publicIpsListJsonObj.getJSONArray("publicipaddress");
				if (publicIpsJsonArrayObj != null) {
					for (int i = 0; i < publicIpsJsonArrayObj.length(); i++) {
						JSONObject jsonObj = (JSONObject) publicIpsJsonArrayObj.get(i);
						if (jsonObj.getBoolean("issourcenat")) {
							result = new PublicIpDto();
							result.setPublicIpId(jsonObj.getString("id"));
							result.setPublicIpAddress(jsonObj.getString("ipaddress"));
							break;
						}
					}
				}
			}
		}
		
		return result;
	}
}
