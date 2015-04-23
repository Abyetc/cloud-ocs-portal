package com.cloud.ocs.portal.core.business.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cloud.ocs.portal.common.bean.City;
import com.cloud.ocs.portal.common.bean.CityNetwork;
import com.cloud.ocs.portal.common.dto.OperateObjectDto;
import com.cloud.ocs.portal.core.business.dto.AddCityDto;
import com.cloud.ocs.portal.core.business.dto.AddCityNetworkDto;
import com.cloud.ocs.portal.core.business.dto.AddOcsVmDto;
import com.cloud.ocs.portal.core.business.dto.CityNetworkListDto;
import com.cloud.ocs.portal.core.business.dto.OcsVmDto;
import com.cloud.ocs.portal.core.business.service.CityNetworkService;
import com.cloud.ocs.portal.core.business.service.CityService;
import com.cloud.ocs.portal.core.business.service.OcsVmService;

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
	
	@Resource
	private OcsVmService ocsVmService;
	
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
	
	@RequestMapping(value="/removeCity", method=RequestMethod.GET)
	@ResponseBody
	public OperateObjectDto removeCity(@RequestParam("cityId") Integer cityId) {
		return cityService.removeCity(cityId);
	}
	
	@RequestMapping(value="/listCityNetworks", method=RequestMethod.GET)
	@ResponseBody
	public List<CityNetworkListDto> listCityNetworks(@RequestParam("cityId") Integer cityId) {
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
	
	@RequestMapping(value="/removeCityNetwork", method=RequestMethod.GET)
	@ResponseBody
	public OperateObjectDto removeCityNetwork(@RequestParam("cityNetworkId") String cityNetworkId) {
		return cityNetworkService.removeCityNetwork(cityNetworkId);
	}
	
	@RequestMapping(value="/listOcsVmsByNetwork", method=RequestMethod.GET)
	@ResponseBody
	public List<OcsVmDto> listOcsVmsByNetwork(@RequestParam("networkId") String networkId) {
		return ocsVmService.getOcsVmsListByNetworkId(networkId);
	}
	
	@RequestMapping(value="/listOcsVmsByCity", method=RequestMethod.GET)
	@ResponseBody
	public Map<String, List<OcsVmDto>> listOcsVmsByCity(@RequestParam("cityId") Integer cityId) {
		return ocsVmService.getOcsVmsListByCityId(cityId);
	}
	
	@RequestMapping(value="/addOcsVm", method=RequestMethod.POST)
	@ResponseBody
	public AddOcsVmDto addOcsVm(@RequestParam("vmName") String vmName,
			@RequestParam("networkId") String networkId,
			@RequestParam("zoneId") String zoneId,
			@RequestParam("serviceOfferingId") String serviceOfferingId,
			@RequestParam("templateId") String templateId) {
		return ocsVmService.addOcsVm(vmName, networkId, zoneId,
				serviceOfferingId, templateId);
	}
	
	@RequestMapping(value="/removeOcsVm", method=RequestMethod.GET)
	@ResponseBody
	public OperateObjectDto removeOcsVm(@RequestParam("vmId") String vmId) {
		return ocsVmService.removeOcsVm(vmId);
	}
	
	@RequestMapping(value="/stopOcsVm", method=RequestMethod.GET)
	@ResponseBody
	public OperateObjectDto stopOcsVm(@RequestParam("vmId") String vmId) {
		return ocsVmService.stopOcsVm(vmId);
	}
	
	@RequestMapping(value="/startOcsVm", method=RequestMethod.GET)
	@ResponseBody
	public OperateObjectDto startOcsVm(@RequestParam("vmId") String vmId) {
		return ocsVmService.startOcsVm(vmId);
	}
}
