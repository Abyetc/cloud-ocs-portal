package com.cloud.ocs.portal.core.business.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloud.ocs.portal.common.cs.CloudStackApiRequest;
import com.cloud.ocs.portal.core.business.bean.CityNetwork;
import com.cloud.ocs.portal.core.business.constant.BusinessApiName;
import com.cloud.ocs.portal.core.business.constant.CloudOcsServicePublicPort;
import com.cloud.ocs.portal.core.business.constant.NetworkState;
import com.cloud.ocs.portal.core.business.dao.CityNetworkDao;
import com.cloud.ocs.portal.core.business.dto.AddCityNetworkDto;
import com.cloud.ocs.portal.core.business.service.CityNetworkService;
import com.cloud.ocs.portal.utils.cs.CloudStackApiRequestSender;
import com.cloud.ocs.portal.utils.cs.CloudStackApiSignatureUtil;

/**
 * 城市-网络service实现类
 * 
 * @author Wang Chao
 *
 * @date 2014-12-31 下午12:04:50
 *
 */
@Transactional
@Service
public class CityNetworkServiceImpl implements CityNetworkService {
	
	@Resource
	private CityNetworkDao cityNetworkDao;

	@Override
	public List<CityNetwork> getCityNetworksList(Integer cityId) {
		
		return cityNetworkDao.findCityNetworksbyCityId(cityId);
	}

	@Override
	public AddCityNetworkDto addCityNetwork(CityNetwork cityNetwork) {
		AddCityNetworkDto result = new AddCityNetworkDto();
		result.setCode(AddCityNetworkDto.ADD_NETWORK_CODE_ERROR); //初始默认先添加失败
		result.setMessage("Add City-Network Error.");
		
		this.sendAddingNetworkRequestToCS(cityNetwork);
		if (cityNetwork.getNetworkId() != null) {
			Date created = new Date();
			cityNetwork.setCreated(new Timestamp(created.getTime()));
			cityNetwork.setServicePort(CloudOcsServicePublicPort.PUBLIC_SERVICE_PORT);
			cityNetwork.setNetworkState(NetworkState.ALLOCATED.getCode());
			//持久化到数据库
			cityNetworkDao.persist(cityNetwork);
			if (cityNetwork.getId() != null) {  //添加成功
				result.setCode(AddCityNetworkDto.ADD_NETWORK_CODE_SUCCESS);
				result.setMessage("Add City-Network Success.");
				result.setCityNetwork(cityNetwork);
				result.setIndex(cityNetworkDao.findCityNetworksbyCityId(cityNetwork.getCityId()).size());
			}
		}
		
		return result;
	}

	/**
	 * 发送添加网络的请求到CloudStack
	 * @return
	 */
	private void sendAddingNetworkRequestToCS(CityNetwork cityNetwork) {
		CloudStackApiRequest request = new CloudStackApiRequest(BusinessApiName.BUSINESS_API_ADD_NETWORK);
		request.addRequestParams("zoneid", cityNetwork.getZoneId());
		request.addRequestParams("networkofferingid", cityNetwork.getNetworkOfferingId());
		request.addRequestParams("name", cityNetwork.getNetworkName());
		request.addRequestParams("displaytext", cityNetwork.getNetworkName());
		CloudStackApiSignatureUtil.generateSignature(request);
		String requestUrl = request.generateRequestURL();
		String response = CloudStackApiRequestSender.sendGetRequest(requestUrl);
		
		if (response != null) {
			JSONObject responseJsonObj = new JSONObject(response);
			JSONObject resultJsonObj = responseJsonObj.getJSONObject("createnetworkresponse");
			if (resultJsonObj.has("network")) {
				cityNetwork.setNetworkId(resultJsonObj.getJSONObject("network").getString("id"));
			}
		}
	}
}
