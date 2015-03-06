package com.cloud.ocs.portal.properties;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Ocs Host常量Properties类
 * 
 * @author Wang Chao
 *
 * @date 2015-3-3 下午2:09:34
 *
 */
public class OcsHostProperties {

private static Properties ocsHostProperties;
	
	static {
		ocsHostProperties = new Properties();
		try {
			String filePath = CloudStackApiProperties.class.getClassLoader().getResource("/properties/ocs-host.properties").getPath();
			InputStream in =  new BufferedInputStream(new FileInputStream(filePath));
			ocsHostProperties.load(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getOcsHostUsername() {
		return ocsHostProperties.getProperty("ocs.host.username");
	}
	
	public static String getOcsHostPassword() {
		return ocsHostProperties.getProperty("ocs.host.password");
	}
	
	public static Integer getOcsHostSshPort() {
		return Integer.valueOf(ocsHostProperties.getProperty("ocs.host.ssh.port"));
	}
	
	public static String getCurCpuUsagePercentageCmd() {
		return ocsHostProperties.getProperty("ocs.host.monitor.cmd.cur.cpu.usage.percentage");
	}
	
	public static String getCurMemoryUsagePercentageCmd() {
		return ocsHostProperties.getProperty("ocs.host.monitor.cmd.cur.memory.usage.percentage");
	}
	
	public static String getHistoryCpuUsagePercentageCmd() {
		return ocsHostProperties.getProperty("ocs.host.monitor.cmd.history.cpu.usage.percentage");
	}
	
	public static String getHistoryMemoryUsagePercentageCmd() {
		return ocsHostProperties.getProperty("ocs.host.monitor.cmd.history.memory.usage.percentage");
	}
}
