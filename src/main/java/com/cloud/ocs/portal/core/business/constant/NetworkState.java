package com.cloud.ocs.portal.core.business.constant;

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
	SETUP("Setup", 1), //Happens for Shared networks. Isolated network不涉及
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
		for (NetworkState status : NetworkState.values()) {
			if (status.getCode() == code) {
				return status.getDescription();
			}
		}

		return null;
	}
	
	/**
	 * 根据描述找到对应的Code
	 * @param description
	 * @return
	 */
	public static Integer getCode(String description) {
		for (NetworkState status : NetworkState.values()) {
			if (status.getDescription().equals(description)) {
				return status.getCode();
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
