package com.cloud.ocs.portal.core.resource.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cloud.ocs.portal.core.resource.dto.HostDto;

/**
 * 系统资源系统容量模块Controller
 * 
 * @author Wang Chao
 *
 * @date 2014-12-11 上午11:30:27
 *
 */
@Controller
@RequestMapping(value="/resource/capacity")
public class CapacityController {

	@RequestMapping(value="/listCapacity", method=RequestMethod.GET)
	@ResponseBody
	public String listHosts(@RequestParam("zoneId") String zoneId) {
//		return infrastructureService.getHostsList(clusterId);
		return null;
	}
}
