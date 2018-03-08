package com.cloudbox.mgmt.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cloudbox.mgmt.entity.FileDeleteException;
import com.rsclouds.jdbc.repository.JdbcRepository;
@Service
public class FileDeleteExceptionRespository extends JdbcRepository<FileDeleteException, String> {
	@Autowired
	@Qualifier("basejdbcdao")
	private BaseJdbcDao dao;
	
	public List<Map<String,Object>> getFileDeleteExceptionList(){
		String sql="select * from file_delete_exception where status=1";
		return dao.queryForList(sql, "jdbctemplate");
	}
	public void updateFileDeleteExceptionById(String id,Integer status){
		String sql="update file_delete_exception set status=?,update_dt=? where id=?";
		dao.updateByArgs(sql, "jdbctemplate",new Object[]{status,new Date(),id});
	}
}
