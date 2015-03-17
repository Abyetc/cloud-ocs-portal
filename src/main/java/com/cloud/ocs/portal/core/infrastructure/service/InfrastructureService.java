package com.cloud.ocs.portal.core.infrastructure.service;

import java.util.List;

import com.cloud.ocs.portal.common.dto.OperateObjectDto;
import com.cloud.ocs.portal.core.infrastructure.dto.AddHostDto;
import com.cloud.ocs.portal.core.infrastructure.dto.ClusterDto;
import com.cloud.ocs.portal.core.infrastructure.dto.HostDto;
import com.cloud.ocs.portal.core.infrastructure.dto.NetworkOfferingDto;
import com.cloud.ocs.portal.core.infrastructure.dto.PodDto;
import com.cloud.ocs.portal.core.infrastructure.dto.PrimaryStorageDto;
import com.cloud.ocs.portal.core.infrastructure.dto.SecondaryStorageDto;
import com.cloud.ocs.portal.core.infrastructure.dto.ServiceOfferingDto;
import com.cloud.ocs.portal.core.infrastructure.dto.SystemVmDto;
import com.cloud.ocs.portal.core.infrastructure.dto.TemplateDto;
import com.cloud.ocs.portal.core.infrastructure.dto.ZoneDto;

/**
 * 系统资源基础设施模块service接口
 * 
 * @author Wang Chao
 * 
 * @date 2014-12-11 下午2:32:41
 * 
 */
public interface InfrastructureService {

	/**
	 * 获取区域列表
	 * @return
	 */
	public List<ZoneDto> getZonesList();

	/**
	 * 获取区域中的机架列表
	 * @param zoneId
	 * @return
	 */
	public List<PodDto> getPodsList(String zoneId);

	/**
	 * 获取区域中的二级存储列表
	 * @param zoneId
	 * @return
	 */
	public List<SecondaryStorageDto> getSecondaryStorageList(String zoneId);
	
	/**
	 * 在区域中添加二级存储
	 * @param zoneId
	 * @param name
	 * @param provider
	 * @param serverIp
	 * @param path
	 * @return
	 */
	public OperateObjectDto addSecondaryStorage(String zoneId, String name, String provider, String serverIp, String path);
	
	/**
	 * 删除二级存储
	 * @param secondaryStorageId
	 * @return
	 */
	public OperateObjectDto removeSecondaryStorage(String secondaryStorageId);

	/**
	 * 获取区域中系统虚拟机列表
	 * @param zoneId
	 * @return
	 */
	public List<SystemVmDto> getSystemVmsList(String zoneId);

	/**
	 * 获取机架中集群列表
	 * @param podId
	 * @return
	 */
	public List<ClusterDto> getClustersList(String podId);

	/**
	 * 获取集群中主存储列表
	 * @param clusterId
	 * @return
	 */
	public List<PrimaryStorageDto> getPrimaryStorageList(String clusterId);
	
	/**
	 * 在集群中添加主存储
	 * @param zoneId
	 * @param podId
	 * @param clusterId
	 * @param name
	 * @param protocol
	 * @param serverIp
	 * @param path
	 * @return
	 */
	public OperateObjectDto addPrimaryStorageList(String zoneId, String podId, String clusterId, String name, String protocol, String serverIp, String path);

	/**
	 * 删除主存储
	 * @param primaryStorageId
	 * @return
	 */
	public OperateObjectDto removePrimaryStorageList(String primaryStorageId);
	
	/**
	 * 获取集群中主机列表
	 * @param clusterId
	 * @return
	 */
	public List<HostDto> getHostsList(String clusterId);

	/**
	 * 添加主机
	 * @param zoneId
	 * @param podId
	 * @param clusterId
	 * @param ipAddress
	 * @param hostAccount
	 * @param hostPassword
	 * @return
	 */
	public AddHostDto addHost(String zoneId, String podId, String clusterId,
			String ipAddress, String hostAccount, String hostPassword);
	
	/**
	 * 删除主机
	 * @param hostId
	 * @return
	 */
	public OperateObjectDto removeHost(String hostId);

	/**
	 * 获取网络方案列表
	 * @return
	 */
	public List<NetworkOfferingDto> getIsolatedNetworkOfferingsWithSourceNatServiceList();

	/**
	 * 获取模板列表
	 * @return
	 */
	public List<TemplateDto> getSelfExecutableTemplates();

	/**
	 * 获取服务方案列表
	 * @return
	 */
	public List<ServiceOfferingDto> getNonSystemServiceOfferingList();
}
