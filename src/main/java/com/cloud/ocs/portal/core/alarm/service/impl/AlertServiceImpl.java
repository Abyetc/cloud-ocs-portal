package com.cloud.ocs.portal.core.alarm.service.impl;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloud.ocs.portal.common.bean.Alert;
import com.cloud.ocs.portal.common.bean.City;
import com.cloud.ocs.portal.common.bean.InfrastructureAlertMonitorPoint;
import com.cloud.ocs.portal.common.dao.AlertDao;
import com.cloud.ocs.portal.common.dao.OcsHostDao;
import com.cloud.ocs.portal.core.alarm.Dto.AlertListDto;
import com.cloud.ocs.portal.core.alarm.service.AlertService;
import com.cloud.ocs.portal.core.alarm.service.InfrastructureAlertMonitorPointService;
import com.cloud.ocs.portal.core.business.service.CityService;

@Transactional(value = "portal_em")
@Service
public class AlertServiceImpl implements AlertService {

	@Resource
	private AlertDao alertDao;

	@Resource
	private CityService cityService;

	@Resource
	private InfrastructureAlertMonitorPointService infraAlertMonitorPointService;
	
	@Resource
	private OcsHostDao hostDao;

	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public void generateAlert(Alert alert) {
		alertDao.persist(alert);
	}

	@Override
	public Alert getAlertById() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Alert> getAllAlerts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AlertListDto> getAllBizAlerts() {
		List<AlertListDto> result = new ArrayList<AlertListDto>();

		List<Alert> allBizAlerts = alertDao.findAllBizAlerts();
		if (allBizAlerts != null) {
			for (Alert one : allBizAlerts) {
				AlertListDto alertDto = new AlertListDto();
				alertDto.setAlertId(one.getId());
				City city = cityService.getCityById(one.getSource());
				alertDto.setSource(city.getName());
				alertDto.setDescription(one.getDescription());
				Timestamp timestamp = one.getCreated();
				Date date = new Date(timestamp.getTime());
				alertDto.setCreated(df.format(date));
				result.add(alertDto);
			}
		}

		return result;

	}

	@Override
	public List<AlertListDto> getAllInfrastructureAlerts() {
		List<AlertListDto> result = new ArrayList<AlertListDto>();

		List<Alert> allInfraAlerts = alertDao.findAllInfraAlerts();
		if (allInfraAlerts != null) {
			for (Alert one : allInfraAlerts) {
				AlertListDto alertDto = new AlertListDto();
				alertDto.setAlertId(one.getId());
				InfrastructureAlertMonitorPoint monitorPoint = infraAlertMonitorPointService
						.getInfrastructureAlertMonitorPointById(one
								.getAlertMonitorPointId());
				String hostId = monitorPoint.getMonitorObjectId();
				alertDto.setSource(hostDao.findByHostId(hostId).getHostName());
				alertDto.setDescription(one.getDescription());
				Timestamp timestamp = one.getCreated();
				Date date = new Date(timestamp.getTime());
				alertDto.setCreated(df.format(date));
				result.add(alertDto);
			}
		}

		return result;
	}

}
