package com.cloud.ocs.portal.properties;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * OCS Vm常量Properties类
 * 
 * @author Wang Chao
 *
 * @date 2015-1-20 上午10:08:45
 *
 */
public class OcsVmProperties {

	private static Properties ocsVmProperties;
	
	static {
		ocsVmProperties = new Properties();
		try {
			String filePath = CloudStackApiProperties.class.getClassLoader().getResource("/properties/ocs-vm.properties").getPath();
			InputStream in =  new BufferedInputStream(new FileInputStream(filePath));
			ocsVmProperties.load(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getOcsVmUsername() {
		return ocsVmProperties.getProperty("ocs.vm.username");
	}
	
	public static String getOcsVmPassword() {
		return ocsVmProperties.getProperty("ocs.vm.password");
	}
	
	public static Integer getOcsVmMonitorPrivateServicePort() {
		return Integer.valueOf(ocsVmProperties.getProperty("ocs.vm.monitor.private.service.port"));
	}
	
	public static Integer getOcsVmSshPrivateServicePort() {
		return Integer.valueOf(ocsVmProperties.getProperty("ocs.vm.ssh.private.service.port"));
	}
	
	public static Integer getOcsVmEngineServicePort() {
		return Integer.valueOf(ocsVmProperties.getProperty("ocs.vm.engine.service.port"));
	}
	
	public static String getOcsVmEngineStartCmdBase() {
		return ocsVmProperties.getProperty("ocs.vm.engine.start.cmd.base");
	}
	
	public static String getOcsVmEngineStartCmdRedirect() {
		return ocsVmProperties.getProperty("ocs.vm.engine.start.cmd.redirect");
	}
	
	public static String getOcsVmEngineCheckStateCmd() {
		return ocsVmProperties.getProperty("ocs.vm.engine.check.state.cmd");
	}
}
