package com.cloud.ocs.portal.core.alarm.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloud.ocs.portal.common.bean.InfrastructureAlertMonitorPoint;
import com.cloud.ocs.portal.common.dao.InfrastructureAlertMonitorPointDao;
import com.cloud.ocs.portal.common.dto.OperateObjectDto;
import com.cloud.ocs.portal.core.alarm.service.InfrastructureAlertMonitorPointService;

@Transactional(value="portal_em")
@Service
public class InfrastructureAlertMonitorPointServiceImpl implements InfrastructureAlertMonitorPointService{

	@Resource
	private InfrastructureAlertMonitorPointDao infraAlertMonitorPointDao;
	
	@Override
	public OperateObjectDto addInfrastructureAlertMonitorPoint(
			InfrastructureAlertMonitorPoint point) {
		OperateObjectDto result = new OperateObjectDto();
		Date created = new Date();
		point.setCreated(new Timestamp(created.getTime()));
		infraAlertMonitorPointDao.persist(point);
		
		if (point.getId() != null) {
			result.setCode(OperateObjectDto.OPERATE_OBJECT_CODE_SUCCESS);
			result.setMessage("add success.");
			result.setOperatedObject(point);
		}
		else {
			result.setCode(OperateObjectDto.OPERATE_OBJECT_CODE_ERROR);
			result.setMessage("add failed.");
		}
		
		return result;
	}

	@Override
	public List<InfrastructureAlertMonitorPoint> getAllInfrastructureAlertMonitorPoints() {
		return infraAlertMonitorPointDao.findAll();
	}

	@Override
	public OperateObjectDto removeInfrastructureAlertMonitorPoint(Integer id) {
		OperateObjectDto result = new OperateObjectDto();
		result.setCode(OperateObjectDto.OPERATE_OBJECT_CODE_ERROR);
		result.setMessage("Remove alert monitor point failed.");
		
		InfrastructureAlertMonitorPoint point = infraAlertMonitorPointDao.findById(id);
		if (point != null) {
			infraAlertMonitorPointDao.remove(point);
			result.setCode(OperateObjectDto.OPERATE_OBJECT_CODE_SUCCESS);
			result.setMessage("Remove alert monitor point success.");
		}
		
		return result;
	}

	@Override
	public InfrastructureAlertMonitorPoint getInfrastructureAlertMonitorPointById(
			Integer id) {
		return infraAlertMonitorPointDao.findById(id);
	}

}
