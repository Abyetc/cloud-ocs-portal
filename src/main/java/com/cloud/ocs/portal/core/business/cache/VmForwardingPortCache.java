package com.cloud.ocs.portal.core.business.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cloud.ocs.portal.core.business.bean.VmForwardingPort;
import com.cloud.ocs.portal.core.business.dao.VmForwardingPortDao;
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
public class VmForwardingPortCache {
	
	@Resource
	private VmForwardingPortDao vmForwardingPortDao;
	
	@Resource
	private CityNetworkService cityNetworkService;

	private Map<String, VmForwardingPort> vmForwardingPortMapByVmId;
	private Map<String, List<VmForwardingPort>> vmForwardingPortMapByNetworkId;
	private Map<Integer, List<VmForwardingPort>> vmForwardingPortMapByCityId;
	
	public Map<String, VmForwardingPort> getVmForwardingPortMapByVmId() {
		if (vmForwardingPortMapByVmId == null) {
			List<VmForwardingPort> vmForwardingPortList = vmForwardingPortDao.findAll();
			loadVmForwardingPortMapByVmIdFromDB(vmForwardingPortList);
		}
		
		return vmForwardingPortMapByVmId;
	}
	
	public Map<String, List<VmForwardingPort>> getVmForwardingPortMapByNetworkId() {
		if (vmForwardingPortMapByNetworkId == null) {
			List<VmForwardingPort> vmForwardingPortList = vmForwardingPortDao.findAll();
			loadVmForwardingPortMapByNetworkIdFromDB(vmForwardingPortList);
		}
		
		return vmForwardingPortMapByNetworkId;
	}
	
	public Map<Integer, List<VmForwardingPort>> getVmForwardingPortMapByCityId() {
		if (vmForwardingPortMapByCityId == null) {
			List<VmForwardingPort> vmForwardingPortList = vmForwardingPortDao.findAll();
			loadVmForwardingPortMapByCityIdFromDB(vmForwardingPortList);
		}
		
		return vmForwardingPortMapByCityId;
	}
	
	public void reloadDataFromDB() {
		List<VmForwardingPort> vmForwardingPortList = vmForwardingPortDao.findAll();
		loadVmForwardingPortMapByVmIdFromDB(vmForwardingPortList);
		loadVmForwardingPortMapByNetworkIdFromDB(vmForwardingPortList);
		loadVmForwardingPortMapByCityIdFromDB(vmForwardingPortList);
	}
	
	private void loadVmForwardingPortMapByVmIdFromDB(List<VmForwardingPort> vmForwardingPortList) {
		if (vmForwardingPortList != null) {
			vmForwardingPortMapByVmId = new TreeMap<String, VmForwardingPort>();
			for (VmForwardingPort vmForwardingPort : vmForwardingPortList) {
				vmForwardingPortMapByVmId.put(vmForwardingPort.getVmId(), vmForwardingPort);
			}
		}
	}
	
	private void loadVmForwardingPortMapByNetworkIdFromDB(List<VmForwardingPort> vmForwardingPortList) {
		if (vmForwardingPortList != null) {
			vmForwardingPortMapByNetworkId = new TreeMap<String, List<VmForwardingPort>>();
			for (VmForwardingPort vmForwardingPort : vmForwardingPortList) {
				String networkId = vmForwardingPort.getNetworkId();
				if (vmForwardingPortMapByNetworkId.containsKey(networkId)) {
					List<VmForwardingPort> oldVmForwardingPortList = vmForwardingPortMapByNetworkId.get(networkId);
					oldVmForwardingPortList.add(vmForwardingPort);
					vmForwardingPortMapByNetworkId.put(networkId, oldVmForwardingPortList);
				}
				else {
					List<VmForwardingPort> newVmForwardingPortList = new ArrayList<VmForwardingPort>();
					newVmForwardingPortList.add(vmForwardingPort);
					vmForwardingPortMapByNetworkId.put(networkId, newVmForwardingPortList);
				}
			}
		}
	}
	
	private void loadVmForwardingPortMapByCityIdFromDB(List<VmForwardingPort> vmForwardingPortList) {
		if (vmForwardingPortList != null) {
			vmForwardingPortMapByCityId = new TreeMap<Integer, List<VmForwardingPort>>();
			Map<String, Integer> networkIdCityIdMap = cityNetworkService.getNetworkIdCityIdMap();
			for (VmForwardingPort vmForwardingPort : vmForwardingPortList) {
				Integer cityId = networkIdCityIdMap.get(vmForwardingPort.getNetworkId());
				if (vmForwardingPortMapByCityId.containsKey(cityId)) {
					List<VmForwardingPort> oldVmForwardingPortList = vmForwardingPortMapByCityId.get(cityId);
					oldVmForwardingPortList.add(vmForwardingPort);
					vmForwardingPortMapByCityId.put(cityId, oldVmForwardingPortList);
				}
				else {
					List<VmForwardingPort> newVmForwardingPortList = new ArrayList<VmForwardingPort>();
					newVmForwardingPortList.add(vmForwardingPort);
					vmForwardingPortMapByCityId.put(cityId, newVmForwardingPortList);
				}
			}
		}
	}
}
