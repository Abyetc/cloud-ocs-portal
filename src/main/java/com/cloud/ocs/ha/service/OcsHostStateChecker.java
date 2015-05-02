package com.cloud.ocs.ha.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.ocs.ha.CheckingState;
import com.cloud.ocs.portal.common.bean.OcsHost;
import com.cloud.ocs.portal.common.cache.FailureHostCache;
import com.cloud.ocs.portal.properties.OcsHostProperties;
import com.cloud.ocs.portal.utils.ssh.SSHClient;

/**
 * 用于检测主机状态的工具类
 * 
 * @author Wang Chao
 *
 * @date 2015-5-2 下午1:59:58
 *
 */
@Service
public class OcsHostStateChecker {
	
	@Autowired
	private FailureHostCache failureHostCache;

	public CheckingState checkOcsHostState(OcsHost ocsHost) {
		String hostIp = ocsHost.getIpAddress();
		int port = OcsHostProperties.getOcsHostSshPort();
		String userName = OcsHostProperties.getOcsHostUsername();
		String password = OcsHostProperties.getOcsHostPassword();
		String cmd = "ls";
		
		try {
			SSHClient.sendCmd(hostIp, port, userName, password, cmd);
		} catch (IOException e) {
			String exceptionMessage = e.getMessage();
			if (exceptionMessage.contains("Connection timed out")) {
				failureHostCache.addFailureHost(ocsHost.getHostId());
				return CheckingState.UNNORMAL;
			}
		}
		
		return CheckingState.NORMAL;
	}
}
