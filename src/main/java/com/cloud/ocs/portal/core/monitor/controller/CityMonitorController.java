package com.cloud.ocs.portal.core.monitor.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cloud.ocs.portal.core.monitor.dto.RxbpsTxbpsDto;
import com.cloud.ocs.portal.core.monitor.service.CityMonitorService;

/**
 * 监控城市Controller
 * 
 * @author Wang Chao
 *
 * @date 2015-1-15 上午10:41:46
 *
 */
@Controller
@RequestMapping(value="/monitor/city")
public class CityMonitorController {

	@Resource
	private CityMonitorService cityMonitorService;
	
	@RequestMapping(value="/getCityRxbpsTxbps", method=RequestMethod.GET)
	@ResponseBody
	public RxbpsTxbpsDto getCityRxbpsTxbps(@RequestParam("cityId") Integer cityId,
			@RequestParam("interfaceName") String interfaceName) {
		return cityMonitorService.getCityRxbpsTxbps(cityId, interfaceName);
	}
	
	@RequestMapping(value="/getCityConcurrencyRequestNum", method=RequestMethod.GET)
	@ResponseBody
	public Long getCityConcurrencyRequestNum(@RequestParam("cityId") Integer cityId) {
		return cityMonitorService.getCityConcurrencyRequestNum(cityId);
	}
	
	@RequestMapping(value="/getCityNetworkRxbpsTxbps", method=RequestMethod.GET)
	@ResponseBody
	public RxbpsTxbpsDto getCityNetworkRxbpsTxbps(@RequestParam("cityNetworkId") String cityNetworkId,
			@RequestParam("interfaceName") String interfaceName) {
		return cityMonitorService.getCityNetworkRxbpsTxbps(cityNetworkId, interfaceName);
	}
	
	@RequestMapping(value="/getCityNetworkConcurrencyRequestNum", method=RequestMethod.GET)
	@ResponseBody
	public Long getCityNetworkConcurrencyRequestNum(@RequestParam("cityNetworkId") String cityNetworkId) {
		return cityMonitorService.getCityNetworkConcurrencyRequestNum(cityNetworkId);
	}
}
