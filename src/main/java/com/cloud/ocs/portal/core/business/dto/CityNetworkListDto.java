package com.cloud.ocs.portal.core.business.dto;

import java.sql.Timestamp;

/**
 * 用于返回城市网络列表的Dto
 * 
 * @author Wang Chao
 * 
 * @date 2015-1-3 下午9:31:31
 * 
 */
public class CityNetworkListDto {

	private String networkId;
	private String networkName;
	private String publicIp;
	private Integer networkState;
	private String realmName;
	private Timestamp created;
	private Integer vmNum;

	public String getNetworkId() {
		return networkId;
	}

	public void setNetworkId(String networkId) {
		this.networkId = networkId;
	}

	public String getNetworkName() {
		return networkName;
	}

	public void setNetworkName(String networkName) {
		this.networkName = networkName;
	}

	public String getPublicIp() {
		return publicIp;
	}

	public void setPublicIp(String publicIp) {
		this.publicIp = publicIp;
	}

	public Integer getNetworkState() {
		return networkState;
	}

	public void setNetworkState(Integer networkState) {
		this.networkState = networkState;
	}

	public String getRealmName() {
		return realmName;
	}

	public void setRealmName(String realmName) {
		this.realmName = realmName;
	}

	public Timestamp getCreated() {
		return created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public Integer getVmNum() {
		return vmNum;
	}

	public void setVmNum(Integer vmNum) {
		this.vmNum = vmNum;
	}

}
