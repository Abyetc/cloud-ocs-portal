package com.cloud.ocs.portal.common.cs.asyncjob.service;

import org.json.JSONObject;

import com.cloud.ocs.portal.common.cs.CloudStackApiRequest;
import com.cloud.ocs.portal.common.cs.asyncjob.constant.AsyncJobApiName;
import com.cloud.ocs.portal.common.cs.asyncjob.constant.AsyncJobStatus;
import com.cloud.ocs.portal.common.cs.asyncjob.dto.AsynJobResultDto;
import com.cloud.ocs.portal.utils.cs.CloudStackApiSignatureUtil;
import com.cloud.ocs.portal.utils.http.HttpRequestSender;

/**
 * 用于查询CloudStack异步Job执行结果的Service类
 * 
 * @author Wang Chao
 *
 * @date 2014-12-30 下午7:29:38
 *
 */
public class QueryAsyncJobResultService {
	
	public static AsynJobResultDto queryAsyncJobResult(String jobId, String objectResultName) {
		CloudStackApiRequest request = new CloudStackApiRequest(AsyncJobApiName.ASYNC_JOB_QUERY_RESULT);
		request.addRequestParams("jobid", jobId);
		CloudStackApiSignatureUtil.generateSignature(request);
		String requestUrl = request.generateRequestURL();
		String response = HttpRequestSender.sendGetRequest(requestUrl);
		
		AsynJobResultDto result = null;
		
		if (response != null) {
			JSONObject responseJsonObj = new JSONObject(response);
			JSONObject resultJsonObj = responseJsonObj.getJSONObject("queryasyncjobresultresponse");
			if (resultJsonObj.has("jobstatus")) {
				result = new AsynJobResultDto();
				switch (resultJsonObj.getInt("jobstatus")) {
					case 0: //正在执行
						result.setJobStatus(AsyncJobStatus.PENDING);
						break;
					case 1: //执行成功
						result.setJobStatus(AsyncJobStatus.SUCCESS);
						if (resultJsonObj.has("jobresulttype") && resultJsonObj.getString("jobresulttype").equals("object")) {
							result.setJobResultType("object");
							if (resultJsonObj.has("jobresult")) {
								JSONObject resultobj = resultJsonObj.getJSONObject("jobresult");
								if (resultobj.has(objectResultName)) {
									result.setJobResult(resultobj.getJSONObject(objectResultName));
								}
							}
						}
						break;
					case 2: //执行失败
						result.setJobStatus(AsyncJobStatus.FAILED);
						if (resultJsonObj.has("jobresulttype") && resultJsonObj.getString("jobresulttype").equals("text")) {
							result.setJobResultType("text");
							if (resultJsonObj.has("jobresult")) {
								result.setJobResult(resultJsonObj.getString("jobresult"));
							}
						}
						break;
					default:
						break;
				}
			}
		}
		
		return result;
	}
}
