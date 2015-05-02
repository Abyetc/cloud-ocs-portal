package com.cloud.ocs.ha.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloud.ocs.portal.common.bean.CityNetwork;
import com.cloud.ocs.portal.common.bean.WarmStandbyOcsVm;
import com.cloud.ocs.portal.core.business.service.CityNetworkService;
import com.cloud.ocs.portal.core.business.service.OcsVmService;

@Transactional(value="portal_em")
@Service
public class WarmStandbyVmPool {

	private final static long POOL_SIZE = 1;

	private List<WarmStandbyOcsVm> warmStandbyOcsVms;

	@Resource
	private WarmStandbyOcsVmService warmStandbyOcsVmService;

	@Resource
	private CityNetworkService cityNetworkService;
	
	@Resource
	private OcsVmService ocsVmService;

	/**
	 * 根据出传入的主机上虚拟机Id列表，选取合适的温备虚拟机
	 * @param vmIdsOnFailureHost
	 * @return
	 */
	public List<WarmStandbyOcsVm> getWarmStandbyOcsVms(List<String> vmIdsOnFailureHost) {
		List<WarmStandbyOcsVm> allAvailableWarmStandbyOcsVms = warmStandbyOcsVmService.getAllSuitableWarmStandbyOcsVmList();
		
		if (allAvailableWarmStandbyOcsVms == null) {
			return null;
		}
		
		Map<String, List<WarmStandbyOcsVm>> warmStandbyOcsVmMap = new HashMap<String, List<WarmStandbyOcsVm>>();
		for (WarmStandbyOcsVm oneWarmStandbyOcsVm : allAvailableWarmStandbyOcsVms) {
			String networkId = oneWarmStandbyOcsVm.getNetworkId();
			if (warmStandbyOcsVmMap.containsKey(networkId)) {
				warmStandbyOcsVmMap.get(networkId).add(oneWarmStandbyOcsVm);
			}
			else {
				List<WarmStandbyOcsVm> newList = new ArrayList<WarmStandbyOcsVm>();
				newList.add(oneWarmStandbyOcsVm);
				warmStandbyOcsVmMap.put(networkId, newList);
			}
		}
		
		List<WarmStandbyOcsVm> result = new ArrayList<WarmStandbyOcsVm>();
		for (String vmIdOnFailureHost : vmIdsOnFailureHost) {
			String vmNetworkIdOnFailureHost = ocsVmService.getOcsVmByVmId(vmIdOnFailureHost).getNetworkId();
			WarmStandbyOcsVm selected = warmStandbyOcsVmMap.get(vmNetworkIdOnFailureHost).get(0);
			result.add(selected);
			warmStandbyOcsVmMap.get(vmNetworkIdOnFailureHost).remove(selected);
		}
		
		return result;
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
