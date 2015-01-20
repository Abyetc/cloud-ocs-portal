package com.cloud.ocs.portal.core.business.constant;

/**
 * 用于表示Ocs引擎程序状态的枚举类
 * 
 * @author Wang Chao
 *
 * @date 2015-1-20 下午4:11:27
 *
 */
public enum OcsEngineState {

	STOPPED("stopped", 0),
	RUNNING("Running", 1);
	
	private String description;
	private int code;
	
	private OcsEngineState(String description, int code) {
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
		for (OcsEngineState status : OcsEngineState.values()) {
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
		for (OcsEngineState status : OcsEngineState.values()) {
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
