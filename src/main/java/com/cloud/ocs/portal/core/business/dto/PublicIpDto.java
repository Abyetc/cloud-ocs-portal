package com.cloud.ocs.portal.core.business.dto;

/**
 * 用于返回public ip信息的Dto
 * 
 * @author Wang Chao
 * 
 * @date 2015-1-6 下午3:56:49
 * 
 */
public class PublicIpDto {

	private String publicIpId;
	private String publicIpAddress;
	private String state;

	public String getPublicIpId() {
		return publicIpId;
	}

	public void setPublicIpId(String publicIpId) {
		this.publicIpId = publicIpId;
	}

	public String getPublicIpAddress() {
		return publicIpAddress;
	}

	public void setPublicIpAddress(String publicIpAddress) {
		this.publicIpAddress = publicIpAddress;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
