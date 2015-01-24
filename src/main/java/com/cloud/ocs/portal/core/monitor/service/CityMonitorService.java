package com.cloud.ocs.portal.core.monitor.service;

import com.cloud.ocs.portal.core.monitor.dto.MessageProcessTimeDto;
import com.cloud.ocs.portal.core.monitor.dto.RxbpsTxbpsDto;

/**
 * 监控城市Service接口
 * 
 * @author Wang Chao
 *
 * @date 2015-1-15 上午10:42:52
 *
 */
public interface CityMonitorService {
	
	/**
	 * 城市的实时会话数
	 * @param cityId
	 * @return
	 */
	public Long getRealtimeSessionNum(Integer cityId);
	
	/**
	 * 城市包处理平均时长
	 * @param cityId
	 * @return
	 */
	public MessageProcessTimeDto getMessageProcessTime(Integer cityId);

	/**
	 * 某个城市在某个网卡上的数据吞吐率数据汇总
	 * @param cityId
	 * @param interfaceName
	 * @return
	 */
	public RxbpsTxbpsDto getCityRxbpsTxbps(Integer cityId, String interfaceName);
	
	/**
	 * 某一时刻某个城市正在处理的并发请求连接数汇总
	 * @param cityId
	 * @return
	 */
	public Long getCityConcurrencyRequestNum(Integer cityId);
	
}
