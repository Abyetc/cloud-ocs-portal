package com.cloud.ocs.portal.core.business.service;

import java.util.List;

import com.cloud.ocs.portal.core.business.bean.VmForwardingPort;

/**
 * 虚拟机转发端口service接口
 * 
 * @author Wang Chao
 *
 * @date 2015-1-13 下午2:50:12
 *
 */
public interface VmForwardingPortService {

	/**
	 * 得到该网络中一个还未被使用的公共端口用于端口转发
	 * @param networkId
	 * @return
	 */
	public Integer generateUniquePublicPort(String networkId);
	
	/**
	 * 保存端口转发规则
	 * @param networkId
	 * @param publicIp
	 * @param publicIpId
	 * @param vmId
	 * @param publicPort
	 * @param privatePort
	 */
	public void saveForwardingPort(String networkId, String publicIp,
			String publicIpId, String vmId, Integer publicPort,
			Integer privatePort);
	
	/**
	 * 得到虚拟机的端口转发规则
	 * @param vmId
	 * @return
	 */
	public VmForwardingPort getVmForwardingPortByVmId(String vmId);
	
	/**
	 * 得到某一城市下所有虚拟机的端口转发规则
	 * @param cityId
	 * @return
	 */
	public List<VmForwardingPort> getVmForwardingPortListByCityId(String cityId);
	
	/**
	 * 的到某一城市的某一网络下所有虚拟机的端口转发规则
	 * @param networkId
	 * @return
	 */
	public List<VmForwardingPort> getVmForwardingPortListByNetworkId(String networkId);
}
