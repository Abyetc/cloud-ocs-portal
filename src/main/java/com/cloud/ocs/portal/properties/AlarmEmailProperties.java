package com.cloud.ocs.portal.properties;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 报警服务发送提醒邮箱相关常量
 * 
 * @author Wang Chao
 * 
 * @date 2015-4-5 下午10:26:05
 * 
 */
public class AlarmEmailProperties {

	private static Properties alarmEmailProperties;

	static {
		alarmEmailProperties = new Properties();
		try {
			String filePath = CloudStackApiProperties.class.getClassLoader()
					.getResource("/properties/alarm-email.properties")
					.getPath();
			InputStream in = new BufferedInputStream(new FileInputStream(
					filePath));
			alarmEmailProperties.load(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getMailSmtpHost() {
		return alarmEmailProperties.getProperty("mail.smtp.host");
	}
	
	public static String getMailSmtpAuth() {
		return alarmEmailProperties.getProperty("mail.smtp.auth");
	}
	
	public static String getMailTransportProtocol() {
		return alarmEmailProperties.getProperty("mail.transport.protocol");
	}
	
	public static String getFromAddress() {
		return alarmEmailProperties.getProperty("from.address");
	}
	
	public static String getFromUser() {
		return alarmEmailProperties.getProperty("from.user");
	}
	
	public static String getFromPassword() {
		return alarmEmailProperties.getProperty("from.password");
	}
}
