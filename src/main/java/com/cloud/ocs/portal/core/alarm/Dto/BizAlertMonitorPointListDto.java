package com.cloud.ocs.portal.core.alarm.Dto;

public class BizAlertMonitorPointListDto {

	private String monitorObjectName;
	private String principalName;
	private String created;

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

}
