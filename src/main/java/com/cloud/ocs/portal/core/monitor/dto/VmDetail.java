package com.cloud.ocs.portal.core.monitor.dto;

/**
 * 用于返回用户VM详细信息的Dto
 * 
 * @author Wang Chao
 * 
 * @date 2014-12-27 下午4:43:37
 * 
 */
public class VmDetail {

	private String zoneId;
	private String zoneName;
	private String hostId;
	private String hostName;
	private String vmId;
	private String vmName;
	private String instanceName; // 内部名称
	private String created; // 创建时间
	private String templateName;
	private String hypervisor;
	private String state;

	private int cpuNum;
	private String cupSpeed;
	private String memory;
	private String networkRead; // 网络读取量
	private String networkWrite; // 网络写入量
	private String diskRead; // 磁盘读取量
	private String diskWrite; // 磁盘写入量
	private long diskIORead; // 磁盘IO读取量
	private long diskIOWrite; // 磁盘IO写入量
	private boolean haHost; // 是否启用了高可用HA

	private String networkName;
	private String ipAddress;
	private String isolationUri;

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

	public String getVmId() {
		return vmId;
	}

	public void setVmId(String vmId) {
		this.vmId = vmId;
	}

	public String getVmName() {
		return vmName;
	}

	public void setVmName(String vmName) {
		this.vmName = vmName;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
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

	public String getMemory() {
		return memory;
	}

	public void setMemory(String memory) {
		this.memory = memory;
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

	public String getDiskRead() {
		return diskRead;
	}

	public void setDiskRead(String diskRead) {
		this.diskRead = diskRead;
	}

	public String getDiskWrite() {
		return diskWrite;
	}

	public void setDiskWrite(String diskWrite) {
		this.diskWrite = diskWrite;
	}

	public long getDiskIORead() {
		return diskIORead;
	}

	public void setDiskIORead(long diskIORead) {
		this.diskIORead = diskIORead;
	}

	public long getDiskIOWrite() {
		return diskIOWrite;
	}

	public void setDiskIOWrite(long diskIOWrite) {
		this.diskIOWrite = diskIOWrite;
	}

	public boolean isHaHost() {
		return haHost;
	}

	public void setHaHost(boolean haHost) {
		this.haHost = haHost;
	}

	public String getNetworkName() {
		return networkName;
	}

	public void setNetworkName(String networkName) {
		this.networkName = networkName;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getIsolationUri() {
		return isolationUri;
	}

	public void setIsolationUri(String isolationUri) {
		this.isolationUri = isolationUri;
	}

}
