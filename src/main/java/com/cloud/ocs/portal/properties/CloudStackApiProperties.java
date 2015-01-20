package com.cloud.ocs.portal.properties;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * 调用CloudStack Api的常量Properties
 * 
 * @author Wang Chao
 *
 * @date 2015-1-19 下午10:15:51
 *
 */
public class CloudStackApiProperties {
	
	private static Properties cloudStackAPISecretProperties;
	
	static {
		cloudStackAPISecretProperties = new Properties();
		try {
			String filePath = CloudStackApiProperties.class.getClassLoader().getResource("/properties/cloudstack-api.properties").getPath();
			InputStream in =  new BufferedInputStream(new FileInputStream(filePath));
			cloudStackAPISecretProperties.load(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getBaseURL() {
		return cloudStackAPISecretProperties.getProperty("cloudstack.api.baseurl");
	}
	
	public static String getApiPath() {
		return cloudStackAPISecretProperties.getProperty("cloudstack.api.path");
	}

	public static String getAPIKey() {
		return cloudStackAPISecretProperties.getProperty("cloudstack.api.key");
	}
	
	public static String getSecurityKey() {
		return cloudStackAPISecretProperties.getProperty("cloudstack.security.key");
	}
}
