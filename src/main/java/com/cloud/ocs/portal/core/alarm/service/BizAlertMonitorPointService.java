package com.cloud.ocs.portal.core.alarm.service;

import java.util.List;

import com.cloud.ocs.portal.common.bean.BizAlertMonitorPoint;
import com.cloud.ocs.portal.common.dto.OperateObjectDto;

public interface BizAlertMonitorPointService {

	public OperateObjectDto addBizAlertMonitorPoint(BizAlertMonitorPoint bizAlertMonitorPoint);
	
	public List<BizAlertMonitorPoint> getAllBizAlertMonitorPoint();
	
	public OperateObjectDto removeBizAlertMonitorPoint(Integer id);
}
