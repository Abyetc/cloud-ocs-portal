package com.cloud.ocs.portal.core.alarm.service;

import java.util.List;

import com.cloud.ocs.portal.common.bean.Alert;

public interface AlertService {
	
	public void generateBizAlert();
	
	public void generateInfrastructureAlert();

	public List<Alert> getAllAlerts();
	
	public Alert getAlertById();
	
	public List<Alert> getAllBizAlerts();
	
	public List<Alert> getAllInfrastructureAlerts();
}
