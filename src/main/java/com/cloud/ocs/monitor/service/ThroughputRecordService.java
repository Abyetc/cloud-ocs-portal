package com.cloud.ocs.monitor.service;

import com.cloud.ocs.portal.core.monitor.dto.MessageThroughputDto;

/**
 * 用于处理吞吐量记录的Service接口
 * 
 * @author Charles
 *
 */
public interface ThroughputRecordService {

	/**
	 * 得到城市的包处理吞吐量信息
	 * @param cityId
	 * @return
	 */
	public MessageThroughputDto getMessageThroughputOfCity(Integer cityId);
	
	/**
	 * 得到网络的包处理吞吐量信息
	 * @param networkIp
	 * @return
	 */
	public MessageThroughputDto getMessageThroughputOfNetwork(String networkIp);
	
	/**
	 * 得到虚拟机的包处理吞吐量信息
	 * @param networkIp
	 * @param vmIp
	 * @return
	 */
	public MessageThroughputDto getMessageThroughputOfVm(String networkIp, String vmIp);
}
