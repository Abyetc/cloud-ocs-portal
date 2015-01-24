package com.cloud.ocs.monitor.dao.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.cloud.ocs.monitor.bean.MessageRecord;
import com.cloud.ocs.monitor.common.GenericDaoImpl;
import com.cloud.ocs.monitor.constant.MessageType;
import com.cloud.ocs.monitor.dao.MessageRecordDao;
import com.cloud.ocs.portal.core.business.service.CityNetworkService;
import com.cloud.ocs.portal.utils.DateUtil;

/**
 * 用于访问包处理记录的Dao的实现类
 * 
 * @author Wang Chao
 *
 * @date 2015-1-22 下午5:03:23
 *
 */
@Repository
public class MessageRecordDaoImpl extends GenericDaoImpl<MessageRecord> implements MessageRecordDao {
	
	@Resource
	private CityNetworkService cityNetworkService;

	@Override
	public List<MessageRecord> findAll() {
		Query query = em.createQuery("select model from MessageRecord model");
		return query.getResultList();
	}

	@Override
	public Double getMessageAverageProcessTimeOfVm(String networkIp,
			String vmIp, MessageType messageType) {
		String messageTypeQueryCondition = this.transferMessageTypeToQueryConditionString(messageType);
		
		//统计在从现在开始到一分钟之前之间处理完成的包
		Date dNow = new Date();
		Timestamp oneMinAgoTimestamp = DateUtil.transferDateInSecondField(dNow, -60);
		
		Query query =  em.createQuery("select avg(record.processTime) from MessageRecord record where " +
				"record.routeIp = '" + networkIp + "' and " +
				"record.vmip = '" + vmIp + "' " +
				messageTypeQueryCondition +
				" and record.startTime >= '" + oneMinAgoTimestamp + "'");
		
		return (Double)query.getSingleResult();
	}

	@Override
	public Double getMessageAverageProcessTimeOfNetwork(String networkIp,
			MessageType messageType) {
		String messageTypeQueryCondition = this.transferMessageTypeToQueryConditionString(messageType);
		
		//统计在从现在开始到一分钟之前之间处理完成的包
		Date dNow = new Date();
		Timestamp oneMinAgoTimestamp = DateUtil.transferDateInSecondField(dNow, -60);
		
		Query query =  em.createQuery("select avg(record.processTime) from MessageRecord record where " +
				"record.routeIp = '" + networkIp + "' " +
				messageTypeQueryCondition +
				" and record.startTime >= '" + oneMinAgoTimestamp + "'");
		
		return (Double)query.getSingleResult();
	}

	@Override
	public Double getMessageAverageProcessTimeOfCity(Integer cityId,
			MessageType messageType) {
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
		
		String messageTypeQueryCondition = this.transferMessageTypeToQueryConditionString(messageType);
		
		//统计在从现在开始到一分钟之前之间处理完成的包
		Date dNow = new Date();
		Timestamp oneMinAgoTimestamp = DateUtil.transferDateInSecondField(dNow, -60);
		
		Query query =  em.createQuery("select avg(record.processTime) from MessageRecord record where " +
				"(" + networkIpQueryCondition.toString() + ") " +
				messageTypeQueryCondition +
				" and record.startTime >= '" + oneMinAgoTimestamp + "'");
		
		return (Double)query.getSingleResult();
	}
	
	/**
	 * 将包类型转换为数据库查询的查询字符串
	 * @param messageType
	 * @return
	 */
	private String transferMessageTypeToQueryConditionString(MessageType messageType) {
		StringBuffer messageTypeQueryCondition = new StringBuffer();
		switch (messageType.getCode()) {
			case 0:
				messageTypeQueryCondition.append("");
				break;
			case 1:
				messageTypeQueryCondition.append("and record.requestType=1");
				break;
			case 2:
				messageTypeQueryCondition.append("and record.requestType=2");
				break;
			case 3:
				messageTypeQueryCondition.append("and record.requestType=3");
				break;
			default:
				break;
		}
		
		return messageTypeQueryCondition.toString();
	}

}
