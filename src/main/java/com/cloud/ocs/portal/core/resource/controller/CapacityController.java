package com.cloud.ocs.portal.core.resource.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cloud.ocs.portal.core.resource.dto.CapacityDto;
import com.cloud.ocs.portal.core.resource.service.CapacityService;

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
	
	@Resource
	private CapacityService capacityService;

	@RequestMapping(value="/listCapacity", method=RequestMethod.GET)
	@ResponseBody
	public List<CapacityDto> listHosts(@RequestParam("zoneId") String zoneId) {
		return capacityService.getCapacityList(zoneId);
	}
}
