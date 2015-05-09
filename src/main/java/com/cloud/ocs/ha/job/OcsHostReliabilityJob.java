package com.cloud.ocs.ha.job;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloud.ocs.ha.CheckingState;
import com.cloud.ocs.ha.service.OcsHostStateChecker;
import com.cloud.ocs.ha.service.OcsVmTackOverService;
import com.cloud.ocs.ha.service.WarmStandbyVmPool;
import com.cloud.ocs.portal.common.bean.OcsHost;
import com.cloud.ocs.portal.common.bean.WarmStandbyOcsVm;
import com.cloud.ocs.portal.common.cache.FailureHostCache;
import com.cloud.ocs.portal.common.dao.OcsHostDao;
import com.cloud.ocs.portal.common.dao.OcsVmDao;

@Transactional(value="portal_em")
@Service
public class OcsHostReliabilityJob {

	@Resource
	private WarmStandbyVmPool warmStandbyVmPool;

	@Resource
	private OcsVmTackOverService ocsVmTackOverService;

	@Resource
	private OcsHostDao ocsHostDao;

	@Resource
	private OcsVmDao ocsVmDao;
	
	@Resource
	private OcsHostStateChecker ocsHostStateChecker;
	
	@Autowired
	private FailureHostCache failureHostCache;

	public void executeHostReliabilityJob() {
		List<OcsHost> allHosts = ocsHostDao.findAllHost();

		if (allHosts != null) {
			for (final OcsHost host : allHosts) {
				//一台主机用一条线程检测
				Thread thread = new Thread() {
					public void run() {
						if (failureHostCache.getFailureHostIds().contains(host.getHostId())) {
							return;
						}
						
						CheckingState ocsHostState = ocsHostStateChecker.checkOcsHostState(host);
						
						if (ocsHostState.getCode() == CheckingState.UNNORMAL.getCode()) {
							//从数据库中获得故障主机上正在运行的所有虚拟机列表ID
							final List<String> allRunningVmOnHostIds = ocsVmDao.findAllRunningVmsOnHost(host.getHostId());
							
							final List<WarmStandbyOcsVm> warmStandbyOcsVmList = warmStandbyVmPool.getWarmStandbyOcsVms(allRunningVmOnHostIds);
							
							for (final String oneRunningVmOnHostId : allRunningVmOnHostIds) {
								Thread tackOverThread = new Thread() {
						              public void run() {
						            	  ocsVmTackOverService.tackOverOcsVm(oneRunningVmOnHostId, warmStandbyOcsVmList.get(allRunningVmOnHostIds.indexOf(oneRunningVmOnHostId)));
						              }
								};
								tackOverThread.start();
							}
							
//							ocsHostDao.deleteByHostId(host.getHostId());
						}
					}
				};
				thread.start();
			}
		}
	}
}
