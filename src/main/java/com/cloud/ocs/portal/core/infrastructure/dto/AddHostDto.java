package com.cloud.ocs.portal.core.infrastructure.dto;

/**
 * 用于添加物理主机返回信息的Dto
 * 
 * @author Wang Chao
 * 
 * @date 2014-12-20 下午3:23:05
 * 
 */
public class AddHostDto {
	
	public static final int ADD_HOST_CODE_SUCCESS = 20000000;
	public static final int ADD_HOST_CODE_ERROR   = 50000000;

	private int code;
	private String message;
	private HostDto hostDto;
	private int index; //用于表示：新添加的这台主机是集群中的第几台

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

	public HostDto getHostDto() {
		return hostDto;
	}

	public void setHostDto(HostDto hostDto) {
		this.hostDto = hostDto;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
