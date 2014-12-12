package com.cloud.ocs.portal.core.resource.service;

import java.util.List;

import com.cloud.ocs.portal.core.resource.dto.PodDto;
import com.cloud.ocs.portal.core.resource.dto.SecondaryStorageDto;
import com.cloud.ocs.portal.core.resource.dto.SystemVmDto;
import com.cloud.ocs.portal.core.resource.dto.ZoneDto;

/**
 * 系统资源基础设施模块service接口
 * 
 * @author Wang Chao
 *
 * @date 2014-12-11 下午2:32:41
 *
 */
public interface InfrastructureService {
	
	public List<ZoneDto> getZonesList();
	
	public List<PodDto> getPodsList(String zoneId);
	public List<SecondaryStorageDto> getSecondaryStorageList(String zoneId);
	public List<SystemVmDto> getSystemVmsList(String zoneId);
}
