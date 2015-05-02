package com.cloud.ocs.ha.service;

import java.util.List;

import com.cloud.ocs.portal.common.bean.WarmStandbyOcsVm;

public interface WarmStandbyOcsVmService {

	/**
	 * 获取所有可用的温备虚拟机
	 * @return
	 */
	public List<WarmStandbyOcsVm> getAllSuitableWarmStandbyOcsVmList();
	
	public void addWarmStandbyOcsVm(String networkId);
	
	public WarmStandbyOcsVm getWarmStandbyOcsVmById(String vmId);
	
	public long getWarmStandbyOcsVmNumInNetwork(String networkId);
	
}
