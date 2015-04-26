package com.cloud.ocs.portal.core.alarm.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloud.ocs.portal.common.bean.BizAlertMonitorPoint;
import com.cloud.ocs.portal.common.dao.BizAlertMonitorPointDao;
import com.cloud.ocs.portal.common.dto.OperateObjectDto;
import com.cloud.ocs.portal.core.alarm.service.BizAlertMonitorPointService;

@Transactional(value="portal_em")
@Service
public class BizAlertMonitorPointServiceImpl implements BizAlertMonitorPointService {
	
	@Resource
	private BizAlertMonitorPointDao bizAlertMonitorPointDao;

	@Override
	public OperateObjectDto addBizAlertMonitorPoint(
			BizAlertMonitorPoint bizAlertMonitorPoint) {
		OperateObjectDto result = new OperateObjectDto();
		Date created = new Date();
		bizAlertMonitorPoint.setCreated(new Timestamp(created.getTime()));
		bizAlertMonitorPointDao.persist(bizAlertMonitorPoint);
		
		if (bizAlertMonitorPoint.getId() != null) {
			result.setCode(OperateObjectDto.OPERATE_OBJECT_CODE_SUCCESS);
			result.setMessage("add success.");
			result.setOperatedObject(bizAlertMonitorPoint);
		}
		else {
			result.setCode(OperateObjectDto.OPERATE_OBJECT_CODE_ERROR);
			result.setMessage("add failed.");
		}
		
		return result;
	}

	@Override
	public List<BizAlertMonitorPoint> getAllBizAlertMonitorPoint() {
		return bizAlertMonitorPointDao.findAll();
	}

	@Override
	public OperateObjectDto removeBizAlertMonitorPoint(Integer id) {
		OperateObjectDto result = new OperateObjectDto();
		result.setCode(OperateObjectDto.OPERATE_OBJECT_CODE_ERROR);
		result.setMessage("Remove alert monitor point failed.");
		
		BizAlertMonitorPoint point = bizAlertMonitorPointDao.findById(id);
		if (point != null) {
			bizAlertMonitorPointDao.remove(point);
			result.setCode(OperateObjectDto.OPERATE_OBJECT_CODE_SUCCESS);
			result.setMessage("Remove alert monitor point success.");
		}
		
		return result;
	}

	@Override
	public BizAlertMonitorPoint getlBizAlertMonitorPointById(Integer id) {
		return bizAlertMonitorPointDao.findById(id);
	}
	
}
