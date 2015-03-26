package com.cloud.ocs.portal.core.alarm.controller;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cloud.ocs.portal.common.bean.Alert;
import com.cloud.ocs.portal.common.bean.BizAlertMonitorPoint;
import com.cloud.ocs.portal.common.bean.City;
import com.cloud.ocs.portal.common.bean.Employee;
import com.cloud.ocs.portal.common.bean.InfrastructureAlertMonitorPoint;
import com.cloud.ocs.portal.common.dao.EmployeeDao;
import com.cloud.ocs.portal.common.dto.OperateObjectDto;
import com.cloud.ocs.portal.core.alarm.Dto.BizAlertMonitorPointListDto;
import com.cloud.ocs.portal.core.alarm.service.AlertService;
import com.cloud.ocs.portal.core.alarm.service.BizAlertMonitorPointService;
import com.cloud.ocs.portal.core.alarm.service.InfrastructureAlertMonitorPointService;
import com.cloud.ocs.portal.core.business.service.CityService;

@Controller
@RequestMapping(value="/alert")
public class AlertController {
	
	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Resource
	private EmployeeDao employeeDao;
	
	@Resource
	private BizAlertMonitorPointService bizAlertMonitorPointService;
	
	@Resource
	private CityService cityService;
	
	@Resource
	private InfrastructureAlertMonitorPointService infrastructureAlertMonitorPointService;
	
	@Resource
	private AlertService alertService;
	
	@RequestMapping(value="/getAllBizEmployee", method=RequestMethod.GET)
	@ResponseBody
	public List<Employee> getAllBizEmployee() {
		return employeeDao.findAllBizEmployee();
	}
	
	@RequestMapping(value="/listAllBizAlertMonitorPoint", method=RequestMethod.GET)
	@ResponseBody
	public List<BizAlertMonitorPointListDto> listAllBizAlertMonitorPoint() {
		List<BizAlertMonitorPointListDto> result = new ArrayList<BizAlertMonitorPointListDto>();
		
		List<BizAlertMonitorPoint> list = bizAlertMonitorPointService.getAllBizAlertMonitorPoint();
		
		for (BizAlertMonitorPoint oneRecord : list) {
			BizAlertMonitorPointListDto oneDto = new BizAlertMonitorPointListDto();
			int objectTyep = oneRecord.getMonitorObjectType();
			if (objectTyep == 0) { //城市
				City city = cityService.getCityById(Integer.parseInt(oneRecord.getMonitorObjectId()));
				oneDto.setMonitorObjectName(city.getName());
			}
			if(objectTyep == 1) { //网络
				
			}
			oneDto.setPrincipalName(employeeDao.findUserByAccountId(oneRecord.getAlertPrincipalId()).getName());
			Timestamp timestamp = oneRecord.getCreated();
			Date date = new Date(timestamp.getTime());
			oneDto.setCreated(df.format(date));
			
			result.add(oneDto);
		}
		
		return result;
	}
	
	@RequestMapping(value="/addBizAlertMonitorPoint", method=RequestMethod.POST)
	@ResponseBody
	public OperateObjectDto addBizMonitorPoint(@RequestParam("objectType") Integer objectType,
			@RequestParam("monitorObjectId") String monitorObjectId,
			@RequestParam("messageProcessTimeThresholdValue") Double messageProcessTimeThresholdValue,
			@RequestParam("activeFlexibleResource") Integer activeFlexibleResource,
			@RequestParam("resourceLoadThresholdValue") Double resourceLoadThresholdValue,
			@RequestParam("alertPrincipalId") String alertPrincipalId) {
		BizAlertMonitorPoint bizAlertMonitorPoint = new BizAlertMonitorPoint();
		bizAlertMonitorPoint.setMonitorObjectType(objectType);
		bizAlertMonitorPoint.setMonitorObjectId(monitorObjectId);
		bizAlertMonitorPoint.setMessageProcessTimeThresholdValue(messageProcessTimeThresholdValue);
		bizAlertMonitorPoint.setActiveFlexibleResource(activeFlexibleResource);
		bizAlertMonitorPoint.setResourceLoadThresholdValue(resourceLoadThresholdValue);
		bizAlertMonitorPoint.setAlertPrincipalId(alertPrincipalId);
		
		BizAlertMonitorPointListDto bizAlertMonitorPointListDto = new BizAlertMonitorPointListDto();
		int objectTyep = bizAlertMonitorPoint.getMonitorObjectType();
		if (objectTyep == 0) { //城市
			City city = cityService.getCityById(Integer.parseInt(bizAlertMonitorPoint.getMonitorObjectId()));
			bizAlertMonitorPointListDto.setMonitorObjectName(city.getName());
		}
		if(objectTyep == 1) { //网络
			
		}
		bizAlertMonitorPointListDto.setPrincipalName(employeeDao.findUserByAccountId(bizAlertMonitorPoint.getAlertPrincipalId()).getName());
		OperateObjectDto result = bizAlertMonitorPointService.addBizAlertMonitorPoint(bizAlertMonitorPoint);
		Timestamp timestamp = ((BizAlertMonitorPoint)result.getOperatedObject()).getCreated();
		Date date = new Date(timestamp.getTime());
		bizAlertMonitorPointListDto.setCreated(df.format(date));
		
		result.setOperatedObject(bizAlertMonitorPointListDto);
		result.setIndex(bizAlertMonitorPointService.getAllBizAlertMonitorPoint().size());
		return result;
	}
	
	public OperateObjectDto removeBizMonitorPoint(Integer pointId) {
		return null;
	}
	
	public List<InfrastructureAlertMonitorPoint> listAllInfrastructureAlertMonitorPoint() {
		return null;
	}
	
	public OperateObjectDto addInfrastructureMonitorPoint(String a, String b, Double c, Integer d, String e) {
		return null;
	}
	
	public OperateObjectDto removeInfrastructureMonitorPoint(Integer id) {
		return null;
	}
	
	public List<Alert> listAllBizAlerts() {
		return null;
	}
	
	public List<Alert> listAllInfrastructureAlerts() {
		return null;
	}
	
	public OperateObjectDto removeBizAlert() {
		return null;
	}
	
	public OperateObjectDto removeInfrastructureAlert() {
		return null;
	}
}
