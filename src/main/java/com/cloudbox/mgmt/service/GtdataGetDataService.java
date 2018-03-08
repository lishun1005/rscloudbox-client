package com.cloudbox.mgmt.service;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

public interface GtdataGetDataService {
	public Map<String, Object> getXMLInfo(String filepath,String name, String dataType);
	public void getFileStreamInResponse(String filePath, String name,HttpServletResponse httpResponse, String type);
	public void getfileonline(String filepath, String name,HttpServletResponse httpResponse, String type, boolean isOnLine);
	public void getalltargzfileonline(String allfilepath,HttpServletResponse httpServletResponse, String type, boolean b);
}
