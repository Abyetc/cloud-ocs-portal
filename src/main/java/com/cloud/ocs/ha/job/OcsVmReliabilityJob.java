package com.cloud.ocs.ha.job;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.ocs.ha.CheckingState;
import com.cloud.ocs.ha.service.OcsHostStateChecker;
import com.cloud.ocs.ha.service.OcsVmStateChecker;
import com.cloud.ocs.portal.common.bean.OcsHost;
import com.cloud.ocs.portal.common.bean.OcsVm;
import com.cloud.ocs.portal.common.cache.FailureVmCache;
import com.cloud.ocs.portal.common.cache.StoppingAndDeletingVmCache;
import com.cloud.ocs.portal.common.dao.OcsHostDao;
import com.cloud.ocs.portal.common.dao.OcsVmDao;
import com.cloud.ocs.portal.core.business.dto.OcsVmDto;
import com.cloud.ocs.portal.core.business.service.OcsVmService;

@Service
public class OcsVmReliabilityJob {

	@Resource
	private OcsVmService ocsVmSerivce;

	@Resource
	private OcsVmDao ocsVmDao;
	
	@Resource
	private OcsHostDao ocsHostDao;
	
	@Resource
	private OcsVmStateChecker ocsVmStateChecker;
	
	@Resource
	private OcsHostStateChecker ocsHostStateChecker;
	
	@Autowired
	private FailureVmCache failureVmCache;
	
	@Autowired
	private StoppingAndDeletingVmCache stoppingAndDeletingVmCache;
	
	public void executeVmReliabilityJob() {
		List<OcsVm> allRunningOcsVms = ocsVmDao.findAllRunningOcsVms();
		
		if (allRunningOcsVms == null) {
			return;
		}
		
		for (OcsVm oneOcsVm : allRunningOcsVms) {
			String vmId = oneOcsVm.getVmId();
			
			if (failureVmCache.getFailureVmIds().contains(vmId)) { //已经在故障列表中，不处理，避免重复处理
				continue;
			}
			
			//正在进行暂停或删除的虚拟机，不检测！！！
			if (stoppingAndDeletingVmCache.getStoppingAndDeletingVmIds().contains(vmId)) {
				continue;
			}
			
			CheckingState ocsVmState = ocsVmStateChecker.checkOcsVmState(oneOcsVm);
			
			if (ocsVmState.getCode() == CheckingState.NORMAL.getCode()) { //状态正常
				continue;
			}
			
			//若状态不正常
			//首先检测所在主机的状态
			String hostId = oneOcsVm.getHsotId();
			OcsHost ocsHost = ocsHostDao.findByHostId(hostId);
			CheckingState ocsHostState = ocsHostStateChecker.checkOcsHostState(ocsHost);
			
			if (ocsHostState.getCode() == CheckingState.UNNORMAL.getCode()) { //主机故障优先
				continue;
			}
			
			startNewVm(vmId);
		}
	}

	private void startNewVm(final String vmId) {
		OcsVm ocsVm = ocsVmDao.findByVmId(vmId);
		final String networkId = ocsVm.getNetworkId();

		List<OcsVmDto> ocsVmsInNetwork = ocsVmSerivce
				.getOcsVmsListInLBRuleByNetworkId(networkId);

		OcsVmDto ocsVmDto = null;
		if (ocsVmsInNetwork != null) {
			for (OcsVmDto one : ocsVmsInNetwork) {
				if (one.getVmId().equals(vmId)) {
					ocsVmDto = one;
					break;
				}
			}
		}

		if (ocsVmDto != null) {
			final String zoneId = ocsVmDto.getZoneId();
			final String serviceOfferingId = ocsVmDto.getServiceOfferingId();
			final String templateId = ocsVmDto.getTemplateId();
			final String newName = ocsVmDto.getVmName() + "-ha";
			final String hostId = ocsVmDto.getHostId();
			Thread thread1 = new Thread() {
	              public void run() {
	            	  ocsVmSerivce.removeOcsVm(vmId);
	              }
			};
			thread1.start();
			Thread thread2 = new Thread() {
	              public void run() {
	            	  ocsVmSerivce.addOcsVm(newName, networkId,
	      					zoneId, serviceOfferingId, templateId, hostId);
	              }
			};
			thread2.start();
		}
	}
}
