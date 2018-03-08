package com.cloudbox.mgmt.controller;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cloudbox.mgmt.service.InputrecordService;

@Controller
public class InputrecordController extends BaseController {

	@Autowired
	private InputrecordService inputRecordServer;

	/**
	 * @Description查询入库记录
	 */
	@RequestMapping("/queryinputrecord")
	@ResponseBody
	public Map<String, Object> queryInputRecord() {
		return inputRecordServer.queryInputRecord();
	}
	
	/**
	 *@Description错误条目查询
	 */
	@RequestMapping("/queryinputrecorderroritems")
	@ResponseBody
	public Map<String,Object> queryInputRecordErrorItems(){
		return inputRecordServer.queryInputRecordErrorItems();
	}
	/**
	 * @Description已完成的入库记录
	 */
	@RequestMapping("queryinputrecordfinished")
	@ResponseBody
	public Map<String,Object> queryInputRecordFinished(){
		return inputRecordServer.queryInputRecordFinished();
	}
	
	
	/**
	 * @Description删除入库记录
	 * @param  id 入库记录id
	 */
	@RequestMapping("/deleteinputrecord")
	@ResponseBody
	public Map<String, Object> deleteInputRecord(String id){
		return inputRecordServer.deleteInputRecord(id);
	}
	

}
