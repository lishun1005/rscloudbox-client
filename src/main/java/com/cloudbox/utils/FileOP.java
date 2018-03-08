package com.cloudbox.utils;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

public class FileOP {
	/* 二级目录查询数据的完整性,三级目录不支持 */
	
	private static XmlModelMap xmlModelMap= (XmlModelMap) SpringContextUtil.getBean("xmlModelMap");
	
	public static Map checkFileListTwo(String Path) {
		Map map = new HashMap();
		File dir = new File(Path);
		File[] files = dir.listFiles(new DirectoryfomatFilter());
		if (files.length == 0) {
			Map mapsub = checkFileListOne(Path);			
			map.put("0", mapsub);
		} else {
			for (int i = 0; i < files.length; i++) {
				File[] filesubs = files[i].listFiles(new DirectoryfomatFilter());
				if (filesubs.length != 0) {
					map.put("code", "0");
					map.put("message", "目前支持二级目录的导入，无法处理多级目录或二级目录下存在文件夹，请仔细查看！");
					return map;
				}
				Map mapsub = checkFileListOne(files[i].getAbsolutePath());
				map.put(i, mapsub);
			}
		}
		map.put("code", "1");
		map.put("message", "循环结束！");
		return map;
	}

	/* 一级目录查询数据的完整性 */
	public static Map checkFileListOne(String Path) {
		Map map = new HashMap();
		File dir = new File(Path);
		File[] xmlfiles = dir.listFiles(new xmlfomatFilter());
		if (xmlfiles.length == 0) {
			map.put("code", "0");
			map.put("message", dir.getName() + "目录中缺少xml元信息文件！");
			return map;
		}
		for (int i = 0; i < xmlfiles.length; i++) {
			String xmlFileName = xmlfiles[i].getName();
			/* System.out.println(xmlFileName); */
			xmlFileName = xmlFileName.substring(0, xmlFileName.lastIndexOf("."));
			File[] jpgfiles = dir.listFiles(new jpgfomatFilter(xmlFileName));
			if (jpgfiles.length == 0) {
				continue;
			}

			File[] tiffiles = dir.listFiles(new tiffomatFilter(xmlFileName));
			if (tiffiles.length == 0) {
				continue;
			}
			/*
			 * for (int j = 0; j < tiffiles.length; j++) { String tiffilesName =
			 * tiffiles[j].getName(); System.out.println(tiffilesName); }
			 */
			if (map.get("xmlfile")==null) {
				map.put("xmlfile", xmlfiles[i].getName());	
			}else {
				map.put("xmlfile", xmlfiles[i].getName()+","+map.get("xmlfile"));
			}
		}
		if (map.get("xmlfile")!=null) {
			map.put("code", "1");
			map.put("message", "该目录下图像内容信息完整");
		}else {
			map.put("code", "0");
			map.put("message", "该目录下图像内容信息不完整,缺少xml文件！");
		}
		return map;
	}
	/* 一级目录查询数据的完整性 */
	public static Map checkFileListOneForShpFile(String Path) {
		Map map = new HashMap();
		File dir = new File(Path);
		File[] shpfiles = dir.listFiles(new ShpFilefomatFilter());
		if (shpfiles.length == 0) {
			map.put("code", "0");
			map.put("message", dir.getName() + "目录中缺少shp文件！");
			return map;
		}
		for (int i = 0; i < shpfiles.length; i++) {
			String shpFileName = shpfiles[i].getName();
			/* System.out.println(xmlFileName); */
			shpFileName = shpFileName.substring(0, shpFileName.lastIndexOf("."));
			File[] tiffiles = dir.listFiles(new tiffomatFilter(shpFileName));
			if (tiffiles.length == 0) {
				continue;
			}
			/*
			 * for (int j = 0; j < tiffiles.length; j++) { String tiffilesName =
			 * tiffiles[j].getName(); System.out.println(tiffilesName); }
			 */
			if (map.get("shpfile")==null) {
				map.put("shpfile", shpfiles[i].getAbsolutePath());	
			}else {
				map.put("shpfile", shpfiles[i].getAbsolutePath()+","+map.get("shpfile"));
			}
		}
		if (map.get("shpfile")!=null) {
			map.put("code", "1");
			map.put("message", "该目录下图像内容信息完整");
		}else {
			map.put("code", "0");
			map.put("message", "该目录下图像内容信息不完整,缺少xml文件！");
		}
		return map;
	}
	
