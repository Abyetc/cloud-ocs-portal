package com.cloud.ocs.portal.core.monitor.service;

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
