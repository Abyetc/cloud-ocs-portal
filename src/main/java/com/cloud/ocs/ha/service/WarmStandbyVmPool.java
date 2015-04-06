package com.cloud.ocs.ha.service;

import java.util.List;

import javax.annotation.Resource;

import com.cloud.ocs.portal.common.bean.WarmStandbyOcsVm;
import com.cloud.ocs.portal.core.business.service.OcsVmService;

public class WarmStandbyVmPool {
	
	private final static int POOL_SIZE = 30;

	private List<WarmStandbyOcsVm> warmStandbyOcsVms;
	
	@Resource
	private WarmStandbyOcsVmService warmStandbyOcsVmService;
	
	@Resource
	private OcsVmService ocsVmService;
	
	public List<WarmStandbyOcsVm> getAllWarmStandbyOcsVms() {
		return null;
	}
	
	public List<WarmStandbyOcsVm> getFixedNumWarmStandbyOcsVms(int num) {
		return null;
	}
	
	public int curAvailableWarmStandbyOcsVmNum() {
		return 0;
	}
	
	public void fillWarmStandbyVmPool() {
		
	}
	
	public boolean isFull() {
		return false;
	}
}
