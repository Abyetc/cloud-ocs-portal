package com.cloud.ocs.portal.core.infrastructure.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.cloud.ocs.portal.common.cs.CloudStackApiRequest;
import com.cloud.ocs.portal.core.infrastructure.constant.ResourceApiName;
import com.cloud.ocs.portal.core.infrastructure.dto.CapacityDto;
import com.cloud.ocs.portal.core.infrastructure.service.CapacityService;
import com.cloud.ocs.portal.utils.UnitUtil;
import com.cloud.ocs.portal.utils.cs.CloudStackApiSignatureUtil;
import com.cloud.ocs.portal.utils.http.HttpRequestSender;

/**
 * 系统资源系统容量模块service实现类
 * 
 * @author Wang Chao
 *
 * @date 2014-12-11 下午2:39:34
 *
 */
@Service
public class CapacityServiceImpl implements CapacityService {

	@Override
	public List<CapacityDto> getCapacityList(String zoneId) {
		CloudStackApiRequest request = new CloudStackApiRequest(ResourceApiName.RESOURCE_API_LIST_CAPACITY);
		request.addRequestParams("zoneid", zoneId);
		request.addRequestParams("fetchlatest", "true");
		request.addRequestParams("sortby", "usage");
		CloudStackApiSignatureUtil.generateSignature(request);
		String requestUrl = request.generateRequestURL();
		String response = HttpRequestSender.sendGetRequest(requestUrl);
		
		List<CapacityDto> result = new ArrayList<CapacityDto>();
		
		if (response != null) {
			JSONObject responseJsonObj = new JSONObject(response);
			JSONObject capacityListJsonObj = responseJsonObj.getJSONObject("listcapacityresponse");
			if (capacityListJsonObj.has("capacity")) {
				JSONArray capacityJsonArrayObj = capacityListJsonObj.getJSONArray("capacity");
				if (capacityJsonArrayObj != null) {
					for (int i = 0; i < capacityJsonArrayObj.length(); i++) {
						CapacityDto capacityDto = new CapacityDto();
						JSONObject jsonObject = (JSONObject)capacityJsonArrayObj.get(i);
						int type = jsonObject.getInt("type");
						capacityDto.setCapacityCSCode(type);
						switch (type) {
							case 0: //内存
								capacityDto.setCapacityName("内存");
								capacityDto.setUnit("GB");
								capacityDto.setCapacityTotal(UnitUtil.formatSizeUnit(jsonObject.getLong("capacitytotal")));
								capacityDto.setCapacityUsed(UnitUtil.formatSizeUnit(jsonObject.getLong("capacityused")));
								break;
							case 1: //CPU
								capacityDto.setCapacityName("CPU");
								capacityDto.setUnit("GHz");
								capacityDto.setCapacityTotal(UnitUtil.formatSizeFromHzToGHz(jsonObject.getLong("capacitytotal")));
								capacityDto.setCapacityUsed(UnitUtil.formatSizeFromHzToGHz(jsonObject.getLong("capacityused")));
								break;
							case 2: //
								break;
							case 3: //主存储
								capacityDto.setCapacityName("主存储");
								capacityDto.setUnit("GB");
								capacityDto.setCapacityTotal(UnitUtil.formatSizeUnit(jsonObject.getLong("capacitytotal")));
								capacityDto.setCapacityUsed(UnitUtil.formatSizeUnit(jsonObject.getLong("capacityused")));
								break;
							case 4: //公用类IP地址
								capacityDto.setCapacityName("公用类IP地址");
								capacityDto.setUnit("个");
								capacityDto.setCapacityTotal(Long.toString(jsonObject.getLong("capacitytotal")));
								capacityDto.setCapacityUsed(Long.toString(jsonObject.getLong("capacityused")));
								break;
							case 5: //管理类IP地址
								capacityDto.setCapacityName("管理类IP地址");
								capacityDto.setUnit("个");
								capacityDto.setCapacityTotal(Long.toString(jsonObject.getLong("capacitytotal")));
								capacityDto.setCapacityUsed(Long.toString(jsonObject.getLong("capacityused")));
								break;
							case 6: //辅助存储
								capacityDto.setCapacityName("辅助存储");
								capacityDto.setUnit("GB");
								capacityDto.setCapacityTotal(UnitUtil.formatSizeUnit(jsonObject.getLong("capacitytotal")));
								capacityDto.setCapacityUsed(UnitUtil.formatSizeUnit(jsonObject.getLong("capacityused")));
								break;
							case 7: //VLAN
								capacityDto.setCapacityName("VLAN");
								capacityDto.setUnit("个");
								capacityDto.setCapacityTotal(Long.toString(jsonObject.getLong("capacitytotal")));
								capacityDto.setCapacityUsed(Long.toString(jsonObject.getLong("capacityused")));
								break;
							case 19: //GPU
								capacityDto.setCapacityName("GPU");
								capacityDto.setUnit("个");
								capacityDto.setCapacityTotal(Long.toString(jsonObject.getLong("capacitytotal")));
								capacityDto.setCapacityUsed(Long.toString(jsonObject.getLong("capacityused")));
								break;
							default:
								break;
						}
						capacityDto.setPercentUsed(jsonObject.getDouble("percentused"));
						result.add(capacityDto);
					}
				}
			}
		}
		
		return result;
	}

}
