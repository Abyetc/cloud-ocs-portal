package com.cloud.ocs.portal.common.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.cloud.ocs.portal.common.bean.WarmStandbyOcsVm;
import com.cloud.ocs.portal.common.dao.WarmStandbyOcsVmDao;

@Repository
public class WarmStandbyOcsVmDaoImpl extends GenericDaoImpl<WarmStandbyOcsVm> implements WarmStandbyOcsVmDao {

	@Override
	public List<WarmStandbyOcsVm> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WarmStandbyOcsVm findById() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long findWarmStandbyOcsVmNumInNetwork(String networkId) {
		Query query = em
				.createQuery("select count(*) from WarmStandbyOcsVm model where model.networkId='" + networkId + "'");
		
		return (Long)query.getSingleResult();
	}

	@Override
	public List<WarmStandbyOcsVm> findAllAvailableWarmStandbyOcsVm() {
		Query query = em
				.createQuery("select model from WarmStandbyOcsVm model where model.state=1");
		
		return query.getResultList();
	}

	@Override
	public int removeByVmId(String vmId) {
		Query query = em.createQuery("delete from WarmStandbyOcsVm model where model.vmId='" + vmId + "'");
		
		return query.executeUpdate();
	}

}
