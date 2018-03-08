package com.cloudbox.mgmt.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinarsgeo.rscloudmart.web.dao.entity.DownloadRecord;
import com.cloudbox.mgmt.dao.DownloadRecordRespository;
import com.cloudbox.mgmt.service.DownloadRecordService;

@Service("downloadRecordService")
public class DownloadRecordServiceImpl implements DownloadRecordService {
	@Autowired
	private DownloadRecordRespository downloadRecordRespository;
	
	public void updateDownloadEndTime(String downloadendtime, String name) {
		downloadRecordRespository.updateDownloadEndTime(downloadendtime, name);
	}
	public void updateTargzEndTime(String targzendtime, String name) {
		downloadRecordRespository.updateTargzEndTime(targzendtime, name);
	}
	public void updateCutEndTime(String cutendtime, String recordid) {
		downloadRecordRespository.updateCutEndTime(cutendtime, recordid);
	}
	public int updateRecordStutasFordownloadstatus(Integer downloadstatus,String name) {
		return downloadRecordRespository.updateRecordStutasFordownloadstatus(downloadstatus, name);
	}
	@Override
	public void insertRecordStutas(String recordid, String recordtime,
			String userordertype, String name, String downloadFilepath,
			String imageProductId, Integer downloadstatus, Long downloadsize,
			String taskendtime) {
		downloadRecordRespository.insertRecordStutas(recordid, recordtime, userordertype, name, downloadFilepath, imageProductId, downloadstatus, downloadsize, taskendtime);
	}
	@Override
	public int updateRecordStutasFordownloadsize(Long downloadsize, String name) {
		return downloadRecordRespository.updateRecordStutasFordownloadsize(downloadsize, name);
	}
	@Override
	public Map<String, Object> queryRecordStutas() {
		return downloadRecordRespository.queryRecordStutas();
	}
	@Override
	public Map<String, Object> queryrecordstutasfordownloadstatus(
			String recordid) {
		return downloadRecordRespository.queryrecordstutasfordownloadstatus(recordid);
	}
	@Override
	public int qureyRecordbyrecordid(String recordid) {
		return downloadRecordRespository.qureyRecordbyrecordid(recordid);
	}
	@Override
	public void updateInputstorageEndTime(String inputstorageendtime,
			String name) {
		downloadRecordRespository.updateInputstorageEndTime(inputstorageendtime, name);	
	}
	@Override
	public DownloadRecord queryRecordStutasByRecordid(String recordid) {
		return downloadRecordRespository.queryRecordStutasByRecordid(recordid);
	}
	@Override
	public void updateTaskEndTime(String taskendtime, String name) {
		downloadRecordRespository.updateTaskEndTime(taskendtime, name);	
	}
	@Override
	public Map<String, Object> getRecordstutasAndOpStatus(String recordId) {
		return downloadRecordRespository.getRecordstutasAndOpStatus(recordId);
	}
	@Override
	public List<Map<String, Object>> getDownloadrecordByDownloadStatus(Integer downloadstatus) {
		return downloadRecordRespository.getDownloadrecordByDownloadStatus(downloadstatus);
	}
	@Override
	public Boolean checkIsDownloadByName(String name, Integer status) {
		return downloadRecordRespository.checkIsDownloadByName(name, status);
	}
	@Override
	public List<Map<String, Object>> queryDownrecordByName(String name) {
		return downloadRecordRespository.queryDownrecordByName(name);
	}
	@Override
	public int updateRecordStutasByName(Integer downloadstatus, String name) {
		return downloadRecordRespository.updateRecordStutasByName(downloadstatus, name);
	}
	@Override
	public List<Map<String, Object>> getDownloadrecordGroupByName(Integer downloadstatus) {
		return downloadRecordRespository.getDownloadrecordGroupByName(downloadstatus);
	}
	@Override
	public void deleteByName(String name) {
		downloadRecordRespository.deleteByName(name);	
	}
	@Override
	public void deleteById(String id) {
		downloadRecordRespository.deleteById(id);
	}
	@Override
	public void addFileDeleteException(String downloadPath, String imageName,
			Integer status) {
		downloadRecordRespository.addFileDeleteException(downloadPath, imageName, status);
	}
	
}
