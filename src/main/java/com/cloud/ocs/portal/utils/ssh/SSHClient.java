package com.cloud.ocs.portal.utils.ssh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

/**
 * 用于SSH远程至其他主机的工具类
 * 
 * @author Wang Chao
 *
 * @date 2015-1-19 下午9:44:44
 *
 */
public class SSHClient {
	
	/**
	 * 发送命令到远程主机并返回命令执行结果
	 * @param host
	 * @param userName
	 * @param password
	 * @param cmd
	 * @return
	 */
	public static String sendCmd(String host, int port, String userName, String password,
			String cmd) {
		String result = null;
		Connection connection = null;
		Session session = null;
		try {
			connection = connectTo(host, port, userName, password);
			session = connection.openSession();
			session.execCommand(cmd);
			InputStream stdout = new StreamGobbler(session.getStdout());
			BufferedReader br = new BufferedReader(
					new InputStreamReader(stdout));
			StringBuffer strBuffer = new StringBuffer();
			String line = br.readLine();
			while (line != null) {
				strBuffer.append(line + "\n");
				line = br.readLine();
			}
			result = strBuffer.toString();
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
			if (connection != null) {
				connection.close();
			}
		}
		
		return result;
	}

	/**
	 * 获得与远程主机的SSH连接
	 * @param host
	 * @param port
	 * @param userName
	 * @param password
	 * @return
	 * @throws IOException
	 */
	public static Connection connectTo(String host, int port, String userName, String password) throws IOException {
        Connection connection = new Connection(host, port);
        connection.connect();
        connection.authenticateWithPassword(userName, password);

        return connection;
    }
}
