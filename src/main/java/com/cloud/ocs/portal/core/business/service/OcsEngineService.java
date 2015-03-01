package com.cloud.ocs.portal.core.business.service;

import java.util.List;

import com.cloud.ocs.portal.common.bean.OcsEngine;
import com.cloud.ocs.portal.core.business.constant.OcsEngineState;

/**
 * Vm上的OCS引擎程序service接口
 * 
 * @author Wang Chao
 *
 * @date 2015-1-19 下午9:41:19
 *
 */
public interface OcsEngineService {
	
	/**
	 * 持久化到数据库
	 * @param ocsEngine
	 */
	public void save(OcsEngine ocsEngine);
	
	/**
	 * 得到数据库中所有的Ocs Engine实体
	 * @return
	 */
	public List<OcsEngine> getAllOcsEngines();
	
	/**
	 * 更新数据库中Ocs Engine的状态
	 * @param ocsEngine
	 * @return
	 */
	public OcsEngine updateOcsEngine(OcsEngine ocsEngine);

	/**
	 * 通过SSH远程执行命令启动VM上的OCS引擎程序
	 * @param vmId
	 * @return
	 */
	public Boolean startOcsEngineService(String vmId);
	
	/**
	 * 通过SSH远程执行命令检查VM上的OCS引擎程序是否在运行
	 * @param vmId
	 * @return
	 */
	public OcsEngineState checkOcsEngineServiceRunningState(String vmId);
}
