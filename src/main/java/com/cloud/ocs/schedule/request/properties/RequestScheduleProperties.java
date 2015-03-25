package com.cloud.ocs.schedule.request.properties;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.cloud.ocs.portal.properties.CloudStackApiProperties;

public class RequestScheduleProperties {

	private static Properties requestScheduleProperties;
	
	static {
		requestScheduleProperties = new Properties();
		try {
			String filePath = CloudStackApiProperties.class.getClassLoader().getResource("/properties/request-schedule.properties").getPath();
			InputStream in =  new BufferedInputStream(new FileInputStream(filePath));
			requestScheduleProperties.load(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static int getCityId() {
		return Integer.valueOf(requestScheduleProperties.getProperty("city.id"));
	}
	
	public static double getCpuCoefficient() {
		return Double.parseDouble(requestScheduleProperties.getProperty("cpu.coefficient"));
	}
	
	public static double getMemoryCoefficient() {
		return Double.parseDouble(requestScheduleProperties.getProperty("memory.coefficient"));
	}
	
	public static double getThreadNumCoefficient() {
		return Double.parseDouble(requestScheduleProperties.getProperty("thread.num.coefficient"));
	}
	
	public static double getTcpConnectionNumCoefficient() {
		return Double.parseDouble(requestScheduleProperties.getProperty("tcp.connection.coefficient"));
	}
	
	public static double getResponseTimeCoefficient() {
		return Double.parseDouble(requestScheduleProperties.getProperty("response.time.coefficient"));
	}
}
