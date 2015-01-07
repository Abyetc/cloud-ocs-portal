package com.cloud.ocs.portal.core.business.dao;

import java.util.List;

import com.cloud.ocs.portal.common.dao.GenericDao;
import com.cloud.ocs.portal.core.business.bean.CityNetwork;

/**
 * 城市-网络Dao接口
 * 
 * @author Wang Chao
 *
 * @date 2014-12-31 上午11:58:47
 *
 */
public interface CityNetworkDao extends GenericDao<CityNetwork> {

	public List<CityNetwork> findCityNetworksByCityId(Integer cityId);
	public CityNetwork findCityNetworkByNetworkId(String networkId);
}
