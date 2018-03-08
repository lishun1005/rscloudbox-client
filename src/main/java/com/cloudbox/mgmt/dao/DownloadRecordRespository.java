package com.cloudbox.mgmt.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.chinarsgeo.rscloudmart.web.dao.entity.DownloadRecord;
import com.cloudbox.mgmt.entity.Area;
import com.rsclouds.jdbc.repository.JdbcRepository;
@Service
public class DownloadRecordRespository extends JdbcRepository<Area, String> {
	@Autowired
	@Qualifier("basejdbcdao")
	private BaseJdbcDao dao;
	private Map<String, Object> map;
	 
	public void addFileDeleteException(String downloadPath,String imageName,Integer status){
		String id=UUID.randomUUID().toString();
		String sql="insert into file_delete_exception(id,download_path,image_name,status) values(?,?,?,?)";
		dao.insertByArgs(sql, "jdbctemplate", new Object[]{id,downloadPath,imageName,status});
	}
	
	
	public void insertRecordStutas(String recordid, String recordtime,String userordertype,String name,
			String downloadFilepath,String imageProductId,Integer downloadstatus,
			Long downloadsize,String taskendtime) {
		String sql="";
		if(taskendtime!=null){
			sql= "INSERT INTO downloadrecord(recordid,recordtime, userordertype,name,downloadfilepath," +
				"image_product_id,downloadstatus,downloadsize,targzendtime,inputstorageendtime,taskendtime," +
				"downloadendtime) " +
				" VALUES ('"+ recordid + "','" + recordtime + "','" + userordertype + "','"+name+"'" +
						",'"+downloadFilepath+"','"+imageProductId+"',"+downloadstatus+","+downloadsize+
						",'"+taskendtime+"','"+taskendtime+"','"+taskendtime+"','"+taskendtime+"')";
		}else{
			sql= "INSERT INTO downloadrecord(recordid,recordtime, userordertype,name,downloadfilepath," +
					"image_product_id,downloadstatus,downloadsize) " +
					" VALUES ('"+ recordid + "','" + recordtime + "','" + userordertype + "','"+name+"'" +
							",'"+downloadFilepath+"','"+imageProductId+"',"+downloadstatus+","+downloadsize+")";
		}
		dao.insert(sql, "jdbctemplate");
	}
	
	
	public List<Map<String,Object>> getDownloadrecordByDownloadStatus(Integer downloadstatus) {
		String sql = "select * from downloadrecord " +
				"where downloadstatus=" +downloadstatus+" order by recordtime";
		return dao.queryForList(sql, "jdbctemplate");
	}
	
	public List<Map<String,Object>> getDownloadrecordGroupByName(Integer downloadstatus) {
		String sql="";
		if(downloadstatus==null){
			sql = "select name,max(recordid) as id  from downloadrecord where " +
					"downloadstatus is null group by name order by recordtime desc";
		}else{
			sql = "select name,max(recordid) as id  from downloadrecord where " +
					"downloadstatus="+downloadstatus+" group by name";
		}
		return dao.queryForList(sql, "jdbctemplate");	
	}
	
	public int updateRecordStutasFordownloadstatus(Integer downloadstatus,
			String name) {
		String sql = "UPDATE downloadrecord SET  downloadstatus="+ downloadstatus + " where name='" + name + "'";
		return dao.update(sql, "jdbctemplate");
	}
	
	public int updateRecordStutasByName(Integer downloadstatus,
			String name) {
		String sql = "UPDATE downloadrecord SET  downloadstatus="+ downloadstatus + " where name='" + name + "'";
		return dao.update(sql, "jdbctemplate");
	}
	
	
	public int updateRecordStutasFordownloadsize(Long downloadsize,String name) {
		String sql = "UPDATE downloadrecord SET  downloadsize=" + downloadsize+ "where name='" + name + "'";
		return dao.update(sql, "jdbctemplate");
	}
	
	
	
