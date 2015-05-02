package com.cloud.ocs.portal.common.cache;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class StoppingAndDeletingVmCache {

	private Set<String> StoppingAndDeletingVmIds = new HashSet<String>();

	public void addStoppingAndDeletingVm(String vmId) {
		StoppingAndDeletingVmIds.add(vmId);
	}

	public Set<String> getStoppingAndDeletingVmIds() {
		return StoppingAndDeletingVmIds;
	}

	public void setStoppingAndDeletingVmIds(Set<String> stoppingAndDeletingVmIds) {
		StoppingAndDeletingVmIds = stoppingAndDeletingVmIds;
	}

}
