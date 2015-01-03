package com.cloud.ocs.portal.core.business.dao.impl;

import java.util.Iterator;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.cloud.ocs.portal.common.dao.impl.GenericDaoImpl;
import com.cloud.ocs.portal.core.business.bean.City;
import com.cloud.ocs.portal.core.business.dao.CityDao;

/**
 * 城市Dao实现类
 * 
 * @author Wang Chao
 *
 * @date 2014-12-29 下午7:25:56
 *
 */
@Repository
public class CityDaoImpl extends GenericDaoImpl<City> implements CityDao {

	@Override
	public List<City> findAll() {
		Query query = em.createQuery("select city from City city");
		
		return query.getResultList();
	}

}
