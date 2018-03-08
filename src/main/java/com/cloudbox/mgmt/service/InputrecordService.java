package com.cloudbox.mgmt.service;

import java.util.Map;

import com.cloudbox.mgmt.old.entity.InputRecord;


public interface InputrecordService {
	void inserInputRecord(InputRecord inputrecord);

	void updataInputRecordForMassageByRecordId(String id, String message);

	void updataInputRecordForMassageByDataId(String dataid, String message);

	void updataInputRecordForDatanameByRecordId(String uuid,String fileDirectory);

	void updataInputRecordForDownloadsizeByRecordId(String uuid, long downloadsize);

	void updataInputRecordForOpstatusByRecordId(String recordid, int opStatus);

	Map<String, Object> queryInputRecord();
	
	//入库记录出现问题条目反馈
	Map<String, Object> queryInputRecordErrorItems();
	
	//已完成的入库记录查询
	Map<String,Object> queryInputRecordFinished();

	void updataInputRecordForEndtimeByRecordId(String recordid, String endtime);
	//删除入库记录操作
	Map<String, Object> deleteInputRecord(String id);

}
