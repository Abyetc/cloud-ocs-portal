package com.cloud.ocs.portal.core.business.dto;

import com.cloud.ocs.portal.core.business.bean.CityNetwork;

/**
 * 新增服务城市网络返回信息的Dto
 * 
 * @author Wang Chao
 * 
 * @date 2014-12-31 上午11:54:29
 * 
 */
public class AddCityNetworkDto {

	public static final int ADD_NETWORK_CODE_SUCCESS = 20000000;
	public static final int ADD_NETWORK_CODE_ERROR = 50000000;

	private int code;
	private String message;
	private CityNetwork cityNetwork;
	private int index;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public CityNetwork getCityNetwork() {
		return cityNetwork;
	}

	public void setCityNetwork(CityNetwork cityNetwork) {
		this.cityNetwork = cityNetwork;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