	public Map<String, Object> queryRecordStutas() {
		map = new HashMap<String, Object>();
		String sql = "SELECT * FROM downloadrecord order by recordtime desc";
		try {
			List list = dao.queryForList(sql, "jdbctemplate");
			if (list != null && list.size() != 0) {
				map.put("code", "1");
				map.put("Msg", "查询成功！");
				map.put("list", list);
			} else {
				map.put("code", "0");
				map.put("Msg", "无数据！");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			map.put("code", "0");
			map.put("Msg", "数据库查询发生异常！");
			return map;
		}
		return map;
	}
	
	
	public DownloadRecord queryRecordStutasByRecordid(String recordid) {
		String sql = "SELECT * FROM downloadrecord where recordid='" + recordid+ "'";
		List<DownloadRecord> list = dao.queryForBeanPropertyRowMapper(sql,"jdbctemplate", DownloadRecord.class);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	
	public List<Map<String, Object>> queryDownrecordByName(String name){
		String sql = "SELECT * FROM downloadrecord where name='"+ name + "'";
		return dao.queryForList(sql, "jdbctemplate");
	}
	
	public Map<String, Object> queryrecordstutasfordownloadstatus(String recordid) {
		map = new HashMap<String, Object>();
		String sql = "SELECT downloadstatus FROM downloadrecord where recordid='"+ recordid + "'";
		try {
			List list = dao.queryForList(sql, "jdbctemplate");
			if (list != null && list.size() != 0) {
				map.put("code", "1");
				map.put("list", list);
				map.put("Msg", "查询成功！");
			} else {
				map.put("code", "0");
				map.put("Msg", "无数据！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("code", "0");
			map.put("Msg", "数据库查询发生异常！");
			return map;
		}
		return map;
	}
	
	
	public Map<String, Object> getRecordstutasAndOpStatus(String recordId) {
		String sql = "SELECT downloadstatus FROM downloadrecord where recordid='"+ recordId + "'";
		List<Map<String,Object>> list = dao.queryForList(sql, "jdbctemplate");
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	public Boolean checkIsDownloadByName(String name,Integer status) {
		String sql = "SELECT count(*) FROM downloadrecord where name='"+ name + "' and downloadstatus="+status;
		int i=dao.queryForInt(sql, "jdbctemplate");
		if(i>0){
			return true;
		}else{
			return false;
		}
	}
	
	public int qureyRecordbyrecordid(String recordid) {
		String sql = "select count(*) from downloadrecord where recordid='"+ recordid + "'";
		return dao.queryForInt(sql, "jdbctemplate");
	}

	
	
	public void updateDownloadEndTime(String downloadendtime, String name) {
		String sql = "UPDATE downloadrecord SET downloadendtime='"+ downloadendtime + "' WHERE name='" + name + "'";
		dao.update(sql, "jdbctemplate");
	}
	
	
	
	public void updateTargzEndTime(String targzendtime, String name) {
		String sql = "UPDATE downloadrecord SET targzendtime='" + targzendtime+ "' WHERE name='" + name + "'";
		dao.update(sql, "jdbctemplate");
	}

	
	
	public void updateCutEndTime(String cutendtime, String recordid) {
		String sql = "UPDATE downloadrecord SET cutendtime='" + cutendtime+ "' WHERE recordid='" + recordid + "'";
		dao.update(sql, "jdbctemplate");
	}

	
	
	public void updateInputstorageEndTime(String inputstorageendtime,
			String name) {
		String sql = "UPDATE downloadrecord SET inputstorageendtime='"+ inputstorageendtime + "' WHERE name='" + name + "'";
		dao.update(sql, "jdbctemplate");
	}
	
	
	
	public void updateTaskEndTime(String taskendtime, String name) {
		String sql = "UPDATE downloadrecord SET taskendtime='" + taskendtime + "' WHERE name='" + name + "'";
		dao.update(sql, "jdbctemplate");
	}
	
	public void deleteByName(String name) {
		String sql = "delete from  downloadrecord where name='"+name+"'";
		dao.update(sql, "jdbctemplate");
	}

	
	public void deleteById(String id) {
		String sql = "delete from  downloadrecord where recordid='"+id+"'";
		dao.update(sql, "jdbctemplate");
		
	}
}
