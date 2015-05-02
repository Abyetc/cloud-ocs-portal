package com.cloud.ocs.ha;

/**
 * 用于检测主机或虚拟机状态的枚举类
 * 
 * @author Wang Chao
 * 
 * @date 2015-5-2 下午1:51:55
 * 
 */
public enum CheckingState {

	UNNORMAL("Unnormal", 0), NORMAL("Normal", 1);

	private String description;
	private int code;

	private CheckingState(String description, int code) {
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
		for (CheckingState status : CheckingState.values()) {
			if (status.getCode() == code) {
				return status.getDescription();
			}
		}

		return null;
	}

	/**
	 * 根据描述找到对应的code
	 * 
	 * @param description
	 * @return
	 */
	public static Integer getCode(String description) {
		for (CheckingState status : CheckingState.values()) {
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
