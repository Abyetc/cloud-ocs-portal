package com.cloud.ocs.portal.core.infrastructure.dto;

/**
 * 用来返回系统Pod信息的Dto
 * 
 * @author Wang Chao
 * 
 * @date 2014-12-11 下午7:06:29
 * 
 */
public class PodDto {

	private String podId;
	private String podName;
	private String allocationState;

	public String getPodId() {
		return podId;
	}

	public void setPodId(String podId) {
		this.podId = podId;
	}

	public String getPodName() {
		return podName;
	}

	public void setPodName(String podName) {
		this.podName = podName;
	}

	public String getAllocationState() {
		return allocationState;
	}

	public void setAllocationState(String allocationState) {
		this.allocationState = allocationState;
	}
}
