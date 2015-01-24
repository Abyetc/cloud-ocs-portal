package com.cloud.ocs.monitor.dto;

import com.cloud.ocs.monitor.constant.MessageType;

/**
 * 用于返回包平均处理时长的包装类
 * 
 * @author Wang Chao
 * 
 * @date 2015-1-24 下午2:27:42
 * 
 */
public class MessageAverageProcessTimeWrapper {

	private MessageType messageType;
	private double processTime;

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	public double getProcessTime() {
		return processTime;
	}

	public void setProcessTime(double processTime) {
		this.processTime = processTime;
	}

}
