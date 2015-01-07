package com.cloud.ocs.portal.core.business.dao.impl;

import java.util.Iterator;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.cloud.ocs.portal.common.dao.impl.GenericDaoImpl;
import com.cloud.ocs.portal.core.business.bean.CityNetwork;
import com.cloud.ocs.portal.core.business.dao.CityNetworkDao;

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

}
