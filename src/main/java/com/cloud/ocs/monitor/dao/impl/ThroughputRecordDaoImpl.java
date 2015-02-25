package com.cloud.ocs.monitor.dao.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.cloud.ocs.monitor.bean.ThroughputRecord;
import com.cloud.ocs.monitor.common.GenericDaoImpl;
import com.cloud.ocs.monitor.dao.ThroughputRecordDao;
import com.cloud.ocs.portal.core.business.service.CityNetworkService;
import com.cloud.ocs.portal.core.monitor.dto.MessageThroughputDto;

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
	public MessageThroughputDto getMessageThroughputOfCity(Integer cityId) {
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
		
		Date dNow = new Date();
		Timestamp timestamp = new Timestamp(dNow.getTime());
		Query query =  em.createQuery("select sum(record.receiveMsgCount), sum(record.finishedMsgCount) from ThroughputRecord record where "
				+ "(" + networkIpQueryCondition.toString() + ") and " +
				"record.recordTime = '" + timestamp + "'");
		List<Object[]> queryResult = (List<Object[]>)query.getResultList();
		if (queryResult != null) {
			result = new MessageThroughputDto();
			if (queryResult.size() != 0) {
				result.setReceivedMessageNum((Long)queryResult.get(0)[0]);
				result.setFinishedMessageNum((Long)queryResult.get(0)[1]);
			}
		}
		
		return result;
	}

	@Override
	public MessageThroughputDto getMessageThroughputOfNetwork(String networkIp) {
		MessageThroughputDto result = null;
		
		Date dNow = new Date();
		Timestamp timestamp = new Timestamp(dNow.getTime());
		Query query =  em.createQuery("select sum(record.receiveMsgCount), sum(record.finishedMsgCount) from ThroughputRecord record where "
				+ "record.routeIp = '" + networkIp + "' and " +
				"record.recordTime = '" + timestamp + "'");
		List<Object[]> queryResult = (List<Object[]>)query.getResultList();
		if (queryResult != null) {
			result = new MessageThroughputDto();
			if (queryResult.size() != 0) {
				result.setReceivedMessageNum((Long)queryResult.get(0)[0]);
				result.setFinishedMessageNum((Long)queryResult.get(0)[1]);
			}
		}
		
		return result;
	}

	@Override
	public MessageThroughputDto getMessageThroughputOfVm(String networkIp,
			String vmIp) {
		MessageThroughputDto result = null;
		
		Date dNow = new Date();
		Timestamp timestamp = new Timestamp(dNow.getTime());
		Query query =  em.createQuery("select record.receiveMsgCount, record.finishedMsgCount from ThroughputRecord record where "
				+ "record.routeIp = '" + networkIp + "' and " +
				"record.vmip = '" + vmIp + "' and " +
				"record.recordTime = '" + timestamp + "'");
		List<Object[]> queryResult = (List<Object[]>)query.getResultList();
		if (queryResult != null) {
			result = new MessageThroughputDto();
			if (queryResult.size() != 0) {
				result.setReceivedMessageNum((Long)queryResult.get(0)[0]);
				result.setFinishedMessageNum((Long)queryResult.get(0)[1]);
			}
		}
		
		return result;
	}

}
