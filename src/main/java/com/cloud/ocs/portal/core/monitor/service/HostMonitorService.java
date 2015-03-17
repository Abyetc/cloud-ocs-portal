package com.cloud.ocs.portal.core.monitor.service;

import java.util.List;

import com.cloud.ocs.portal.core.monitor.dto.HostDetail;
import com.cloud.ocs.portal.core.monitor.dto.RxbpsTxbpsDto;

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
	 * 获得主机的CPU总量
	 * @param hostId
	 * @return
	 */
	public Long getHostTotalCpuCapacity(String hostId);
	
	/**
	 * 从CloudStack获取host当前的CPU使用率
	 * @param hostId
	 * @return
	 */
//	public double getHostCurCpuUsagePercentageFromCs(String hostId);
	
	/**
	 * 从CloudStack获取host当前的Memory使用量
	 * @param hostId
	 * @return
	 */
//	public double getHostCurUsedMemoryFromCs(String hostId);
	
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
	 * 获取主机在某个网卡上的数据吞吐率实时数据
	 * @param hostId
	 * @param interfaceName
	 * @return
	 */
	public RxbpsTxbpsDto getHostCurRxbpsTxbps(String hostId, String interfaceName);
	
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
	
	/**
	 * 获取主机在某个网卡上的数据吞吐率历史数据
	 * @param hostId
	 * @param interfaceName
	 * @return
	 */
	public List<List<Object>> getHostHistoryRxbpsTxbps(String hostId, String interfaceName);
}
