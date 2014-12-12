package com.cloud.ocs.portal.core.resource.dto;

/**
 * 用来返回系统Zone信息的Dto
 * 
 * @author Wang Chao
 * 
 * @date 2014-12-11 下午3:27:03
 * 
 */
public class ZoneDto {

	private String zoneId;
	private String zoneName;
	private String networkType;
	private String allocationState;

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

	public String getNetworkType() {
		return networkType;
	}

	public void setNetworkType(String networkType) {
		this.networkType = networkType;
	}

	public String getAllocationState() {
		return allocationState;
	}

	public void setAllocationState(String allocationState) {
		this.allocationState = allocationState;
	}
}
