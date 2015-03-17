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
import com.cloud.ocs.portal.core.monitor.dto.RxbpsTxbpsDto;
import com.cloud.ocs.portal.core.monitor.dto.OcsVmDetail;
import com.cloud.ocs.portal.core.monitor.service.OcsVmMonitorService;

/**
 * 用户VM监控模块Controller
 * 
 * @author Wang Chao
 *
 * @date 2014-12-27 下午5:31:22
 *
 */
@Controller
@RequestMapping(value="/monitor/vm")
public class OcsVmMonitorController {
	
	private static DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");  
	
	@Resource
	private OcsVmMonitorService vmMonitorService;
	
	@RequestMapping(value="/listVmDetail", method=RequestMethod.GET)
	@ResponseBody
	public List<OcsVmDetail> listVmDetail(@RequestParam("hostId") String hostId) {
		return vmMonitorService.getVmDetailList(hostId);
	}
	
	@RequestMapping(value="/getCurVmCpuUsagePercentage", method=RequestMethod.GET)
	@ResponseBody
	public double getVmCurCpuUsagePercentage(@RequestParam("vmId") String vmId) {
		return vmMonitorService.getVmCurCpuUsagePercentage(vmId);
	}
	
	@RequestMapping(value="/getCurVmMemoryUsagePercentage", method=RequestMethod.GET)
	@ResponseBody
	public double getVmCurMemoryUsagePercentage(@RequestParam("vmId") String vmId) {
		return vmMonitorService.getVmCurMemoryUsagePercentage(vmId);
	}
	
	@RequestMapping(value="/getCurRxbpsTxbps", method=RequestMethod.GET)
	@ResponseBody
	public RxbpsTxbpsDto getVmCurNetworkUsage(@RequestParam("cityVmId") String cityVmId,
			@RequestParam("interfaceName") String interfaceName) {
		return vmMonitorService.getVmCurRxbpsTxbps(cityVmId, interfaceName);
	}
	
	@RequestMapping(value="/getVmHistoryCpuUsagePercentage", method=RequestMethod.GET)
	@ResponseBody
	public List<List<Object>> getVmHistoryCpuUsagePercentage(@RequestParam("vmId") String vmId,
			@RequestParam("dayOfMonth") int dayOfMonth) {
		return vmMonitorService.getVmHistoryCpuUsagePercentage(vmId, dayOfMonth);
	}
	
	@RequestMapping(value="/getVmHistoryMemoryUsagePercentage", method=RequestMethod.GET)
	@ResponseBody
	public List<List<Object>> getVmHistoryMemoryUsagePercentage(@RequestParam("vmId") String vmId,
			@RequestParam("dayOfMonth") int dayOfMonth) {
		return vmMonitorService.getVmHistoryMemoryUsagePercentage(vmId, dayOfMonth);
	}
	
	@RequestMapping(value="/getVmHistoryNetworkUsage", method=RequestMethod.GET)
	@ResponseBody
	public List<List<Object>> getVmHistoryNetworkUsage(@RequestParam("vmId") String vmId,
			@RequestParam("interfaceName") String interfaceName,
			@RequestParam("dayOfMonth") int dayOfMonth) {
		return vmMonitorService.getVmHistoryRxbpsTxbps(vmId, interfaceName, dayOfMonth);
	}
	
	@RequestMapping(value="/getConcurrencyRequestNum", method=RequestMethod.GET)
	@ResponseBody
	public Long getConcurrencyRequestNum(@RequestParam("cityVmId") String cityVmId) {
		return vmMonitorService.getVmCurConcurrencyRequestNum(cityVmId);
	}
	
	@RequestMapping(value="/getCityVmMessageThroughput", method=RequestMethod.GET)
	@ResponseBody
	public MessageThroughputDto getCityVmCurMessageThroughput(@RequestParam("cityVmId") String cityVmId) {
		return vmMonitorService.getVmCurMessageThroughput(cityVmId);
	}
	
	@RequestMapping(value="/getCityVmMessageProcessTime", method=RequestMethod.GET)
	@ResponseBody
	public MessageProcessTimeDto getCityVmCurMessageProcessTime(@RequestParam("cityVmId") String cityVmId) {
		return vmMonitorService.getVmCurMessageAverageProcessTime(cityVmId);
	}
	
	@RequestMapping(value="/getCityVmHistoryMessageThroughput", method=RequestMethod.GET)
	@ResponseBody
	public Map<String, List<List<Object>>> getCityVmHistoryMessageThroughput(@RequestParam("cityVmId") String cityVmId,
			@RequestParam("date") String date) {
		Date date1 = null;
		try {
			date1 = dateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return vmMonitorService.getVmHistoryMessageThroughput(cityVmId, date1);
	}
	
	@RequestMapping(value="/getCityVmHistoryMessageProcessTime", method=RequestMethod.GET)
	@ResponseBody
	public Map<String, List<List<Object>>> getCityVmHistoryMessageProcessTime(@RequestParam("cityVmId") String cityVmId,
			@RequestParam("date") String date) {
		Date date1 = null;
		try {
			date1 = dateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return vmMonitorService.getVmHistoryMessageAverageProcessTime(cityVmId, date1);
	}

}
