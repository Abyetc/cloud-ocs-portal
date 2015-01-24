package com.cloud.ocs.portal.core.business.service;

import java.util.List;
import java.util.Map;

import com.cloud.ocs.portal.core.business.bean.OcsVm;
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
	
	/**
	 * 得到属于该network下的所有ocs虚拟机的数量
	 * @param networkId
	 * @return
	 */
	public Integer getOcsVmsNum(String networkId);

	/**
	 * 获得属于该network下的所有ocs虚拟机列表
	 * @param networkId
	 * @return
	 */
	public List<OcsVmDto> getOcsVmsListByNetworkId(String networkId);
	
	/**
	 * 得到以network name为key，该network中的Vm List为value的map
	 * @param cityId
	 * @return 
	 */
	public Map<String, List<OcsVmDto>> getOcsVmsListByCityId(Integer cityId);

	/**
	 * 添加新的ocs虚拟机
	 * @param vmName
	 * @param networkId
	 * @param zoneId
	 * @param serviceOfferingId
	 * @param templateId
	 * @return
	 */
	public AddOcsVmDto addOcsVm(String vmName, String networkId, String zoneId,
			String serviceOfferingId, String templateId);
	
	/**
	 * 通过vm id找到OcsVm实体
	 * @param vmId
	 * @return
	 */
	public OcsVm getOcsVmByVmId(String vmId);

}
