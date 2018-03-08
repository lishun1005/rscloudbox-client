package com.cloudbox.utils;


import java.io.File;
import java.io.FilenameFilter;
import java.util.StringTokenizer;

public class xmlfomatFilter implements FilenameFilter {

	/**
	 * @param args
	 * 
	 * 实现功能; 实现FilenameFilter接口,定义出指定的文件筛选器
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
		if (token.equals("xml")) {	
			if (IfMetaXmlfile(name)) {
				flag = false;
			}else {
				flag = true;
			}
		} else {
			flag = false;
		}
		// 返回定义的返回值

		// 当返回true时,表示传入的文件满足条件
		return flag;
	}
	private boolean IfMetaXmlfile(String name) {
		String SternName=name.substring(name.indexOf("."), name.length());
		boolean flag=false;
		if (name.contains("order.xml")) {
			flag=true;
		}
		if (name.contains(".png.aux.xml")) {
			flag=true;
		}
		if (SternName.contains(".rpb.aux.xml")) {
			flag=true;
		}
		if (SternName.contains(".tiff.xml")) {
			flag=true;
		}
		if (SternName.contains(".orientation.xml")) {
			flag=true;
		}
		return flag;
	}

}
