package com.cloud.ocs.ha.job;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.cloud.ocs.ha.service.OcsVmTackOverService;
import com.cloud.ocs.ha.service.WarmStandbyVmPool;
import com.cloud.ocs.portal.common.bean.OcsHost;
import com.cloud.ocs.portal.common.cache.FailureHostCache;
import com.cloud.ocs.portal.common.dao.OcsHostDao;
import com.cloud.ocs.portal.common.dao.OcsVmDao;
import com.cloud.ocs.portal.core.business.service.OcsEngineService;
import com.cloud.ocs.portal.utils.ssh.SSHClient;

public class OcsHostReliabilityJob {

	@Resource
	private WarmStandbyVmPool warmStandbyVmPool;

	@Resource
	private OcsEngineService ocsHostService;

	@Resource
	private OcsVmTackOverService ocsVmTackOverService;

	@Resource
	private OcsHostDao ocsHostDao;

	@Resource
	private OcsVmDao ocsVmDao;

	@Resource
	private OcsVmReliabilityJob ocsVmReliabilityJob;
	
	@Autowired
	private FailureHostCache failureHostCache;

	public void executeHostReliabilityJob() {
		List<OcsHost> allHosts = ocsHostDao.findAllHost();

		if (allHosts != null) {
			for (OcsHost host : allHosts) {
				if (failureHostCache.getFailureHostIds().contains(host.getHostId())) {
					continue;
				}
				
				String hostIp = host.getIpAddress();
				int port = 22;
				String userName = "root";
				String password = "wf123456";
				String cmd = "ls";
				try {
					SSHClient.sendCmd(hostIp, port, userName, password, cmd);
				} catch (IOException e) {
					String exceptionMessage = e.getMessage();
					if (exceptionMessage.contains("Connection timed out")) {
						failureHostCache.addFailureHost(host.getHostId());
						List<String> vmIdsOnHost = ocsVmDao
								.findAllRunningVmsOnHost(host.getHostId());
						for (String oneVmId : vmIdsOnHost) {
							ocsVmReliabilityJob
									.executeVmReliabilityJob(oneVmId);
						}
						
					}
					// e.printStackTrace();
				}
			}
		}
	}
}
