package com.cloud.ocs.portal.core.business.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloud.ocs.portal.common.bean.CityNetwork;
import com.cloud.ocs.portal.common.cs.CloudStackApiRequest;
import com.cloud.ocs.portal.common.cs.asyncjob.constant.AsyncJobStatus;
import com.cloud.ocs.portal.common.cs.asyncjob.dto.AsynJobResultDto;
import com.cloud.ocs.portal.common.cs.asyncjob.service.QueryAsyncJobResultService;
import com.cloud.ocs.portal.common.dao.CityNetworkDao;
import com.cloud.ocs.portal.common.dto.OperateObjectDto;
import com.cloud.ocs.portal.core.business.constant.BusinessApiName;
import com.cloud.ocs.portal.core.business.constant.NetworkState;
import com.cloud.ocs.portal.core.business.dto.AddCityNetworkDto;
import com.cloud.ocs.portal.core.business.dto.CityNetworkListDto;
import com.cloud.ocs.portal.core.business.service.CityNetworkService;
import com.cloud.ocs.portal.core.business.service.OcsVmService;
import com.cloud.ocs.portal.properties.OcsVmProperties;
import com.cloud.ocs.portal.utils.cs.CloudStackApiSignatureUtil;
import com.cloud.ocs.portal.utils.http.HttpRequestSender;

/**
 * 城市-网络service实现类
 * 
 * @author Wang Chao
 *
 * @date 2014-12-31 下午12:04:50
 *
 */
@Transactional(value="portal_em")
@Service
public class CityNetworkServiceImpl implements CityNetworkService {
	
	@Resource
	private CityNetworkDao cityNetworkDao;
	
	@Resource
	private OcsVmService networkVmService;
	
	@Override
	public CityNetwork getCityNetworkByNetworkId(String networkId) {
		
		return cityNetworkDao.findCityNetworkByNetworkId(networkId);
	}

	@Override
	public List<CityNetworkListDto> getCityNetworksList(Integer cityId) {
		List<CityNetworkListDto> result = null;
		List<CityNetwork> networkList = cityNetworkDao.findCityNetworksByCityId(cityId);
		
		if (networkList != null) {
			result = new ArrayList<CityNetworkListDto>();
			for (int i = 0; i < networkList.size(); i++) {
				CityNetworkListDto cityNetworkListDto = new CityNetworkListDto();
				CityNetwork network = networkList.get(i);
				cityNetworkListDto.setNetworkId(network.getNetworkId());
				cityNetworkListDto.setNetworkName(network.getNetworkName());
				cityNetworkListDto.setNetworkState(network.getNetworkState());
				cityNetworkListDto.setPublicIp(network.getPublicIp());
				cityNetworkListDto.setRealmName(network.getRealmName());
				cityNetworkListDto.setCreated(network.getCreated());
				Integer vmNum = networkVmService.getOcsVmsNumInInLBRule(network.getNetworkId());
				if (vmNum == null) {
					vmNum = 0;
				}
				cityNetworkListDto.setVmNum(vmNum);
				result.add(cityNetworkListDto);
			}
		}
		
		return result;
	}
	
	@Override
	public List<CityNetwork> getAllCityNetwork() {
		return cityNetworkDao.findAll();
	}
	
	@Override
	public List<String> getAllPublicIpsOfCity(Integer cityId) {
		return cityNetworkDao.findAllPublicIpsOfCity(cityId);
	}

	@Override
	public AddCityNetworkDto addCityNetwork(CityNetwork cityNetwork) {
		AddCityNetworkDto result = new AddCityNetworkDto();
		result.setCode(AddCityNetworkDto.ADD_NETWORK_CODE_ERROR); //初始默认先添加失败
		result.setMessage("Add City-Network Error.");
		
		this.sendAddingNetworkRequestToCS(cityNetwork);
		if (cityNetwork.getNetworkId() != null) { 
			//如果networkId不为null，则表示CloudStack创建成功
			result.setCode(AddCityNetworkDto.ADD_NETWORK_CODE_SUCCESS);
			result.setMessage("Add City-Network Success.");
			Date created = new Date();
			cityNetwork.setCreated(new Timestamp(created.getTime()));
			cityNetwork.setServicePort(OcsVmProperties.getOcsVmEngineServicePort());
			cityNetwork.setNetworkState(NetworkState.ALLOCATED.getCode());
			
			//持久化到本地数据库
			cityNetworkDao.persist(cityNetwork);
			if (cityNetwork.getId() != null) {  //持久化到自己的数据库成功
				result.setCityNetwork(cityNetwork);
				Integer vmNum = networkVmService.getOcsVmsNumInInLBRule(cityNetwork.getNetworkId());
				if (vmNum == null) {
					vmNum = 0;
				}
				result.setVmNum(vmNum);
				result.setIndex(cityNetworkDao.findCityNetworksByCityId(cityNetwork.getCityId()).size() + 1);
			}
			
			//添加该新增网络的出口规则(异步)
			sendCreatingEgressFirewallRuleAsyncRequestToCs(cityNetwork.getNetworkId());
		}
		
		return result;
	}
	
