package com.cloud.ocs.portal.core.business.dao.impl;

import java.util.Iterator;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.cloud.ocs.portal.common.dao.impl.GenericDaoImpl;
import com.cloud.ocs.portal.core.business.bean.VmForwardingPort;
import com.cloud.ocs.portal.core.business.dao.VmForwardingPortDao;

/**
 * 虚拟机转发端口Dao实现类
 * 
 * @author Wang Chao
 * 
 * @date 2015-1-13 下午2:48:59
 * 
 */
@Repository
public class VmForwardingPortDaoImpl extends GenericDaoImpl<VmForwardingPort>
		implements VmForwardingPortDao {

	@Override
	public List<Integer> findAllPublicPortInNetwork(String networkId) {
		Query query = em
				.createQuery("select model.publicPort from VmForwardingPort model where model.networkId='"
						+ networkId + "'");

		return query.getResultList();
	}

	@Override
	public VmForwardingPort findByVmId(String vmId) {
		Query query = em
				.createQuery("select model from VmForwardingPort model where model.vmId='"
						+ vmId + "'");
		List result = query.getResultList();
		Iterator iterator = result.iterator();
		while (iterator.hasNext()) {
			return (VmForwardingPort) iterator.next();
		}

		return null;
	}

	@Override
	public List<VmForwardingPort> findByCityId(String cityId) {
		Query query = em
				.createQuery("select vmForwardingPort from CityNetwork cityNetwork, VmForwardingPort vmForwardingPort where cityNetwork.cityId='"
						+ cityId + "' and cityNetwork.networkId = vmForwardingPort.networkId");
		
		return query.getResultList();
	}

	@Override
	public List<VmForwardingPort> findByNetworkId(String networkId) {
		Query query = em
				.createQuery("select model from VmForwardingPort model where model.networkId='"
						+ networkId + "'");

		return query.getResultList();
	}

}
