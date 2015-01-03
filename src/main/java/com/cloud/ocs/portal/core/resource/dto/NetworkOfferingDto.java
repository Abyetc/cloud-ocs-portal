package com.cloud.ocs.portal.core.resource.dto;

/**
 * 返回NetworkOffering信息的Dto
 * 
 * @author Wang Chao
 * 
 * @date 2014-12-31 下午3:53:13
 * 
 */
public class NetworkOfferingDto {

	private String networkOfferingId;
	private String networkOfferingName;
	private String displayText;
	private String trafficType;
	private String state;
	private String guestIpType;
	private String serviceOfferingId;

	public String getNetworkOfferingId() {
		return networkOfferingId;
	}

	public void setNetworkOfferingId(String networkOfferingId) {
		this.networkOfferingId = networkOfferingId;
	}

	public String getNetworkOfferingName() {
		return networkOfferingName;
	}

	public void setNetworkOfferingName(String networkOfferingName) {
		this.networkOfferingName = networkOfferingName;
	}

	public String getDisplayText() {
		return displayText;
	}

	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}

	public String getTrafficType() {
		return trafficType;
	}

	public void setTrafficType(String trafficType) {
		this.trafficType = trafficType;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getGuestIpType() {
		return guestIpType;
	}

	public void setGuestIpType(String guestIpType) {
		this.guestIpType = guestIpType;
	}

	public String getServiceOfferingId() {
		return serviceOfferingId;
	}

	public void setServiceOfferingId(String serviceOfferingId) {
		this.serviceOfferingId = serviceOfferingId;
	}

}
