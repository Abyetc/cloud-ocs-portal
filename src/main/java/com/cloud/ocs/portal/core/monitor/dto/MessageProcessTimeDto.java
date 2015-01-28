package com.cloud.ocs.portal.core.monitor.dto;

/**
 * 用于返回包处理平均时间的Dto
 * 
 * @author Wang Chao
 * 
 * @date 2015-1-24 下午2:15:32
 * 
 */
public class MessageProcessTimeDto {

	private double allMessageProcessTime;
	private double messageIProcessTime;
	private double messageUProcessTime;
	private double messageTProcessTime;

	public double getAllMessageProcessTime() {
		return allMessageProcessTime;
	}

	public void setAllMessageProcessTime(double allMessageProcessTime) {
		this.allMessageProcessTime = allMessageProcessTime;
	}

	public double getMessageIProcessTime() {
		return messageIProcessTime;
	}

	public void setMessageIProcessTime(double messageIProcessTime) {
		this.messageIProcessTime = messageIProcessTime;
	}

	public double getMessageUProcessTime() {
		return messageUProcessTime;
	}

	public void setMessageUProcessTime(double messageUProcessTime) {
		this.messageUProcessTime = messageUProcessTime;
	}

	public double getMessageTProcessTime() {
		return messageTProcessTime;
	}

	public void setMessageTProcessTime(double messageTProcessTime) {
		this.messageTProcessTime = messageTProcessTime;
	}

}
