package com.cloud.ocs.portal.core.monitor.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cloud.ocs.portal.core.monitor.dto.MessageProcessTimeDto;
import com.cloud.ocs.portal.core.monitor.dto.MessageThroughputDto;
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
@RequestMapping(value = "/monitor/network")
public class CityNetworkMonitorController {
	
	private static DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");  

	@Resource
	private CityNetworkMonitorService cityNetworkMonitorService;

	@RequestMapping(value = "/getCityNetworkRealtimeSessionNum", method = RequestMethod.GET)
	@ResponseBody
	public Long getCityNetworkRealtimeSessionNum(
			@RequestParam("cityNetworkId") String cityNetworkId) {
		return cityNetworkMonitorService.getNetworkRealtimeSessionNum(cityNetworkId);
	}
	
	@RequestMapping(value = "/getCityNetworkMessageThroughput", method = RequestMethod.GET)
	@ResponseBody
	public MessageThroughputDto getCityNetworkRealtimeMessageThroughput(@RequestParam("cityNetworkId") String cityNetworkId) {
		return cityNetworkMonitorService.getNetworkRealtimeMessageThroughput(cityNetworkId);
	}

	@RequestMapping(value = "/getCityNetworkMessageProcessTime", method = RequestMethod.GET)
	@ResponseBody
	public MessageProcessTimeDto getCityNetworkRealtimeMessageProcessTime(
			@RequestParam("cityNetworkId") String cityNetworkId) {
		return cityNetworkMonitorService.getNetworkRealtimeMessageAverageProcessTime(cityNetworkId);
	}
	
	@RequestMapping(value = "/getCityNetworkHistorySessionNum", method = RequestMethod.GET)
	@ResponseBody
	public List<List<Object>> getCityNetworkHistorySessionNum(
			@RequestParam("cityNetworkId") String cityNetworkId,
			@RequestParam("date") String date) {
		Date date1 = null;
		try {
			date1 = dateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return cityNetworkMonitorService.getNetworkHistorySessionNum(cityNetworkId, date1);
	}
	
	@RequestMapping(value = "/getCityNetworkHistoryMessageThroughput", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, List<List<Object>>> getCityNetworkHistoryMessageThroughput(
			@RequestParam("cityNetworkId") String cityNetworkId,
			@RequestParam("date") String date) {
		Date date1 = null;
		try {
			date1 = dateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return cityNetworkMonitorService.getNetworkHistoryMessageThroughput(cityNetworkId, date1);
	}
	
	@RequestMapping(value = "/getCityNetworkHistoryMessageProcessTime", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, List<List<Object>>> getCityNetworkHistoryMessageProcessTime(
			@RequestParam("cityNetworkId") String cityNetworkId,
			@RequestParam("date") String date) {
		Date date1 = null;
		try {
			date1 = dateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return cityNetworkMonitorService.getNetworkHistoryMessageAverageProcessTime(cityNetworkId, date1);
	}
	

//	@RequestMapping(value = "/getCityNetworkRxbpsTxbps", method = RequestMethod.GET)
//	@ResponseBody
//	public RxbpsTxbpsDto getCityNetworkRxbpsTxbps(
//			@RequestParam("cityNetworkId") String cityNetworkId,
//			@RequestParam("interfaceName") String interfaceName) {
//		return cityNetworkMonitorService.getCityNetworkRxbpsTxbps(
//				cityNetworkId, interfaceName);
//	}
//
//	@RequestMapping(value = "/getCityNetworkConcurrencyRequestNum", method = RequestMethod.GET)
//	@ResponseBody
//	public Long getCityNetworkConcurrencyRequestNum(
//			@RequestParam("cityNetworkId") String cityNetworkId) {
//		return cityNetworkMonitorService
//				.getCityNetworkConcurrencyRequestNum(cityNetworkId);
//	}
}
