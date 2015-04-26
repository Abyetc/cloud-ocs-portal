package com.cloud.ocs.portal.core.alarm.service;

import java.util.List;

import com.cloud.ocs.portal.common.bean.Alert;
import com.cloud.ocs.portal.core.alarm.Dto.AlertListDto;

public interface AlertService {
	
	public void generateAlert(Alert alert);
	
	public List<Alert> getAllAlerts();
	
	public Alert getAlertById();
	
	public List<AlertListDto> getAllBizAlerts();
	
	public List<AlertListDto> getAllInfrastructureAlerts();
}
