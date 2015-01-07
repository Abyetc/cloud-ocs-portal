package com.cloud.ocs.portal.core.business.dto;

/**
 * 用于返回OCS VM信息的Dto
 * 
 * @author Wang Chao
 * 
 * @date 2015-1-4 下午3:36:21
 * 
 */
public class OcsVmDto {

	private String vmId;
	private String vmName;
	private String zoneId;
	private String zoneName;
	private String networkId;
	private String templateId;
	private String serviceOfferingId;
	private String hostId;
	private String hostName;
	private Integer vmState;
	private String created;

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

	public String getNetworkId() {
		return networkId;
	}

	public void setNetworkId(String networkId) {
		this.networkId = networkId;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getServiceOfferingId() {
		return serviceOfferingId;
	}

	public void setServiceOfferingId(String serviceOfferingId) {
		this.serviceOfferingId = serviceOfferingId;
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

	public Integer getVmState() {
		return vmState;
	}

	public void setVmState(Integer vmState) {
		this.vmState = vmState;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

}
