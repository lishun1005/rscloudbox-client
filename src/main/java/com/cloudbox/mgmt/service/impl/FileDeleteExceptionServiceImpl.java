package com.cloudbox.mgmt.service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloudbox.mgmt.dao.FileDeleteExceptionRespository;
import com.cloudbox.mgmt.service.FileDeleteExceptionService;
import com.cloudbox.utils.RsmartGtdataEncryptionUtil;

@Service
@Transactional
public class  FileDeleteExceptionServiceImpl implements FileDeleteExceptionService {
	private static Logger logger = LoggerFactory.getLogger(FileDeleteExceptionServiceImpl.class);
	@Autowired
	public FileDeleteExceptionRespository fileDeleteExceptionRespository;
	@Override
	public List<Map<String, Object>> getFileDeleteExceptionList() {
		return fileDeleteExceptionRespository.getFileDeleteExceptionList();
	}
	@Override
	public void updateFileDeleteExceptionById(String id, Integer status) {
		fileDeleteExceptionRespository.updateFileDeleteExceptionById(id, status);
	}
	@Override
	public void checkFileDeleteException(){
		try {
			List<Map<String, Object>> list = getFileDeleteExceptionList();
			for (Map<String, Object> map : list) {
				String downloadPath = String.valueOf(map.get("download_path"));
				String id = String.valueOf(map.get("id"));
				Map<String, String> resultMap = RsmartGtdataEncryptionUtil
						.deleteData(downloadPath);// 删除代理服务器上的加密数据
				if ("1".equals(resultMap.get("result"))) {
					updateFileDeleteExceptionById(id, 0);
				}
				logger.info(resultMap.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
