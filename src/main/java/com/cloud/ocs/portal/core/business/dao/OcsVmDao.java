package com.cloud.ocs.portal.core.business.dao;

import com.cloud.ocs.portal.common.dao.GenericDao;
import com.cloud.ocs.portal.core.business.bean.OcsVm;

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
}
