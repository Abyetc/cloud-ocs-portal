package com.cloud.ocs.portal.core.monitor.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.cloud.ocs.portal.core.monitor.dto.MessageProcessTimeDto;
import com.cloud.ocs.portal.core.monitor.dto.MessageThroughputDto;

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
	public Long getCityRealtimeSessionNum(Integer cityId);
	
	/**
	 * 城市包吞吐量
	 * @param cityId
	 * @return
	 */
	public MessageThroughputDto getCityRealtimeMessageThroughput(Integer cityId);
	
	/**
	 * 城市包处理平均时长
	 * @param cityId
	 * @return
	 */
	public MessageProcessTimeDto getCityRealtimeMessageAverageProcessTime(Integer cityId);
	
	/**
	 * 获取城市历史会话数
	 * @param cityId
	 * @param date
	 * @return
	 */
	public List<List<Object>> getCityHistoryeSessionNum(Integer cityId, Date date);

	/**
	 * 获取城市历史包吞吐量
	 * @param cityId
	 * @param date
	 * @return
	 */
	public Map<String, List<List<Object>>> getCityHistoryMessageThroughput(Integer cityId, Date date);
	
	/**
	 * 获取城市历史包平均处理时长
	 * @param cityId
	 * @param date
	 * @return
	 */
	public Map<String, List<List<Object>>> getCityHistoryMessageAverageProcessTime(Integer cityId, Date date);
	
	/**
	 * 某个城市在某个网卡上的数据吞吐率数据汇总
	 * @param cityId
	 * @param interfaceName
	 * @return
	 */
//	public RxbpsTxbpsDto getCityRxbpsTxbps(Integer cityId, String interfaceName);
	
	/**
	 * 某一时刻某个城市正在处理的并发请求连接数汇总
	 * @param cityId
	 * @return
	 */
//	public Long getCityConcurrencyRequestNum(Integer cityId);
	
}
