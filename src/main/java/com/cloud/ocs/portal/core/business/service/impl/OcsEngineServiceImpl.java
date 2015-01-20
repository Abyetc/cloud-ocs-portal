package com.cloud.ocs.portal.core.business.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloud.ocs.portal.core.business.bean.OcsEngine;
import com.cloud.ocs.portal.core.business.bean.OcsVmForwardingPort;
import com.cloud.ocs.portal.core.business.constant.OcsEngineState;
import com.cloud.ocs.portal.core.business.dao.OcsEngineDao;
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
@Transactional
@Service
public class OcsEngineServiceImpl implements OcsEngineService {
	
	@Resource
	private OcsEngineDao ocsEngineDao;
	
	@Resource
	private OcsVmForwardingPortService ocsVmForwardingPortService;
	
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
		
		String startEngineResult = SSHClient.sendCmd(ocsVmForwardingPort.getPublicIp(), 
				ocsVmForwardingPort.getSshPublicPort(), OcsVmProperties.getOcsVmUsername(), 
				OcsVmProperties.getOcsVmPassword(), cmd.toString());
		
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
		
		String checkEngineStateResult = SSHClient.sendCmd(ocsVmForwardingPort.getPublicIp(), 
				ocsVmForwardingPort.getSshPublicPort(), OcsVmProperties.getOcsVmUsername(), 
				OcsVmProperties.getOcsVmPassword(), OcsVmProperties.getOcsVmEngineCheckStateCmd());
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
