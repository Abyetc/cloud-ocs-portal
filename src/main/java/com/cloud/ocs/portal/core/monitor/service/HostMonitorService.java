package com.cloud.ocs.portal.core.monitor.service;

import java.util.List;

import com.cloud.ocs.portal.core.monitor.dto.HostDetail;

/**
 * 监控主机service接口
 * 
 * @author Wang Chao
 *
 * @date 2014-12-21 下午4:09:20
 *
 */
public interface HostMonitorService {

	public List<HostDetail> getHostDetailList(String zoneId);
	public double getCurHostCpuUsagePercentage(String hostId);
	public double getCurHostUsedMemory(String hostId);
}
