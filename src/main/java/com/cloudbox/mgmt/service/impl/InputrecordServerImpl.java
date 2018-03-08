package com.cloudbox.mgmt.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudbox.mgmt.dao.InputrecordRespository;
import com.cloudbox.mgmt.old.entity.InputRecord;
import com.cloudbox.mgmt.service.InputrecordService;

@Service("inputrecordServer")
public class InputrecordServerImpl implements InputrecordService {

	private Map<String, Object> map;

	private List<Map<String, Object>> list;

	@Autowired
	private InputrecordRespository inputRecordManagement;

	/**
	 * @Description 插入入库记录
	 * @param  InputRecord 入库记录实体
	 */
	@Override
	public void inserInputRecord(InputRecord inputrecord) {
		// TODO Auto-generated method stub
		inputRecordManagement.inserInputRecord(inputrecord);
	}
	
	/**
	 * @Description 更新入库记录的提示信息
	 * @param  id 入库记录id
	 * @param  message 提示信息
	 */
	@Override
	public void updataInputRecordForMassageByRecordId(String id,String message) {
		// TODO Auto-generated method stub
		inputRecordManagement.updataInputRecordForMassageByRecordId(id, message);
	}
	
	/**
	 * @Description 更新入库记录的提示信息
	 * @param  dataid 入库记录数据id
	 * @param  message 提示信息
	 */
	@Override
	public void updataInputRecordForMassageByDataId(String dataid, String message) {
		// TODO Auto-generated method stub
		inputRecordManagement.updataInputRecordForMassageByDataId(dataid, message);
	}
	
	/**
	 * @Description 更新入库记录的数据名称
	 * @param  uuid 入库记录id
	 * @param  fileDirectory 数据名称
	 */
	@Override
	public void updataInputRecordForDatanameByRecordId(String uuid,
			String fileDirectory) {
		inputRecordManagement.updataInputRecordForDatanameByRecordId(uuid, fileDirectory);
		
	}
	
	/**
	 * @Description 更新入库记录的数据大小
	 * @param  uuid 入库记录id
	 * @param  downloadsize 数据大小
	 */
	@Override
	public void updataInputRecordForDownloadsizeByRecordId(String uuid,
			long downloadsize) {
		// TODO Auto-generated method stub
		inputRecordManagement.updataInputRecordForDownloadsizeByRecordId(uuid, downloadsize);
	}

	/**
	 * @Description 更新入库记录的数据操作状态
	 * @param  recordid 入库记录id
	 * @param  opStatus 操作状态
	 */
	@Override
	public void updataInputRecordForOpstatusByRecordId(String recordid, int opStatus) {
		// TODO Auto-generated method stub
		inputRecordManagement.updataInputRecordForOpStatusByRecordId(recordid, opStatus);
	}

	/**
	 * @Description 查询入库记录
	 */
	@Override
	public Map<String, Object> queryInputRecord() {
		// TODO Auto-generated method stub
		Map<String, Object> map =new HashMap<String, Object>();
		List<InputRecord> list=inputRecordManagement.queryInputRecord();
		if (list!=null||list.size()!=0) {
			map.put("code", "1");
			map.put("message", "查询记录成功！");
			map.put("recordlist", list);
		}else {
			map.clear();
			map.put("code", "0");
			map.put("message", "查询记录失败！");
		}
		return map;
	}

	/**
	 * @Description 查询入库记录错误条目总和
	 */
	@Override
	public Map<String, Object> queryInputRecordErrorItems() {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String,Object>();
		List<InputRecord> errorItemsList= inputRecordManagement.queryInputRecordErrorItems();
		if(!errorItemsList.isEmpty()){
			map.put("code", "1");
			map.put("message", "查询记录成功！");
			map.put("erroritemscount", errorItemsList.size());
		}else{
			map.clear();
			map.put("code", "0");
			map.put("message", "无错误的入库记录或查询记录失败！");
			map.put("erroritemscount", 0);
		}
		return map;
	}
	
	/**
	 * @Description 查询入库记录已完成的条目
	 */
	@Override
	public Map<String, Object> queryInputRecordFinished() {
		Map<String, Object> map = new HashMap<String,Object>();
		List<InputRecord> finishedList = inputRecordManagement.queryInputRecordFinished();
		if(finishedList!=null){
			map.put("code", "1");
			map.put("message", "查询入库完成记录成功");
			map.put("finishedlist", finishedList);
		}else{
			map.clear();
			map.put("code", "0");
			map.put("message", "暂无入库完成记录");
		}
		return map;
	}
	
	/**
	 * @Description 删除入库记录
	 * @param  id 入库记录id
	 */
	@Override
	public Map<String, Object> deleteInputRecord(String id) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String,Object>();
		int i= inputRecordManagement.deleteInputRecord(id);
		if(i>0){
			map.put("code", "1");
			map.put("message", "删除入库记录成功");
			map.put("id", id);
		}else{
			map.put("code", "0");
			map.put("message", "删除入库记录失败");
		}
		return map;
	}
	
	/**
	 * @Description 更新入库记录结束时间
	 * @param  recordid 入库记录id
	 */
	@Override
	public void updataInputRecordForEndtimeByRecordId(String recordid,
			String endtime) {
		inputRecordManagement.updataInputRecordForOpStatusByRecordId(recordid, endtime);
		
	}
}
