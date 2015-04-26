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
import com.cloud.ocs.portal.common.bean.OcsHost;
import com.cloud.ocs.portal.common.dao.EmployeeDao;
import com.cloud.ocs.portal.common.dao.OcsHostDao;
import com.cloud.ocs.portal.common.dto.OperateObjectDto;
import com.cloud.ocs.portal.core.alarm.Dto.AlertListDto;
import com.cloud.ocs.portal.core.alarm.Dto.AlertMonitorPointListDto;
import com.cloud.ocs.portal.core.alarm.job.AlertMonitorJob;
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
	private OcsHostDao hostDao;
	
	@Resource
	private InfrastructureAlertMonitorPointService infraAlertMonitorPointService;
	
	@Resource
	private AlertService alertService;
	
	@Resource
	private AlertMonitorJob alertMonitorJob;
	
	@RequestMapping(value="/getAllBizEmployee", method=RequestMethod.GET)
	@ResponseBody
	public List<Employee> getAllBizEmployee() {
		return employeeDao.findAllBizEmployee();
	}
	
	@RequestMapping(value="/getAllMaintenanceEmployee", method=RequestMethod.GET)
	@ResponseBody
	public List<Employee> getAllMaintenanceEmployee() {
		return employeeDao.findAllMaintenanceEmployee();
	}
	
	@RequestMapping(value="/listAllBizAlertMonitorPoint", method=RequestMethod.GET)
	@ResponseBody
	public List<AlertMonitorPointListDto> listAllBizAlertMonitorPoint() {
		List<AlertMonitorPointListDto> result = new ArrayList<AlertMonitorPointListDto>();
		
		List<BizAlertMonitorPoint> list = bizAlertMonitorPointService.getAllBizAlertMonitorPoint();
		
		for (BizAlertMonitorPoint oneRecord : list) {
			AlertMonitorPointListDto oneDto = new AlertMonitorPointListDto();
			int objectTyep = oneRecord.getMonitorObjectType();
			if (objectTyep == 0) { //城市
				City city = cityService.getCityById(Integer.parseInt(oneRecord.getMonitorObjectId()));
				oneDto.setMonitorObjectName(city.getName());
			}
			if(objectTyep == 1) { //网络
				
			}
			oneDto.setAlertMonitorPointId(oneRecord.getId());
			oneDto.setPrincipalName(employeeDao.findUserByAccountId(oneRecord.getAlertPrincipalId()).getName());
			Timestamp timestamp = oneRecord.getCreated();
			Date date = new Date(timestamp.getTime());
			oneDto.setCreated(df.format(date));
			
			oneDto.setMessageProcessTimeThreshold(oneRecord.getMessageProcessTimeThresholdValue());
			
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
		
		AlertMonitorPointListDto bizAlertMonitorPointListDto = new AlertMonitorPointListDto();
		int objectTyep = bizAlertMonitorPoint.getMonitorObjectType();
		if (objectTyep == 0) { //城市
			City city = cityService.getCityById(Integer.parseInt(bizAlertMonitorPoint.getMonitorObjectId()));
			bizAlertMonitorPointListDto.setMonitorObjectName(city.getName());
		}
		if(objectTyep == 1) { //网络
			
		}
		bizAlertMonitorPointListDto.setPrincipalName(employeeDao.findUserByAccountId(bizAlertMonitorPoint.getAlertPrincipalId()).getName());
		OperateObjectDto result = bizAlertMonitorPointService.addBizAlertMonitorPoint(bizAlertMonitorPoint);
		bizAlertMonitorPointListDto.setAlertMonitorPointId(((BizAlertMonitorPoint)result.getOperatedObject()).getId());
		Timestamp timestamp = ((BizAlertMonitorPoint)result.getOperatedObject()).getCreated();
		Date date = new Date(timestamp.getTime());
		bizAlertMonitorPointListDto.setCreated(df.format(date));
		bizAlertMonitorPointListDto.setMessageProcessTimeThreshold(((BizAlertMonitorPoint)result.getOperatedObject()).getMessageProcessTimeThresholdValue());
		
		result.setOperatedObject(bizAlertMonitorPointListDto);
		result.setIndex(bizAlertMonitorPointService.getAllBizAlertMonitorPoint().size());
		return result;
	}
	
	@RequestMapping(value="/removeBizMonitorPoint", method=RequestMethod.GET)
	@ResponseBody
	public OperateObjectDto removeBizMonitorPoint(@RequestParam("monitorPointId") Integer monitorPointId) {
		return bizAlertMonitorPointService.removeBizAlertMonitorPoint(monitorPointId);
	}
	
	@RequestMapping(value="/getAllInfraAlertMonitorPoints", method=RequestMethod.GET)
	@ResponseBody
	public List<AlertMonitorPointListDto> listAllInfrastructureAlertMonitorPoint() {
		List<AlertMonitorPointListDto> result = new ArrayList<AlertMonitorPointListDto>();
		
		List<InfrastructureAlertMonitorPoint> allInfraAlertMonitorPoints = infraAlertMonitorPointService.getAllInfrastructureAlertMonitorPoints();
		
		if (allInfraAlertMonitorPoints == null) {
			return result;
		}
		
		for (InfrastructureAlertMonitorPoint one : allInfraAlertMonitorPoints) {
			OcsHost host = hostDao.findByHostId(one.getMonitorObjectId());
			Employee principal = employeeDao.findUserByAccountId(one.getAlertPrincipalId());
			AlertMonitorPointListDto oneRet = new AlertMonitorPointListDto();
			oneRet.setAlertMonitorPointId(one.getId());
			oneRet.setMonitorObjectName(host.getHostName());
			oneRet.setPrincipalName(principal.getName());
			Timestamp timestamp = one.getCreated();
			Date date = new Date(timestamp.getTime());
			oneRet.setCreated(df.format(date));
			
			oneRet.setMonitorObjectType((one.getMonitorObjectType() == 0 ? "CPU" : "内存"));
			oneRet.setUsagePercentageThreshold(one.getLoadThresholdValue());
			
			result.add(oneRet);
		}
		return result;
	}
	
	@RequestMapping(value="/addInfraAlertMonitorPoint", method=RequestMethod.POST)
	@ResponseBody
	public OperateObjectDto addInfrastructureMonitorPoint(
			@RequestParam("monitorObjectId") String monitorObjectId,
			@RequestParam("monitorType") Integer monitorType,
			@RequestParam("usageThresholdValue") Double usageThresholdValue,
			@RequestParam("alertPrincipal") String alertPrincipal) {
		
		InfrastructureAlertMonitorPoint monitorPoint = new InfrastructureAlertMonitorPoint();
		monitorPoint.setMonitorObjectId(monitorObjectId);
		monitorPoint.setMonitorObjectType(monitorType);
		monitorPoint.setLoadThresholdValue(usageThresholdValue);
		monitorPoint.setAlertPrincipalId(alertPrincipal);
		OperateObjectDto operateObjDto = infraAlertMonitorPointService.addInfrastructureAlertMonitorPoint(monitorPoint);
		
		AlertMonitorPointListDto ret = new AlertMonitorPointListDto();
		OcsHost host = hostDao.findByHostId(monitorObjectId);
		Employee principal = employeeDao.findUserByAccountId(alertPrincipal);
		ret.setAlertMonitorPointId(((InfrastructureAlertMonitorPoint)operateObjDto.getOperatedObject()).getId());
		ret.setMonitorObjectName(host.getHostName());
		ret.setPrincipalName(principal.getName());
		Timestamp timestamp = ((InfrastructureAlertMonitorPoint)operateObjDto.getOperatedObject()).getCreated();
		Date date = new Date(timestamp.getTime());
		ret.setCreated(df.format(date));
		ret.setMonitorObjectType(((InfrastructureAlertMonitorPoint)operateObjDto.getOperatedObject()).getMonitorObjectType() == 0 ? "CPU" : "内存");
		ret.setUsagePercentageThreshold(((InfrastructureAlertMonitorPoint)operateObjDto.getOperatedObject()).getLoadThresholdValue());
				
		operateObjDto.setOperatedObject(ret);
		operateObjDto.setIndex(infraAlertMonitorPointService.getAllInfrastructureAlertMonitorPoints().size());
		return operateObjDto;
		
	}
	
	@RequestMapping(value="/removeInfrastructureMonitorPoint", method=RequestMethod.GET)
	@ResponseBody
	public OperateObjectDto removeInfrastructureMonitorPoint(@RequestParam("monitorPointId") Integer monitorPointId) {
		return infraAlertMonitorPointService.removeInfrastructureAlertMonitorPoint(monitorPointId);
	}
	
	@RequestMapping(value="/checkBizAlert", method=RequestMethod.GET)
	@ResponseBody
	public List<Alert> checkBizAlert() {
		return alertMonitorJob.executeBizAlertCheckingJob();
	}
	
	@RequestMapping(value="/getAllBizAlertList", method=RequestMethod.GET)
	@ResponseBody
	public List<AlertListDto> listAllBizAlerts() {
		return alertService.getAllBizAlerts();
	}
	
	@RequestMapping(value="/getAllInfraAlertList", method=RequestMethod.GET)
	@ResponseBody
	public List<AlertListDto> listAllInfrastructureAlerts() {
		return alertService.getAllInfrastructureAlerts();
	}
	
	@RequestMapping(value="/checkInfraAlert", method=RequestMethod.GET)
	@ResponseBody
	public List<Alert> checkInfraAlert() {
		return alertMonitorJob.executeInfrastructureAlertCheckingJob();
	}
}
