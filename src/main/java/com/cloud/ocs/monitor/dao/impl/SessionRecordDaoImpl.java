package com.cloud.ocs.monitor.dao.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.cloud.ocs.monitor.bean.SessionRecord;
import com.cloud.ocs.monitor.common.GenericDaoImpl;
import com.cloud.ocs.monitor.dao.SessionRecordDao;
import com.cloud.ocs.portal.core.business.service.CityNetworkService;
import com.cloud.ocs.portal.utils.DateUtil;

/**
 * 用于访问会话处理记录的Dao实现类
 * 
 * @author Wang Chao
 *
 * @date 2015-1-22 下午5:06:08
 *
 */
@Repository
public class SessionRecordDaoImpl extends GenericDaoImpl<SessionRecord> implements SessionRecordDao {
	
	@Resource
	private CityNetworkService cityNetworkService;

	@Override
	public Long getNetworkCurSessionNum(String networkIp) {
		//只统计五分钟内还在进行的会话，防止由于错误会话一直处于进行状态的情况
		Date dNow = new Date();
		Timestamp timestamp = DateUtil.transferDateInSecondField(dNow, -300);
		
		Query query =  em.createQuery("select count(*) from SessionRecord record where " +
				"record.routeIp = '" + networkIp + "' and " +
				"record.sessionState = " + 0 + " and " +
				"record.startTime > '" + timestamp + "'");
		
		return (Long)query.getSingleResult();
	}

	@Override
	public Long getCityCurSessionNum(Integer cityId) {
		List<String> allNetworkIps = cityNetworkService.getAllPublicIpsOfCity(cityId);
		
		if (allNetworkIps == null) {
			return null;
		}
		
		StringBuffer networkIpQueryCondition = new StringBuffer();
		int i = 0;
		for (; i < allNetworkIps.size() - 1; i++) {
			networkIpQueryCondition.append("record.routeIp = '" + allNetworkIps.get(i) + "' or ");
		}
		networkIpQueryCondition.append("record.routeIp = '" + allNetworkIps.get(i) + "'");
		
		//只统计五分钟内还在进行的会话，防止由于错误会话一直处于进行状态的情况
		Date dNow = new Date();
		Timestamp timestamp = DateUtil.transferDateInSecondField(dNow, -300);
		
		Query query =  em.createQuery("select count(*) from SessionRecord record where "
				+ "(" + networkIpQueryCondition.toString() + ") and " +
				"record.sessionState = " + 0 + " and " +
				"record.startTime > '" + timestamp + "'");
		
		return (Long)query.getSingleResult();
	}

	@Override
	public Long getCityHistorySessionNum(Integer cityId, Date from, Date to) {
		List<String> allNetworkIps = cityNetworkService.getAllPublicIpsOfCity(cityId);
		
		if (allNetworkIps == null) {
			return null;
		}
		
		StringBuffer networkIpQueryCondition = new StringBuffer();
		int i = 0;
		for (; i < allNetworkIps.size() - 1; i++) {
			networkIpQueryCondition.append("record.routeIp = '" + allNetworkIps.get(i) + "' or ");
		}
		networkIpQueryCondition.append("record.routeIp = '" + allNetworkIps.get(i) + "'");
		
		Timestamp timestamp1 = DateUtil.transferDateInSecondField(from, 0);
		Timestamp timestamp2 = DateUtil.transferDateInSecondField(to, 0);
		
		Query query =  em.createQuery("select count(*) from SessionRecord record where "
				+ "(" + networkIpQueryCondition.toString() + ") and " +
				"record.sessionState = " + 1 + " and " +
				"record.startTime > '" + timestamp1 + "' and " + 
				"record.startTime <= '" + timestamp2 + "'");
		
		return (Long)query.getSingleResult();
	}

	@Override
	public Long getNetworkHistorySessionNum(Integer networkIp, Date from,
			Date to) {
		// TODO Auto-generated method stub
		return null;
	}

}
