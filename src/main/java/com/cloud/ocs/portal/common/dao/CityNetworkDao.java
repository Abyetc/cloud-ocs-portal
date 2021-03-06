package com.cloud.ocs.portal.common.dao;

import java.util.List;

import com.cloud.ocs.portal.common.bean.CityNetwork;

/**
 * 城市-网络Dao接口
 * 
 * @author Wang Chao
 *
 * @date 2014-12-31 上午11:58:47
 *
 */
public interface CityNetworkDao extends GenericDao<CityNetwork> {

	public List<CityNetwork> findAll();
	public List<CityNetwork> findCityNetworksByCityId(Integer cityId);
	public List<String> findAllPublicIpsOfCity(Integer cityId);
	public CityNetwork findCityNetworkByNetworkId(String networkId);
	
	/**
	 * 根据network id从数据库中删除记录
	 * @param networkId
	 */
	public int removeByNetworkId(String networkId);
}
