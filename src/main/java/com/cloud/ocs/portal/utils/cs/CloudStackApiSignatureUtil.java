package com.cloud.ocs.portal.utils.cs;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Map.Entry;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.cloud.ocs.portal.common.cs.CloudStackApiRequest;
import com.cloud.ocs.portal.properties.CloudStackApiProperties;

/**
 * 用于生成调用CloudStack API请求签名的工具类
 * @author Wang Chao
 *
 */
public class CloudStackApiSignatureUtil {

	public static void generateSignature(CloudStackApiRequest cloudStackApiRequest) {
		if (cloudStackApiRequest == null) 
			return;
		
		Map<String, String> requestParams = cloudStackApiRequest.getRequestParams();
		StringBuilder requestStringBuilder = new StringBuilder();
		
		for (Entry<String, String> entry : requestParams.entrySet()) {
			requestStringBuilder.append(entry.getKey());
			requestStringBuilder.append('=');
			try {
				requestStringBuilder.append(URLEncoder.encode(entry.getValue(), "UTF-8").replaceAll("\\+", "%20").toLowerCase());
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			requestStringBuilder.append('&');
		}
		String requestString = requestStringBuilder.substring(0, requestStringBuilder.length()-1);
		
		String signature = null;
		Mac mac = null;
		try {
			mac = Mac.getInstance("HmacSHA1");
			SecretKeySpec keySpec = new SecretKeySpec(CloudStackApiProperties.getSecurityKey().getBytes(), "HmacSHA1");
			mac.init(keySpec);
			mac.update(requestString.getBytes());
			byte[] encryptedBytes = mac.doFinal();
			signature = URLEncoder.encode(Base64.encodeBase64String(encryptedBytes), "UTF-8");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		if (signature != null) {
			cloudStackApiRequest.setSignature(signature);
		}
	}
}
