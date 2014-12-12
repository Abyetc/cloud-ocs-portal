package com.cloud.ocs.portal.core.resource.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloud.ocs.portal.common.cs.CloudStackApiRequest;
import com.cloud.ocs.portal.core.resource.constant.ResourceApiName;
import com.cloud.ocs.portal.core.resource.dto.ClusterDto;
import com.cloud.ocs.portal.core.resource.dto.HostDto;
import com.cloud.ocs.portal.core.resource.dto.PodDto;
import com.cloud.ocs.portal.core.resource.dto.PrimaryStorageDto;
import com.cloud.ocs.portal.core.resource.dto.SecondaryStorageDto;
import com.cloud.ocs.portal.core.resource.dto.SystemVmDto;
import com.cloud.ocs.portal.core.resource.dto.ZoneDto;
import com.cloud.ocs.portal.core.resource.service.InfrastructureService;
import com.cloud.ocs.portal.utils.CloudStackApiRequestSender;
import com.cloud.ocs.portal.utils.CloudStackApiSignatureUtil;

/**
 * 系统资源基础设施模块service实现类
 * 
 * @author Wang Chao
 *
 * @date 2014-12-11 下午2:36:59
 *
 */
@Transactional
@Service
public class InfrastructureServiceImpl implements InfrastructureService {
	
	@Override
	public List<ZoneDto> getZonesList() {
		CloudStackApiRequest request = new CloudStackApiRequest(ResourceApiName.RESOURCE_API_LIST_ZONES);
		CloudStackApiSignatureUtil.generateSignature(request);
		String requestUrl = request.generateRequestURL();
		String response = CloudStackApiRequestSender.sendGetRequest(requestUrl);
		
		List<ZoneDto> result = new ArrayList<ZoneDto>();
		
		if (response != null) {
			JSONObject responseJsonObj = new JSONObject(response);
			JSONObject zonesListJsonObj = responseJsonObj.getJSONObject("listzonesresponse");
			JSONArray zonesJsonArrayObj = zonesListJsonObj.getJSONArray("zone");
			if (zonesJsonArrayObj != null) {
				for (int i = 0; i < zonesJsonArrayObj.length(); i++) {
					ZoneDto zoneDto = new ZoneDto();
					zoneDto.setZoneId(((JSONObject)zonesJsonArrayObj.get(i)).getString("id"));
					zoneDto.setZoneName(((JSONObject)zonesJsonArrayObj.get(i)).getString("name"));
					zoneDto.setNetworkType(((JSONObject)zonesJsonArrayObj.get(i)).getString("networktype"));
					zoneDto.setAllocationState(((JSONObject)zonesJsonArrayObj.get(i)).getString("allocationstate"));
					result.add(zoneDto);
				}
			}
		}
		
		return result;
	}

	@Override
	public List<PodDto> getPodsList(String zoneId) {
		CloudStackApiRequest request = new CloudStackApiRequest(ResourceApiName.RESOURCE_API_LIST_PODS);
		request.addRequestParams("zoneid", zoneId);
		CloudStackApiSignatureUtil.generateSignature(request);
		String requestUrl = request.generateRequestURL();
		String response = CloudStackApiRequestSender.sendGetRequest(requestUrl);
		
		List<PodDto> result = new ArrayList<PodDto>();
		
		if (response != null) {
			JSONObject responseJsonObj = new JSONObject(response);
			JSONObject podsListJsonObj = responseJsonObj.getJSONObject("listpodsresponse");
			JSONArray podsJsonArrayObj = podsListJsonObj.getJSONArray("pod");
			if (podsJsonArrayObj != null) {
				for (int i = 0; i < podsJsonArrayObj.length(); i++) {
					PodDto podDto = new PodDto();
					podDto.setPodId(((JSONObject)podsJsonArrayObj.get(i)).getString("id"));
					podDto.setPodName(((JSONObject)podsJsonArrayObj.get(i)).getString("name"));
					podDto.setAllocationState(((JSONObject)podsJsonArrayObj.get(i)).getString("allocationstate"));
					result.add(podDto);
				}
			}
		}
		
		return result;
	}

