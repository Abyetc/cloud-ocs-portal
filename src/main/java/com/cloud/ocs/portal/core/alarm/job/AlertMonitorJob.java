package com.cloud.ocs.portal.core.alarm.job;

import javax.annotation.Resource;

import com.cloud.ocs.portal.common.bean.Alert;
import com.cloud.ocs.portal.core.alarm.service.AlertService;
import com.cloud.ocs.portal.core.alarm.service.BizAlertMonitorPointService;
import com.cloud.ocs.portal.core.alarm.service.InfrastructureAlertMonitorPointService;
import com.cloud.ocs.portal.core.business.service.OcsVmService;

public class AlertMonitorJob {

	@Resource
	private BizAlertMonitorPointService bizAlertMonitorPointService;
	
	@Resource
	private InfrastructureAlertMonitorPointService infrastructureAlertMonitorPointService;
	
	@Resource
	private AlertService alertService;
	
	@Resource
	private OcsVmService ocsVmService;
	
	public void executeBizAlertCheckingJob() {
		
	}
	
	public void executeInfrastructureAlertCheckingJob() {
		
	}
	
	public void generateAlert(Alert alert) {
		
	}
	
	public void elasticallyAddVmResource(Alert alert, String networkId) {
		
	}
	
	public void elasticallyReduceVmResource(Alert alert, String networkId) {
		
	}
}
