package com.cloud.ocs.portal.common.dao.impl;

import java.util.Iterator;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.cloud.ocs.portal.common.bean.CityNetwork;
import com.cloud.ocs.portal.common.dao.CityNetworkDao;

/**
 * 城市-网络Dao实现类
 * 
 * @author Wang Chao
 *
 * @date 2014-12-31 下午12:01:13
 *
 */
@Repository
public class CityNetworkDaoImpl extends GenericDaoImpl<CityNetwork> implements CityNetworkDao {

	@Override
	public List<CityNetwork> findCityNetworksByCityId(Integer cityId) {
		Query query = em.createQuery("select model from CityNetwork model where model.cityId='" + cityId + "'");
		
		return query.getResultList();
	}

	@Override
	public CityNetwork findCityNetworkByNetworkId(String networkId) {
		Query query = em.createQuery("select model from CityNetwork model where model.networkId='" + networkId + "'");
		List result = query.getResultList();
		Iterator iterator = result.iterator();
		while( iterator.hasNext() ) {
			return (CityNetwork)iterator.next();
		}
		
		return null;
	}

	@Override
	public List<CityNetwork> findAll() {
		Query query = em.createQuery("select model from CityNetwork model");
		
		return query.getResultList();
	}

	@Override
	public List<String> findAllPublicIpsOfCity(Integer cityId) {
		Query query = em.createQuery("select DISTINCT model.publicIp from CityNetwork model "
				+ "where model.cityId=" + cityId);
		
		return query.getResultList();
	}

	@Override
	public int removeByNetworkId(String networkId) {
		Query query = em.createQuery("delete from CityNetwork model where model.networkId='" + networkId + "'");
		
		return query.executeUpdate();
	}
	
	

}
