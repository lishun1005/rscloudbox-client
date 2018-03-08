package com.cloudbox.utils;


import java.io.File;
import java.io.FilenameFilter;
import java.util.StringTokenizer;

public class tiffomatFilter implements FilenameFilter {
	private String xmlFileName;

	public tiffomatFilter(String xmlFileName) {
		// TODO Auto-generated constructor stub
		this.xmlFileName = xmlFileName;
	}

	/**
	 * @param args
	 * 
	 *            实现功能; 实现FilenameFilter接口,定义出指定的文件筛选器
	 * 
	 */

	// 重写accept方法,测试指定文件是否应该包含在某一文件列表中
	public boolean accept(File dir, String name) {
		// TODO Auto-generated method stub
		// 创建返回值
		boolean flag = true;
		// 定义筛选条件
		StringTokenizer st = new StringTokenizer(name, ".");
		// 定义筛选条件
		String  token = "";
		while (st.hasMoreTokens()) {
	        token = st.nextToken();
	    }
		if (token.equals("tif")||token.equals("tiff")||token.equals("TTF")) {
		} else {
			flag = false;
		}
		// 返回定义的返回值

		// 当返回true时,表示传入的文件满足条件
		return flag;
	}
}
