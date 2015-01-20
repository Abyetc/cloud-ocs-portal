package com.cloud.ocs.portal.core.monitor.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cloud.ocs.portal.core.monitor.dto.RxbpsTxbpsDto;
import com.cloud.ocs.portal.core.monitor.service.CityNetworkMonitorService;

/**
 * 监控城市-网络Controller
 * 
 * @author Wang Chao
 *
 * @date 2015-1-19 下午9:18:25
 *
 */
@Controller
@RequestMapping(value="/monitor/city")
public class CityNetworkMonitorController {
	
	@Resource
	private CityNetworkMonitorService cityNetworkMonitorService;

	@RequestMapping(value="/getCityNetworkRxbpsTxbps", method=RequestMethod.GET)
	@ResponseBody
	public RxbpsTxbpsDto getCityNetworkRxbpsTxbps(@RequestParam("cityNetworkId") String cityNetworkId,
			@RequestParam("interfaceName") String interfaceName) {
		return cityNetworkMonitorService.getCityNetworkRxbpsTxbps(cityNetworkId, interfaceName);
	}
	
	@RequestMapping(value="/getCityNetworkConcurrencyRequestNum", method=RequestMethod.GET)
	@ResponseBody
	public Long getCityNetworkConcurrencyRequestNum(@RequestParam("cityNetworkId") String cityNetworkId) {
		return cityNetworkMonitorService.getCityNetworkConcurrencyRequestNum(cityNetworkId);
	}
}
