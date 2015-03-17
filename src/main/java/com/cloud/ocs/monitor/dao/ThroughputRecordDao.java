package com.cloud.ocs.monitor.dao;

import java.util.Date;
import java.util.Map;

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
	public MessageThroughputDto getCityCurMessageThroughput(Integer cityId);
	
	/**
	 * 得到网络的包处理吞吐量信息
	 * @param networkIp
	 * @return
	 */
	public MessageThroughputDto getNetworkCurMessageThroughput(String networkIp);
	
	/**
	 * 得到虚拟机的包处理吞吐量信息
	 * @param networkIp
	 * @param vmIp
	 * @return
	 */
	public MessageThroughputDto getVmCurMessageThroughput(String networkIp, String vmIp);
	
	/**
	 * 根据日期区间获取城市包吞吐量历史信息
	 * @param cityId
	 * @param from
	 * @param to
	 * @return
	 */
	public Map<Date, MessageThroughputDto> getCityHistoryMessageThroughput(Integer cityId, Date from, Date to);

	/**
	 * 根据日期区间获取网络包吞吐量历史信息
	 * @param cityId
	 * @param from
	 * @param to
	 * @return
	 */
	public Map<Date, MessageThroughputDto> getNetworkHistoryMessageThroughput(String networkIp, Date from, Date to);
	
	/**
	 * 根据日期区间获取虚拟机包吞吐量历史信息
	 * @param cityId
	 * @param from
	 * @param to
	 * @return
	 */
	public Map<Date, MessageThroughputDto> getVmHistoryMessageThroughput(String networkIp, String vmIp, Date from, Date to);

	
}
