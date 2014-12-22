package com.cloud.ocs.portal.core.monitor.dto;

/**
 * 用于返回主机详细信息的Dto
 * 
 * @author Wang Chao
 * 
 * @date 2014-12-21 下午3:46:38
 * 
 */
public class HostDetail {

	private String zoneId;
	private String zoneName;
	private String podId;
	private String podName;
	private String clusterId;
	private String clusterName;
	private String hostId;
	private String hostName;
	private String ipAddress;
	private String type;
	private String hypervisor;
	private String state;
	private String createdDate;

	private int cpuNum;
	private String cupSpeed;
	private String cpuAllocated; // 已经分配给VM的CPU(百分数)
	private String memoryTotal;
	private String memoryAllocated; // 已经分配的内存(大小)
	private String memoryAllocatedPercentage; // 已经分配的内存(百分数)
	private String networkRead; // 网络读取量
	private String networkWrite; // 网络写入量
	private boolean haHost; // 是否启用了高可用HA

	public String getZoneId() {
		return zoneId;
	}

	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}

	public String getZoneName() {
		return zoneName;
	}

	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

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

	public String getHostId() {
		return hostId;
	}

	public void setHostId(String hostId) {
		this.hostId = hostId;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getHypervisor() {
		return hypervisor;
	}

	public void setHypervisor(String hypervisor) {
		this.hypervisor = hypervisor;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public int getCpuNum() {
		return cpuNum;
	}

	public void setCpuNum(int cpuNum) {
		this.cpuNum = cpuNum;
	}

	public String getCupSpeed() {
		return cupSpeed;
	}

	public void setCupSpeed(String cupSpeed) {
		this.cupSpeed = cupSpeed;
	}

	public String getCpuAllocated() {
		return cpuAllocated;
	}

	public void setCpuAllocated(String cpuAllocated) {
		this.cpuAllocated = cpuAllocated;
	}

	public String getMemoryTotal() {
		return memoryTotal;
	}

	public void setMemoryTotal(String memoryTotal) {
		this.memoryTotal = memoryTotal;
	}

	public String getMemoryAllocated() {
		return memoryAllocated;
	}

	public void setMemoryAllocated(String memoryAllocated) {
		this.memoryAllocated = memoryAllocated;
	}

	public String getMemoryAllocatedPercentage() {
		return memoryAllocatedPercentage;
	}

	public void setMemoryAllocatedPercentage(String memoryAllocatedPercentage) {
		this.memoryAllocatedPercentage = memoryAllocatedPercentage;
	}

	public String getNetworkRead() {
		return networkRead;
	}

	public void setNetworkRead(String networkRead) {
		this.networkRead = networkRead;
	}

	public String getNetworkWrite() {
		return networkWrite;
	}

	public void setNetworkWrite(String networkWrite) {
		this.networkWrite = networkWrite;
	}

	public boolean isHaHost() {
		return haHost;
	}

	public void setHaHost(boolean haHost) {
		this.haHost = haHost;
	}

}
