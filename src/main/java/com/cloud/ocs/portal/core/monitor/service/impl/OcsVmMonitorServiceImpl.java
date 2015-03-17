package com.cloud.ocs.portal.core.monitor.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloud.ocs.monitor.constant.MessageType;
import com.cloud.ocs.monitor.dao.ThroughputRecordDao;
import com.cloud.ocs.monitor.dto.MessageAverageProcessTimeWrapper;
import com.cloud.ocs.monitor.service.MessageRecordService;
import com.cloud.ocs.portal.common.bean.OcsVm;
import com.cloud.ocs.portal.common.bean.OcsVmForwardingPort;
import com.cloud.ocs.portal.common.cs.CloudStackApiRequest;
import com.cloud.ocs.portal.core.business.service.OcsVmForwardingPortService;
import com.cloud.ocs.portal.core.business.service.OcsVmService;
import com.cloud.ocs.portal.core.monitor.constant.MonitorApiName;
import com.cloud.ocs.portal.core.monitor.dto.MessageProcessTimeDto;
import com.cloud.ocs.portal.core.monitor.dto.MessageThroughputDto;
import com.cloud.ocs.portal.core.monitor.dto.RxbpsTxbpsDto;
import com.cloud.ocs.portal.core.monitor.dto.OcsVmDetail;
import com.cloud.ocs.portal.core.monitor.service.OcsVmMonitorService;
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
@Transactional(value="portal_em")
@Service
public class OcsVmMonitorServiceImpl implements OcsVmMonitorService {
	
	@Resource
	private OcsVmForwardingPortService vmForwardingPortService;
	
	@Resource
	private MessageRecordService messageRecordService;
	
	@Resource
	private ThroughputRecordDao throughputRecordDao;
	
