package com.cloud.ocs.portal.core.business.service;

import java.util.List;
import java.util.Map;

import com.cloud.ocs.portal.core.business.dto.AddOcsVmDto;
import com.cloud.ocs.portal.core.business.dto.OcsVmDto;

/**
 * 网络-Vm Service接口
 * 
 * @author Wang Chao
 * 
 * @date 2015-1-3 下午9:06:57
 * 
 */
public interface OcsVmService {

	public List<OcsVmDto> getOcsVmsListByNetworkId(String networkId);
	
	/**
	 * 
	 * @param cityId
	 * @return 返回以network name为key，该network中的Vm List为value的map
	 */
	public Map<String, List<OcsVmDto>> getOcsVmsListByCityId(Integer cityId);

	public Integer getOcsVmsNum(String networkId);

	public AddOcsVmDto addOcsVm(String vmName, String networkId, String zoneId,
			String serviceOfferingId, String templateId);
}
