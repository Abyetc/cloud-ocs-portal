package com.cloud.ocs.monitor.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cloud.ocs.monitor.dao.SessionRecordDao;
import com.cloud.ocs.monitor.service.SessionRecordService;

/**
 * 会话处理记录service实现类
 * 
 * @author Wang Chao
 *
 * @date 2015-1-22 下午5:09:30
 *
 */
@Service
public class SessionRecordServiceImpl implements SessionRecordService {
	
	@Resource
	private SessionRecordDao sessionRecordDao;

	@Override
	public Long getNetworkCurSessionNum(String networkIp) {

		return sessionRecordDao.getNetworkCurSessionNum(networkIp);
	}

	@Override
	public Long getCityCurSessionNum(Integer cityId) {
		return sessionRecordDao.getCityCurSessionNum(cityId);
	}

}
