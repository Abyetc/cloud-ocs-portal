package com.cloud.ocs.portal.utils;

/**
 * 用于单位转换的工具类
 * 
 * @author Wang Chao
 *
 * @date 2014-12-20 下午9:01:39
 *
 */
public class UnitUtil {
	
	private static final long K = 1024;
	private static final long M = K * K;
	private static final long G = M * K;
	private static final long T = G * K;
	
	private static final long GHz = 1000;

	/**
	 * 将Byte装换为GB
	 * @param sizeInBytes 单位为Byte的整数
	 * @return 返回带单位GB大小的字符串（小数点后两位）
	 */
	public static String formatSizeFromByteToGigaByte(long sizeInBytes) {
		if (sizeInBytes < 0) {
			return null;
		}
		
		double result = sizeInBytes / G;		
		return String.format("%.2f %s", Double.valueOf(result), "GB");
	}
	
	/**
	 * 将Hz装换为GHz
	 * @param sizeInHz
	 * @return
	 */
	public static String formatSizeFromHzToGHz(long sizeInHz) {
		if (sizeInHz < 0) {
			return null;
		}
		
		double result = sizeInHz / GHz;
		return String.format("%.2f %s", Double.valueOf(result), "GHz");
	}
}
