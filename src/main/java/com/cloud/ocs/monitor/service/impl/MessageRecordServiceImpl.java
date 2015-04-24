package com.cloud.ocs.monitor.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cloud.ocs.monitor.bean.MessageRecord;
import com.cloud.ocs.monitor.constant.MessageType;
import com.cloud.ocs.monitor.dao.MessageRecordDao;
import com.cloud.ocs.monitor.dto.MessageAverageProcessTimeWrapper;
import com.cloud.ocs.monitor.service.MessageRecordService;

/**
 * 包处理记录service实现类
 * 
 * @author Wang Chao
 * 
 * @date 2015-1-22 下午5:08:28
 * 
 */
@Service
public class MessageRecordServiceImpl implements MessageRecordService {

	@Resource
	private MessageRecordDao messageRecordDao;

	@Override
	public List<MessageRecord> getAll() {

		return messageRecordDao.findAll();
	}

	@Override
	public MessageAverageProcessTimeWrapper getMessageAverageProcessTimeOfVm(
			String networkIp, String vmIp, MessageType messageType) {
		MessageAverageProcessTimeWrapper result = new MessageAverageProcessTimeWrapper();
		result.setMessageType(messageType);
		double resultProcessTime = 0.0;
		Double processTime = messageRecordDao
				.getMessageAverageProcessTimeOfVm(networkIp, vmIp, messageType);
		if (processTime != null) {
			resultProcessTime = processTime.doubleValue();
		}
		result.setProcessTime(resultProcessTime);

		return result;
	}

	@Override
	public MessageAverageProcessTimeWrapper getMessageAverageProcessTimeOfNetwork(
			String networkIp, MessageType messageType) {
		MessageAverageProcessTimeWrapper result = new MessageAverageProcessTimeWrapper();
		result.setMessageType(messageType);
		double resultProcessTime = 0.0;
		Double processTime = messageRecordDao
				.getMessageAverageProcessTimeOfNetwork(networkIp, messageType);
		if (processTime != null) {
			resultProcessTime = processTime.doubleValue();
		}
		result.setProcessTime(resultProcessTime);

		return result;
	}

	@Override
	public MessageAverageProcessTimeWrapper getMessageAverageProcessTimeOfCity(
			Integer cityId, MessageType messageType) {
		MessageAverageProcessTimeWrapper result = new MessageAverageProcessTimeWrapper();
		result.setMessageType(messageType);
		double resultProcessTime = 0.0;
		Double processTime = messageRecordDao
				.getMessageAverageProcessTimeOfCity(cityId, messageType);
		if (processTime != null) {
			resultProcessTime = processTime.doubleValue();
		}
		result.setProcessTime(resultProcessTime);
		
		return result;
	}
	
	@Override
	public MessageAverageProcessTimeWrapper getMessageAverageProcessTimeOfCityAtSpecificDate(
			Integer cityId, MessageType messageType, Date date) {
		MessageAverageProcessTimeWrapper result = new MessageAverageProcessTimeWrapper();
		result.setMessageType(messageType);
		double resultProcessTime = 0.0;
		Double processTime = messageRecordDao
				.getMessageAverageProcessTimeOfCityAtSpecificDate(cityId, messageType, date);
		if (processTime != null) {
			resultProcessTime = processTime.doubleValue();
		}
		result.setProcessTime(resultProcessTime);
		
		return result;
	}

	@Override
	public MessageAverageProcessTimeWrapper getMessageAverageProcessTimeOfNetworkAtSpecificDate(
			String networkIp, MessageType messageType, Date date) {
		MessageAverageProcessTimeWrapper result = new MessageAverageProcessTimeWrapper();
		result.setMessageType(messageType);
		double resultProcessTime = 0.0;
		Double processTime = messageRecordDao
				.getMessageAverageProcessTimeOfNetworkAtSpecificDate(networkIp, messageType, date);
		if (processTime != null) {
			resultProcessTime = processTime.doubleValue();
		}
		result.setProcessTime(resultProcessTime);
		
		return result;
	}

	@Override
	public MessageAverageProcessTimeWrapper getMessageAverageProcessTimeOfVmAtSpecificDate(
			String networkIp, String vmIp, MessageType messageType, Date date) {
		// TODO Auto-generated method stub
		MessageAverageProcessTimeWrapper result = new MessageAverageProcessTimeWrapper();
		result.setMessageType(messageType);
		double resultProcessTime = 0.0;
		Double processTime = messageRecordDao
				.getMessageAverageProcessTimeOfVmAtSpecificDate(networkIp, vmIp, messageType, date);
		if (processTime != null) {
			resultProcessTime = processTime.doubleValue();
		}
		result.setProcessTime(resultProcessTime);
		
		return result;
	}

}
