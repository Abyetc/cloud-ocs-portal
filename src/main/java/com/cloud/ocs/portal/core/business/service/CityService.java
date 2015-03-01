package com.cloud.ocs.portal.core.business.service;

import java.util.List;

import com.cloud.ocs.portal.common.bean.City;
import com.cloud.ocs.portal.core.business.dto.AddCityDto;

/**
 * 城市Service接口
 * 
 * @author Wang Chao
 *
 * @date 2014-12-29 下午7:28:03
 *
 */
public interface CityService {

	public List<City> getCitiesList();
	public AddCityDto addCity(City city);
}
