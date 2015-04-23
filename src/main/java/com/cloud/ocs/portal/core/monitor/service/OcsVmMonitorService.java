package com.cloud.ocs.portal.core.monitor.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.cloud.ocs.portal.core.monitor.dto.MessageProcessTimeDto;
import com.cloud.ocs.portal.core.monitor.dto.MessageThroughputDto;
import com.cloud.ocs.portal.core.monitor.dto.RxbpsTxbpsDto;
import com.cloud.ocs.portal.core.monitor.dto.OcsVmDetail;

/**
 * 监控VM service接口
 * 
 * @author Wang Chao
 *
 * @date 2014-12-26 下午10:06:16
 *
 */
public interface OcsVmMonitorService {
	
	/**
	 * 某台主机上用于虚拟机的数量
	 * @param hostId
	 * @return
	 */
	public int getVmNumOnHost(String hostId);
	
	/**
	 * 某台主机上所有虚拟机详细信息列表
	 * @param hostId
	 * @return
	 */
	public List<OcsVmDetail> getVmDetailList(String hostId);
	
	/**
	 * 获得VM的CPU总量
	 * @param vmId
	 * @return
	 */
	public Long getVmTotalCpuCapacity(String vmId);
	
	/**
	 * 从CloudStack的API获取某个虚拟机的CPU的使用率
	 * @param vmId
	 * @return
	 */
//	public double getCurVmCpuUsagePercentageFromCs(String vmId);
	
	/**
	 * 获取虚拟机CPU实时使用率数据
	 * @param vmId
	 * @return
	 */
	public Double getVmCurCpuUsagePercentage(String vmId);
	
	/**
	 * 获取虚拟机内存实时使用率数据
	 * @param vmId
	 * @return
	 */
	public Double getVmCurMemoryUsagePercentage(String vmId);
	
	/**
	 * 获取虚拟机在某个网卡上的数据吞吐率实时数据
	 * @param vmId
	 * @param interfaceName
	 * @return
	 */
	public RxbpsTxbpsDto getVmCurRxbpsTxbps(String vmId, String interfaceName);
	
	/**
	 * 获取虚拟机CPU使用率历史数据
	 * @param vmId
	 * @param dayOfMonth
	 * @return
	 */
	public List<List<Object>> getVmHistoryCpuUsagePercentage(String vmId, int dayOfMonth);
	
	/**
	 * 获取虚拟机内存使用率历史数据
	 * @param vmId
	 * @param dayOfMonth
	 * @return
	 */
	public List<List<Object>> getVmHistoryMemoryUsagePercentage(String vmId, int dayOfMonth);
	
	/**
	 * 获取虚拟机在某个网卡上的数据吞吐率历史数据
	 * @param vmId
	 * @param interfaceName
	 * @param dayOfMonth
	 * @return
	 */
	public Map<String, List<List<Object>>> getVmHistoryRxbpsTxbps(String vmId, String interfaceName, int dayOfMonth);
	
	/**
	 * 某一时刻虚拟机正在处理的并发请求连接数
	 * @param vmId
	 * @return
	 */
	public Long getVmCurConcurrencyRequestNum(String vmId);
	
	/**
	 * 获取虚拟机包吞吐量实时数据
	 * @param vmId
	 * @return
	 */
	public MessageThroughputDto getVmCurMessageThroughput(String vmId);
	
	/**
	 * 获取虚拟机包处理平均时长实时数据
	 * @param vmId
	 * @return
	 */
	public MessageProcessTimeDto getVmCurMessageAverageProcessTime(String vmId);
	
	/**
	 * 获取虚拟机包吞吐量历史数据
	 * @param vmId
	 * @param date
	 * @return
	 */
	public Map<String, List<List<Object>>> getVmHistoryMessageThroughput(String vmId, Date date);
	
	/**
	 * 获取虚拟机包处理平均时长历史数据
	 * @param vmId
	 * @param date
	 * @return
	 */
	public Map<String, List<List<Object>>> getVmHistoryMessageAverageProcessTime(String vmId, Date date);
}
