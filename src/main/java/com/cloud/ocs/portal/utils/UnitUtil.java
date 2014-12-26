package com.cloud.ocs.portal.utils;

import java.text.DecimalFormat;

/**
 * 用于单位转换的工具类
 * 
 * @author Wang Chao
 *
 * @date 2014-12-20 下午9:01:39
 *
 */
public class UnitUtil {
	
	private static DecimalFormat dec = new DecimalFormat("0.00");
	
	private static final long GHz = 1000;

	/**
	 * 根据传入的数值大小，将Byte装换为合适的单位（KB/MB/GB/TB）
	 * @param sizeInBytes 单位为Byte的整数
	 * @return 返回带单位大小的字符串（小数点后两位）
	 */
	public static String formatSizeUnit(long size) {
	    String hrSize = null;

	    double b = size;
	    double k = size/1024.0;
	    double m = ((size/1024.0)/1024.0);
	    double g = (((size/1024.0)/1024.0)/1024.0);
	    double t = ((((size/1024.0)/1024.0)/1024.0)/1024.0);

	    if ( t>1 ) {
	        hrSize = dec.format(t).concat(" TB");
	    } else if ( g>1 ) {
	        hrSize = dec.format(g).concat(" GB");
	    } else if ( m>1 ) {
	        hrSize = dec.format(m).concat(" MB");
	    } else if ( k>1 ) {
	        hrSize = dec.format(k).concat(" KB");
	    } else {
	        hrSize = dec.format(b).concat(" Bytes");
	    }

	    return hrSize;
	}
	
	/**
	 * 根据传入的数值大小，将Byte装换为GB
	 * @param size
	 * @return
	 */
	public static double formatSizeUnitToGB(long size) {
		double result = 0.0;
		
		if (size >= 0) {
			result = (((size/1024.0)/1024.0)/1024.0);
		}
		
		return result;
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
	
	/**
	 * 传入两个long型数，计算Percentage
	 * @param numerator 分子
	 * @param denominator 分母
	 * @return
	 */
	public static String calculatePercentage(long numerator, long denominator) {
		double result = (numerator * 100.0 / denominator);
		
		return dec.format(result).concat("%");
	}
}
