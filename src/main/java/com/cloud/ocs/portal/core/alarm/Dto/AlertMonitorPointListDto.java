package com.cloud.ocs.portal.core.alarm.Dto;

public class AlertMonitorPointListDto {

	private Integer alertMonitorPointId;
	private String monitorObjectName;
	private String principalName;
	private String created;
	
	private Double messageProcessTimeThreshold; //用于业务类报警监控点
	
	private String monitorObjectType; //CPU;内存
	private Double usagePercentageThreshold;

	public Integer getAlertMonitorPointId() {
		return alertMonitorPointId;
	}

	public void setAlertMonitorPointId(Integer alertMonitorPointId) {
		this.alertMonitorPointId = alertMonitorPointId;
	}

	public String getMonitorObjectName() {
		return monitorObjectName;
	}

	public void setMonitorObjectName(String monitorObjectName) {
		this.monitorObjectName = monitorObjectName;
	}

	public String getPrincipalName() {
		return principalName;
	}

	public void setPrincipalName(String principalName) {
		this.principalName = principalName;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public Double getMessageProcessTimeThreshold() {
		return messageProcessTimeThreshold;
	}

	public void setMessageProcessTimeThreshold(Double messageProcessTimeThreshold) {
		this.messageProcessTimeThreshold = messageProcessTimeThreshold;
	}

	public String getMonitorObjectType() {
		return monitorObjectType;
	}

	public void setMonitorObjectType(String monitorObjectType) {
		this.monitorObjectType = monitorObjectType;
	}

	public Double getUsagePercentageThreshold() {
		return usagePercentageThreshold;
	}

	public void setUsagePercentageThreshold(Double usagePercentageThreshold) {
		this.usagePercentageThreshold = usagePercentageThreshold;
	}

}
