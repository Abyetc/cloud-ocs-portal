package com.cloud.ocs.portal.core.monitor.service;

/**
 * 用于监控Ocs引擎程序的service接口
 * 
 * @author Wang Chao
 *
 * @date 2015-1-20 下午6:34:25
 *
 */
public interface OcsEngineMonitorService {
	
	/**
	 * 检查所有Ocs Vm上Ocs引擎程序的运行状态并更新数据库
	 */
	public void checkAndUpdateOcsEngineStateOnAllVms();
}
