package com.cloud.ocs.portal.utils.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

/**
 * 用于向CloudStack发送API请求的工具类
 * @author Wang Chao
 *
 */
public class HttpRequestSender {
	
	private final static Logger LOGGER = Logger.getLogger(HttpRequestSender.class.getName());

	/**
	 * 向CloudStack管理节点发送GET请求
	 * @param targetURL
	 * @return Json字符串
	 */
	public static String sendGetRequest(String targetURL) {
		LOGGER.info("Start to Send request:" + targetURL);
		URL url = null;
		HttpURLConnection connection = null;
		try {
			// Create connection
			url = new URL(targetURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setConnectTimeout(10*1000);
			connection.setRequestProperty("Content-Type", "application/json");

			// connection.setRequestProperty("Content-Length", "" +
			// Integer.toString(urlParameters.getBytes().length));
//			connection.setRequestProperty("Content-Language", "en-US");

			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Send request
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			// wr.writeBytes (urlParameters);
			wr.flush();
			wr.close();

			// Get Response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			StringBuffer response = new StringBuffer();
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			LOGGER.info(response.toString());
			return response.toString();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
}
