package com.cloud.ocs.ha.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloud.ocs.portal.common.bean.OcsVm;
import com.cloud.ocs.portal.common.bean.WarmStandbyOcsVm;
import com.cloud.ocs.portal.common.dao.OcsVmDao;
import com.cloud.ocs.portal.common.dao.WarmStandbyOcsVmDao;
import com.cloud.ocs.portal.core.business.service.CityNetworkService;
import com.cloud.ocs.portal.core.business.service.OcsVmService;

@Transactional(value="portal_em")
@Service
public class OcsVmTackOverService {
	
	@Resource
	private WarmStandbyOcsVmDao warmStandbyOcsVmDao;
	
	@Resource
	private OcsVmDao ocsVmDao;
	
	@Resource
	private OcsVmService ocsVmService;
	
	@Resource
	private CityNetworkService cityNetworkService;

	/**
	 * 使用温备虚拟机接管原有故障Host上的虚拟机
	 * @param vmIdOnFailureHost
	 * @param warmStandbyOcsVm
	 */
	public void tackOverOcsVm(String vmIdOnFailureHost, WarmStandbyOcsVm warmStandbyOcsVm) {
		warmStandbyOcsVmDao.removeByVmId(warmStandbyOcsVm.getVmId());
		ocsVmDao.persist(transfer(warmStandbyOcsVm));
		
		//将温备虚拟机添加到负载均衡规则中
		String networkId = warmStandbyOcsVm.getNetworkId();
		String publicIpId = warmStandbyOcsVm.getPublicIpId();
		String vmId = warmStandbyOcsVm.getVmId();
		String networkName = cityNetworkService.getCityNetworkByNetworkId(networkId).getNetworkName();
		ocsVmService.checkAndAddVmToLoadBalancerRule(publicIpId, vmId, networkId, networkName);
		
		//删除Host上的故障虚拟机
		ocsVmService.removeOcsVm(vmIdOnFailureHost);
	}
	
	private OcsVm transfer( WarmStandbyOcsVm warmStandbyOcsVm) {
		OcsVm ocsVm = new OcsVm();
		
		ocsVm.setVmId(warmStandbyOcsVm.getVmId());
		ocsVm.setState(warmStandbyOcsVm.getState());
		ocsVm.setPublicIpId(warmStandbyOcsVm.getPublicIpId());
		ocsVm.setPublicIp(warmStandbyOcsVm.getPublicIp());
		ocsVm.setPrivateIp(warmStandbyOcsVm.getPrivateIp());
		ocsVm.setNetworkId(warmStandbyOcsVm.getNetworkId());
		ocsVm.setHsotId(warmStandbyOcsVm.getHostId());
		ocsVm.setHostName(warmStandbyOcsVm.getHostName());
		ocsVm.setCreated(warmStandbyOcsVm.getCreated());
		
		return ocsVm;
	}
}
