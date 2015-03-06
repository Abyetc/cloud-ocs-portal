package com.cloud.ocs.portal.common.dao;

import com.cloud.ocs.portal.common.bean.OcsHost;

/**
 * Ocs Host Dao接口
 * 
 * @author Charles
 *
 */
public interface OcsHostDao extends GenericDao<OcsHost> {

	/**
	 * 以hostId获取OcsHost实体
	 * @param hostId
	 * @return
	 */
	public OcsHost findByHostId(String hostId);
}
