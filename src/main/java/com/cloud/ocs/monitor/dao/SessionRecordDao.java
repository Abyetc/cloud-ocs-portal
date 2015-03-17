package com.cloud.ocs.monitor.dao;

import java.util.Date;

import com.cloud.ocs.monitor.bean.SessionRecord;
import com.cloud.ocs.monitor.common.GenericDao;

/**
 * 用于访问会话处理记录的Dao接口
 * 
 * @author Wang Chao
 *
 * @date 2015-1-22 下午5:04:46
 *
 */
public interface SessionRecordDao extends GenericDao<SessionRecord> {

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
	
	/**
	 * 城市在指定时间区间内接入的会话数
	 * @param cityId
	 * @param from
	 * @param to
	 * @return
	 */
	public Long getCityHistorySessionNum(Integer cityId, Date from, Date to);
	
	/**
	 * 网络在指定时间区间内接入的会话数
	 * @param cityId
	 * @param from
	 * @param to
	 * @return
	 */
	public Long getNetworkHistorySessionNum(Integer networkIp, Date from, Date to);
}
