package com.cloud.ocs.portal.core.resource.service;

import java.util.List;

import com.cloud.ocs.portal.core.resource.dto.CapacityDto;

/**
 * 系统资源系统容量模块service接口
 * @author Wang Chao
 *
 * @date 2014-12-11 下午2:35:57
 *
 */
public interface CapacityService {

	public List<CapacityDto> getCapacityList(String zoneId);
}
