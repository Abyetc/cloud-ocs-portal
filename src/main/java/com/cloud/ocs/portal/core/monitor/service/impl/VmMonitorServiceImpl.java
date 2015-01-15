package com.cloud.ocs.portal.core.monitor.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloud.ocs.portal.common.cs.CloudStackApiRequest;
import com.cloud.ocs.portal.core.business.bean.VmForwardingPort;
import com.cloud.ocs.portal.core.business.service.VmForwardingPortService;
import com.cloud.ocs.portal.core.monitor.constant.MonitorApiName;
import com.cloud.ocs.portal.core.monitor.dto.RxbpsTxbpsDto;
import com.cloud.ocs.portal.core.monitor.dto.VmDetail;
import com.cloud.ocs.portal.core.monitor.service.VmMonitorService;
import com.cloud.ocs.portal.utils.UnitUtil;
import com.cloud.ocs.portal.utils.cs.CloudStackApiSignatureUtil;
import com.cloud.ocs.portal.utils.http.HttpRequestSender;
import com.cloud.ocs.portal.utils.http.RestfulClient;
import com.google.gson.Gson;

/**
 * 监控VM service实现类
 * 
 * @author Wang Chao
 *
 * @date 2014-12-26 下午10:07:41
 *
 */
@Transactional
@Service
public class VmMonitorServiceImpl implements VmMonitorService {
	
	@Resource
	private VmForwardingPortService vmForwardingPortService;

	@Override
	public int getVmNumOnHost(String hostId) {
		CloudStackApiRequest request = new CloudStackApiRequest(MonitorApiName.MONITOR_API_LIST_VM_DETAIL);
		request.addRequestParams("hostid", hostId);
		request.addRequestParams("listall", "true");
		CloudStackApiSignatureUtil.generateSignature(request);
		String requestUrl = request.generateRequestURL();
		String response = HttpRequestSender.sendGetRequest(requestUrl);
		
		int result = 0;
		
		if (response != null) {
			JSONObject responseJsonObj = new JSONObject(response);
			JSONObject vmsListJsonObj = responseJsonObj.getJSONObject("listvirtualmachinesresponse");
			if (vmsListJsonObj.has("virtualmachine")) {
				JSONArray vmsJsonArrayObj = vmsListJsonObj.getJSONArray("virtualmachine");
				if (vmsJsonArrayObj != null) {
					result = vmsJsonArrayObj.length();
				}
			}
			
		}
		
		return result;
	}

	@Override
	public List<VmDetail> getVmDetailList(String hostId) {
		CloudStackApiRequest request = new CloudStackApiRequest(MonitorApiName.MONITOR_API_LIST_VM_DETAIL);
		request.addRequestParams("hostid", hostId);
		request.addRequestParams("listall", "true");
		CloudStackApiSignatureUtil.generateSignature(request);
		String requestUrl = request.generateRequestURL();
		String response = HttpRequestSender.sendGetRequest(requestUrl);
		
		List<VmDetail> result = new ArrayList<VmDetail>();
		
		if (response != null) {
			JSONObject responseJsonObj = new JSONObject(response);
			JSONObject vmsListJsonObj = responseJsonObj.getJSONObject("listvirtualmachinesresponse");
			if (vmsListJsonObj.has("virtualmachine")) {
				JSONArray vmsJsonArrayObj = vmsListJsonObj.getJSONArray("virtualmachine");
				if (vmsJsonArrayObj != null) {
					for (int i = 0; i < vmsJsonArrayObj.length(); i++) {
						VmDetail vmDetail = new VmDetail();
						JSONObject jsonObject = (JSONObject)vmsJsonArrayObj.get(i);
						vmDetail.setZoneId(jsonObject.getString("zoneid"));
						vmDetail.setZoneName(jsonObject.getString("zonename"));
						vmDetail.setHostId(jsonObject.getString("hostid"));
						vmDetail.setHostName(jsonObject.getString("hostname"));
						vmDetail.setVmId(jsonObject.getString("id"));
						vmDetail.setVmName(jsonObject.getString("name"));
						vmDetail.setInstanceName(jsonObject.getString("instancename"));
						vmDetail.setCreated(jsonObject.getString("created"));
						vmDetail.setTemplateName(jsonObject.getString("templatename"));
						vmDetail.setHypervisor(jsonObject.getString("hypervisor"));
						vmDetail.setState(jsonObject.getString("state"));
						vmDetail.setCpuNum(jsonObject.getInt("cpunumber"));
						vmDetail.setCupSpeed(UnitUtil.formatSizeFromHzToGHz(jsonObject.getLong("cpuspeed")));
						vmDetail.setMemory(UnitUtil.formatSizeUnit(jsonObject.getLong("memory")*1024*1024));
						vmDetail.setNetworkRead(UnitUtil.formatSizeUnit(jsonObject.getLong("networkkbsread")*1024));
						vmDetail.setNetworkWrite(UnitUtil.formatSizeUnit(jsonObject.getLong("networkkbswrite")*1024));
						vmDetail.setDiskRead(UnitUtil.formatSizeUnit(jsonObject.getLong("diskkbsread")*1024));
						vmDetail.setDiskWrite(UnitUtil.formatSizeUnit(jsonObject.getLong("diskkbswrite")*1024));
						vmDetail.setDiskIORead(jsonObject.getLong("diskioread"));
						vmDetail.setDiskIOWrite(jsonObject.getLong("diskiowrite"));
						vmDetail.setHaHost(jsonObject.getBoolean("haenable"));
						//网卡信息(默认取第一块网卡)
						if (jsonObject.has("nic")) {
							JSONObject nicJsonObj = (JSONObject)jsonObject.getJSONArray("nic").get(0);
							vmDetail.setNetworkName(nicJsonObj.getString("networkname"));
							vmDetail.setIpAddress(nicJsonObj.getString("ipaddress"));
							vmDetail.setIsolationUri(nicJsonObj.getString("isolationuri"));
						}
						result.add(vmDetail);
					}
				}
			}
		}
		
		return result;
	}