	@Resource
	private OcsVmService ocsVmService;

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
	public List<OcsVmDetail> getVmDetailList(String hostId) {
		CloudStackApiRequest request = new CloudStackApiRequest(MonitorApiName.MONITOR_API_LIST_VM_DETAIL);
		request.addRequestParams("hostid", hostId);
//		request.addRequestParams("listall", "true");
		CloudStackApiSignatureUtil.generateSignature(request);
		String requestUrl = request.generateRequestURL();
		String response = HttpRequestSender.sendGetRequest(requestUrl);
		
		List<OcsVmDetail> result = new ArrayList<OcsVmDetail>();
		
		if (response != null) {
			JSONObject responseJsonObj = new JSONObject(response);
			JSONObject vmsListJsonObj = responseJsonObj.getJSONObject("listvirtualmachinesresponse");
			if (vmsListJsonObj.has("virtualmachine")) {
				JSONArray vmsJsonArrayObj = vmsListJsonObj.getJSONArray("virtualmachine");
				if (vmsJsonArrayObj != null) {
					for (int i = 0; i < vmsJsonArrayObj.length(); i++) {
						OcsVmDetail vmDetail = new OcsVmDetail();
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
	public Long getVmTotalCpuCapacity(String vmId) {
		CloudStackApiRequest request = new CloudStackApiRequest(MonitorApiName.MONITOR_API_LIST_VM_DETAIL);
		request.addRequestParams("id", vmId);
		CloudStackApiSignatureUtil.generateSignature(request);
		String requestUrl = request.generateRequestURL();
		String response = HttpRequestSender.sendGetRequest(requestUrl);
		
		Long result = 0L;
		
		if (response != null) {
			JSONObject responseJsonObj = new JSONObject(response);
			JSONObject vmsListJsonObj = responseJsonObj.getJSONObject("listvirtualmachinesresponse");
			if (vmsListJsonObj.has("virtualmachine")) {
				JSONArray vmsJsonArrayObj = vmsListJsonObj.getJSONArray("virtualmachine");
				if (vmsJsonArrayObj != null && vmsJsonArrayObj.length() != 0) {
					JSONObject jsonObject = (JSONObject)vmsJsonArrayObj.get(0);
					int cpuNum = jsonObject.getInt("cpunumber");
					long cpuSpeed = jsonObject.getLong("cpuspeed");
					result = cpuNum * cpuSpeed;
				}
			}
		}
		
		return result;
	}

//	@Override
//	public double getCurVmCpuUsagePercentageFromCs(String vmId) {
//		CloudStackApiRequest request = new CloudStackApiRequest(MonitorApiName.MONITOR_API_LIST_VM_DETAIL);
//		request.addRequestParams("id", vmId);
//		CloudStackApiSignatureUtil.generateSignature(request);
//		String requestUrl = request.generateRequestURL();
//		String response = HttpRequestSender.sendGetRequest(requestUrl);
//		
//		double result = 0.0;
//		
//		if (response != null) {
//			JSONObject responseJsonObj = new JSONObject(response);
//			JSONObject vmsListJsonObj = responseJsonObj.getJSONObject("listvirtualmachinesresponse");
//			if (vmsListJsonObj.has("virtualmachine")) {
//				JSONArray vmsJsonArrayObj = vmsListJsonObj.getJSONArray("virtualmachine");
//				if (vmsJsonArrayObj != null && vmsJsonArrayObj.length() != 0) {
//					String resultStr = ((JSONObject)vmsJsonArrayObj.get(0)).getString("cpuused");
//					result = Double.parseDouble(resultStr.substring(0, resultStr.indexOf('%')));
//				}
//			}
//		}
//		
//		return result;
//	}
	
	@Override
	public Double getVmCurCpuUsagePercentage(String vmId) {
		OcsVmForwardingPort vmForwardingPort = vmForwardingPortService.getVmForwardingPortByVmId(vmId);
		
		if (vmForwardingPort == null) {
			return null;
		}
		
		StringBuffer url = new StringBuffer();
		url.append("http://" + vmForwardingPort.getPublicIp() + ":" + vmForwardingPort.getMonitorPublicPort());
		url.append("/cloud-ocs-monitor-data-gatherer/gatherer/cpu/getCpuUsage");
		
		String json = RestfulClient.sendGetRequest(url.toString());
		Gson gson = new Gson();
		Double result = gson.fromJson(json, Double.class);
		if (result == null) {
			result = new Double(0.0);
		}
		
		return result*100.0;
	}

	@Override
	public Double getVmCurMemoryUsagePercentage(String vmId) {
		OcsVmForwardingPort vmForwardingPort = vmForwardingPortService.getVmForwardingPortByVmId(vmId);
		
		if (vmForwardingPort == null) {
			return null;
		}
		
		StringBuffer url = new StringBuffer();
		url.append("http://" + vmForwardingPort.getPublicIp() + ":" + vmForwardingPort.getMonitorPublicPort());
		url.append("/cloud-ocs-monitor-data-gatherer/gatherer/memory/getMemoryUsage");
		
		String json = RestfulClient.sendGetRequest(url.toString());
		Gson gson = new Gson();
		Double result = gson.fromJson(json, Double.class);
		if (result == null) {
			result = new Double(0.0);
		}
		
		return result;
	}

	@Override
	public RxbpsTxbpsDto getVmCurRxbpsTxbps(String vmId, String interfaceName) {
		OcsVmForwardingPort vmForwardingPort = vmForwardingPortService.getVmForwardingPortByVmId(vmId);
		
		if (vmForwardingPort == null) {
			return null;
		}
		
		StringBuffer url = new StringBuffer();
		url.append("http://" + vmForwardingPort.getPublicIp() + ":" + vmForwardingPort.getMonitorPublicPort());
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
	public List<List<Object>> getVmHistoryCpuUsagePercentage(String vmId,
			int dayOfMonth) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<List<Object>> getVmHistoryMemoryUsagePercentage(String vmId,
			int dayOfMonth) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<List<Object>> getVmHistoryRxbpsTxbps(String vmId,
			String interfaceName, int dayOfMonth) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getVmCurConcurrencyRequestNum(String vmId) {
		Long result = 0L;
		
		OcsVmForwardingPort vmForwardingPort = vmForwardingPortService.getVmForwardingPortByVmId(vmId);
		
		if (vmForwardingPort == null) {
			return 0L;
		}
		
		StringBuffer url = new StringBuffer();
		url.append("http://" + vmForwardingPort.getPublicIp() + ":" + vmForwardingPort.getMonitorPublicPort());
		url.append("/cloud-ocs-monitor-data-gatherer/gatherer/network/getRequestNum");
		
		String json = RestfulClient.sendGetRequest(url.toString());
		Gson gson = new Gson();
		result = gson.fromJson(json, Long.class);
		if (result == null) {
			return 0L;
		}
		
		return result;
	}
	
	@Override
	public MessageThroughputDto getVmCurMessageThroughput(String vmId) {
		OcsVm ocsVm = ocsVmService.getOcsVmByVmId(vmId);
		if (ocsVm == null) {
			return null;
		}
		String networkIp = ocsVm.getPublicIp();
		String vmIp = ocsVm.getPrivateIp();
		
		return throughputRecordDao.getVmCurMessageThroughput(networkIp, vmIp);
	}

	@Override
	public MessageProcessTimeDto getVmCurMessageAverageProcessTime(String vmId) {
		OcsVm ocsVm = ocsVmService.getOcsVmByVmId(vmId);
		if (ocsVm == null) {
			return null;
		}
		final String networkIp = ocsVm.getPublicIp();
		final String vmIp = ocsVm.getPrivateIp();
		MessageProcessTimeDto result = new MessageProcessTimeDto();
		
		ExecutorService executor = Executors.newCachedThreadPool();
		CompletionService<MessageAverageProcessTimeWrapper> comp = new ExecutorCompletionService<MessageAverageProcessTimeWrapper>(executor);
		List<MessageType> threeMessageType = MessageType.getAllMessageType();
		for (final MessageType messageType : threeMessageType) {
			comp.submit(new Callable<MessageAverageProcessTimeWrapper>() {
				public MessageAverageProcessTimeWrapper call() throws Exception {
					return messageRecordService.getMessageAverageProcessTimeOfVm(networkIp, vmIp, messageType);
				}
			});
		}
		executor.shutdown();
		int count = 0;
		while (count < threeMessageType.size()) {
			Future<MessageAverageProcessTimeWrapper> future = comp.poll();
			if (future == null) {
				continue;
			}
			else {
				try {
					MessageAverageProcessTimeWrapper messageProcessTimeWrapper = future.get();
					MessageType messageType = messageProcessTimeWrapper.getMessageType();
					if (messageType.equals(MessageType.ALL)) {
						result.setAllMessageProcessTime(messageProcessTimeWrapper.getProcessTime());
					}
					if (messageType.equals(MessageType.INITIAL)) {
						result.setMessageIProcessTime(messageProcessTimeWrapper.getProcessTime());
					}
					if (messageType.equals(MessageType.UPDATE)) {
						result.setMessageUProcessTime(messageProcessTimeWrapper.getProcessTime());
					}
					if (messageType.equals(MessageType.TERMINAL)) {
						result.setMessageTProcessTime(messageProcessTimeWrapper.getProcessTime());
					}
					count++;
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
		}
		
		return result;
	}

	@Override
	public Map<String, List<List<Object>>> getVmHistoryMessageAverageProcessTime(
			String vmId, Date date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, List<List<Object>>> getVmHistoryMessageThroughput(String vmId,
			Date date) {
		// TODO Auto-generated method stub
		return null;
	}

}
