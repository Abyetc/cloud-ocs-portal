package com.cloud.ocs.ha.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.cloud.ocs.ha.service.WarmStandbyOcsVmService;
import com.cloud.ocs.portal.common.bean.WarmStandbyOcsVm;
import com.cloud.ocs.portal.common.dao.WarmStandbyOcsVmDao;

public class WarmStandbyOcsVmServiceImpl implements WarmStandbyOcsVmService{
	
	@Resource
	private  WarmStandbyOcsVmDao  warmStandbyOcsVmDao;

	public List<WarmStandbyOcsVm> getAllWarmStandbyOcsVm() {
		return null;
	}
	
	public void addWarmStandbyOcsVm(WarmStandbyOcsVm one) {
		
	}

	@Override
	public WarmStandbyOcsVm getWarmStandbyOcsVmById(String vmId) {
		// TODO Auto-generated method stub
		return null;
	}
}
