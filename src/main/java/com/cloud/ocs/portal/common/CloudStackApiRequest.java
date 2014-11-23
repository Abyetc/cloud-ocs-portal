package com.cloud.ocs.portal.common;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.cloud.ocs.portal.properties.CloudStackApiProperties;

/**
 * 用于封装调用CloudStack API的请求
 * @author Wang Chao
 *
 */
public class CloudStackApiRequest {
	
	/** API请求命令  */
	private String command;
	
	/** API请求的参数,包括:
	 * 1.apiKey;
	 * 2.command;
	 * 3.其他参数 */
	private Map<String, String> requestParams; 
	
	/** 该请求对应的签名  */
	private String signature;

	public CloudStackApiRequest(String command) {
		super();
		this.command = command;
		this.requestParams = new TreeMap<String, String>();
		
		//初始化时，先将command和api key放到请求参数列表中
		requestParams.put("command", command);
		requestParams.put("apikey", CloudStackApiProperties.getAPIKey());
		requestParams.put("response", "json"); //全部返回Json类型数据
	}
	
	public void addRequestParams(String name, String value) {
		if (this.requestParams == null) 
			return;
		
		this.requestParams.put(name, value);
	}
	
	public String generateRequestURL() {
		if (this.requestParams == null) 
			return null;
		
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(CloudStackApiProperties.getBaseURL());
		strBuilder.append(CloudStackApiProperties.getApiPath());
		for (Entry<String, String> entry : this.requestParams.entrySet()) {
			strBuilder.append(entry.getKey());
			strBuilder.append('=');
			strBuilder.append(entry.getValue());
			strBuilder.append('&');
		}
		strBuilder.append("signature=" + this.getSignature());
		
		return strBuilder.toString();
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public Map<String, String> getRequestParams() {
		return requestParams;
	}

	public void setRequestParams(Map<String, String> requestParams) {
		this.requestParams = requestParams;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}
	
}
