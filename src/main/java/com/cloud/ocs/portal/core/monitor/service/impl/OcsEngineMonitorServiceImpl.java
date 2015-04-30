package com.cloud.ocs.portal.core.monitor.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloud.ocs.portal.common.bean.OcsEngine;
import com.cloud.ocs.portal.common.cache.FailureVmCache;
import com.cloud.ocs.portal.core.business.constant.OcsEngineState;
import com.cloud.ocs.portal.core.business.constant.OcsVmState;
import com.cloud.ocs.portal.core.business.service.OcsEngineService;
import com.cloud.ocs.portal.core.business.service.OcsVmService;
import com.cloud.ocs.portal.core.monitor.service.OcsEngineMonitorService;

/**
 * 用于监控Ocs引擎程序的service实现类
 * 
 * @author Wang Chao
 *
 * @date 2015-1-20 下午6:35:41
 *
 */
@Transactional(value="portal_em")
@Service
public class OcsEngineMonitorServiceImpl implements OcsEngineMonitorService {
	
	private final static Logger LOGGER = Logger.getLogger(OcsEngineMonitorServiceImpl.class.getName());
	
	@Resource
	private OcsEngineService ocsEngineService;
	
	@Resource
	private OcsVmService ocsVmService;
	
	@Autowired
	private FailureVmCache failureVmCache;

	@Override
	public void checkAndUpdateOcsEngineStateOnAllVms() {
		LOGGER.info("Start to Check and Update Ocs Engine State on all Vms.");
		List<OcsEngine> allOcsEngines = ocsEngineService.getAllOcsEngines();
		
		if (allOcsEngines == null) {
			return;
		}
		
		for (OcsEngine ocsEngine : allOcsEngines) {
			if (failureVmCache.getFailureVmIds().contains(ocsEngine.getVmId())) {
				continue;
			}
			
			if (ocsVmService.getOcsVmstate(ocsEngine.getVmId()) != OcsVmState.RUNNING.getCode()) {
				continue;
			}
			
			OcsEngineState ocsEngineState = ocsEngineService.checkOcsEngineServiceRunningState(ocsEngine.getVmId());
			if (ocsEngineState == null) {
				continue;
			}
			if (ocsEngine.getOcsEngineState().equals(ocsEngineState.getCode())) {
				if (ocsEngineState.equals(OcsEngineState.STOPPED)) {
					ocsEngineService.startOcsEngineService(ocsEngine.getVmId());
					continue;
				}
			}
			else {
				ocsEngine.setOcsEngineState(ocsEngineState.getCode());
				Date startDate = new Date();
				ocsEngine.setLastStartDate(new Timestamp(startDate.getTime()));
				if (ocsEngineState.equals(OcsEngineState.STOPPED)) {
					ocsEngineService.startOcsEngineService(ocsEngine.getVmId());
				}
				ocsEngineService.updateOcsEngine(ocsEngine);
			}
		}
		
		LOGGER.info("Finished to Check and Update Ocs Engine State on all Vms.");
	}

}
