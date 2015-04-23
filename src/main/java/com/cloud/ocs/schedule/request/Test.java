package com.cloud.ocs.schedule.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.cloud.ocs.portal.utils.ssh.SSHClient;

public class Test {
	public static void main(String args[]) throws ParseException {
		 DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd "); 
		 DateFormat dateFormat3 = new SimpleDateFormat("yyyy-MM-dd HH时mm分ss秒");
		 String str = "06时32分01秒      eth0     21.06     14.43     26.80      1.11      0.00      0.00      0.00";
		 String dateStr = str.substring(0, str.indexOf(" "));
		 System.out.println(dateStr);
		 String yyyyMMdd = dateFormat1.format(new Date());
		 System.out.println(dateFormat3.parse(yyyyMMdd + dateStr));
		 System.out.println(dateFormat3.parse(yyyyMMdd + dateStr).getTime());
		 
		 int indexOfThirdDot = 0;
		 int indexOfFourthDot = 0;
		 int dotCount = 0;
		 for (int i = 0; i < str.length(); i++) {
			 if (str.charAt(i) == '.') {
				 ++dotCount;
			 }
			 if (dotCount == 2) {
				 indexOfThirdDot = i;
			 }
			 if (dotCount == 3) {
				 indexOfFourthDot = i;
			 }
		 }
		 
			int beg = indexOfThirdDot - 1;
			int end = indexOfThirdDot + 1;
			while (str.charAt(beg) != ' ') {
				beg--;
			}
			while (str.charAt(end) != ' ') {
				end++;
			}
			String resStr = str.substring(beg + 1, end);
			System.out.println(resStr);
			
			beg = indexOfFourthDot - 1;
			end = indexOfFourthDot + 1;
			while (str.charAt(beg) != ' ') {
				beg--;
			}
			while (str.charAt(end) != ' ') {
				end++;
			}
			String resStr2 = str.substring(beg + 1, end);
			System.out.println(resStr2);
    }

}
