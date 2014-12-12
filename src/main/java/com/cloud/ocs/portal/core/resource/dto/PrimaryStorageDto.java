package com.cloud.ocs.portal.core.resource.dto;

/**
 * 用来返回系统主存储信息的Dto
 * 
 * @author Wang Chao
 * 
 * @date 2014-12-12 下午3:14:29
 * 
 */
public class PrimaryStorageDto {

	private String primaryStorageId;
	private String primaryStorageName;
	private String hostIpAddress;
	private String path;
	private String type;
	private String state;

	public String getPrimaryStorageId() {
		return primaryStorageId;
	}

	public void setPrimaryStorageId(String primaryStorageId) {
		this.primaryStorageId = primaryStorageId;
	}

	public String getPrimaryStorageName() {
		return primaryStorageName;
	}

	public void setPrimaryStorageName(String primaryStorageName) {
		this.primaryStorageName = primaryStorageName;
	}

	public String getHostIpAddress() {
		return hostIpAddress;
	}

	public void setHostIpAddress(String hostIpAddress) {
		this.hostIpAddress = hostIpAddress;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}