	@Override
	public double getCurVmCpuUsagePercentage(String vmId) {
		CloudStackApiRequest request = new CloudStackApiRequest(MonitorApiName.MONITOR_API_LIST_VM_DETAIL);
		request.addRequestParams("id", vmId);
		CloudStackApiSignatureUtil.generateSignature(request);
		String requestUrl = request.generateRequestURL();
		String response = HttpRequestSender.sendGetRequest(requestUrl);
		
		double result = 0.0;
		
		if (response != null) {
			JSONObject responseJsonObj = new JSONObject(response);
			JSONObject vmsListJsonObj = responseJsonObj.getJSONObject("listvirtualmachinesresponse");
			if (vmsListJsonObj.has("virtualmachine")) {
				JSONArray vmsJsonArrayObj = vmsListJsonObj.getJSONArray("virtualmachine");
				if (vmsJsonArrayObj != null && vmsJsonArrayObj.length() != 0) {
					String resultStr = ((JSONObject)vmsJsonArrayObj.get(0)).getString("cpuused");
					result = Double.parseDouble(resultStr.substring(0, resultStr.indexOf('%')));
				}
			}
		}
		
		return result;
	}

	@Override
	public RxbpsTxbpsDto getVmRxbpsTxbps(String vmId, String interfaceName) {
		VmForwardingPort vmForwardingPort = vmForwardingPortService.getVmForwardingPortByVmId(vmId);
		
		if (vmForwardingPort == null) {
			return null;
		}
		
		StringBuffer url = new StringBuffer();
		url.append("http://" + vmForwardingPort.getPublicIp() + ":" + vmForwardingPort.getPublicPort());
		url.append("/cloud-ocs-monitor-data-gatherer/gatherer/network/getRxbpsTxbps");
		url.append("?interfaceName=" + interfaceName);
		
		String json = RestfulClient.sendGetRequest(url.toString());
		Gson gson = new Gson();
		RxbpsTxbpsDto result = gson.fromJson(json, RxbpsTxbpsDto.class);
		if (result == null) {
			result = new RxbpsTxbpsDto();
		}

		return result;
	}

	@Override
	public Long getVmConcurrencyRequestNum(String vmId) {
		Long result = 0L;
		
		VmForwardingPort vmForwardingPort = vmForwardingPortService.getVmForwardingPortByVmId(vmId);
		
		if (vmForwardingPort == null) {
			return 0L;
		}
		
		StringBuffer url = new StringBuffer();
		url.append("http://" + vmForwardingPort.getPublicIp() + ":" + vmForwardingPort.getPublicPort());
		url.append("/cloud-ocs-monitor-data-gatherer/gatherer/network/getRequestNum");
		
		String json = RestfulClient.sendGetRequest(url.toString());
		Gson gson = new Gson();
		result = gson.fromJson(json, Long.class);
		if (result == null) {
			return 0L;
		}
		
		return result;
	}

}
