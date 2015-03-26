package com.cloud.ocs.ha.job;

import javax.annotation.Resource;

import com.cloud.ocs.ha.service.OcsVmTackOverService;
import com.cloud.ocs.ha.service.WarmStandbyVmPool;
import com.cloud.ocs.portal.core.business.service.OcsEngineService;

public class OcsHostReliabilityJob {

	@Resource
	private WarmStandbyVmPool warmStandbyVmPool;
	
	@Resource
	private OcsEngineService ocsHostService;
	
	@Resource
	private OcsVmTackOverService ocsVmTackOverService;
	
	public void executeHostReliabilityJob() {
		
	}
}
