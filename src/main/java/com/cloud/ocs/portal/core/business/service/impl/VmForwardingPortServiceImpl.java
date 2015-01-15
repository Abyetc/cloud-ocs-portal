package com.cloud.ocs.portal.core.business.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloud.ocs.portal.core.business.bean.VmForwardingPort;
import com.cloud.ocs.portal.core.business.dao.VmForwardingPortDao;
import com.cloud.ocs.portal.core.business.service.VmForwardingPortService;
import com.cloud.ocs.portal.utils.RandomNumUtil;

/**
 * 虚拟机转发端口service实现类
 * 
 * @author Wang Chao
 *
 * @date 2015-1-13 下午2:51:17
 *
 */
@Transactional
@Service
public class VmForwardingPortServiceImpl implements VmForwardingPortService {
	
	private static final Integer MIN_PORT = 1024;
	private static final Integer MAX_PORT = 65535;
	
	@Resource
	private VmForwardingPortDao vmForwardingPortDao;

	@Override
	public Integer generateUniquePublicPort(String networkId) {
		List<Integer> usedPublicPorts = vmForwardingPortDao.findAllPublicPortInNetwork(networkId);
		
		Integer randomPort = RandomNumUtil.randInt(MIN_PORT, MAX_PORT);
		while (checkPortUsed(randomPort, usedPublicPorts)) {
			randomPort = RandomNumUtil.randInt(MIN_PORT, MAX_PORT);
		}
		
		return randomPort;
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
			Integer publicPort, Integer privatePort) {
		VmForwardingPort model = new VmForwardingPort();
		model.setNetworkId(networkId);
		model.setPublicIp(publicIp);
		model.setPublicIpId(publicIpId);
		model.setVmId(vmId);
		model.setPublicPort(publicPort);
		model.setPrivatePort(privatePort);
		
		vmForwardingPortDao.persist(model);
	}

	@Override
	public VmForwardingPort getVmForwardingPortByVmId(String vmId) {
		
		return vmForwardingPortDao.findByVmId(vmId);
	}

	@Override
	public List<VmForwardingPort> getVmForwardingPortListByCityId(String cityId) {
		
		return vmForwardingPortDao.findByCityId(cityId);
	}

	@Override
	public List<VmForwardingPort> getVmForwardingPortListByNetworkId(
			String networkId) {
		
		return vmForwardingPortDao.findByNetworkId(networkId);
	}

}
