package com.cloud.ocs.portal.common.cs.asyncjob.constant;

import com.cloud.ocs.portal.core.auth.constant.LoginStatus;

/**
 * 用于表示查询CloudStack异步任务返回结果状态的枚举
 * 
 * @author Wang Chao
 *
 * @date 2014-12-30 下午7:44:11
 *
 */
public enum AsyncJobStatus {
	
	PENDING("正在执行", 0),
	SUCCESS("执行成功", 1),
	FAILED("执行失败", 2);
	
	
	private String description;
	private int code;
	
	private AsyncJobStatus(String description, int code) {
		this.description = description;
		this.code = code;
	}
	
	/**
	 * 根据code找到对应的描述
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
