package com.cloudbox.utils;


import java.io.File;
import java.io.FilenameFilter;

public class DirectoryfomatFilter implements FilenameFilter {

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
		String filepathname=dir.getAbsolutePath()+"/"+name;
		File filepathnameFile=new File(filepathname);
		if (filepathnameFile.isDirectory()) {
		} else {
			flag = false;
		}
		// 返回定义的返回值

		// 当返回true时,表示传入的文件满足条件
		return flag;
	}

}
