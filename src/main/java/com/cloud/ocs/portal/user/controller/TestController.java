package com.cloud.ocs.portal.user.controller;

import java.util.Map;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cloud.ocs.portal.common.CloudStackApiRequest;
import com.cloud.ocs.portal.utils.CloudStackApiRequestSender;
import com.cloud.ocs.portal.utils.CloudStackApiSignatureUtil;

@Controller
public class TestController {

	@RequestMapping(value="/test")
	@ResponseBody
	public String getUser() {
//		System.out.println("hihihihi");
		CloudStackApiRequest cloudStackApiRequest = new CloudStackApiRequest("listHosts");
		Map<String, String> params = cloudStackApiRequest.getRequestParams();
		params.put("type", "Routing");
		cloudStackApiRequest.setRequestParams(params);
		
		CloudStackApiSignatureUtil.generateSignature(cloudStackApiRequest);
		
		System.out.println(cloudStackApiRequest.getSignature());
		
		String url = cloudStackApiRequest.generateRequestURL();
		System.out.println(url);
		String responseJson = CloudStackApiRequestSender.sendGetRequest(url);
		System.out.println(responseJson);
		
//		JSONObject jsonObject = new JSONObject(responseJson);
//		JSONArray jsonArray = jsonObject.getJSONObject("listusagetypesresponse").getJSONArray("listUsageTypes");
//		for (int i = 0; i < jsonArray.length(); i++) {
//			System.out.println(((JSONObject)jsonArray.get(i)).getString("name"));
//		}
		
		return responseJson;
	}
}
