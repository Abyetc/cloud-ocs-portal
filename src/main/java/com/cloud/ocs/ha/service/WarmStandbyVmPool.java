package com.cloud.ocs.ha.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloud.ocs.portal.common.bean.CityNetwork;
import com.cloud.ocs.portal.common.bean.WarmStandbyOcsVm;
import com.cloud.ocs.portal.core.business.constant.NetworkState;
import com.cloud.ocs.portal.core.business.service.CityNetworkService;

@Transactional(value="portal_em")
@Service
public class WarmStandbyVmPool {

	private final static long POOL_SIZE = 1;

	private List<WarmStandbyOcsVm> warmStandbyOcsVms;

	@Resource
	private WarmStandbyOcsVmService warmStandbyOcsVmService;

	@Resource
	private CityNetworkService cityNetworkService;

	public List<WarmStandbyOcsVm> getAllWarmStandbyOcsVms() {
		return null;
	}

	public List<WarmStandbyOcsVm> getFixedNumWarmStandbyOcsVms(int num) {
		return null;
	}

	public int curAvailableWarmStandbyOcsVmNum() {
		return 0;
	}

	public void fillWarmStandbyVmPoolJob() {
		List<CityNetwork> allNetworks = cityNetworkService.getAllCityNetwork();
		if (allNetworks == null || allNetworks.size() == 0) {
			return;
		}

		for (CityNetwork cityNetwork : allNetworks) {
			String networkId = cityNetwork.getNetworkId();
			
			if (isFull(networkId)) {
				continue;
			}
			
//			if (cityNetwork.getNetworkState().equals(NetworkState.IMPLEMENTED.getCode()) ) {
//				fillWarmStandbyVmPool(networkId);
//			}
			fillWarmStandbyVmPool(networkId);
		}
	}

	public boolean isFull(String networkId) {
		long warmStandybyOcsVmNum = warmStandbyOcsVmService
				.getWarmStandbyOcsVmNumInNetwork(networkId);
		
		if (warmStandybyOcsVmNum == POOL_SIZE) {
			return true;
		}
		
		return false;
	}
	
	private void fillWarmStandbyVmPool(final String networkId) {
		long curWarmStandybyOcsVmNum = warmStandbyOcsVmService
				.getWarmStandbyOcsVmNumInNetwork(networkId);
		
		while (curWarmStandybyOcsVmNum < POOL_SIZE) {
			//多线程
			Thread thread = new Thread() {
	              public void run() {
	            	  warmStandbyOcsVmService.addWarmStandbyOcsVm(networkId);
	              }
			};
			thread.start();
			curWarmStandybyOcsVmNum++;
		}
	}
}
