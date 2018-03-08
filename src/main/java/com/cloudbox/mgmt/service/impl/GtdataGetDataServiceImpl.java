package com.cloudbox.mgmt.service.impl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudbox.mgmt.service.GtdataGetDataService;
import com.cloudbox.utils.FileOP;
import com.cloudbox.utils.GtdataFileUtil;
import com.cloudbox.utils.XmlModelMap;

@Service
public class GtdataGetDataServiceImpl implements GtdataGetDataService {

	private static Logger logger = LoggerFactory
			.getLogger(GtdataGetDataServiceImpl.class);
	
	private Map<String, Object> map;
	private List list;
	@Autowired
	private XmlModelMap xmlModelMap;

	/**
	 *  Description：  获取数据模型的XML信息
	 *  @param filepath 文件存放路径.
	 *	@param name 文件名称.
	 *	@param dataType 数据类型.
	 **/
	public Map<String, Object> getXMLInfo(String filepath,
			String name, String dataType) {
		// TODO Auto-generated method stub
		map = new HashMap<String, Object>();
		list = new ArrayList();
		String ClassName = (String) xmlModelMap.getXmlModel(dataType);
		// 拼接xml路径全名
		String fullName = filepath + "/" + name + ".xml";
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(Class.forName(ClassName));
			Unmarshaller shaller = context.createUnmarshaller();
			list.add(shaller.unmarshal(GtdataFileUtil.getDownloadFile(fullName)));
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
	
	/**
	 *  Description：  获取文件流并写入HttpServletResponse实例
	 **/
	public void getFileStreamInResponse(String filePath, String name,
			HttpServletResponse httpResponse, String type) {
		try {
			ServletOutputStream OutputStream = httpResponse.getOutputStream();
			String fullName = filePath + "/" + name + type;
			InputStream fis = null;
			fis = GtdataFileUtil.getDownloadFile(fullName);
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

	/**
	 *  Description：    获取文件流并写入HttpServletResponse实例
	 **/
	@Override
	public void getfileonline(String filepath, String name,
			HttpServletResponse httpResponse, String type, boolean isOnLine) {
		// TODO Auto-generated method stub
		try {
			String fullName = filepath +""+ name + type;
			//String fullName = "DatamanagementSys/storage/GF1_PMS1_E87.5_N47.2_20140604_L1A0000244005/GF1_PMS1_E87.5_N47.2_20140604_L1A0000244005" + type;
			InputStream fis = null;
			fis = GtdataFileUtil.getDownloadFile(fullName);
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
				httpResponse.setContentType(u.openConnection().getContentType());
				httpResponse.setHeader("Content-Disposition","inline; filename=" + name + type);
				// 文件名应该编码成UTF-8
			} else { // 纯下载方式
				httpResponse.setContentType("application/x-msdownload");
				httpResponse.setHeader("Content-Disposition","attachment; filename=" + name + type);
			}
			OutputStream out = httpResponse.getOutputStream();
			while ((len = br.read(buf)) > 0)
			out.write(buf, 0, len);
			br.close();
			out.close();
			FileOP.deleteAll(new File(fullName));
		} catch (Exception e) {
			try {
				httpResponse.sendError(404, "File not found!");
			} catch (IOException e1) {
			}
			return;
		}
	}
	
	@Override
	public void getalltargzfileonline(String allfilepath,HttpServletResponse httpResponse, String type, boolean isOnLine) {
		// TODO Auto-generated method stub
		
	}
}
