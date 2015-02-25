package com.cloud.ocs.portal.core.monitor.dto;

/**
 * 用于记录包吞吐量的Dto
 * 
 * @author Charles
 *
 */
public class MessageThroughputDto {

	private Long receivedMessageNum;
	private Long finishedMessageNum;
	
	public MessageThroughputDto() {
		this.receivedMessageNum = 0L;
		this.finishedMessageNum = 0L;
	}

	public Long getReceivedMessageNum() {
		return receivedMessageNum;
	}

	public void setReceivedMessageNum(Long receivedMessageNum) {
		this.receivedMessageNum = receivedMessageNum;
	}

	public Long getFinishedMessageNum() {
		return finishedMessageNum;
	}

	public void setFinishedMessageNum(Long finishedMessageNum) {
		this.finishedMessageNum = finishedMessageNum;
	}

}
