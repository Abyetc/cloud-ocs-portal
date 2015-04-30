package com.cloud.ocs.portal.common.cache;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class FailureVmCache {

	private Set<String> failureVmIds = new HashSet<String>();

	public void addFailureVm(String vmId) {
		failureVmIds.add(vmId);
	}

	public Set<String> getFailureVmIds() {
		return failureVmIds;
	}

	public void setFailureVmIds(Set<String> failureVmIds) {
		this.failureVmIds = failureVmIds;
	}

}
