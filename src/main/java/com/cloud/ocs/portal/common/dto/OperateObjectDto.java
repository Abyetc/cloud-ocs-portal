package com.cloud.ocs.portal.common.dto;

/**
 * 用于操作(如：删除、停止、启动等)对象(如：虚拟机、网络等)后返回的Dto
 * 
 * @author Wang Chao
 * 
 * @date 2015-1-24 下午10:10:17
 * 
 */
public class OperateObjectDto {

	public static final int OPERATE_OBJECT_CODE_SUCCESS = 20000000;
	public static final int OPERATE_OBJECT_CODE_ERROR = 50000000;

	private int code;
	private String message;
	private Object operatedObject;

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

	public Object getOperatedObject() {
		return operatedObject;
	}

	public void setOperatedObject(Object operatedObject) {
		this.operatedObject = operatedObject;
	}
	
}