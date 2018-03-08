package com.cloudbox.utils;


import java.io.InputStreamReader;
import java.io.LineNumberReader;

/**
 * 
 * Description:java在linux环境下执行linux命令
 *
 * @author JhYao 2015年1月12日
 * 
 * @version v1.0
 *
 */
public class ExecLinuxCmd {

	/**
	 * 
	 * Description：java在linux环境下执行linux命令
	 * 
	 * @param cmd
	 * @return 返回命令返回值
	 *
	 */
	public static String exec(String cmd) {
		try {
			String[] cmdA = { "/bin/sh", "-c", cmd };
			Process process = Runtime.getRuntime().exec(cmdA);
			LineNumberReader br = new LineNumberReader(new InputStreamReader(process.getInputStream()));
			StringBuffer sb = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null) {
//				System.out.println(line);
				sb.append(line).append("\n");
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