	@Override
	public List<SecondaryStorageDto> getSecondaryStorageList(String zoneId) {
		CloudStackApiRequest request = new CloudStackApiRequest(ResourceApiName.RESOURCE_API_LIST_SECONDARY_STORAGE);
		request.addRequestParams("zoneid", zoneId);
		CloudStackApiSignatureUtil.generateSignature(request);
		String requestUrl = request.generateRequestURL();
		String response = CloudStackApiRequestSender.sendGetRequest(requestUrl);
		
		List<SecondaryStorageDto> result = new ArrayList<SecondaryStorageDto>();
		
		if (response != null) {
			JSONObject responseJsonObj = new JSONObject(response);
			JSONObject secondaryStorageListJsonObj = responseJsonObj.getJSONObject("listimagestoresresponse");
			JSONArray secondaryStorageJsonArrayObj = secondaryStorageListJsonObj.getJSONArray("imagestore");
			if (secondaryStorageJsonArrayObj != null) {
				for (int i = 0; i < secondaryStorageJsonArrayObj.length(); i++) {
					SecondaryStorageDto secondaryStorageDto = new SecondaryStorageDto();
					secondaryStorageDto.setSecondaryStorageId(((JSONObject)secondaryStorageJsonArrayObj.get(i)).getString("id"));
					secondaryStorageDto.setSecondaryStorageName(((JSONObject)secondaryStorageJsonArrayObj.get(i)).getString("name"));
					secondaryStorageDto.setUrl(((JSONObject)secondaryStorageJsonArrayObj.get(i)).getString("url"));
					secondaryStorageDto.setProtocol(((JSONObject)secondaryStorageJsonArrayObj.get(i)).getString("protocol"));
					secondaryStorageDto.setProviderName(((JSONObject)secondaryStorageJsonArrayObj.get(i)).getString("providername"));
					result.add(secondaryStorageDto);
				}
			}
		}
		return result;
	}

	@Override
	public List<SystemVmDto> getSystemVmsList(String zoneId) {
		CloudStackApiRequest request = new CloudStackApiRequest(ResourceApiName.RESOURCE_API_LIST_SYSTEM_VMS);
		request.addRequestParams("zoneid", zoneId);
		CloudStackApiSignatureUtil.generateSignature(request);
		String requestUrl = request.generateRequestURL();
		String response = CloudStackApiRequestSender.sendGetRequest(requestUrl);
		
		List<SystemVmDto> result = new ArrayList<SystemVmDto>();
		
		if (response != null) {
			JSONObject responseJsonObj = new JSONObject(response);
			JSONObject systemVmsListJsonObj = responseJsonObj.getJSONObject("listsystemvmsresponse");
			JSONArray systemVmsJsonArrayObj = systemVmsListJsonObj.getJSONArray("systemvm");
			if (systemVmsJsonArrayObj != null) {
				for (int i = 0; i < systemVmsJsonArrayObj.length(); i++) {
					SystemVmDto systemVmDto = new SystemVmDto();
					systemVmDto.setSystemVmId(((JSONObject)systemVmsJsonArrayObj.get(i)).getString("id"));
					systemVmDto.setSystemVmName(((JSONObject)systemVmsJsonArrayObj.get(i)).getString("name"));
					systemVmDto.setSystemVmType(((JSONObject)systemVmsJsonArrayObj.get(i)).getString("systemvmtype"));
					systemVmDto.setHostName(((JSONObject)systemVmsJsonArrayObj.get(i)).getString("hostname"));
					systemVmDto.setCreatedDate(((JSONObject)systemVmsJsonArrayObj.get(i)).getString("created"));
					systemVmDto.setState(((JSONObject)systemVmsJsonArrayObj.get(i)).getString("state"));
					result.add(systemVmDto);
				}
			}
		}
		
		return result;
	}

	@Override
	public List<ClusterDto> getClustersList(String podId) {
		CloudStackApiRequest request = new CloudStackApiRequest(ResourceApiName.RESOURCE_API_LIST_CLUSTERS);
		request.addRequestParams("podid", podId);
		CloudStackApiSignatureUtil.generateSignature(request);
		String requestUrl = request.generateRequestURL();
		String response = CloudStackApiRequestSender.sendGetRequest(requestUrl);
		
		List<ClusterDto> result = new ArrayList<ClusterDto>();
		
		if (response != null) {
			JSONObject responseJsonObj = new JSONObject(response);
			JSONObject clustersListJsonObj = responseJsonObj.getJSONObject("listclustersresponse");
			JSONArray clustersJsonArrayObj = clustersListJsonObj.getJSONArray("cluster");
			if (clustersJsonArrayObj != null) {
				for (int i = 0; i < clustersJsonArrayObj.length(); i++) {
					ClusterDto clusterDto = new ClusterDto();
					clusterDto.setClusterId(((JSONObject)clustersJsonArrayObj.get(i)).getString("id"));
					clusterDto.setClusterName(((JSONObject)clustersJsonArrayObj.get(i)).getString("name"));
					clusterDto.setHypervisorType(((JSONObject)clustersJsonArrayObj.get(i)).getString("hypervisortype"));
					clusterDto.setAllocationState(((JSONObject)clustersJsonArrayObj.get(i)).getString("allocationstate"));
					result.add(clusterDto);
				}
			}
		}
		
		return result;
	}

