package com.cloud.ocs.portal.core.monitor.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.annotation.Resource;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.cloud.ocs.portal.common.bean.OcsHost;
import com.cloud.ocs.portal.common.cs.CloudStackApiRequest;
import com.cloud.ocs.portal.common.dao.OcsHostDao;
import com.cloud.ocs.portal.core.monitor.constant.MonitorApiName;
import com.cloud.ocs.portal.core.monitor.dto.HostDetail;
import com.cloud.ocs.portal.core.monitor.service.HostMonitorService;
import com.cloud.ocs.portal.core.monitor.service.OcsVmMonitorService;
import com.cloud.ocs.portal.properties.OcsHostProperties;
import com.cloud.ocs.portal.utils.DateUtil;
import com.cloud.ocs.portal.utils.UnitUtil;
import com.cloud.ocs.portal.utils.cs.CloudStackApiSignatureUtil;
import com.cloud.ocs.portal.utils.http.HttpRequestSender;
import com.cloud.ocs.portal.utils.ssh.SSHClient;

/**
 * 监控主机service实现类
 * 
 * @author Wang Chao
 *
 * @date 2014-12-21 下午4:09:42
 *
 */
@Service
public class HostMonitorServiceImpl implements HostMonitorService {
	
	@Resource
	private OcsHostDao ocsHostDao;
	
	@Resource
	private OcsVmMonitorService vmMonitorService;
	
	private DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-");
	private DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa");
	private DateFormat dateFormat3 = new SimpleDateFormat("dd");