	@Override
	public OperateObjectDto removeCityNetwork(String networkId) {
		OperateObjectDto result = new OperateObjectDto();
		result.setCode(OperateObjectDto.OPERATE_OBJECT_CODE_ERROR); //初始化为默认不成功
		
		String jobId = this.sendRemoveNetworkRequestToCs(networkId);
		
		if (jobId == null) {
			result.setMessage("Send Removing Network Request to Cloudstack Error.");
			return result;
		}
		
		AsynJobResultDto asyncJobResult = QueryAsyncJobResultService.queryAsyncJobResult(jobId, "null");
		//每隔3秒发送一次请求，查询Job状态
		while (asyncJobResult != null && asyncJobResult.getJobStatus().getCode() == AsyncJobStatus.PENDING.getCode()) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			asyncJobResult = QueryAsyncJobResultService.queryAsyncJobResult(jobId, "null");
		}
		
		//Job执行失败
		if (asyncJobResult != null
				&& asyncJobResult.getJobStatus().getCode() == AsyncJobStatus.FAILED
						.getCode()) {
			result.setMessage("Failed to delete network");
			return result;
		}
		
		//Job执行成功
		if (asyncJobResult != null
				&& asyncJobResult.getJobStatus().getCode() == AsyncJobStatus.SUCCESS
						.getCode()) {
			result.setCode(OperateObjectDto.OPERATE_OBJECT_CODE_SUCCESS);
			result.setMessage("Removing Network Success.");
			
			//更新数据库中记录
			cityNetworkDao.removeByNetworkId(networkId);
		}
		
		return result;
	}
	
	@Override
	public Map<String, Integer> getNetworkIdCityIdMap() {
		List<CityNetwork> cityNetworkList = cityNetworkDao.findAll();
		Map<String, Integer> result = null;
		
		if (cityNetworkList != null) {
			result = new TreeMap<String, Integer>();
			for (CityNetwork cityNetwork : cityNetworkList) {
				result.put(cityNetwork.getNetworkId(), cityNetwork.getCityId());
			}
		}
		return result;
	}

	/**
	 * 发送 添加网络 的请求到CloudStack
	 * @return
	 */
	private void sendAddingNetworkRequestToCS(CityNetwork cityNetwork) {
		CloudStackApiRequest request = new CloudStackApiRequest(BusinessApiName.BUSINESS_API_ADD_NETWORK);
		request.addRequestParams("zoneid", cityNetwork.getZoneId());
		request.addRequestParams("networkofferingid", cityNetwork.getNetworkOfferingId());
		request.addRequestParams("name", cityNetwork.getNetworkName());
		request.addRequestParams("displaytext", cityNetwork.getNetworkName());
		CloudStackApiSignatureUtil.generateSignature(request);
		String requestUrl = request.generateRequestURL();
		String response = HttpRequestSender.sendGetRequest(requestUrl);
		
		if (response != null) {
			JSONObject responseJsonObj = new JSONObject(response);
			JSONObject resultJsonObj = responseJsonObj.getJSONObject("createnetworkresponse");
			if (resultJsonObj.has("network")) {
				cityNetwork.setNetworkId(resultJsonObj.getJSONObject("network").getString("id"));
			}
		}
	}
	
	/**
	 * 发送添加出口规则的异步请求到CloudStack
	 * @param networkId
	 * @return 异步Job的Id
	 */
	private String sendCreatingEgressFirewallRuleAsyncRequestToCs(String networkId) {
		CloudStackApiRequest request = new CloudStackApiRequest(BusinessApiName.BUSINESS_API_CREATE_EGRESS_FIREWALL_RULE);
		request.addRequestParams("networkid", networkId);
		request.addRequestParams("protocol", "all");
		request.addRequestParams("cidrlist", "0.0.0.0/0");
		CloudStackApiSignatureUtil.generateSignature(request);
		String requestUrl = request.generateRequestURL();
		String response = HttpRequestSender.sendGetRequest(requestUrl);
		
		String result = null;
		
		if (response != null) {
			JSONObject responseJsonObj = new JSONObject(response);
			JSONObject resultJsonObj = responseJsonObj.getJSONObject("createegressfirewallruleresponse");
			if (resultJsonObj.has("jobid")) {
				result = resultJsonObj.getString("jobid");
			}
		}
		
		return result;
	}
	
	/**
	 * 发送删除网络命令到CloudStack(异步请求)
	 * @param networkId
	 * @return
	 */
	private String sendRemoveNetworkRequestToCs(String networkId) {
		CloudStackApiRequest request = new CloudStackApiRequest(BusinessApiName.BUSINESS_API_REMOVE_NETWORK);
		request.addRequestParams("id", networkId);
		CloudStackApiSignatureUtil.generateSignature(request);
		String requestUrl = request.generateRequestURL();
		String response = HttpRequestSender.sendGetRequest(requestUrl);
		
		String result = null;
		
		if (response != null) {
			JSONObject responseJsonObj = new JSONObject(response);
			JSONObject resultJsonObj = responseJsonObj.getJSONObject("deletenetworkresponse");
			if (resultJsonObj.has("jobid")) {
				result = resultJsonObj.getString("jobid");
			}
		}
		
		return result;
	}

}
