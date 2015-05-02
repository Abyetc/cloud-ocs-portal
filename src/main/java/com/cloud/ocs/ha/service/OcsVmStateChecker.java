package com.cloud.ocs.ha.service;

import java.io.IOException;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.ocs.ha.CheckingState;
import com.cloud.ocs.portal.common.bean.OcsVm;
import com.cloud.ocs.portal.common.bean.OcsVmForwardingPort;
import com.cloud.ocs.portal.common.cache.FailureVmCache;
import com.cloud.ocs.portal.core.business.service.OcsVmForwardingPortService;
import com.cloud.ocs.portal.properties.OcsVmProperties;
import com.cloud.ocs.portal.utils.ssh.SSHClient;

/**
 * 用于检测计费虚拟机状态的工具类
 * 
 * @author Wang Chao
 *
 * @date 2015-5-2 下午10:09:35
 *
 */
@Service
public class OcsVmStateChecker {
	
	@Resource
	private OcsVmForwardingPortService ocsVmForwardingPortService;

	@Autowired
	private FailureVmCache failureVmCache;
	
	public CheckingState checkOcsVmState(OcsVm ocsVm) {
		String vmId = ocsVm.getVmId();
		OcsVmForwardingPort ocsVmForwardingPort = ocsVmForwardingPortService.getVmForwardingPortByVmId(vmId);
		
		try {
			SSHClient.sendCmd(ocsVmForwardingPort.getPublicIp(), 
					ocsVmForwardingPort.getSshPublicPort(), OcsVmProperties.getOcsVmUsername(), 
					OcsVmProperties.getOcsVmPassword(), "ls");
		} catch (IOException e) {
			String exceptionMessage = e.getMessage();
			if (exceptionMessage.contains("Connection timed out")) {
				failureVmCache.addFailureVm(vmId); //加入故障列表
				return CheckingState.UNNORMAL;
			}
		}
		
		return CheckingState.NORMAL;
	}
}
