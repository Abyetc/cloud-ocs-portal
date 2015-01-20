package com.cloud.ocs.portal.core.business.constant;

/**
 * CloudStack中VM的状态
 * 
 * @author Wang Chao
 *
 * @date 2014-12-30 下午9:28:01
 *
 */
public enum OcsVmState {

	SRARTING("Starting", 0), 
	RUNNING("Running", 1), 
	STOPPING("Stopping", 2),
	STOPPED("Stopped", 3),
	MIGRATING("Migrating", 4), 
	ERROR("Error", 5), 
	UNKNOWN("Unknown", 6),
	SHUTDOWNED("Shutdowned", 7),
	DESTROYED("Destroyed", 8),
	EXPUNGING("Expunging", 9);

	private String description;
	private int code;

	private OcsVmState(String description, int code) {
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
		for (OcsVmState status : OcsVmState.values()) {
			if (status.getCode() == code) {
				return status.getDescription();
			}
		}

		return null;
	}
	
	/**
	 * 根据描述找到对应的code
	 * @param description
	 * @return
	 */
	public static Integer getCode(String description) {
		for (OcsVmState status : OcsVmState.values()) {
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
