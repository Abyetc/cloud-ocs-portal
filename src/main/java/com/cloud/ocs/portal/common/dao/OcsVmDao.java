package com.cloud.ocs.portal.common.dao;

import com.cloud.ocs.portal.common.bean.OcsVm;

/**
 * Ocs Vm Dao接口
 * 
 * @author Wang Chao
 *
 * @date 2015-1-21 下午4:08:38
 *
 */
public interface OcsVmDao extends GenericDao<OcsVm> {

	/**
	 * 根据vmId找到OcsVm实体
	 * @param vmId
	 * @return
	 */
	public OcsVm findByVmId(String vmId);
	
	/**
	 * 根据vmId删除数据库中的实体
	 * @param vmId
	 * @return
	 */
	public int deleteByVmId(String vmId);
	
	/**
	 * 更新记录
	 * @param ocsVm
	 * @return
	 */
	public OcsVm update(OcsVm ocsVm);
	
	/**
	 * 根据Vm Id得到Vm的状态
	 * @param vmId
	 * @return
	 */
	public Integer findOcsVmStateByVmId(String vmId);
}
