package com.cloud.ocs.portal.core.monitor.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.cloud.ocs.portal.core.monitor.dto.MessageProcessTimeDto;
import com.cloud.ocs.portal.core.monitor.dto.MessageThroughputDto;

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
	public Long getNetworkRealtimeSessionNum(String networkId);
	
	/**
	 * 网络包吞吐量
	 * @param networkId
	 * @return
	 */
	public MessageThroughputDto getNetworkRealtimeMessageThroughput(String networkId);
	
	/**
	 * 网络包处理平均时长
	 * @param networkId
	 */
	public MessageProcessTimeDto getNetworkRealtimeMessageAverageProcessTime(String networkId);

	/**
	 * 获取网络历史会话数据
	 * @param networkId
	 * @param date
	 * @return
	 */
	public List<List<Object>> getNetworkHistorySessionNum(String networkId, Date date);
	
	/**
	 * 获取网络历史包吞吐量数据
	 * @param networkId
	 * @param date
	 * @return
	 */
	public Map<String, List<List<Object>>> getNetworkHistoryMessageThroughput(String networkId, Date date);
	
	/**
	 * 获取网络历史包处理平均时长数据
	 * @param networkId
	 * @param date
	 * @return
	 */
	public Map<String, List<List<Object>>> getNetworkHistoryMessageAverageProcessTime(String networkId, Date date);
	
	/**
	 * 某个城市的网络在某个网卡上的数据吞吐率数据汇总
	 * @param networkId
	 * @param interfaceName
	 * @return
	 */
//	public RxbpsTxbpsDto getCityNetworkRxbpsTxbps(String networkId, String interfaceName);
	
	/**
	 * 某一时刻某个城市的网络正在处理的并发请求连接数汇总
	 * @param cityId
	 * @return
//	 */
//	public Long getCityNetworkConcurrencyRequestNum(String networkId);
}
