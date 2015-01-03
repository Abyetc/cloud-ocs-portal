package com.cloud.ocs.portal.core.resource.service;

import java.util.List;

import com.cloud.ocs.portal.core.resource.dto.AddHostDto;
import com.cloud.ocs.portal.core.resource.dto.ClusterDto;
import com.cloud.ocs.portal.core.resource.dto.HostDto;
import com.cloud.ocs.portal.core.resource.dto.NetworkOfferingDto;
import com.cloud.ocs.portal.core.resource.dto.PodDto;
import com.cloud.ocs.portal.core.resource.dto.PrimaryStorageDto;
import com.cloud.ocs.portal.core.resource.dto.SecondaryStorageDto;
import com.cloud.ocs.portal.core.resource.dto.SystemVmDto;
import com.cloud.ocs.portal.core.resource.dto.ZoneDto;

/**
 * 系统资源基础设施模块service接口
 * 
 * @author Wang Chao
 *
 * @date 2014-12-11 下午2:32:41
 *
 */
public interface InfrastructureService {
	
	public List<ZoneDto> getZonesList();
	
	public List<PodDto> getPodsList(String zoneId);
	public List<SecondaryStorageDto> getSecondaryStorageList(String zoneId);
	public List<SystemVmDto> getSystemVmsList(String zoneId);
	
	public List<ClusterDto> getClustersList(String podId);
	
	public List<PrimaryStorageDto> getPrimaryStorageList(String clusterId);
	public List<HostDto> getHostsList(String clusterId);
	public AddHostDto addHost(String zoneId, String podId, String clusterId,
			String ipAddress, String hostAccount, String hostPassword);
	
	public List<NetworkOfferingDto> getIsolatedNetworkOfferingsWithSourceNatServiceList();
}
