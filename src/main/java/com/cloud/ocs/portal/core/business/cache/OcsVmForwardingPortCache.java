package com.cloud.ocs.portal.core.business.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cloud.ocs.portal.common.bean.OcsVmForwardingPort;
import com.cloud.ocs.portal.common.dao.OcsVmForwardingPortDao;
import com.cloud.ocs.portal.core.business.service.CityNetworkService;

/**
 * 用于缓存vm_forwarding_port表中的内容，以减少对数据库的频繁访问
 * 
 * @author Wang Chao
 *
 * @date 2015-1-17 下午5:20:28
 *
 */
@Service
public class OcsVmForwardingPortCache {
	
	@Resource
	private OcsVmForwardingPortDao vmForwardingPortDao;
	
	@Resource
	private CityNetworkService cityNetworkService;

	private Map<String, OcsVmForwardingPort> vmForwardingPortMapByVmId;
	private Map<String, List<OcsVmForwardingPort>> vmForwardingPortMapByNetworkId;
	private Map<Integer, List<OcsVmForwardingPort>> vmForwardingPortMapByCityId;
	
	public Map<String, OcsVmForwardingPort> getVmForwardingPortMapByVmId() {
		if (vmForwardingPortMapByVmId == null) {
			List<OcsVmForwardingPort> vmForwardingPortList = vmForwardingPortDao.findAll();
			loadVmForwardingPortMapByVmIdFromDB(vmForwardingPortList);
		}
		
		return vmForwardingPortMapByVmId;
	}
	
	public Map<String, List<OcsVmForwardingPort>> getVmForwardingPortMapByNetworkId() {
		if (vmForwardingPortMapByNetworkId == null) {
			List<OcsVmForwardingPort> vmForwardingPortList = vmForwardingPortDao.findAll();
			loadVmForwardingPortMapByNetworkIdFromDB(vmForwardingPortList);
		}
		
		return vmForwardingPortMapByNetworkId;
	}
	
	public Map<Integer, List<OcsVmForwardingPort>> getVmForwardingPortMapByCityId() {
		if (vmForwardingPortMapByCityId == null) {
			List<OcsVmForwardingPort> vmForwardingPortList = vmForwardingPortDao.findAll();
			loadVmForwardingPortMapByCityIdFromDB(vmForwardingPortList);
		}
		
		return vmForwardingPortMapByCityId;
	}
	
	public void reloadDataFromDB() {
		List<OcsVmForwardingPort> vmForwardingPortList = vmForwardingPortDao.findAll();
		loadVmForwardingPortMapByVmIdFromDB(vmForwardingPortList);
		loadVmForwardingPortMapByNetworkIdFromDB(vmForwardingPortList);
		loadVmForwardingPortMapByCityIdFromDB(vmForwardingPortList);
	}
	
	private void loadVmForwardingPortMapByVmIdFromDB(List<OcsVmForwardingPort> vmForwardingPortList) {
		if (vmForwardingPortList != null) {
			vmForwardingPortMapByVmId = new TreeMap<String, OcsVmForwardingPort>();
			for (OcsVmForwardingPort vmForwardingPort : vmForwardingPortList) {
				vmForwardingPortMapByVmId.put(vmForwardingPort.getVmId(), vmForwardingPort);
			}
		}
	}
	
	private void loadVmForwardingPortMapByNetworkIdFromDB(List<OcsVmForwardingPort> vmForwardingPortList) {
		if (vmForwardingPortList != null) {
			vmForwardingPortMapByNetworkId = new TreeMap<String, List<OcsVmForwardingPort>>();
			for (OcsVmForwardingPort vmForwardingPort : vmForwardingPortList) {
				String networkId = vmForwardingPort.getNetworkId();
				if (vmForwardingPortMapByNetworkId.containsKey(networkId)) {
					List<OcsVmForwardingPort> oldVmForwardingPortList = vmForwardingPortMapByNetworkId.get(networkId);
					oldVmForwardingPortList.add(vmForwardingPort);
					vmForwardingPortMapByNetworkId.put(networkId, oldVmForwardingPortList);
				}
				else {
					List<OcsVmForwardingPort> newVmForwardingPortList = new ArrayList<OcsVmForwardingPort>();
					newVmForwardingPortList.add(vmForwardingPort);
					vmForwardingPortMapByNetworkId.put(networkId, newVmForwardingPortList);
				}
			}
		}
	}
	
	private void loadVmForwardingPortMapByCityIdFromDB(List<OcsVmForwardingPort> vmForwardingPortList) {
		if (vmForwardingPortList != null) {
			vmForwardingPortMapByCityId = new TreeMap<Integer, List<OcsVmForwardingPort>>();
			Map<String, Integer> networkIdCityIdMap = cityNetworkService.getNetworkIdCityIdMap();
			for (OcsVmForwardingPort vmForwardingPort : vmForwardingPortList) {
				Integer cityId = networkIdCityIdMap.get(vmForwardingPort.getNetworkId());
				if (vmForwardingPortMapByCityId.containsKey(cityId)) {
					List<OcsVmForwardingPort> oldVmForwardingPortList = vmForwardingPortMapByCityId.get(cityId);
					oldVmForwardingPortList.add(vmForwardingPort);
					vmForwardingPortMapByCityId.put(cityId, oldVmForwardingPortList);
				}
				else {
					List<OcsVmForwardingPort> newVmForwardingPortList = new ArrayList<OcsVmForwardingPort>();
					newVmForwardingPortList.add(vmForwardingPort);
					vmForwardingPortMapByCityId.put(cityId, newVmForwardingPortList);
				}
			}
		}
	}
}
