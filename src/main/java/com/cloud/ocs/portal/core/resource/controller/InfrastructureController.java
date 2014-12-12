package com.cloud.ocs.portal.core.resource.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cloud.ocs.portal.core.resource.dto.PodDto;
import com.cloud.ocs.portal.core.resource.dto.SecondaryStorageDto;
import com.cloud.ocs.portal.core.resource.dto.SystemVmDto;
import com.cloud.ocs.portal.core.resource.dto.ZoneDto;
import com.cloud.ocs.portal.core.resource.service.InfrastructureService;

/**
 * 系统资源基础设施模块Controller
 * 
 * @author Wang Chao
 *
 * @date 2014-12-11 上午11:29:31
 *
 */
@Controller
@RequestMapping(value="/resource/infrastructure")
public class InfrastructureController {
	
	@Resource
	private InfrastructureService infrastructureService;

	@RequestMapping(value="/listZones", method=RequestMethod.GET)
	@ResponseBody
	public List<ZoneDto> listZones() {
		return infrastructureService.getZonesList();
	}
	
	@RequestMapping(value="/listPods", method=RequestMethod.GET)
	@ResponseBody
	public List<PodDto> listPods(@RequestParam("zoneId") String zoneId) {
		return infrastructureService.getPodsList(zoneId);
	}
	
	@RequestMapping(value="/listSecondaryStorage", method=RequestMethod.GET)
	@ResponseBody
	public List<SecondaryStorageDto> listSecondaryStorage(@RequestParam("zoneId") String zoneId) {
		return infrastructureService.getSecondaryStorageList(zoneId);
	}
	
	@RequestMapping(value="/listSystemVms", method=RequestMethod.GET)
	@ResponseBody
	public List<SystemVmDto> listSystemVms(@RequestParam("zoneId") String zoneId) {
		return infrastructureService.getSystemVmsList(zoneId);
	}
}
