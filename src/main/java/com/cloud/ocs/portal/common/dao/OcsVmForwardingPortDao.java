package com.cloud.ocs.portal.common.dao;

import java.util.List;

import com.cloud.ocs.portal.common.bean.OcsVmForwardingPort;

/**
 * 虚拟机转发端口Dao接口
 * 
 * @author Wang Chao
 *
 * @date 2015-1-13 下午2:46:40
 *
 */
public interface OcsVmForwardingPortDao extends GenericDao<OcsVmForwardingPort> {
	
	/**
	 * 得到所有的端口转发规则
	 * @return
	 */
	public List<OcsVmForwardingPort> findAll();

	/**
	 * 找到在特定network中已经被使用的用于monitor的public port
	 * @param networkId
	 * @return
	 */
	public List<Integer> findAllMonitorPublicPortInNetwork(String networkId);
	
	/**
	 * 找到在特定network中已经被使用的用于ssh的public port
	 * @param networkId
	 * @return
	 */
	public List<Integer> findAllSshPublicPortInNetwork(String networkId);
	
	/**
	 * 得到虚拟机的端口转发规则
	 * @param vmId
	 * @return
	 */
	public OcsVmForwardingPort findByVmId(String vmId);
	
	/**
	 * 得到某一城市下所有虚拟机的端口转发规则
	 * @param cityId
	 * @return
	 */
	public List<OcsVmForwardingPort> findByCityId(String cityId);
	
	/**
	 * 得到某一城市的某一具体网络的端口转发规则
	 * @param networkId
	 * @return
	 */
	public List<OcsVmForwardingPort> findByNetworkId(String networkId);
	
	public int deleteByVmId(String vmId);
}
