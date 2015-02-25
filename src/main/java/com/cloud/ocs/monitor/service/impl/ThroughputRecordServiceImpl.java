package com.cloud.ocs.monitor.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cloud.ocs.monitor.dao.ThroughputRecordDao;
import com.cloud.ocs.monitor.service.ThroughputRecordService;
import com.cloud.ocs.portal.core.monitor.dto.MessageThroughputDto;

/**
 * 用于处理吞吐量记录的Service实现类
 * 
 * @author Charles
 *
 */
@Service
public class ThroughputRecordServiceImpl implements ThroughputRecordService {

	@Resource
	private ThroughputRecordDao throughputRecordDao;

	@Override
	public MessageThroughputDto getMessageThroughputOfCity(Integer cityId) {
		return throughputRecordDao.getMessageThroughputOfCity(cityId);
	}

	@Override
	public MessageThroughputDto getMessageThroughputOfNetwork(String networkIp) {
		return throughputRecordDao.getMessageThroughputOfNetwork(networkIp);
	}

	@Override
	public MessageThroughputDto getMessageThroughputOfVm(String networkIp,
			String vmIp) {
		return throughputRecordDao.getMessageThroughputOfVm(networkIp, vmIp);
	}

}
