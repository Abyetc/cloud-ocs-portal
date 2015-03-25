package com.cloud.ocs.portal.core.business.service;

import java.util.List;
import java.util.Map;

import com.cloud.ocs.portal.common.bean.OcsVm;
import com.cloud.ocs.portal.common.dto.OperateObjectDto;
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
	 * 通过vm id找到OcsVm实体
	 * @param vmId
	 * @return
	 */
	public OcsVm getOcsVmByVmId(String vmId);
	
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
	 * 获取城市内所有虚拟机
	 * @param cityId
	 * @return
	 */
	public List<OcsVmDto> getOcsVmListByCityId(Integer cityId);

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
	 * 删除ocs虚拟机
	 * @param vmId
	 * @return
	 */
	public OperateObjectDto removeOcsVm(String vmId);
	
	/**
	 * 停止Ocs Vm的运行
	 * @param vmId
	 * @return
	 */
	public OperateObjectDto stopOcsVm(String vmId);
	
	/**
	 * 开启Ocs Vm
	 * @param vmId
	 * @return
	 */
	public OperateObjectDto startOcsVm(String vmId);
	
	/**
	 * 使用SSH远程启动vm上的ocs引擎程序，并将记录入库
	 * @param vmId
	 */
	public void startOcsEngineOnOcsVm(String vmId);
	
	/**
	 * 得到Ocs Vm的状态
	 * @param vmId
	 * @return
	 */
	public Integer getOcsVmstate(String vmId);
	
}
