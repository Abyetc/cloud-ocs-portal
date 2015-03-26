package com.cloud.ocs.portal.core.alarm.service;

import java.util.List;

import com.cloud.ocs.portal.common.bean.InfrastructureAlertMonitorPoint;
import com.cloud.ocs.portal.common.dto.OperateObjectDto;

public interface InfrastructureAlertMonitorPointService {

	public OperateObjectDto addInfrastructureAlertMonitorPoint(InfrastructureAlertMonitorPoint point);
	
	public List<InfrastructureAlertMonitorPoint> getAllInfrastructureAlertMonitorPoints();
	
	public OperateObjectDto removeInfrastructureAlertMonitorPoint(Integer id);
}
