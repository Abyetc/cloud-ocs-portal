package com.cloud.ocs.ha.service;

import java.util.List;

import com.cloud.ocs.portal.common.bean.WarmStandbyOcsVm;

public interface WarmStandbyOcsVmService {

	public List<WarmStandbyOcsVm> getAllWarmStandbyOcsVm();
	
	public void addWarmStandbyOcsVm(String networkId);
	
	public WarmStandbyOcsVm getWarmStandbyOcsVmById(String vmId);
	
	public long getWarmStandbyOcsVmNumInNetwork(String networkId);
}