	@Override
	public List<PrimaryStorageDto> getPrimaryStorageList(String clusterId) {
		CloudStackApiRequest request = new CloudStackApiRequest(ResourceApiName.RESOURCE_API_LIST_PRIMARY_STORAGE);
//		request.addRequestParams("clusterid", clusterId); //很奇怪，加了这个参数就返回空，不知为啥！
		CloudStackApiSignatureUtil.generateSignature(request);
		String requestUrl = request.generateRequestURL();
		String response = CloudStackApiRequestSender.sendGetRequest(requestUrl);
		
		List<PrimaryStorageDto> result = new ArrayList<PrimaryStorageDto>();
		
		if (response != null) {
			JSONObject responseJsonObj = new JSONObject(response);
			JSONObject primaryStorageListJsonObj = responseJsonObj.getJSONObject("liststoragepoolsresponse");
			JSONArray primaryStorageJsonArrayObj = primaryStorageListJsonObj.getJSONArray("storagepool");
			if (primaryStorageJsonArrayObj != null) {
				for (int i = 0; i < primaryStorageJsonArrayObj.length(); i++) {
					PrimaryStorageDto primaryStorageDto = new PrimaryStorageDto();
					primaryStorageDto.setPrimaryStorageId(((JSONObject)primaryStorageJsonArrayObj.get(i)).getString("id"));
					primaryStorageDto.setPrimaryStorageName(((JSONObject)primaryStorageJsonArrayObj.get(i)).getString("name"));
					primaryStorageDto.setType(((JSONObject)primaryStorageJsonArrayObj.get(i)).getString("type"));
					primaryStorageDto.setPath(((JSONObject)primaryStorageJsonArrayObj.get(i)).getString("path"));
					primaryStorageDto.setHostIpAddress(((JSONObject)primaryStorageJsonArrayObj.get(i)).getString("ipaddress"));
					primaryStorageDto.setState(((JSONObject)primaryStorageJsonArrayObj.get(i)).getString("state"));
					result.add(primaryStorageDto);
				}
			}
		}
		return result;
	}

	@Override
	public List<HostDto> getHostsList(String clusterId) {
		CloudStackApiRequest request = new CloudStackApiRequest(ResourceApiName.RESOURCE_API_LIST_HOSTS);
		request.addRequestParams("clusterid", clusterId);
		CloudStackApiSignatureUtil.generateSignature(request);
		String requestUrl = request.generateRequestURL();
		String response = CloudStackApiRequestSender.sendGetRequest(requestUrl);
		
		List<HostDto> result = new ArrayList<HostDto>();
		
		if (result != null) {
			JSONObject responseJsonObj = new JSONObject(response);
			JSONObject hostsListJsonObj = responseJsonObj.getJSONObject("listhostsresponse");
			JSONArray hostsJsonArrayObj = hostsListJsonObj.getJSONArray("host");
			if (hostsJsonArrayObj != null) {
				for (int i = 0; i < hostsJsonArrayObj.length(); i++) {
					HostDto hostDto = new HostDto();
					hostDto.setHostId(((JSONObject)hostsJsonArrayObj.get(i)).getString("id"));
					hostDto.setHostName(((JSONObject)hostsJsonArrayObj.get(i)).getString("name"));
					hostDto.setHypervisor(((JSONObject)hostsJsonArrayObj.get(i)).getString("hypervisor"));
					hostDto.setIpAddress(((JSONObject)hostsJsonArrayObj.get(i)).getString("ipaddress"));
					hostDto.setState(((JSONObject)hostsJsonArrayObj.get(i)).getString("state"));
					hostDto.setType(((JSONObject)hostsJsonArrayObj.get(i)).getString("type"));
					hostDto.setCreatedDate(((JSONObject)hostsJsonArrayObj.get(i)).getString("created"));
					result.add(hostDto);
				}
			}
			
		}
		
		return result;
	}

}
