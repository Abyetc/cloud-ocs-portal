package com.cloud.ocs.monitor.dao.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.cloud.ocs.monitor.bean.ThroughputRecord;
import com.cloud.ocs.monitor.common.GenericDaoImpl;
import com.cloud.ocs.monitor.dao.ThroughputRecordDao;
import com.cloud.ocs.portal.core.business.service.CityNetworkService;
import com.cloud.ocs.portal.core.monitor.dto.MessageThroughputDto;
import com.cloud.ocs.portal.utils.DateUtil;

/**
 * 用于访问吞吐量记录的Dao实现类
 * 
 * @author Charles
 *
 */
@Repository
public class ThroughputRecordDaoImpl extends GenericDaoImpl<ThroughputRecord> implements ThroughputRecordDao {
	
	@Resource
	private CityNetworkService cityNetworkService;

	@Override
	public MessageThroughputDto getCityCurMessageThroughput(Integer cityId) {
		List<String> allNetworkIps = cityNetworkService.getAllPublicIpsOfCity(cityId);
		
		if (allNetworkIps == null) {
			return null;
		}
		
		MessageThroughputDto result = null;
		
		StringBuffer networkIpQueryCondition = new StringBuffer();
		int i = 0;
		for (; i < allNetworkIps.size() - 1; i++) {
			networkIpQueryCondition.append("record.routeIp = '" + allNetworkIps.get(i) + "' or ");
		}
		networkIpQueryCondition.append("record.routeIp = '" + allNetworkIps.get(i) + "'");
		
		Query query =  em.createQuery("select sum(record.receiveMsgCount), sum(record.finishedMsgCount) from ThroughputRecord record where "
				+ "(" + networkIpQueryCondition.toString() + ") and " +
				"record.recordTime = (select max(record2.recordTime) from ThroughputRecord record2)");
		Object[] queryResult = (Object[])query.getSingleResult();
		if (queryResult != null) {
			result = new MessageThroughputDto();
			if (queryResult.length != 0) {
				if (queryResult[0] != null) {
					result.setReceivedMessageNum((Long)queryResult[0]);
				}
				if (queryResult[1] != null) {
					result.setFinishedMessageNum((Long)queryResult[1]);
				}
			}
		}
		
		return result;
	}

	@Override
	public MessageThroughputDto getNetworkCurMessageThroughput(String networkIp) {
		MessageThroughputDto result = null;
		
		Query query =  em.createQuery("select sum(record.receiveMsgCount), sum(record.finishedMsgCount) from ThroughputRecord record where "
				+ "record.routeIp = '" + networkIp + "' and " +
				"record.recordTime = (select max(record2.recordTime) from ThroughputRecord record2)");
		Object[] queryResult = (Object[])query.getSingleResult();
		if (queryResult != null) {
			result = new MessageThroughputDto();
			if (queryResult.length != 0) {
				if (queryResult[0] != null) {
					result.setReceivedMessageNum((Long)queryResult[0]);
				}
				if (queryResult[1] != null) {
					result.setFinishedMessageNum((Long)queryResult[1]);
				}
			}
		}
		
		return result;
	}

	@Override
	public MessageThroughputDto getVmCurMessageThroughput(String networkIp,
			String vmIp) {
		MessageThroughputDto result = null;
		
		Query query =  em.createQuery("select sum(record.receiveMsgCount), sum(record.finishedMsgCount) from ThroughputRecord record where "
				+ "record.routeIp = '" + networkIp + "' and " +
				"record.vmip = '" + vmIp + "' and " +
				"record.recordTime = (select max(record2.recordTime) from ThroughputRecord record2)");
		Object[] queryResult = (Object[])query.getSingleResult();
		if (queryResult != null) {
			result = new MessageThroughputDto();
			if (queryResult.length != 0) {
				if (queryResult[0] != null) {
					result.setReceivedMessageNum((Long)queryResult[0]);
				}
				if (queryResult[1] != null) {
					result.setFinishedMessageNum((Long)queryResult[1]);
				}
			}
		}
		
		return result;
	}

