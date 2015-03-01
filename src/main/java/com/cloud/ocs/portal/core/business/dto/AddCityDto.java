package com.cloud.ocs.portal.core.business.dto;

import com.cloud.ocs.portal.common.bean.City;

/**
 * 新增服务城市返回信息的Dto
 * 
 * @author Wang Chao
 * 
 * @date 2014-12-29 下午8:14:22
 * 
 */
public class AddCityDto {

	public static final int ADD_CITY_CODE_SUCCESS = 20000000;
	public static final int ADD_CITY_CODE_ERROR = 50000000;

	private int code;
	private String message;
	private City city;
	private int index; // 用于表示：新添加的城市是所有城市中的第几个

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

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
