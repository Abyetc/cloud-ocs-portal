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
	public MessageThroughputDto getMessageThroughputOfNetwork(String networkIp) {
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
	public MessageThroughputDto getMessageThroughputOfVm(String networkIp,
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

}
