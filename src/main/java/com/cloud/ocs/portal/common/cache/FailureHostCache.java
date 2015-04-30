package com.cloud.ocs.portal.common.cache;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class FailureHostCache {

	private Set<String> failureHostIds = new HashSet<String>();

	public void addFailureHost(String hostId) {
		failureHostIds.add(hostId);
	}

	public Set<String> getFailureHostIds() {
		return failureHostIds;
	}

}
