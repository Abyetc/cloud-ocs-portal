package com.cloud.ocs.portal.core.monitor.service;

import java.util.List;

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
	 * 从CloudStack的API获取某个虚拟机的CPU的使用率
	 * @param vmId
	 * @return
	 */
	public double getCurVmCpuUsagePercentageFromCs(String vmId);
	
	/**
	 * 通过自己写的Restful接口获取CPU使用率数据
	 * @param vmId
	 * @return
	 */
	public Double getCurVmCpuUsagePercentage(String vmId);
	
	/**
	 * 通过自己写的Restful接口获取内存使用率数据
	 * @param vmId
	 * @return
	 */
	public Double getCurMemoryUsagePercentage(String vmId);
	
	/**
	 * 某个虚拟机在某个网卡上的数据吞吐率数据
	 * @param vmId
	 * @param interfaceName
	 * @return
	 */
	public RxbpsTxbpsDto getVmRxbpsTxbps(String vmId, String interfaceName);
	
	/**
	 * 某一时刻虚拟机正在处理的并发请求连接数
	 * @param vmId
	 * @return
	 */
	public Long getVmConcurrencyRequestNum(String vmId);
	
	/**
	 * 虚拟机包吞吐量
	 * @param vmId
	 * @return
	 */
	public MessageThroughputDto getMessageThroughput(String vmId);
	
	/**
	 * 虚拟机包处理平均时长
	 * @param vmId
	 * @return
	 */
	public MessageProcessTimeDto getCityVmMessageProcessTime(String vmId);
}
