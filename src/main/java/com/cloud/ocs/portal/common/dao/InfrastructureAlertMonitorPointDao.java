package com.cloud.ocs.portal.common.dao;

import java.util.List;

import com.cloud.ocs.portal.common.bean.InfrastructureAlertMonitorPoint;

public interface InfrastructureAlertMonitorPointDao extends GenericDao<InfrastructureAlertMonitorPoint> {

	public List<InfrastructureAlertMonitorPoint> findAll();
	
	public InfrastructureAlertMonitorPoint findById();
}
