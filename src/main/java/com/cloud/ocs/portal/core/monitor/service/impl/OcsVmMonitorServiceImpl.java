package com.cloud.ocs.portal.core.monitor.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
import com.cloud.ocs.portal.properties.OcsVmProperties;
import com.cloud.ocs.portal.utils.DateUtil;
import com.cloud.ocs.portal.utils.UnitUtil;
import com.cloud.ocs.portal.utils.cs.CloudStackApiSignatureUtil;
import com.cloud.ocs.portal.utils.http.HttpRequestSender;
import com.cloud.ocs.portal.utils.http.RestfulClient;
import com.cloud.ocs.portal.utils.ssh.SSHClient;
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
	
	DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-"); 
	 DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH时mm分ss秒");

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
		List<List<Object>> res = null;
		OcsVm ocsVm = ocsVmService.getOcsVmByVmId(vmId);
		if (ocsVm == null) {
			return null;
		}
		
		OcsVmForwardingPort ocsVmForwardingPort = vmForwardingPortService.getVmForwardingPortByVmId(vmId);
		int sshPort = ocsVmForwardingPort.getSshPublicPort();
		String publicIp = ocsVmForwardingPort.getPublicIp();
		String rootPwd = OcsVmProperties.getOcsVmPassword();
		String cmd = (dayOfMonth < 10 ? OcsVmProperties.getOcsVmHistoryCpuUsagePercentageCmd() + "0" : OcsVmProperties.getOcsVmHistoryCpuUsagePercentageCmd()) + dayOfMonth;
		String ret = SSHClient.sendCmd(publicIp, sshPort, "root", rootPwd, cmd);
		
		if (ret != null) {
			String arr[] = ret.split("\n");
			res = new ArrayList<List<Object>>();
			for (int i = 0; i < arr.length - 1; i++) {
				if (arr[i].contains("all")) {
					List<Object> oneRecord = processOneLineCpuUsageRecord(arr[i], dayOfMonth);
					if (oneRecord != null) {
						res.add(oneRecord);
					}
				}
			}
			
		}
		
		return res;
	}

	@Override
	public List<List<Object>> getVmHistoryMemoryUsagePercentage(String vmId,
			int dayOfMonth) {
		List<List<Object>> res = null;
		OcsVm ocsVm = ocsVmService.getOcsVmByVmId(vmId);
		if (ocsVm == null) {
			return null;
		}
		
		OcsVmForwardingPort ocsVmForwardingPort = vmForwardingPortService.getVmForwardingPortByVmId(vmId);
		int sshPort = ocsVmForwardingPort.getSshPublicPort();
		String publicIp = ocsVmForwardingPort.getPublicIp();
		String rootPwd = OcsVmProperties.getOcsVmPassword();
		String cmd = (dayOfMonth < 10 ? OcsVmProperties.getOcsVmHistoryMemoryUsagePercentageCmd() + "0" : OcsVmProperties.getOcsVmHistoryMemoryUsagePercentageCmd()) + dayOfMonth;
		String ret = SSHClient.sendCmd(publicIp, sshPort, "root", rootPwd, cmd);
		
		if (ret != null) {
			String arr[] = ret.split("\n");
			res = new ArrayList<List<Object>>();
			for (int i = 3; i < arr.length - 1; i++) {
				if (arr[i].isEmpty() || arr[i].contains("commit") || arr[i].contains("RESTART")) {
					continue;
				}
				List<Object> oneRecord = processOneLineMemoryUsageRecord(arr[i], dayOfMonth);
				if (oneRecord != null) {
					res.add(oneRecord);
				}
			}
		}
		
		return res;
	}

	@Override
	public Map<String, List<List<Object>>> getVmHistoryRxbpsTxbps(String vmId,
			String interfaceName, int dayOfMonth) {
		Map<String, List<List<Object>>> res = null;
		OcsVm ocsVm = ocsVmService.getOcsVmByVmId(vmId);
		if (ocsVm == null) {
			return null;
		}
		
		OcsVmForwardingPort ocsVmForwardingPort = vmForwardingPortService.getVmForwardingPortByVmId(vmId);
		int sshPort = ocsVmForwardingPort.getSshPublicPort();
		String publicIp = ocsVmForwardingPort.getPublicIp();
		String rootPwd = OcsVmProperties.getOcsVmPassword();
		String cmd = (dayOfMonth < 10 ? OcsVmProperties.getOcsVmHistoryNetworkUsagePercentageCmd1() + "0" : OcsVmProperties.getOcsVmHistoryNetworkUsagePercentageCmd1()) + dayOfMonth + OcsVmProperties.getOcsVmHistoryNetworkUsagePercentageCmd2();
		String ret = SSHClient.sendCmd(publicIp, sshPort, "root", rootPwd, cmd);
		
		if (ret != null) {
			String arr[] = ret.split("\n");
			res = new HashMap<String, List<List<Object>>>();
			List<List<Object>> rxbpsList = new ArrayList<List<Object>>();
			List<List<Object>> txbpsList = new ArrayList<List<Object>>();
			
			for (int i = 0; i < arr.length; i++) {
				if (arr[i].contains("Average")) {
					continue;
				}
				Map<String, List<Object>> oneRes = processOneLineNetworkUsageRecord(arr[i], dayOfMonth);
				if (oneRes != null) {
					rxbpsList.add(oneRes.get("rxbps"));
					txbpsList.add(oneRes.get("txbps"));
				}
			}
			
			res.put("rxbps", rxbpsList);
			res.put("txbps", txbpsList);
		}
		
		return res;
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
		Map<String, List<List<Object>>> result = new HashMap<String, List<List<Object>>>();
		
		List<List<Object>> allMesg = new ArrayList<List<Object>>();
		List<List<Object>> iMesg = new ArrayList<List<Object>>();
		List<List<Object>> uMesg = new ArrayList<List<Object>>();
		List<List<Object>> tMesg = new ArrayList<List<Object>>();
		for (int i = 0; i < 24*60; i+=120) {
			Date dt = DateUtil.transferDateInMinuteField(date, i+120);
			MessageProcessTimeDto oneMesgProcessTime = this.getVmMessageAverageProcessTimeAtSpecificDate(vmId, dt);
			List<Object> oneAllMesg = new ArrayList<Object>();
			List<Object> oneIMesg = new ArrayList<Object>();
			List<Object> oneUMesg = new ArrayList<Object>();
			List<Object> oneTMesg = new ArrayList<Object>();
			oneAllMesg.add(dt.getTime());
			oneAllMesg.add(oneMesgProcessTime.getAllMessageProcessTime());
			oneIMesg.add(dt.getTime());
			oneIMesg.add(oneMesgProcessTime.getMessageIProcessTime());
			oneUMesg.add(dt.getTime());
			oneUMesg.add(oneMesgProcessTime.getMessageUProcessTime());
			oneTMesg.add(dt.getTime());
			oneTMesg.add(oneMesgProcessTime.getMessageTProcessTime());
			allMesg.add(oneAllMesg);
			iMesg.add(oneIMesg);
			uMesg.add(oneUMesg);
			tMesg.add(oneTMesg);
		}
		
		result.put("allMsg", allMesg);
		result.put("iMsg", iMesg);
		result.put("uMsg", uMesg);
		result.put("tMsg", tMesg);
		
		return result;
	}

	@Override
	public Map<String, List<List<Object>>> getVmHistoryMessageThroughput(String vmId,
			Date date) {
		OcsVm ocsVm = ocsVmService.getOcsVmByVmId(vmId);
		if (ocsVm == null) {
			return null;
		}
		
		String networkIp = ocsVm.getPublicIp();
		String vmIp = ocsVm.getPrivateIp();
		Date from = date;
		Date to = DateUtil.transferDateInDayField(from, 1);
		Map<Date, MessageThroughputDto> messageThroughputList = throughputRecordDao.getVmHistoryMessageThroughput(networkIp, vmIp, from, to);
		
		Map<String, List<List<Object>>> result = new HashMap<String, List<List<Object>>>();
		if (messageThroughputList != null) {
			List<List<Object>> received = new ArrayList<List<Object>>();
			List<List<Object>> finished = new ArrayList<List<Object>>();
			for (Entry<Date, MessageThroughputDto> entry : messageThroughputList.entrySet()) {
				List<Object> oneReceived = new ArrayList<Object>();
				List<Object> oneFinished = new ArrayList<Object>();
				oneReceived.add(entry.getKey().getTime());
				oneReceived.add(entry.getValue().getReceivedMessageNum());
				oneFinished.add(entry.getKey().getTime());
				oneFinished.add(entry.getValue().getFinishedMessageNum());
				received.add(oneReceived);
				finished.add(oneFinished);
			}
			
			result.put("received", received);
			result.put("finished", finished);
		}
		
		return result;
	}
	
	/**
	 * 处理一行CPU使用记录字符串
	 * @param record
	 * @return
	 */
	private List<Object> processOneLineCpuUsageRecord(String record, int dayOfMonth) {
		List<Object> res = null;
		if(record == null) {
			return null;
		}
		
		res = new ArrayList<Object>();
		String dateStr = record.substring(0, record.indexOf(" "));
		String yyyyMMdd = dateFormat1.format(new Date()) + (dayOfMonth < 10 ?  "0" + dayOfMonth : dayOfMonth);
		Date date = null;
		try {
			date = dateFormat2.parse(yyyyMMdd + " " + dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		String cpuIdleUsageStr = record.substring(record.lastIndexOf(" ") + 1);
		res.add(date.getTime());
		res.add(100.0 - Double.parseDouble(cpuIdleUsageStr));
		
		return res;
	}
	
	/**
	 * 处理一行内存记录
	 * @param record
	 * @param dayOfMonth
	 * @return
	 */
	private List<Object> processOneLineMemoryUsageRecord(String record, int dayOfMonth) {
		List<Object> res = null;
		if(record == null) {
			return null;
		}
		
		res = new ArrayList<Object>();
		String dateStr = record.substring(0, record.indexOf(" "));
		String yyyyMMdd = dateFormat1.format(new Date()) + (dayOfMonth < 10 ?  "0" + dayOfMonth : dayOfMonth);
		Date date = null;
		try {
			date = dateFormat2.parse(yyyyMMdd + " " + dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		int indexOfFirstDot = record.indexOf('.');
		int beg = indexOfFirstDot - 1;
		int end = indexOfFirstDot + 1;
		while (record.charAt(beg) != ' ') {
			beg--;
		}
		while (record.charAt(end) != ' ') {
			end++;
		}
		String resStr = record.substring(beg + 1, end);
		res.add(date.getTime());
		res.add(Double.parseDouble(resStr));
		
		return res;
	}
	
	/**
	 * 处理一行网络记录
	 * @param record
	 * @param dayOfMonth
	 * @return
	 */
	private Map<String, List<Object>> processOneLineNetworkUsageRecord(String record, int dayOfMonth) {
		Map<String, List<Object>> res = null;
		if (record == null || record.isEmpty()) {
			return null;
		}
		
		res = new HashMap<String, List<Object>>();
		String dateStr = record.substring(0, record.indexOf(" "));
		String yyyyMMdd = dateFormat1.format(new Date()) + (dayOfMonth < 10 ?  "0" + dayOfMonth : dayOfMonth);
		Date date = null;
		try {
			date = dateFormat2.parse(yyyyMMdd + " " + dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		int indexOfThirdDot = 0;
		int indexOfFourthDot = 0;
		int dotCount = 0;
		for (int i = 0; i < record.length(); i++) {
			if (record.charAt(i) == '.') {
				++dotCount;
			}
			if (dotCount == 2) {
				indexOfThirdDot = i;
			}
			if (dotCount == 3) {
				indexOfFourthDot = i;
			}
		}

		int beg = indexOfThirdDot - 1;
		int end = indexOfThirdDot + 1;
		while (record.charAt(beg) != ' ') {
			beg--;
		}
		while (record.charAt(end) != ' ') {
			end++;
		}
		String rxbpsStr = record.substring(beg + 1, end);

		beg = indexOfFourthDot - 1;
		end = indexOfFourthDot + 1;
		while (record.charAt(beg) != ' ') {
			beg--;
		}
		while (record.charAt(end) != ' ') {
			end++;
		}
		String txbpsStr = record.substring(beg + 1, end);
		
		List<Object> rxbpsList = new ArrayList<Object>();
		rxbpsList.add(date.getTime());
		rxbpsList.add(Double.parseDouble(rxbpsStr));
		List<Object> txbpsList = new ArrayList<Object>();
		txbpsList.add(date.getTime());
		txbpsList.add(Double.parseDouble(txbpsStr));
		res.put("rxbps", rxbpsList);
		res.put("txbps", txbpsList);
		
		return res;
	}

	private MessageProcessTimeDto getVmMessageAverageProcessTimeAtSpecificDate(final String vmId, final Date date) {
		MessageProcessTimeDto result = new MessageProcessTimeDto();
		
		ExecutorService executor = Executors.newCachedThreadPool();
		CompletionService<MessageAverageProcessTimeWrapper> comp = new ExecutorCompletionService<MessageAverageProcessTimeWrapper>(executor);
		List<MessageType> threeMessageType = MessageType.getAllMessageType();
		OcsVm ocsVm = ocsVmService.getOcsVmByVmId(vmId);
		final String networkIp = ocsVm.getPublicIp();
		final String vmIp = ocsVm.getPrivateIp();
		for (final MessageType messageType : threeMessageType) {
			comp.submit(new Callable<MessageAverageProcessTimeWrapper>() {
				public MessageAverageProcessTimeWrapper call() throws Exception {
					return messageRecordService.getMessageAverageProcessTimeOfVmAtSpecificDate(networkIp, vmIp, messageType, date);
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
}
