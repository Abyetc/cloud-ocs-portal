package com.cloud.ocs.portal.common.cs.asyncjob.dto;

import com.cloud.ocs.portal.common.cs.asyncjob.constant.AsyncJobStatus;

/**
 * 用于表示查询CloudStack异步任务执行结果的Dto
 * 
 * @author Wang Chao
 * 
 * @date 2014-12-30 下午7:48:19
 * 
 */
public class AsynJobResultDto {

	private AsyncJobStatus jobStatus;
	private String jobResultType; // object/text
	private Object jobResult;

	public AsyncJobStatus getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(AsyncJobStatus jobStatus) {
		this.jobStatus = jobStatus;
	}

	public String getJobResultType() {
		return jobResultType;
	}

	public void setJobResultType(String jobResultType) {
		this.jobResultType = jobResultType;
	}

	public Object getJobResult() {
		return jobResult;
	}

	public void setJobResult(Object jobResult) {
		this.jobResult = jobResult;
	}

}
