package com.cloud.ocs.mntportal.test.api;

import static org.junit.Assert.*;

import org.junit.Test;

import com.cloud.ocs.portal.common.CloudStackApiRequest;
import com.cloud.ocs.portal.utils.CloudStackApiSignatureUtil;

public class TestCloudStackApiRequestSignature {

	@Test
	public void testCloudStackApiRequestSignatureGeneration() {
		CloudStackApiRequest cloudStackApiRequest = new CloudStackApiRequest("listHosts");
		
		CloudStackApiSignatureUtil.generateSignature(cloudStackApiRequest);
		
		System.out.println(cloudStackApiRequest.getSignature());
	}

}
