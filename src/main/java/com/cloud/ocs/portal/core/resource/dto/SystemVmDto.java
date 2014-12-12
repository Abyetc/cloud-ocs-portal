package com.cloud.ocs.portal.core.resource.dto;

/**
 * 用来返回系统虚拟机信息的Dto
 * 
 * @author Wang Chao
 * 
 * @date 2014-12-11 下午8:58:27
 * 
 */
public class SystemVmDto {

	private String systemVmId;
	private String systemVmName;
	private String systemVmType;
	private String hostName;
	private String createdDate;
	private String state;

	public String getSystemVmId() {
		return systemVmId;
	}

	public void setSystemVmId(String systemVmId) {
		this.systemVmId = systemVmId;
	}

	public String getSystemVmName() {
		return systemVmName;
	}

	public void setSystemVmName(String systemVmName) {
		this.systemVmName = systemVmName;
	}

	public String getSystemVmType() {
		return systemVmType;
	}

	public void setSystemVmType(String systemVmType) {
		this.systemVmType = systemVmType;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}
