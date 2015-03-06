package com.cloud.ocs.portal.core.monitor.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cloud.ocs.portal.core.monitor.dto.HostDetail;
import com.cloud.ocs.portal.core.monitor.service.HostMonitorService;

/**
 * 系统主机监控模块Controller
 * 
 * @author Wang Chao
 *
 * @date 2014-12-21 下午5:02:20
 *
 */
@Controller
@RequestMapping(value="/monitor/host")
public class HostMonitorController {
	
	@Resource
	private HostMonitorService hostMonitorService;
	
	@RequestMapping(value="/listHostDetail", method=RequestMethod.GET)
	@ResponseBody
	public List<HostDetail> listHostDetail(@RequestParam("zoneId") String zoneId) {
		return hostMonitorService.getHostDetailList(zoneId);
	}
	
	@RequestMapping(value="/getCurHostCpuUsagePercentage", method=RequestMethod.GET)
	@ResponseBody
	public double getCurHostCpuUsagePercentage(@RequestParam("hostId") String hostId) {
		return hostMonitorService.getHostCurCpuUsedPercentage(hostId);
	}
	
	@RequestMapping(value="/getCurHostMemoryUsagePercentage", method=RequestMethod.GET)
	@ResponseBody
	public double getCurHostMemoryUsagePercentage(@RequestParam("hostId") String hostId) {
		return hostMonitorService.getHostCurMemoryUsedPercentage(hostId);
	}
	
	@RequestMapping(value="/getHostHistoryCpuUsedPercentage", method=RequestMethod.GET)
	@ResponseBody
	public List<List<Object>> getHostHistoryCpuUsedPercentage(@RequestParam("hostId") String hostId,
			@RequestParam("dayOfMonth") Integer dayOfMonth) {
		return hostMonitorService.getHostHistoryCpuUsedPercentage(hostId, dayOfMonth);
	}
	
	@RequestMapping(value="/getHostHistoryMemoryUsedPercentage", method=RequestMethod.GET)
	@ResponseBody
	public List<List<Object>> getHostHistoryMemoryUsedPercentage(@RequestParam("hostId") String hostId,
			@RequestParam("dayOfMonth") Integer dayOfMonth) {
		return hostMonitorService.getHostHistoryMemoryUsedPercentage(hostId, dayOfMonth);
	}
}
