package com.cloud.ocs.portal.core.auth.constant;

/**
 * 描述系统用户登陆时状态的枚举类
 * @author Wang Chao
 *
 * @date 2014-12-10 下午7:57:24
 *
 */
public enum LoginStatus {
	
	LOGIN_SUCCESS("登陆成功", 1),
	LOGIN_ACCOUNT_ID_ERROR("账号错误", 2),
	LOGIN_ACCOUNT_PASSWORD_ERROE("登陆密码错误", 3);
	
	
	private String description;
	private int code;
	
	private LoginStatus(String description, int code) {
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
