package com.cloudbox.mgmt.controller;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cloudbox.utils.ShpFileOpUtil;
@Controller
public class ShpFileOpController extends BaseController{

	private Map<String, Object> map;
	/**
	 * @Description  导入shp文件
	 * */
	
	@RequestMapping(value="/Importshpfile")
	@ResponseBody
	public Map<String, Object> Importshpfile(@RequestParam("uploads") MultipartFile MultipartFile) {
		try {
			map = ShpFileOpUtil.readFileAndPareseByStream(MultipartFile.getBytes(),MultipartFile.getOriginalFilename());
		} catch (IOException e) {
			e.printStackTrace();
			map=new HashMap<String, Object>();
			map.put("code", 0);
			map.put("message", "后台发生异常，可能shp压缩包的文件格式不对，请重试！");
		}
		return map;
	}
	
	/**
	 * @Description  导出shp文件 
	 * */
	@RequestMapping("/Exportshpfile")
	public void Exportshpfile(@RequestParam("geomWKT") String geomWKT) {
		try {
			map=ShpFileOpUtil.WriteVectorFileZip(geomWKT);
			HttpServletResponse response = this.getResponse();
			InputStream in = (InputStream)map.get("FileInputStream");
			String filename=(String) map.get("FileName");
			Integer contentLengh =in.available();
			response.setContentLength(contentLengh);
			response.setContentType("application/octet-stream");
			response.setHeader("Content-disposition", "attachment; filename=\""
					+ new String(filename.getBytes("utf-8"), "ISO8859-1")+"\"");
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;
			bis = new BufferedInputStream(in);
			bos = new BufferedOutputStream(response.getOutputStream());
			byte[] buff = new byte[2048];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
			bis.close();
			bos.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
