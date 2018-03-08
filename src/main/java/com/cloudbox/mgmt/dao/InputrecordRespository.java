package com.cloudbox.mgmt.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cloudbox.mgmt.entity.Inputrecord;
import com.cloudbox.mgmt.old.entity.InputRecord;
import com.cloudbox.table.Constant;
import com.cloudbox.utils.ReflectTools;
import com.rsclouds.jdbc.repository.JdbcRepository;
@Service
public class InputrecordRespository extends JdbcRepository<Inputrecord, String> {
	private List list;

	@Autowired
	@Qualifier("basejdbcdao")
	private BaseJdbcDao dao;

	
	public void inserInputRecord(InputRecord inputrecord) {
		// TODO Auto-generated method stub
		Object[][] KV=ReflectTools.ReflectKV(inputrecord);
		//给定key-value 自动拼接Insert的SQL语句
		String sql=ReflectTools.ReflectforInsertsql(KV, Constant.InputRecord);
		dao.insert(sql, "jdbctemplate");
	}
	
	
	public int deleteInputRecord(String id) {
		// TODO Auto-generated method stub
		String sql = "delete from "+Constant.InputRecord+" where id=?";
		int i=dao.deleteByArgs(sql, "jdbctemplate", new Object[]{Integer.valueOf(id)});
		return i;
	}

	
	public void updataInputRecordForMassageByRecordId(String recordid, String message) {
		// TODO Auto-generated method stub
		String sql="update "+Constant.InputRecord+" set message='"+message+"' where recordid='"+recordid+"'";
		dao.update(sql, "jdbctemplate");
	}

	
	public void updataInputRecordForMassageByDataId(String dataid,
			String message) {
		// TODO Auto-generated method stub
		String sql="update "+Constant.InputRecord+" set message='"+message+"' where id='"+dataid+"'";
		dao.update(sql, "jdbctemplate");
	}

	
	public void updataInputRecordForDatanameByRecordId(String recordid,
			String dataname) {
		// TODO Auto-generated method stub
		String sql="update "+Constant.InputRecord+" set dataname='"+dataname+"' where recordid='"+recordid+"'";
		dao.update(sql, "jdbctemplate");
	}

	
	public void updataInputRecordForDownloadsizeByRecordId(String recordid,
			long downloadsize) {
		// TODO Auto-generated method stub
		String sql="update "+Constant.InputRecord+" set downloadsize="+downloadsize+" where recordid='"+recordid+"'";
		dao.update(sql, "jdbctemplate");
	}

	
	public void updataInputRecordForOpStatusByRecordId(String recordid,
			int opStatus) {
		// TODO Auto-generated method stub
		String sql="update "+Constant.InputRecord+" set opstatus="+opStatus+" where recordid='"+recordid+"'";
		dao.update(sql, "jdbctemplate");
	}

	
	public List<InputRecord> queryInputRecord() {
		// TODO Auto-generated method stub
		String sql="SELECT * FROM "+Constant.InputRecord+" WHERE opstatus!=7";
		return dao.queryForBeanPropertyRowMapper(sql, "jdbctemplate", InputRecord.class);
	}
	
	
	public List<InputRecord> queryInputRecordFinished() {
		String sql="SELECT * FROM "+Constant.InputRecord +" WHERE opstatus=7";
		return dao.queryForBeanPropertyRowMapper(sql, "jdbctemplate", InputRecord.class);
	}
	
	
	public List<InputRecord> queryInputRecordErrorItems() {
		// TODO Auto-generated method stub
		//错误标示应该为8，目前接口未调整
		String sql="SELECT * FROM "+Constant.InputRecord +" WHERE opstatus=8";
		return dao.queryForBeanPropertyRowMapper(sql, "jdbctemplate", InputRecord.class);
	}
	
	
	public void updataInputRecordForOpStatusByRecordId(String recordid,
			String endtime) {
		// TODO Auto-generated method stub
		String sql="update "+Constant.InputRecord+" set endtime='"+endtime+"' where recordid='"+recordid+"'";
		dao.update(sql, "jdbctemplate");
	}
}
