package com.cloud.ocs.schedule.request.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cloud.ocs.portal.core.business.service.OcsVmForwardingPortService;

@Service
public class HAProxyService {
	
	@Resource
	private OcsVmForwardingPortService ocsVmForwardingPortService;

	public void dynamicUpdateVmWeight(Map<String, Integer> vmsWeight) {
	}
	
	private List<String> getLastTimeActiveVmIds() {
		return null;
	}
	
	private void executeUpdatingAction(String vmId) {
	}
}
