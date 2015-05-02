package com.cloud.ocs.portal.common.dao;

import java.util.List;

import com.cloud.ocs.monitor.common.GenericDao;
import com.cloud.ocs.portal.common.bean.WarmStandbyOcsVm;

public interface WarmStandbyOcsVmDao extends GenericDao<WarmStandbyOcsVm>{

	public List<WarmStandbyOcsVm> findAll();
	
	public WarmStandbyOcsVm findById();
	
	public long findWarmStandbyOcsVmNumInNetwork(String networkId);
	
	public List<WarmStandbyOcsVm> findAllAvailableWarmStandbyOcsVm();
	
	public int removeByVmId(String vmId);
	
}
