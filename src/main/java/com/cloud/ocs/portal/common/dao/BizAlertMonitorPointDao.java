package com.cloud.ocs.portal.common.dao;

import java.util.List;

import com.cloud.ocs.portal.common.bean.BizAlertMonitorPoint;

public interface BizAlertMonitorPointDao extends GenericDao<BizAlertMonitorPoint> {

	public List<BizAlertMonitorPoint> findAll();
}
