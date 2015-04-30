package com.cloud.ocs.ha.job;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cloud.ocs.portal.common.bean.OcsVm;
import com.cloud.ocs.portal.common.dao.OcsVmDao;
import com.cloud.ocs.portal.core.business.dto.OcsVmDto;
import com.cloud.ocs.portal.core.business.service.OcsVmService;

@Service
public class OcsVmReliabilityJob {

	@Resource
	private OcsVmService ocsVmSerivce;

	@Resource
	private OcsVmDao ocsVmDao;

	public void executeVmReliabilityJob(final String vmId) {
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
			Thread thread1 = new Thread() {
	              public void run() {
	            	  ocsVmSerivce.removeOcsVm(vmId);
	              }
			};
			thread1.start();
			Thread thread2 = new Thread() {
	              public void run() {
	            	  ocsVmSerivce.addOcsVm(newName, networkId,
	      					zoneId, serviceOfferingId, templateId);
	              }
			};
			thread2.start();
		}
	}
}