	public static void getFileStreamInResponse(String filePath,String name,HttpServletResponse httpServletResponse,String  type){
		try {
			ServletOutputStream OutputStream = httpServletResponse.getOutputStream();
			String fullName = filePath + "/" + name + type;
			InputStream fis = null;
			fis =new FileInputStream(new File(fullName));
			// TODO Auto-generated catch block
			int bytesRead = 0;
			byte[] buffer = new byte[8 * 1024];
			while ((bytesRead = fis.read(buffer, 0, 8 * 1024)) != -1) {
				OutputStream.write(buffer, 0, bytesRead);
			}
			fis.close();
			OutputStream.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void getfileonline(String filepath, String name, HttpServletResponse httpResponse, String type, boolean isOnLine) {
		// TODO Auto-generated method stub
		System.out.println("进行过来了");
		try {
			String fullName = filepath + "" + name + type;
			System.out.println(fullName);
			//String fullName = "DatamanagementSys/storage/GF1_PMS1_E87.5_N47.2_20140604_L1A0000244005/GF1_PMS1_E87.5_N47.2_20140604_L1A0000244005" + type;
			InputStream fis = null;
			File file = new File(fullName);
			fis = new FileInputStream(file);
			if (fis == null) {
				httpResponse.sendError(404, "File not found!");
				return;
			}
			BufferedInputStream br = new BufferedInputStream(fis);
			byte[] buf = new byte[1024];
			int len = 0;
			httpResponse.reset(); // 非常重要
			if (isOnLine) { // 在线打开方式
				URL u = new URL("file:///" + fullName);
				httpResponse
						.setContentType(u.openConnection().getContentType());
				httpResponse.setHeader("Content-Disposition",
						"inline; filename=" + name + type);
				// 文件名应该编码成UTF-8
			} else { // 纯下载方式
				httpResponse.setContentType("application/x-msdownload");
				httpResponse.setHeader("Content-Disposition",
						"attachment; filename=" + name + type);
			}
			OutputStream out = httpResponse.getOutputStream();
			while ((len = br.read(buf)) > 0)
				out.write(buf, 0, len);
			br.close();
			out.close();
			deleteAll(file);
		} catch (Exception e) {
			// TODO: handle exception
			try {
				httpResponse.sendError(404, "File not found!");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
			}
			return;
		}
	}
	
	/*
	 * 获取数据模型的XML信息
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, Object> getXMLInfo(String filepath,
			String name, String dataType) {
		// TODO Auto-generated method stub
		Map map = new HashMap<String, Object>();
	    List	list = new ArrayList();
		String ClassName = (String) xmlModelMap.getXmlModel(dataType);
		// 拼接xml路径全名
		String fullName = filepath + "/" + name + ".xml";
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(Class.forName(ClassName));
			Unmarshaller shaller = context.createUnmarshaller();
			list.add(shaller.unmarshal(new File(fullName)));
			map.put("code", 1);
			map.put("list", list);
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", "0");
			map.put("message", "后台查询发生异常！");
			e.printStackTrace();
		}
		return map;
	}
	public static void getallfileonline(String allfilepath,String type,HttpServletRequest request,HttpServletResponse httpResponse, String allfilename,boolean isOnLine) {
		try {
			String[] allfilenameS=allfilepath.split(",");
			InputStream fis = null;
			OutputStream out = httpResponse.getOutputStream();
			String tempoutputDir=((PropertyOputils) SpringContextUtil.getBean("systemProperty")).getKeyValue("FileTempPath");
			String destfile=tempoutputDir+File.separator+allfilename;
			for (int i = 0; i < allfilenameS.length; i++) {
				CompressUtil.zip(allfilenameS[i], destfile, null);
			}
			fis = new FileInputStream(new File(destfile));
			if (fis == null) {
				httpResponse.sendError(404, "File not found!");
				return;
			}
			BufferedInputStream br = new BufferedInputStream(fis);
			byte[] buf = new byte[1024];
			int len = 0;
			httpResponse.reset(); // 非常重要
			if (isOnLine) { // 在线打开方式
				URL u = new URL("file:///" + allfilename);
				httpResponse
						.setContentType(u.openConnection().getContentType());
				httpResponse.setHeader("Content-Disposition",
						"inline; filename=" + allfilename);
				// 文件名应该编码成UTF-8
			} else { // 纯下载方式
				httpResponse.setContentType("application/x-msdownload");
				httpResponse.setHeader("Content-Disposition",
						"attachment; filename=" +allfilename);
			}
			while ((len = br.read(buf)) > 0)
				out.write(buf, 0, len);
			br.close();
			out.close();
		} catch (Exception e) {
			// TODO: handle exception
			try {
				httpResponse.sendError(404, "File not found!");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
			}
			return;
		}
	}
	
	public static void deleteAll(File file) {
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				deleteAll(f);
			}
		}
		try {
			file.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String file = "F:/05_数据管理小组/原始数据/国产卫星/GF1";
		Map map = FileOP.checkFileListTwo(file);
		for (int i = 0; i < map.size()-2; i++) {
			System.out.println(map.get(i));
		}
	}

	
}
