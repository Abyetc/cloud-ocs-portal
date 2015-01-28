package com.cloud.ocs.portal.core.business.service;

import java.util.List;
import java.util.Map;

import com.cloud.ocs.portal.common.dto.OperateObjectDto;
import com.cloud.ocs.portal.core.business.bean.CityNetwork;
import com.cloud.ocs.portal.core.business.dto.AddCityNetworkDto;
import com.cloud.ocs.portal.core.business.dto.CityNetworkListDto;

/**
 * 城市-网络service接口
 * 
 * @author Wang Chao
 *
 * @date 2014-12-31 下午12:03:59
 *
 */
public interface CityNetworkService {

	/**
	 * 根据network id找到cityNetwork
	 * @param networkId
	 * @return
	 */
	public CityNetwork getCityNetworkByNetworkId(String networkId);
	
	/**
	 * 根据city id找到cityNetwork列表
	 * @param cityId
	 * @return
	 */
	public List<CityNetworkListDto> getCityNetworksList(Integer cityId);
	
	/**
	 * 得到city的所有公共IP(虚拟路由器IP)
	 * @param cityId
	 * @return
	 */
	public List<String> getAllPublicIpsOfCity(Integer cityId);
	
	/**
	 * 添加cityNetwork
	 * @param cityNetwork
	 * @return
	 */
	public AddCityNetworkDto addCityNetwork(CityNetwork cityNetwork);
	
	/**
	 * 删除cityNetwork
	 * @param networkId
	 * @return
	 */
	public OperateObjectDto removeCityNetwork(String networkId);
	
	/**
	 * 得到以network id为key, city id为value的map
	 * @return
	 */
	public Map<String, Integer> getNetworkIdCityIdMap();
}
