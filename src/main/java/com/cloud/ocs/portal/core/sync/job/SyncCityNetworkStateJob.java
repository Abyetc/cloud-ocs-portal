package com.cloud.ocs.portal.core.sync.job;

import java.util.logging.Logger;

import javax.annotation.Resource;

import com.cloud.ocs.portal.core.sync.service.SyncCityStateService;
import com.cloud.ocs.portal.core.sync.service.SyncNetworkStateService;


/**
 * 调用CloudStack API将city、network状态同步到本地数据库的Job
 * 
 * @author Wang Chao
 *
 * @date 2015-1-5 下午9:25:00
 *
 */
public class SyncCityNetworkStateJob {
	
	private final static Logger LOGGER = Logger.getLogger(SyncCityNetworkStateJob.class.getName());
	
	@Resource
	private SyncNetworkStateService syncNetworkStateService;
	
	@Resource
	private SyncCityStateService syncCityStateService;

	public void executeSyncCityNetworkStateJob() {
		LOGGER.info("Start to Synchronize City and Network State from CloudStack.");
		
		//同步网络状态
		syncNetworkStateService.syncNetworkState();
		//同步城市状态
		syncCityStateService.syncCityState();
		
		LOGGER.info("Finished to Synchronize City and Network State from CloudStack.");
	}
}
