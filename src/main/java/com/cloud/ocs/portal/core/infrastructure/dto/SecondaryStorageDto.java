package com.cloud.ocs.portal.core.infrastructure.dto;

/**
 * 用来返回系统辅助存储信息的Dto
 * 
 * @author Wang Chao
 * 
 * @date 2014-12-11 下午8:10:40
 * 
 */
public class SecondaryStorageDto {

	private String secondaryStorageId;
	private String secondaryStorageName;
	private String url;
	private String protocol;
	private String providerName;

	public String getSecondaryStorageId() {
		return secondaryStorageId;
	}

	public void setSecondaryStorageId(String secondaryStorageId) {
		this.secondaryStorageId = secondaryStorageId;
	}

	public String getSecondaryStorageName() {
		return secondaryStorageName;
	}

	public void setSecondaryStorageName(String secondaryStorageName) {
		this.secondaryStorageName = secondaryStorageName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}
}