	@Override
	public Map<Date, MessageThroughputDto> getCityHistoryMessageThroughput(
			Integer cityId, Date from, Date to) {
		List<String> allNetworkIps = cityNetworkService.getAllPublicIpsOfCity(cityId);
		
		if (allNetworkIps == null) {
			return null;
		}
		
		Map<Date, MessageThroughputDto> result = new TreeMap<Date, MessageThroughputDto>();
		
		StringBuffer networkIpQueryCondition = new StringBuffer();
		int i = 0;
		for (; i < allNetworkIps.size() - 1; i++) {
			networkIpQueryCondition.append("record.routeIp = '" + allNetworkIps.get(i) + "' or ");
		}
		networkIpQueryCondition.append("record.routeIp = '" + allNetworkIps.get(i) + "'");
		
		Timestamp timestamp1 = DateUtil.transferDateInSecondField(from, 0);
		Timestamp timestamp2 = DateUtil.transferDateInSecondField(to, 0);
		Query query =  em.createQuery("select record.recordTime, record.receiveMsgCount, record.finishedMsgCount from ThroughputRecord record where "
				+ "(" + networkIpQueryCondition.toString() + ") and " +
				"record.recordTime >= '" + timestamp1 + "' and record.recordTime <= '" + timestamp2 + "' order by record.recordTime ASC");
		List<Object[]> queryResult = (List<Object[]>)query.getResultList();
		if (queryResult != null) {
			for (int j = 0; j < queryResult.size(); j++) {
				MessageThroughputDto one = new MessageThroughputDto();
				Date date = (Date)queryResult.get(j)[0];
				if (queryResult.get(j)[1] != null) {
					one.setReceivedMessageNum(new Long((Integer)queryResult.get(j)[1]));
				}
				if (queryResult.get(j)[2] != null) {
					one.setFinishedMessageNum(new Long((Integer)queryResult.get(j)[2]));
				}
				result.put(date, one);
			}
		}
		
//		for (int j = 0; j < 24*60*60; j++) {
//			Date dt = DateUtil.transferDateInSecondField2(from, j);
//			if (result.containsKey(dt)) {
//				continue;
//			}
//			result.put(dt, new MessageThroughputDto());
//		}
		
		return result;
	}

	@Override
	public Map<Date, MessageThroughputDto> getNetworkHistoryMessageThroughput(
			String networkIp, Date from, Date to) {
		Map<Date, MessageThroughputDto> result = new TreeMap<Date, MessageThroughputDto>();
		
		Timestamp timestamp1 = DateUtil.transferDateInSecondField(from, 0);
		Timestamp timestamp2 = DateUtil.transferDateInSecondField(to, 0);
		Query query =  em.createQuery("select record.recordTime, record.receiveMsgCount, record.finishedMsgCount from ThroughputRecord record where "
				+ "record.routeIp = '" + networkIp + "' and " +
				"record.recordTime >= '" + timestamp1 + "' and record.recordTime <= '" + timestamp2 + "' order by record.recordTime ASC");
		List<Object[]> queryResult = (List<Object[]>)query.getResultList();
		if (queryResult != null) {
			for (int j = 0; j < queryResult.size(); j++) {
				MessageThroughputDto one = new MessageThroughputDto();
				Date date = (Date)queryResult.get(j)[0];
				if (queryResult.get(j)[1] != null) {
					one.setReceivedMessageNum(new Long((Integer)queryResult.get(j)[1]));
				}
				if (queryResult.get(j)[2] != null) {
					one.setFinishedMessageNum(new Long((Integer)queryResult.get(j)[2]));
				}
				result.put(date, one);
			}
		}
		
		return result;
	}

	@Override
	public Map<Date, MessageThroughputDto> getVmHistoryMessageThroughput(
			String networkIp, String vmIp, Date from, Date to) {
		// TODO Auto-generated method stub
		Map<Date, MessageThroughputDto> result = new TreeMap<Date, MessageThroughputDto>();
		
		Timestamp timestamp1 = DateUtil.transferDateInSecondField(from, 0);
		Timestamp timestamp2 = DateUtil.transferDateInSecondField(to, 0);
		Query query =  em.createQuery("select record.recordTime, record.receiveMsgCount, record.finishedMsgCount from ThroughputRecord record where "
				+ "record.routeIp = '" + networkIp + "' and " +
				"record.vmip = '" + vmIp + "' and " +
				"record.recordTime >= '" + timestamp1 + "' and record.recordTime <= '" + timestamp2 + "' order by record.recordTime ASC");
		List<Object[]> queryResult = (List<Object[]>)query.getResultList();
		if (queryResult != null) {
			for (int j = 0; j < queryResult.size(); j++) {
				MessageThroughputDto one = new MessageThroughputDto();
				Date date = (Date)queryResult.get(j)[0];
				if (queryResult.get(j)[1] != null) {
					one.setReceivedMessageNum(new Long((Integer)queryResult.get(j)[1]));
				}
				if (queryResult.get(j)[2] != null) {
					one.setFinishedMessageNum(new Long((Integer)queryResult.get(j)[2]));
				}
				result.put(date, one);
			}
		}
		
		return result;
	}

}
