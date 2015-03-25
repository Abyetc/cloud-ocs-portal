package com.cloud.ocs.schedule.request.service;

import org.springframework.stereotype.Service;

import com.cloud.ocs.portal.common.bean.OcsVmForwardingPort;
import com.cloud.ocs.portal.utils.http.RestfulClient;
import com.cloud.ocs.schedule.request.dto.VmLoadData;
import com.google.gson.Gson;

@Service
public class VmLoadDataAsker {

	public VmLoadData requestVmLoadData(String vmId, OcsVmForwardingPort ocsVmForwardingPort) {
		StringBuffer url = new StringBuffer();
		url.append("http://" + ocsVmForwardingPort.getPublicIp() + ":" + ocsVmForwardingPort.getMonitorPublicPort());
		url.append("/cloud-ocs-monitor-data-gatherer/gatherer/sum/vmLoadData");
		
		String json = RestfulClient.sendGetRequest(url.toString());
		Gson gson = new Gson();
		VmLoadData result = gson.fromJson(json, VmLoadData.class);
		if (result == null) {
			result = new VmLoadData();
		}
		
		return result;
	}
	
	public double getVmReponseTime(String vmId, OcsVmForwardingPort ocsVmForwardingPort) {
		return 0.0;
	}
	
}
