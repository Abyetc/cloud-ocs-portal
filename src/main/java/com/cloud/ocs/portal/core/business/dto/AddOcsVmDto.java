package com.cloud.ocs.portal.core.business.dto;

/**
 * 添加OCS Vm返回信息的Dto
 * 
 * @author Wang Chao
 * 
 * @date 2015-1-4 下午4:34:35
 * 
 */
public class AddOcsVmDto {

	public static final int ADD_OCS_VM_CODE_SUCCESS = 20000000;
	public static final int ADD_OCS_VM_CODE_ERROR = 50000000;

	private int code;
	private String message;
	private OcsVmDto ocsVmDto;
	private int index;

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

	public OcsVmDto getOcsVmDto() {
		return ocsVmDto;
	}

	public void setOcsVmDto(OcsVmDto ocsVmDto) {
		this.ocsVmDto = ocsVmDto;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
