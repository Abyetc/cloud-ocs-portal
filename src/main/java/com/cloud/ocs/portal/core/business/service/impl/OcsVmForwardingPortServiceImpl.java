package com.cloud.ocs.portal.core.business.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloud.ocs.portal.common.bean.OcsVmForwardingPort;
import com.cloud.ocs.portal.common.cache.OcsVmForwardingPortCache;
import com.cloud.ocs.portal.common.dao.OcsVmForwardingPortDao;
import com.cloud.ocs.portal.core.business.service.OcsVmForwardingPortService;
import com.cloud.ocs.portal.utils.RandomNumUtil;

/**
 * 虚拟机转发端口service实现类
 * 
 * @author Wang Chao
 *
 * @date 2015-1-13 下午2:51:17
 *
 */
@Transactional(value="portal_em")
@Service
public class OcsVmForwardingPortServiceImpl implements OcsVmForwardingPortService {
	
	private static final Integer MIN_PORT = 1024;
	private static final Integer MAX_PORT = 65535;
	
	@Resource
	private OcsVmForwardingPortDao vmForwardingPortDao;
	
	@Autowired
	private OcsVmForwardingPortCache vmForwardingPortCache;

	@Override
	public Integer generateUniquePublicPort(String networkId) {
		List<Integer> usedPublicPorts = this.getAllPublicPortInNetwork(networkId);
		
		Integer randomPort = RandomNumUtil.randInt(MIN_PORT, MAX_PORT);
		while (checkPortUsed(randomPort, usedPublicPorts)) {
			randomPort = RandomNumUtil.randInt(MIN_PORT, MAX_PORT);
		}
		
		return randomPort;
	}
	
	private List<Integer> getAllPublicPortInNetwork(String networkId) {
		List<Integer> result = new ArrayList<Integer>();
		
		result.addAll(vmForwardingPortDao.findAllMonitorPublicPortInNetwork(networkId));
		result.addAll(vmForwardingPortDao.findAllSshPublicPortInNetwork(networkId));
		
		return result;
		
	}
	
	private boolean checkPortUsed(Integer port, List<Integer> usedPublicPorts) {
		boolean result = false;
		
		for (Integer one : usedPublicPorts) {
			if (port.equals(one)) {
				result = true;
				break;
			}
		}
		
		return result;
	}

	@Override
	public void saveForwardingPort(String networkId, String publicIp,
			String publicIpId, String vmId,
			Integer monitorPublicPort,
			Integer monitorPrivatePort, Integer sshPublicPort, Integer sshPrivatePort) {
		OcsVmForwardingPort model = new OcsVmForwardingPort();
		model.setNetworkId(networkId);
		model.setPublicIp(publicIp);
		model.setPublicIpId(publicIpId);
		model.setVmId(vmId);
		model.setMonitorPublicPort(monitorPublicPort);
		model.setMonitorPrivatePort(monitorPrivatePort);
		model.setSshPublicPort(sshPublicPort);
		model.setSshPrivatePort(sshPrivatePort);
		
		vmForwardingPortDao.persist(model);
	}

	@Override
	public OcsVmForwardingPort getVmForwardingPortByVmId(String vmId) {
		OcsVmForwardingPort result = null;

		Map<String, OcsVmForwardingPort> vmForwardingPortMapByVmId = vmForwardingPortCache
				.getVmForwardingPortMapByVmId();
		if (vmForwardingPortMapByVmId != null) {
			result = vmForwardingPortMapByVmId.get(vmId);
		}

		return result;
	}

	@Override
	public List<OcsVmForwardingPort> getVmForwardingPortListByCityId(Integer cityId) {
		List<OcsVmForwardingPort> result = null;

		Map<Integer, List<OcsVmForwardingPort>> vmForwardingPortMapByCityId = vmForwardingPortCache
				.getVmForwardingPortMapByCityId();
		if (vmForwardingPortMapByCityId != null) {
			result = vmForwardingPortMapByCityId.get(cityId);
		}

		return result;
	}

	@Override
	public List<OcsVmForwardingPort> getVmForwardingPortListByNetworkId(
			String networkId) {
		List<OcsVmForwardingPort> result = null;
		
		Map<String, List<OcsVmForwardingPort>> vmForwardingPortMapByNetworkId = vmForwardingPortCache.getVmForwardingPortMapByNetworkId();
		if (vmForwardingPortMapByNetworkId != null) {
			result = vmForwardingPortMapByNetworkId.get(networkId);
		}
		
		return result;
	}

}
