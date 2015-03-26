package com.cloud.ocs.ha.service;

import java.util.List;

import com.cloud.ocs.portal.common.bean.WarmStandbyOcsVm;

public interface WarmStandbyOcsVmService {

	public List<WarmStandbyOcsVm> getAllWarmStandbyOcsVm();
	
	public void addWarmStandbyOcsVm(WarmStandbyOcsVm one);
	
	public WarmStandbyOcsVm getWarmStandbyOcsVmById(String vmId);
}
