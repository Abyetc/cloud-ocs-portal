package com.cloud.ocs.portal.core.monitor.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloud.ocs.portal.common.cs.CloudStackApiRequest;
import com.cloud.ocs.portal.core.monitor.constant.MonitorApiName;
import com.cloud.ocs.portal.core.monitor.dto.HostDetail;
import com.cloud.ocs.portal.core.monitor.service.HostMonitorService;
import com.cloud.ocs.portal.core.monitor.service.VmMonitorService;
import com.cloud.ocs.portal.utils.UnitUtil;
import com.cloud.ocs.portal.utils.cs.CloudStackApiRequestSender;
import com.cloud.ocs.portal.utils.cs.CloudStackApiSignatureUtil;

/**
 * 监控主机service实现类
 * 
 * @author Wang Chao
 *
 * @date 2014-12-21 下午4:09:42
 *
 */
@Transactional
@Service
public class HostMonitorServiceImpl implements HostMonitorService {
	
	@Resource
	private VmMonitorService vmMonitorService;

	@Override
	public List<HostDetail> getHostDetailList(String zoneId) {
		CloudStackApiRequest request = new CloudStackApiRequest(MonitorApiName.MONITOR_API_LIST_HOST_DETAIL);
		request.addRequestParams("zoneid", zoneId);
		request.addRequestParams("type", "routing");
		request.addRequestParams("listall", "true");
		CloudStackApiSignatureUtil.generateSignature(request);
		String requestUrl = request.generateRequestURL();
		String response = CloudStackApiRequestSender.sendGetRequest(requestUrl);
		
		List<HostDetail> result = new ArrayList<HostDetail>();
		
		if (result != null) {
			JSONObject responseJsonObj = new JSONObject(response);
			JSONObject hostsListJsonObj = responseJsonObj.getJSONObject("listhostsresponse");
			if (hostsListJsonObj.has("host")) {
				JSONArray hostsJsonArrayObj = hostsListJsonObj.getJSONArray("host");
				if (hostsJsonArrayObj != null) {
					for (int i = 0; i < hostsJsonArrayObj.length(); i++) {
						HostDetail hostDetail = new HostDetail();
						JSONObject jsonObject = (JSONObject)hostsJsonArrayObj.get(i);
						hostDetail.setZoneId(jsonObject.getString("zoneid"));
						hostDetail.setZoneName(jsonObject.getString("zonename"));
						hostDetail.setPodId(jsonObject.getString("podid"));
						hostDetail.setPodName(jsonObject.getString("podname"));
						hostDetail.setClusterId(jsonObject.getString("clusterid"));
						hostDetail.setClusterName(jsonObject.getString("clustername"));
						hostDetail.setHostId(jsonObject.getString("id"));
						hostDetail.setHostName(jsonObject.getString("name"));
						hostDetail.setHypervisor(jsonObject.getString("hypervisor"));
						hostDetail.setIpAddress(jsonObject.getString("ipaddress"));
						hostDetail.setState(jsonObject.getString("state"));
						hostDetail.setType(jsonObject.getString("type"));
						hostDetail.setCreatedDate(jsonObject.getString("created"));
						hostDetail.setCpuNum(jsonObject.getInt("cpunumber"));
						hostDetail.setCupSpeed(UnitUtil.formatSizeFromHzToGHz(jsonObject.getLong("cpuspeed")));
						hostDetail.setCpuAllocated(jsonObject.getString("cpuallocated"));
						hostDetail.setMemoryTotal(UnitUtil.formatSizeUnit(jsonObject.getLong("memorytotal")));
						hostDetail.setMemoryAllocated(UnitUtil.formatSizeUnit(jsonObject.getLong("memoryallocated")));
						hostDetail.setMemoryAllocatedPercentage(UnitUtil.calculatePercentage(jsonObject.getLong("memoryallocated"), jsonObject.getLong("memorytotal")));
						hostDetail.setNetworkRead(UnitUtil.formatSizeUnit(jsonObject.getLong("networkkbsread")*1024));
						hostDetail.setNetworkWrite(UnitUtil.formatSizeUnit(jsonObject.getLong("networkkbswrite")*1024));
						hostDetail.setHaHost(jsonObject.getBoolean("hahost"));
						hostDetail.setVmNumOnHost(vmMonitorService.getVmNumOnHost(hostDetail.getHostId()));
						result.add(hostDetail);
					}
				}
			}
		}
		
		return result;
	}

	@Override
	public double getCurHostCpuUsagePercentage(String hostId) {
		CloudStackApiRequest request = new CloudStackApiRequest(MonitorApiName.MONITOR_API_LIST_HOST_DETAIL);
		request.addRequestParams("id", hostId);
		CloudStackApiSignatureUtil.generateSignature(request);
		String requestUrl = request.generateRequestURL();
		String response = CloudStackApiRequestSender.sendGetRequest(requestUrl);
		
		double result = 0.0;
		
		if (response != null) {
			JSONObject responseJsonObj = new JSONObject(response);
			JSONObject hostsListJsonObj = responseJsonObj.getJSONObject("listhostsresponse");
			if (hostsListJsonObj.has("host")) {
				JSONArray hostsJsonArrayObj = hostsListJsonObj.getJSONArray("host");
				if (hostsJsonArrayObj != null && hostsJsonArrayObj.length() != 0) {
					String resultStr = ((JSONObject)hostsJsonArrayObj.get(0)).getString("cpuused");
					result = Double.parseDouble(resultStr.substring(0, resultStr.indexOf('%')));
				}
			}
		}
		
		return result;
	}

	@Override
	public double getCurHostUsedMemory(String hostId) {
		
		CloudStackApiRequest request = new CloudStackApiRequest(MonitorApiName.MONITOR_API_LIST_HOST_DETAIL);
		request.addRequestParams("id", hostId);
		CloudStackApiSignatureUtil.generateSignature(request);
		String requestUrl = request.generateRequestURL();
		String response = CloudStackApiRequestSender.sendGetRequest(requestUrl);
		
		double result = 0.0;
		
		if (response != null) {
			JSONObject responseJsonObj = new JSONObject(response);
			JSONObject hostsListJsonObj = responseJsonObj.getJSONObject("listhostsresponse");
			if (hostsListJsonObj.has("host")) {
				JSONArray hostsJsonArrayObj = hostsListJsonObj.getJSONArray("host");
				if (hostsJsonArrayObj != null && hostsJsonArrayObj.length() != 0) {
					result = UnitUtil.formatSizeUnitToGB(((JSONObject)hostsJsonArrayObj.get(0)).getLong("memoryused"));
				}
			}
		}
		
		return result;
	}

}
