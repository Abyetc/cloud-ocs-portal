package com.cloud.ocs.portal.core.business.constant;

import com.cloud.ocs.portal.core.user.constant.LoginStatus;

/**
 * CloudStack中Network对象的状态
 * 
 * @author Wang Chao
 * 
 * @date 2014-12-30 下午9:25:09
 * 
 */
public enum NetworkState {
	
	ALLOCATED("Allocated", 0), 
	SETUP("Setup", 1), 
	IMPLEMENTING("Implementing", 2),
	IMPLEMENTED("Implemented", 3);

	private String description;
	private int code;

	private NetworkState(String description, int code) {
		this.description = description;
		this.code = code;
	}

	/**
	 * 根据code找到对应的描述
	 * 
	 * @param code
	 * @return
	 */
	public static String getDescription(int code) {
		for (LoginStatus status : LoginStatus.values()) {
			if (status.getCode() == code) {
				return status.getDescription();
			}
		}

		return null;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
