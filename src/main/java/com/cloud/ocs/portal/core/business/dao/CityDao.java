package com.cloud.ocs.portal.core.business.dao;

import java.util.List;

import com.cloud.ocs.portal.common.dao.GenericDao;
import com.cloud.ocs.portal.core.business.bean.City;

/**
 * 城市Dao接口
 * 
 * @author Wang Chao
 *
 * @date 2014-12-29 下午7:24:20
 *
 */
public interface CityDao extends GenericDao<City> {
	
	public List<City> findAll();
}
