package com.cloud.ocs.portal.core.infrastructure.dto;

/**
 * 用来返回系统集群信息的Dto
 * 
 * @author Wang Chao
 * 
 * @date 2014-12-12 下午2:38:47
 * 
 */
public class ClusterDto {

	private String clusterId;
	private String clusterName;
	private String hypervisorType;
	private String allocationState;

	public String getClusterId() {
		return clusterId;
	}

	public void setClusterId(String clusterId) {
		this.clusterId = clusterId;
	}

	public String getClusterName() {
		return clusterName;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

	public String getHypervisorType() {
		return hypervisorType;
	}

	public void setHypervisorType(String hypervisorType) {
		this.hypervisorType = hypervisorType;
	}

	public String getAllocationState() {
		return allocationState;
	}

	public void setAllocationState(String allocationState) {
		this.allocationState = allocationState;
	}
}
