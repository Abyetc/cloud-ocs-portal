package com.cloud.ocs.portal.core.business.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cloud.ocs.portal.core.business.bean.City;
import com.cloud.ocs.portal.core.business.bean.CityNetwork;
import com.cloud.ocs.portal.core.business.dto.AddCityDto;
import com.cloud.ocs.portal.core.business.dto.AddCityNetworkDto;
import com.cloud.ocs.portal.core.business.service.CityNetworkService;
import com.cloud.ocs.portal.core.business.service.CityService;

/**
 * 城市云服务相关功能入口Controller
 * 
 * @author Wang Chao
 *
 * @date 2014-12-29 下午8:09:00
 *
 */
@Controller
@RequestMapping(value="/business")
public class CityBusinessController {
	
	@Resource
	private CityService cityService;
	
	@Resource
	private CityNetworkService cityNetworkService;
	
	@RequestMapping(value="/listCities", method=RequestMethod.GET)
	@ResponseBody
	public List<City> listCities() {
		return cityService.getCitiesList();
	}
	
	@RequestMapping(value="/addCity", method=RequestMethod.POST)
	@ResponseBody
	public AddCityDto addCity(@RequestParam("cityName") String cityName,
			@RequestParam("cityDescription") String cityDescription) {
		City city = new City();
		city.setName(cityName);
		city.setDescription(cityDescription);
		return cityService.addCity(city);
	}
	
	@RequestMapping(value="/listCityNetworks", method=RequestMethod.GET)
	@ResponseBody
	public List<CityNetwork> listCityNetworks(@RequestParam("cityId") Integer cityId) {
		return cityNetworkService.getCityNetworksList(cityId);
	}
	
	@RequestMapping(value="/addCityNetwork", method=RequestMethod.POST)
	@ResponseBody
	public AddCityNetworkDto addCityNetwork(@RequestParam("cityId") Integer cityId,
			@RequestParam("networkName") String networkName,
			@RequestParam("realmName") String realmName,
			@RequestParam("zoneId") String zoneId,
			@RequestParam("networkOfferingId") String networkOfferingId) {
		CityNetwork cityNetwork = new CityNetwork();
		cityNetwork.setCityId(cityId);
		cityNetwork.setNetworkName(networkName);
		cityNetwork.setRealmName(realmName);
		cityNetwork.setZoneId(zoneId);
		cityNetwork.setNetworkOfferingId(networkOfferingId);
		
		return cityNetworkService.addCityNetwork(cityNetwork);
	}
}
