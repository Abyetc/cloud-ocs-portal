package com.cloud.ocs.monitor.service;

import java.util.Date;
import java.util.List;

import com.cloud.ocs.monitor.bean.MessageRecord;
import com.cloud.ocs.monitor.constant.MessageType;
import com.cloud.ocs.monitor.dto.MessageAverageProcessTimeWrapper;

/**
 * 包处理记录service接口
 * 
 * @author Wang Chao
 * 
 * @date 2015-1-22 下午5:08:06
 * 
 */
public interface MessageRecordService {

	/**
	 * 返回所有记录
	 * 
	 * @return
	 */
	public List<MessageRecord> getAll();

	/**
	 * Vm处理包的平均时间
	 * @param networkIp
	 * @param vmIp
	 * @param messageType
	 * @return
	 */
	public MessageAverageProcessTimeWrapper getMessageAverageProcessTimeOfVm(
			String networkIp, String vmIp, MessageType messageType);

	/**
	 * Network处理包的平均时间
	 * @param networkIp
	 * @param messageType
	 * @return
	 */
	public MessageAverageProcessTimeWrapper getMessageAverageProcessTimeOfNetwork(
			String networkIp, MessageType messageType);

	/**
	 * City处理包的平均时间
	 * @param cityId
	 * @param messageType
	 * @return
	 */
	public MessageAverageProcessTimeWrapper getMessageAverageProcessTimeOfCity(
			Integer cityId, MessageType messageType);
	
	/**
	 * 获取城市在某个时间上的包处理时长
	 * @param cityId
	 * @param messageType
	 * @param date
	 * @return
	 */
	public MessageAverageProcessTimeWrapper getMessageAverageProcessTimeOfCityAtSpecificDate(
			Integer cityId, MessageType messageType, Date date);
	
	/**
	 * 获取网络在某个时间上的包处理时长
	 * @param cityId
	 * @param messageType
	 * @param date
	 * @return
	 */
	public MessageAverageProcessTimeWrapper getMessageAverageProcessTimeOfNetworkAtSpecificDate(
			String networkIp, MessageType messageType, Date date);
	
	/**
	 * 获取虚拟机在某个时间上的包处理时长
	 * @param cityId
	 * @param messageType
	 * @param date
	 * @return
	 */
	public MessageAverageProcessTimeWrapper getMessageAverageProcessTimeOfVmAtSpecificDate(
			String networkIp, String vmIp, MessageType messageType, Date date);
}