	@Override
	public List<HostDetail> getHostDetailList(String zoneId) {
		CloudStackApiRequest request = new CloudStackApiRequest(MonitorApiName.MONITOR_API_LIST_HOST_DETAIL);
		request.addRequestParams("zoneid", zoneId);
		request.addRequestParams("type", "routing");
		request.addRequestParams("listall", "true");
		CloudStackApiSignatureUtil.generateSignature(request);
		String requestUrl = request.generateRequestURL();
		String response = HttpRequestSender.sendGetRequest(requestUrl);
		
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
	public Long getHostTotalCpuCapacity(String hostId) {
		CloudStackApiRequest request = new CloudStackApiRequest(MonitorApiName.MONITOR_API_LIST_HOST_DETAIL);
		request.addRequestParams("id", hostId);
		CloudStackApiSignatureUtil.generateSignature(request);
		String requestUrl = request.generateRequestURL();
		String response = HttpRequestSender.sendGetRequest(requestUrl);
		
		Long result = 0L;
		
		if (response != null) {
			JSONObject responseJsonObj = new JSONObject(response);
			JSONObject hostsListJsonObj = responseJsonObj.getJSONObject("listhostsresponse");
			if (hostsListJsonObj.has("host")) {
				JSONArray hostsJsonArrayObj = hostsListJsonObj.getJSONArray("host");
				if (hostsJsonArrayObj != null && hostsJsonArrayObj.length() != 0) {
					JSONObject jsonObject = (JSONObject)hostsJsonArrayObj.get(0);
					int cpuNum = jsonObject.getInt("cpunumber");
					long cpuSpeed = jsonObject.getLong("cpuspeed");
					result = cpuNum * cpuSpeed;
				}
			}
		}
		
		return result;
	}

	@Override
	public double getHostCurCpuUsagePercentageFromCs(String hostId) {
		CloudStackApiRequest request = new CloudStackApiRequest(MonitorApiName.MONITOR_API_LIST_HOST_DETAIL);
		request.addRequestParams("id", hostId);
		CloudStackApiSignatureUtil.generateSignature(request);
		String requestUrl = request.generateRequestURL();
		String response = HttpRequestSender.sendGetRequest(requestUrl);
		
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
	public double getHostCurUsedMemoryFromCs(String hostId) {
		
		CloudStackApiRequest request = new CloudStackApiRequest(MonitorApiName.MONITOR_API_LIST_HOST_DETAIL);
		request.addRequestParams("id", hostId);
		CloudStackApiSignatureUtil.generateSignature(request);
		String requestUrl = request.generateRequestURL();
		String response = HttpRequestSender.sendGetRequest(requestUrl);
		
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

	@Override
	public double getHostCurCpuUsedPercentage(String hostId) {
		double res = 0.0;
		OcsHost ocsHost = ocsHostDao.findByHostId(hostId);
		if (ocsHost == null) {
			return 0.0;
		}
		
		int sshPort = OcsHostProperties.getOcsHostSshPort();
		String hostIp = ocsHost.getIpAddress();
		String rootPwd = OcsHostProperties.getOcsHostPassword();
		String cmd = OcsHostProperties.getCurCpuUsagePercentageCmd();
		String ret = SSHClient.sendCmd(hostIp, sshPort, "root", rootPwd, cmd);
		String resLine = ret.split("\n")[3];
		String resStr = resLine.substring(resLine.lastIndexOf(" "), resLine.length());
		if (resStr != null) {
			res = 100.0 - Double.parseDouble(resStr);
		}
		
		return res;
	}

	@Override
	public double getHostCurMemoryUsedPercentage(String hostId) {
		double res = 0.0;
		OcsHost ocsHost = ocsHostDao.findByHostId(hostId);
		if (ocsHost == null) {
			return 0.0;
		}
		
		int sshPort = OcsHostProperties.getOcsHostSshPort();
		String hostIp = ocsHost.getIpAddress();
		String rootPwd = OcsHostProperties.getOcsHostPassword();
		String cmd = OcsHostProperties.getCurMemoryUsagePercentageCmd();
		String ret = SSHClient.sendCmd(hostIp, sshPort, "root", rootPwd, cmd);
		String resLine = ret.split("\n")[3];
		int indexOfFirstDot = resLine.indexOf('.');
		int beg = indexOfFirstDot - 1;
		int end = indexOfFirstDot + 1;
		while (resLine.charAt(beg) != ' ') {
			beg--;
		}
		while (resLine.charAt(end) != ' ') {
			end++;
		}
		String resStr = resLine.substring(beg + 1, end);
		if (resStr != null) {
			res = Double.parseDouble(resStr);
		}
		
		return res;
	}

	@Override
	public List<List<Object>> getHostHistoryCpuUsedPercentage(String hostId,
			int dayOfMonth) {
		List<List<Object>> res = null;
		OcsHost ocsHost = ocsHostDao.findByHostId(hostId);
		if (ocsHost == null) {
			return null;
		}
		
		int sshPort = OcsHostProperties.getOcsHostSshPort();
		String hostIp = ocsHost.getIpAddress();
		String rootPwd = OcsHostProperties.getOcsHostPassword();
		String cmd = (dayOfMonth < 10 ? OcsHostProperties.getHistoryCpuUsagePercentageCmd() + "0" : OcsHostProperties.getHistoryCpuUsagePercentageCmd()) + dayOfMonth;
		String ret = SSHClient.sendCmd(hostIp, sshPort, "root", rootPwd, cmd);
		
		if (ret != null) {
			String arr[] = ret.split("\n");
			res = new ArrayList<List<Object>>();
			String date = dateFormat3.format(new Date());
			int curDayOfMonth = Integer.parseInt(date);
			for (int i = 0; i < arr.length - 1; i++) {
				if (arr[i].contains("all")) {
					List<Object> oneRecord = processOneLineCpuUsageRecord(arr[i], curDayOfMonth, dayOfMonth);
					if (oneRecord != null) {
						res.add(oneRecord);
					}
				}
			}
			
		}
		
		return res;
	}

	@Override
	public List<List<Object>> getHostHistoryMemoryUsedPercentage(String hostId,
			int dayOfMonth) {
		List<List<Object>> res = null;
		OcsHost ocsHost = ocsHostDao.findByHostId(hostId);
		if (ocsHost == null) {
			return null;
		}
		
		int sshPort = OcsHostProperties.getOcsHostSshPort();
		String hostIp = ocsHost.getIpAddress();
		String rootPwd = OcsHostProperties.getOcsHostPassword();
		String cmd = (dayOfMonth < 10 ? OcsHostProperties.getHistoryMemoryUsagePercentageCmd() + "0" : OcsHostProperties.getHistoryMemoryUsagePercentageCmd()) + dayOfMonth;
		String ret = SSHClient.sendCmd(hostIp, sshPort, "root", rootPwd, cmd);
		
		if (ret != null) {
			String arr[] = ret.split("\n");
			res = new ArrayList<List<Object>>();
			String date = dateFormat3.format(new Date());
			int curDayOfMonth = Integer.parseInt(date);
			for (int i = 3; i < arr.length - 1; i++) {
				if (arr[i].isEmpty() || arr[i].contains("commit") || arr[i].contains("RESTART")) {
					continue;
				}
				List<Object> oneRecord = processOneLineMemoryUsageRecord(arr[i], curDayOfMonth, dayOfMonth);
				if (oneRecord != null) {
					res.add(oneRecord);
				}
			}
		}
		
		return res;
	}
	
	/**
	 * 处理一行CPU使用记录字符串
	 * @param record
	 * @return
	 */
	private List<Object> processOneLineCpuUsageRecord(String record, int curDayOfMonth, int dayOfMonth) {
		List<Object> res = null;
		if(record == null) {
			return null;
		}
		
		String resStr = record.substring(record.lastIndexOf(" "), record.length());
		String time = record.substring(0, record.indexOf("M") + 1);
		String dateStr = dateFormat1.format(new Date()) + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + " " + time;
		String aa = dateStr.substring(dateStr.lastIndexOf(" ")+1, dateStr.length());
		String dateStr2 = dateStr.substring(0, dateStr.lastIndexOf(" "));
		if (aa.equals("AM")) {
			dateStr2 = dateStr2 + " 上午";
		}
		else {
			dateStr2 = dateStr2 + " 下午";
		}
		Date date = null;
		try {
			dateFormat2.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
			date = dateFormat2.parse(dateStr2);
			if (dayOfMonth > curDayOfMonth) {
				date = DateUtil.transferDateInMonthField(date, -1);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (date != null) {
			res = new ArrayList<Object>();
			res.add(date.getTime());
			res.add(100.0 - Double.parseDouble(resStr));
		}
		
		return res;
	}
	
	/**
	 * 处理一行Memory使用记录字符串
	 * @param record
	 * @return
	 */
	private List<Object> processOneLineMemoryUsageRecord(String record, int curDayOfMonth, int dayOfMonth) {
		List<Object> res = null;
		if(record == null) {
			return null;
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
		
		String time = record.substring(0, record.indexOf("M") + 1);
		String dateStr = dateFormat1.format(new Date()) + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + " " + time;
		String aa = dateStr.substring(dateStr.lastIndexOf(" ")+1, dateStr.length());
		String dateStr2 = dateStr.substring(0, dateStr.lastIndexOf(" "));
		if (aa.equals("AM")) {
			dateStr2 = dateStr2 + " 上午";
		}
		else {
			dateStr2 = dateStr2 + " 下午";
		}
		Date date = null;
		try {
			dateFormat2.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
			date = dateFormat2.parse(dateStr2);
			if (dayOfMonth > curDayOfMonth) {
				date = DateUtil.transferDateInMonthField(date, -1);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (date != null) {
			res = new ArrayList<Object>();
			res.add(date.getTime());
			res.add(Double.parseDouble(resStr));
		}
		
		return res;
	}

}
