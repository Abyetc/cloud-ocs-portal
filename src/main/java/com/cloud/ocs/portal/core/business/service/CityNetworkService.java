package com.cloud.ocs.portal.core.business.service;

import java.util.List;

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

	public CityNetwork getCityNetworkByNetworkId(String networkId);
	public List<CityNetworkListDto> getCityNetworksList(Integer cityId);
	public AddCityNetworkDto addCityNetwork(CityNetwork cityNetwork);
}
