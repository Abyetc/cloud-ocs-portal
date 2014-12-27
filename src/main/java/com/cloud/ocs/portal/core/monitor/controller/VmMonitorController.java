package com.cloud.ocs.portal.core.monitor.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cloud.ocs.portal.core.monitor.dto.VmDetail;
import com.cloud.ocs.portal.core.monitor.service.VmMonitorService;

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
public class VmMonitorController {
	
	@Resource
	private VmMonitorService vmMonitorService;
	
	@RequestMapping(value="/listVmDetail", method=RequestMethod.GET)
	@ResponseBody
	public List<VmDetail> listVmDetail(@RequestParam("hostId") String hostId) {
		return vmMonitorService.getVmDetailList(hostId);
	}
	
	@RequestMapping(value="/getCurVmCpuUsagePercentage", method=RequestMethod.GET)
	@ResponseBody
	public double getCurVmCpuUsagePercentage(@RequestParam("vmId") String vmId) {
		return vmMonitorService.getCurVmCpuUsagePercentage(vmId);
	}

}
