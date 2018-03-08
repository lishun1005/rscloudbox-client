package com.cloudbox.mgmt.service;

import java.util.List;
import java.util.Map;

public interface FileDeleteExceptionService {

	/**
	 * 
	* Description:获取全部中转服务器删除文件失败的记录 
	* @return List<Map<String,Object>><br>
	* @author lishun 
	* @date 2016-11-17 下午3:32:36
	 */
	public List<Map<String, Object>> getFileDeleteExceptionList();
	/**
	 * 
	* Description: 更新删除状态
	* @param id
	* @param status  
	* @author lishun 
	* @date 2016-11-17 下午3:36:13
	 */
	public void updateFileDeleteExceptionById(String id, Integer status);
	/**
	 * 
	* Description:检查删除代理服务器文件失败的记录，进行删除
	* @author lishun 
	* @date 2016-11-18 上午9:01:51
	 */
	public void checkFileDeleteException();

}
