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
	
	private static DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");  

	@Resource
	private CityMonitorService cityMonitorService;
	
	@RequestMapping(value="/getCityRealtimeSessionNum", method=RequestMethod.GET)
	@ResponseBody
	public Long getCityRealtimeSessionNum(@RequestParam("cityId") Integer cityId) {
		return cityMonitorService.getCityRealtimeSessionNum(cityId);
	}
	
	@RequestMapping(value="/getCityRealtimeMessageThroughput", method=RequestMethod.GET)
	@ResponseBody
	public MessageThroughputDto getCityRealtimeMessageThroughput(@RequestParam("cityId") Integer cityId) {
		return cityMonitorService.getCityRealtimeMessageThroughput(cityId);
	}
	
	@RequestMapping(value="/getCityRealtimeMessageProcessTime", method=RequestMethod.GET)
	@ResponseBody
	public MessageProcessTimeDto getCityRealtimeMessageProcessTime(@RequestParam("cityId") Integer cityId) {
		return cityMonitorService.getCityRealtimeMessageAverageProcessTime(cityId);
	}
	
	@RequestMapping(value="/getCityHistorySessionNum", method=RequestMethod.GET)
	@ResponseBody
	public List<List<Object>> getCityHistorySessionNum(@RequestParam("cityId") Integer cityId,
			@RequestParam("date") String date) {
		Date date1 = null;
		try {
			date1 = dateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return cityMonitorService.getCityHistoryeSessionNum(cityId, date1);
	}
	
	@RequestMapping(value="/getCityHistoryMessageThroughput", method=RequestMethod.GET)
	@ResponseBody
	public Map<String, List<List<Object>>> getCityHistoryMessageThroughput(@RequestParam("cityId") Integer cityId,
			@RequestParam("date") String date) {
		Date date1 = null;
		try {
			date1 = dateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return cityMonitorService.getCityHistoryMessageThroughput(cityId, date1);
	}
	
	@RequestMapping(value="/getCityHistoryMessageProcessTime", method=RequestMethod.GET)
	@ResponseBody
	public Map<String, List<List<Object>>> getCityHistoryMessageProcessTime(@RequestParam("cityId") Integer cityId,
			@RequestParam("date") String date) {
		Date date1 = null;
		try {
			date1 = dateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return cityMonitorService.getCityHistoryMessageAverageProcessTime(cityId, date1);
	}
	
//	@RequestMapping(value="/getCityRxbpsTxbps", method=RequestMethod.GET)
//	@ResponseBody
//	public RxbpsTxbpsDto getCityRxbpsTxbps(@RequestParam("cityId") Integer cityId,
//			@RequestParam("interfaceName") String interfaceName) {
//		return cityMonitorService.getCityRxbpsTxbps(cityId, interfaceName);
//	}
//	
//	@RequestMapping(value="/getCityConcurrencyRequestNum", method=RequestMethod.GET)
//	@ResponseBody
//	public Long getCityConcurrencyRequestNum(@RequestParam("cityId") Integer cityId) {
//		return cityMonitorService.getCityConcurrencyRequestNum(cityId);
//	}
}
