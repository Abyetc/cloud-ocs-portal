package com.cloud.ocs.portal.core.monitor.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cloud.ocs.portal.core.monitor.dto.MessageProcessTimeDto;
import com.cloud.ocs.portal.core.monitor.dto.MessageThroughputDto;
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
	
	@RequestMapping(value="/getCityRealtimeSessionNum", method=RequestMethod.GET)
	@ResponseBody
	public Long getCityRealtimeSessionNum(@RequestParam("cityId") Integer cityId) {
		return cityMonitorService.getRealtimeSessionNum(cityId);
	}
	
	@RequestMapping(value="/getCityMessageThroughput", method=RequestMethod.GET)
	@ResponseBody
	public MessageThroughputDto getCityMessageThroughput(@RequestParam("cityId") Integer cityId) {
		return cityMonitorService.getMessageThroughput(cityId);
	}
	
	@RequestMapping(value="/getCityMessageProcessTime", method=RequestMethod.GET)
	@ResponseBody
	public MessageProcessTimeDto getCityMessageProcessTime(@RequestParam("cityId") Integer cityId) {
		return cityMonitorService.getMessageProcessTime(cityId);
	}
	
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
}
