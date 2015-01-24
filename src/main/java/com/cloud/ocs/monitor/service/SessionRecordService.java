package com.cloud.ocs.monitor.service;

/**
 * 会话处理记录service接口
 * 
 * @author Wang Chao
 *
 * @date 2015-1-22 下午5:08:01
 *
 */
public interface SessionRecordService {

	/**
	 * 网络当前正在处理的会话数
	 * @param networkIp
	 * @return
	 */
	public Long getNetworkCurSessionNum(String networkIp);
	
	/**
	 * 城市当前正在处理的会话数
	 * @param cityId
	 * @return
	 */
	public Long getCityCurSessionNum(Integer cityId);
}
