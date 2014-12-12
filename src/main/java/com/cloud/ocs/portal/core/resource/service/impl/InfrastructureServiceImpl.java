package com.cloud.ocs.portal.core.resource.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloud.ocs.portal.common.cs.CloudStackApiRequest;
import com.cloud.ocs.portal.core.resource.constant.ResourceApiName;
import com.cloud.ocs.portal.core.resource.dto.PodDto;
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
					try {
						systemVmDto.setCreatedDate(((JSONObject)systemVmsJsonArrayObj.get(i)).getString("created"));
					} catch (Exception e) {
						e.printStackTrace();
					}
					systemVmDto.setState(((JSONObject)systemVmsJsonArrayObj.get(i)).getString("state"));
					result.add(systemVmDto);
				}
			}
		}
		
		return result;
	}

}
