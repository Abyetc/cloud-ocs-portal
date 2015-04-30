package com.cloud.ocs.portal.common.dao.impl;

import java.util.Iterator;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.cloud.ocs.portal.common.bean.OcsVmForwardingPort;
import com.cloud.ocs.portal.common.dao.OcsVmForwardingPortDao;

/**
 * 虚拟机转发端口Dao实现类
 * 
 * @author Wang Chao
 * 
 * @date 2015-1-13 下午2:48:59
 * 
 */
@Repository
public class OcsVmForwardingPortDaoImpl extends GenericDaoImpl<OcsVmForwardingPort>
		implements OcsVmForwardingPortDao {
	
	@Override
	public List<OcsVmForwardingPort> findAll() {
		Query query = em
				.createQuery("select model from OcsVmForwardingPort model");
		
		return query.getResultList();
	}

	@Override
	public List<Integer> findAllMonitorPublicPortInNetwork(String networkId) {
		Query query = em
				.createQuery("select model.monitorPublicPort from OcsVmForwardingPort model where model.networkId='"
						+ networkId + "'");

		return query.getResultList();
	}
	
	@Override
	public List<Integer> findAllSshPublicPortInNetwork(String networkId) {
		Query query = em
				.createQuery("select model.sshPublicPort from OcsVmForwardingPort model where model.networkId='"
						+ networkId + "'");
		
		return query.getResultList();
	}

	@Override
	public OcsVmForwardingPort findByVmId(String vmId) {
		Query query = em
				.createQuery("select model from OcsVmForwardingPort model where model.vmId='"
						+ vmId + "'");
		List result = query.getResultList();
		Iterator iterator = result.iterator();
		while (iterator.hasNext()) {
			return (OcsVmForwardingPort) iterator.next();
		}

		return null;
	}

	@Override
	public List<OcsVmForwardingPort> findByCityId(String cityId) {
		Query query = em
				.createQuery("select vmForwardingPort from CityNetwork cityNetwork, OcsVmForwardingPort vmForwardingPort where cityNetwork.cityId='"
						+ cityId + "' and cityNetwork.networkId = vmForwardingPort.networkId");
		
		return query.getResultList();
	}

	@Override
	public List<OcsVmForwardingPort> findByNetworkId(String networkId) {
		Query query = em
				.createQuery("select model from OcsVmForwardingPort model where model.networkId='"
						+ networkId + "'");

		return query.getResultList();
	}

	@Override
	public int deleteByVmId(String vmId) {
		Query query = em.createQuery("delete from OcsVmForwardingPort model where model.vmId='" + vmId + "'");
		
		return query.executeUpdate();
	}

}
