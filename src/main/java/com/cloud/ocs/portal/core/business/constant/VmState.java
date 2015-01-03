package com.cloud.ocs.portal.core.business.constant;

import com.cloud.ocs.portal.core.user.constant.LoginStatus;

/**
 * CloudStack中VM的状态
 * 
 * @author Wang Chao
 *
 * @date 2014-12-30 下午9:28:01
 *
 */
public enum VmState {

	SRARTING("Starting", 0), 
	RUNNING("Running", 1), 
	STOPPING("Stopping", 2),
	STOPPED("Stopped", 3),
	MIGRATING("Migrating", 4), 
	ERROR("Error", 5), 
	UNKNOWN("Unknown", 6),
	SHUTDOWNED("Shutdowned", 7),;

	private String description;
	private int code;

	private VmState(String description, int code) {
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
