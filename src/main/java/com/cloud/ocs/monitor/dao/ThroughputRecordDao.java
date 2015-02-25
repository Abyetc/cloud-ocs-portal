package com.cloud.ocs.monitor.dao;

import com.cloud.ocs.monitor.bean.ThroughputRecord;
import com.cloud.ocs.monitor.common.GenericDao;
import com.cloud.ocs.portal.core.monitor.dto.MessageThroughputDto;

/**
 * 用于访问吞吐量记录的Dao接口
 * 
 * @author Charles
 *
 */
public interface ThroughputRecordDao extends GenericDao<ThroughputRecord> {
	
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
