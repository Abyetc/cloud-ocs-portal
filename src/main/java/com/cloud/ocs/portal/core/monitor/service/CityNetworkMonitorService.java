package com.cloud.ocs.portal.core.monitor.service;

import com.cloud.ocs.portal.core.monitor.dto.MessageProcessTimeDto;
import com.cloud.ocs.portal.core.monitor.dto.RxbpsTxbpsDto;

/**
 * 城市-网络监控service接口
 * 
 * @author Wang Chao
 *
 * @date 2015-1-19 下午9:19:29
 *
 */
public interface CityNetworkMonitorService {
	
	/**
	 * 网络的实时会话数
	 * @param networkId
	 * @return
	 */
	public Long getRealtimeSessionNum(String networkId);
	
	/**
	 * 网络包处理平均时长
	 * @param networkId
	 */
	public MessageProcessTimeDto getMessageProcessTime(String networkId);

	/**
	 * 某个城市的网络在某个网卡上的数据吞吐率数据汇总
	 * @param networkId
	 * @param interfaceName
	 * @return
	 */
	public RxbpsTxbpsDto getCityNetworkRxbpsTxbps(String networkId, String interfaceName);
	
	/**
	 * 某一时刻某个城市的网络正在处理的并发请求连接数汇总
	 * @param cityId
	 * @return
	 */
	public Long getCityNetworkConcurrencyRequestNum(String networkId);
}
