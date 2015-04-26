package com.cloud.ocs.portal.core.alarm.job;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cloud.ocs.monitor.constant.MessageType;
import com.cloud.ocs.monitor.dao.MessageRecordDao;
import com.cloud.ocs.portal.common.bean.Alert;
import com.cloud.ocs.portal.common.bean.BizAlertMonitorPoint;
import com.cloud.ocs.portal.common.bean.City;
import com.cloud.ocs.portal.common.bean.Employee;
import com.cloud.ocs.portal.common.bean.InfrastructureAlertMonitorPoint;
import com.cloud.ocs.portal.common.bean.OcsHost;
import com.cloud.ocs.portal.common.dao.EmployeeDao;
import com.cloud.ocs.portal.common.dao.OcsHostDao;
import com.cloud.ocs.portal.core.alarm.service.AlertService;
import com.cloud.ocs.portal.core.alarm.service.BizAlertMonitorPointService;
import com.cloud.ocs.portal.core.alarm.service.InfrastructureAlertMonitorPointService;
import com.cloud.ocs.portal.core.business.service.CityService;
import com.cloud.ocs.portal.core.business.service.OcsVmService;
import com.cloud.ocs.portal.core.monitor.service.HostMonitorService;
import com.cloud.ocs.portal.utils.mail.MailSender;

@Service
public class AlertMonitorJob {

	@Resource
	private BizAlertMonitorPointService bizAlertMonitorPointService;

	@Resource
	private InfrastructureAlertMonitorPointService infrastructureAlertMonitorPointService;

	@Resource
	private AlertService alertService;

	@Resource
	private OcsVmService ocsVmService;

	@Resource
	private MessageRecordDao messageRecordDao;

	@Resource
	private CityService cityService;

	@Resource
	private EmployeeDao employeeDao;

	@Resource
	private HostMonitorService hostMonitorService;

	@Resource
	private OcsHostDao hostDao;

	public List<Alert> executeBizAlertCheckingJob() {
		List<Alert> newGeneratedAlerts = new ArrayList<Alert>();

		List<BizAlertMonitorPoint> allBizMonitorPoints = bizAlertMonitorPointService
				.getAllBizAlertMonitorPoint();
		if (allBizMonitorPoints == null) {
			return newGeneratedAlerts;
		}

		for (BizAlertMonitorPoint oneMonitorPoint : allBizMonitorPoints) {
			Integer cityId = Integer.parseInt(oneMonitorPoint
					.getMonitorObjectId());
			City city = cityService.getCityById(cityId);
			Double messageProcessTime = messageRecordDao
					.getMessageAverageProcessTimeOfCityInFiveMinutes(cityId,
							MessageType.ALL);
			if (messageProcessTime == null) {
				continue;
			}
			Double thresholdValue = oneMonitorPoint
					.getMessageProcessTimeThresholdValue();
			if (messageProcessTime.compareTo(thresholdValue) > 0) {
				Alert alert = new Alert();
				alert.setAlertMonitorPointId(oneMonitorPoint.getId());
				alert.setSource(cityId); // 来源城市ID
				alert.setFirstLevelType(1); // 基础设施内报警：0;业务类报警：1。
				alert.setSecondLevelType(0); // 包处理时长：0
				alert.setDescription(city.getName() + "包平均处理时长超过阈值(阈值："
						+ thresholdValue + "ms. 实际值：" + messageProcessTime
						+ "ms.)");
				Date created = new Date();
				alert.setCreated(new Timestamp(created.getTime()));
				generateBizAlert(alert, oneMonitorPoint.getId());

				newGeneratedAlerts.add(alert);
			}
		}

		return newGeneratedAlerts;
	}

