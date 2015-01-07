package com.cloud.ocs.portal.core.resource.dto;

/**
 * 用于返回服务方案的Dto
 * 
 * @author Wang Chao
 * 
 * @date 2015-1-4 下午2:25:04
 * 
 */
public class ServiceOfferingDto {

	private String serviceOfferingId;
	private String serviceOfferingName;
	private String displayText;
	private Integer cpuNum;
	private Integer cpuSpeed;
	private Integer memory;
	private String storageType;

	public String getServiceOfferingId() {
		return serviceOfferingId;
	}

	public void setServiceOfferingId(String serviceOfferingId) {
		this.serviceOfferingId = serviceOfferingId;
	}

	public String getServiceOfferingName() {
		return serviceOfferingName;
	}

	public void setServiceOfferingName(String serviceOfferingName) {
		this.serviceOfferingName = serviceOfferingName;
	}

	public String getDisplayText() {
		return displayText;
	}

	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}

	public Integer getCpuNum() {
		return cpuNum;
	}

	public void setCpuNum(Integer cpuNum) {
		this.cpuNum = cpuNum;
	}

	public Integer getCpuSpeed() {
		return cpuSpeed;
	}

	public void setCpuSpeed(Integer cpuSpeed) {
		this.cpuSpeed = cpuSpeed;
	}

	public Integer getMemory() {
		return memory;
	}

	public void setMemory(Integer memory) {
		this.memory = memory;
	}

	public String getStorageType() {
		return storageType;
	}

	public void setStorageType(String storageType) {
		this.storageType = storageType;
	}

}
