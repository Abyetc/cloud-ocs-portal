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

	/**
	 * 获取zone中所有host的详细信息的列表
	 * @param zoneId
	 * @return
	 */
	public List<HostDetail> getHostDetailList(String zoneId);
	
	/**
	 * 从CloudStack获取host当前的CPU使用率
	 * @param hostId
	 * @return
	 */
	public double getHostCurCpuUsagePercentageFromCs(String hostId);
	
	/**
	 * 从CloudStack获取host当前的Memory使用量
	 * @param hostId
	 * @return
	 */
	public double getHostCurUsedMemoryFromCs(String hostId);
	
	/**
	 * 通过SSH到主机获取host当前的CPU使用率
	 * @param hostId
	 * @return
	 */
	public double getHostCurCpuUsedPercentage(String hostId);
	
	/**
	 * 通过SSH到主机获取host当前的Memory使用率
	 * @param hostId
	 * @return
	 */
	public double getHostCurMemoryUsedPercentage(String hostId);
	
	/**
	 * 通过SSH到主机获取host当月某天的历史CPU使用率
	 * @param hostId
	 * @param dayOfMonth
	 * @return
	 */
	public List<List<Object>> getHostHistoryCpuUsedPercentage(String hostId, int dayOfMonth);
	
	/**
	 * 通过SSH到主机获取host当月某天的历史Memory使用率
	 * @param hostId
	 * @param dayOfMonth
	 * @return
	 */
	public List<List<Object>> getHostHistoryMemoryUsedPercentage(String hostId, int dayOfMonth);
}