	public List<Alert> executeInfrastructureAlertCheckingJob() {
		List<Alert> newGeneratedAlerts = new ArrayList<Alert>();

		List<InfrastructureAlertMonitorPoint> allInfraMonitorPoint = infrastructureAlertMonitorPointService
				.getAllInfrastructureAlertMonitorPoints();
		if (allInfraMonitorPoint == null) {
			return newGeneratedAlerts;
		}

		for (InfrastructureAlertMonitorPoint oneMonitorPoint : allInfraMonitorPoint) {
			String hostId = oneMonitorPoint.getMonitorObjectId();
			OcsHost host = hostDao.findByHostId(hostId);
			Integer monitorType = oneMonitorPoint.getMonitorObjectType();
			Double thresholdValue = oneMonitorPoint.getLoadThresholdValue();
			Calendar cal = Calendar.getInstance();
			int curDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
			if (monitorType.equals(0)) { // CPU
				Double cpuUsagePercentage = hostMonitorService
						.getHostAverageCPUUsagePercentageInFiveMinute(hostId,
								curDayOfMonth);
				if (cpuUsagePercentage.compareTo(thresholdValue) > 0) {
					Alert alert = new Alert();
					alert.setAlertMonitorPointId(oneMonitorPoint.getId());
					alert.setSource(oneMonitorPoint.getId()); // 暂时与monitor
																// point id一样
					alert.setFirstLevelType(0); // 基础设施内报警：0;业务类报警：1。
					alert.setSecondLevelType(0); // CPU：0;内存：1
					alert.setDescription(host.getHostName() + " CPU使用率超过阈值(阈值："
							+ thresholdValue + "%.实际值：" + cpuUsagePercentage
							+ "%.)");
					Date created = new Date();
					alert.setCreated(new Timestamp(created.getTime()));
					
					generateInfraAlert(alert, oneMonitorPoint, host);
					newGeneratedAlerts.add(alert);
				}
			}
			if (monitorType.equals(1)) { // 内存
				Double memoryUsagePercentage = hostMonitorService
						.getHostAverageMemoryUsagePercentageInFiveMinute(
								hostId, curDayOfMonth);
				if (memoryUsagePercentage.compareTo(thresholdValue) > 0) {
					Alert alert = new Alert();
					alert.setAlertMonitorPointId(oneMonitorPoint.getId());
					alert.setSource(oneMonitorPoint.getId()); // 暂时与monitor
																// point id一样
					alert.setFirstLevelType(0); // 基础设施内报警：0;业务类报警：1。
					alert.setSecondLevelType(1); // CPU：0;内存：1
					alert.setDescription(host.getHostName() + " 内存使用率超过阈值(阈值："
							+ thresholdValue + "%.实际值：" + memoryUsagePercentage
							+ "%.)");
					Date created = new Date();
					alert.setCreated(new Timestamp(created.getTime()));
					
					generateInfraAlert(alert, oneMonitorPoint, host);
					newGeneratedAlerts.add(alert);
				}
			}
		}

		return newGeneratedAlerts;
	}

	public void generateBizAlert(Alert alert, Integer alertMonitorPointId) {
		// 将alert存储数据库
		alertService.generateAlert(alert);

		// 发送邮件
		BizAlertMonitorPoint monitorPoint = bizAlertMonitorPointService
				.getlBizAlertMonitorPointById(alertMonitorPointId);
		Employee employee = employeeDao.findUserByAccountId(monitorPoint
				.getAlertPrincipalId());
		City city = cityService.getCityById(alert.getSource());
		String emailSubject = "[警报通知！]来自cloud-ocs的警报通知";
		String emailContent = "==============================来自cloud-ocs的警报通知==============================<br /><br />"
				+ "<strong>警报来源</strong>："
				+ city.getName()
				+ "计费虚拟机集群<br />"
				+ "<strong>触发时间</strong>："
				+ alert.getCreated()
				+ "<br />"
				+ "<strong>警报描述</strong>："
				+ alert.getDescription()
				+ "<br />"
				+ "<br />===========================================================================<br />";
		MailSender.sendMail(employee.getEmail(), emailSubject, emailContent);

	}
	
	public void generateInfraAlert(Alert alert, InfrastructureAlertMonitorPoint monitorPoint, OcsHost host) {
		// 将alert存储数据库
		alertService.generateAlert(alert);
		
		// 发送邮件
		Employee employee = employeeDao.findUserByAccountId(monitorPoint
				.getAlertPrincipalId());
		String emailSubject = "[警报通知！]来自cloud-ocs的警报通知";
		String emailContent = "==============================来自cloud-ocs的警报通知==============================<br /><br />"
				+ "<strong>警报来源</strong>："
				+ host.getHostName()
				+ "计费主机<br />"
				+ "<strong>触发时间</strong>："
				+ alert.getCreated()
				+ "<br />"
				+ "<strong>警报描述</strong>："
				+ alert.getDescription()
				+ "<br />"
				+ "<br />===========================================================================<br />";
		MailSender.sendMail(employee.getEmail(), emailSubject, emailContent);
	}

	public void elasticallyAddVmResource(Alert alert, String networkId) {

	}

	public void elasticallyReduceVmResource(Alert alert, String networkId) {

	}
}
