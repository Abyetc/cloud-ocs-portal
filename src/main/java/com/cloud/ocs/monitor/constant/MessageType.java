package com.cloud.ocs.monitor.constant;

import java.util.ArrayList;
import java.util.List;

/**
 * DCCA协议包类型
 * 
 * @author Wang Chao
 *
 * @date 2015-1-23 下午9:09:59
 *
 */
public enum MessageType {

	ALL("all", 0), 
	INITIAL("initial", 1), 
	UPDATE("update", 2),
	TERMINAL("terminal", 3);
	
	private String description;
	private int code;
	
	private MessageType(String description, int code) {
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
		for (MessageType status : MessageType.values()) {
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
		for (MessageType status : MessageType.values()) {
			if (status.getDescription().equals(description)) {
				return status.getCode();
			}
		}
		
		return null;
	}
	
	public static List<MessageType> getAllMessageType() {
		List<MessageType> result = new ArrayList<MessageType>();
		
		result.add(ALL);
		result.add(INITIAL);
		result.add(UPDATE);
		result.add(TERMINAL);
		
		return result;
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
