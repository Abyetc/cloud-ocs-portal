package com.cloud.ocs.monitor.dao;

import java.util.Date;
import java.util.List;

import com.cloud.ocs.monitor.bean.MessageRecord;
import com.cloud.ocs.monitor.common.GenericDao;
import com.cloud.ocs.monitor.constant.MessageType;

/**
 * 用于访问包处理记录的Dao接口
 * 
 * @author Wang Chao
 *
 * @date 2015-1-22 下午5:03:15
 *
 */
public interface MessageRecordDao extends GenericDao<MessageRecord> {

	/**
	 * 返回所有记录
	 * @return
	 */
	public List<MessageRecord> findAll();
	
	/**
	 * 返回Vm处理包的平均时间
	 * @param networkIp
	 * @param vmIp
	 * @param messageType
	 * @return
	 */
	public Double getMessageAverageProcessTimeOfVm(String networkIp, String vmIp, MessageType messageType);
	
	/**
	 * 返回Network处理包的平均时间
	 * @param networkIp
	 * @param messageType
	 * @return
	 */
	public Double getMessageAverageProcessTimeOfNetwork(String networkIp, MessageType messageType);
	
	/**
	 * 返回city处理包的平均时间
	 * @param cityId
	 * @param messageType
	 * @return
	 */
	public Double getMessageAverageProcessTimeOfCity(Integer cityId, MessageType messageType);
	
	/**
	 * 获取城市在某个具体时间内的包处理时长
	 * @param cityId
	 * @param messageType
	 * @param date
	 * @return
	 */
	public Double getMessageAverageProcessTimeOfCityAtSpecificDate(Integer cityId,
			MessageType messageType, Date date);
	
	/**
	 * 获取网络在某个具体时间内的包处理时长
	 * @param cityId
	 * @param messageType
	 * @param date
	 * @return
	 */
	public Double getMessageAverageProcessTimeOfNetworkAtSpecificDate(String networkIp,
			MessageType messageType, Date date);
	
	/**
	 * 获取虚拟机在某个具体时间内的包处理时长
	 * @param cityId
	 * @param messageType
	 * @param date
	 * @return
	 */
	public Double getMessageAverageProcessTimeOfVmAtSpecificDate(String networkIp, String vmIp,
			MessageType messageType, Date date);
}
