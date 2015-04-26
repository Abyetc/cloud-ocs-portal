package com.cloud.ocs.portal.common.dao;

import java.util.List;

import com.cloud.ocs.portal.common.bean.Alert;

public interface AlertDao extends GenericDao<Alert> {

	public List<Alert> findAll();
	
	public Alert findById(Integer id);
	
	public List<Alert> findAllBizAlerts();
	
	public List<Alert> findAllInfraAlerts();
}
