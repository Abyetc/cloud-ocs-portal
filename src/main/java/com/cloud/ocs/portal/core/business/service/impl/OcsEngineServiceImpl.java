package com.cloud.ocs.portal.core.business.service.impl;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloud.ocs.ha.job.OcsVmReliabilityJob;
import com.cloud.ocs.portal.common.bean.OcsEngine;
import com.cloud.ocs.portal.common.bean.OcsVmForwardingPort;
import com.cloud.ocs.portal.common.cache.FailureVmCache;
import com.cloud.ocs.portal.common.dao.OcsEngineDao;
import com.cloud.ocs.portal.core.business.constant.OcsEngineState;
import com.cloud.ocs.portal.core.business.service.OcsEngineService;
import com.cloud.ocs.portal.core.business.service.OcsVmForwardingPortService;
import com.cloud.ocs.portal.properties.OcsVmProperties;
import com.cloud.ocs.portal.utils.ssh.SSHClient;

/**
 * OCS引擎程序service实现类
 * 
 * @author Wang Chao
 *
 * @date 2015-1-19 下午9:42:19
 *
 */
@Transactional(value="portal_em")
@Service
public class OcsEngineServiceImpl implements OcsEngineService {
	
	@Resource
	private OcsEngineDao ocsEngineDao;
	
	@Resource
	private OcsVmForwardingPortService ocsVmForwardingPortService;
	
	@Resource
	private OcsVmReliabilityJob ocsVmReliabilityJob;
	
	@Autowired
	private FailureVmCache failureVmCache;
	
	@Override
	public void save(OcsEngine ocsEngine) {
		ocsEngineDao.persist(ocsEngine);
	}
	
	@Override
	public List<OcsEngine> getAllOcsEngines() {
		return ocsEngineDao.findAllOcsEngines();
	}
	
	@Override
	public OcsEngine updateOcsEngine(OcsEngine ocsEngine) {
		return ocsEngineDao.merge(ocsEngine);
	}


	@Override
	public Boolean startOcsEngineService(String vmId) {
		OcsVmForwardingPort ocsVmForwardingPort = ocsVmForwardingPortService.getVmForwardingPortByVmId(vmId);
		if (ocsVmForwardingPort == null) {
			return false;
		}
		
		StringBuffer cmd = new StringBuffer();
		cmd.append(OcsVmProperties.getOcsVmEngineStartCmdBase());
		cmd.append(" " + ocsVmForwardingPort.getPublicIp() + " ");
		cmd.append(OcsVmProperties.getOcsVmEngineStartCmdRedirect());
		
		String startEngineResult = null;
		try {
			startEngineResult = SSHClient.sendCmd(ocsVmForwardingPort.getPublicIp(), 
					ocsVmForwardingPort.getSshPublicPort(), OcsVmProperties.getOcsVmUsername(), 
					OcsVmProperties.getOcsVmPassword(), cmd.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		
		if (startEngineResult != null) {
			return true;
		}
		
		return false;
	}

	@Override
	public OcsEngineState checkOcsEngineServiceRunningState(String vmId) {
		OcsVmForwardingPort ocsVmForwardingPort = ocsVmForwardingPortService.getVmForwardingPortByVmId(vmId);
		if (ocsVmForwardingPort == null) {
			return null;
		}
		
		String checkEngineStateResult = null;
		try {
			checkEngineStateResult = SSHClient.sendCmd(ocsVmForwardingPort.getPublicIp(), 
					ocsVmForwardingPort.getSshPublicPort(), OcsVmProperties.getOcsVmUsername(), 
					OcsVmProperties.getOcsVmPassword(), OcsVmProperties.getOcsVmEngineCheckStateCmd());
		} catch (IOException e) {
			String exceptionMessage = e.getMessage();
			if (exceptionMessage.contains("Connection timed out")) {
//				failureVmCache.addFailureVm(vmId);
//				ocsVmReliabilityJob.executeVmReliabilityJob(vmId);
			}
//			e.printStackTrace();
			return null;
		}
		if (checkEngineStateResult != null) {
			if (checkEngineStateResult.equals("")) {
				return OcsEngineState.STOPPED;
			}
			if (checkEngineStateResult.contains(OcsVmProperties.getOcsVmEngineServicePort().toString())
					&& checkEngineStateResult.contains("LISTEN")) {
				return OcsEngineState.RUNNING;
			}
		}
		
		return null;
	}

}
