package com.cloud.ocs.portal.core.business.dao;

import java.util.List;

import com.cloud.ocs.portal.common.dao.GenericDao;
import com.cloud.ocs.portal.core.business.bean.VmForwardingPort;

/**
 * 虚拟机转发端口Dao接口
 * 
 * @author Wang Chao
 *
 * @date 2015-1-13 下午2:46:40
 *
 */
public interface VmForwardingPortDao extends GenericDao<VmForwardingPort> {

	/**
	 * 找到在特定network中已经被使用的public port
	 * @param networkId
	 * @return
	 */
	public List<Integer> findAllPublicPortInNetwork(String networkId);
	
	/**
	 * 得到虚拟机的端口转发规则
	 * @param vmId
	 * @return
	 */
	public VmForwardingPort findByVmId(String vmId);
	
	/**
	 * 得到某一城市下所有虚拟机的端口转发规则
	 * @param cityId
	 * @return
	 */
	public List<VmForwardingPort> findByCityId(String cityId);
	
	/**
	 * 得到某一城市的某一具体网络的端口转发规则
	 * @param networkId
	 * @return
	 */
	public List<VmForwardingPort> findByNetworkId(String networkId);
}
