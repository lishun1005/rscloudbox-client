package com.cloudbox.mgmt.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloudbox.mgmt.entity.Area;
import com.cloudbox.mgmt.old.entity.RsImageMetaData;
import com.cloudbox.table.Constant;
import com.cloudbox.utils.ReflectTools;
import com.rsclouds.jdbc.repository.JdbcRepository;
@Service("GtdataDataPutInstorageRespository")
public class GtdataDataPutInstorageRespository extends JdbcRepository<Area, String> {
	private static Logger logger = LoggerFactory.getLogger(GtdataDataPutInstorageRespository.class);
	@Autowired
	@Qualifier("basejdbcdao")
	private BaseJdbcDao dao;
	/**
	 * @Description 影像信息入库
	 **/
	public void dataInputRecord(RsImageMetaData RsImageMetaData) {
		Object[][] KV = ReflectTools.ReflectKV(RsImageMetaData);
		//通过name和image_start_resolution确定数据是否存在，若存在就更新，不存在就添加
		List<Map<String,Object>> list=
				dao.queryForList("select id  from rscm_area_image where name='"+RsImageMetaData.getName()+"'"+" and image_start_resolution="+RsImageMetaData.getImage_end_resolution(), 
				"jdbctemplate");
		if(list!=null&&list.size()>0){
//			String[] wherek={"name","image_start_resolution"};
//			String updatesql = ReflectTools.ReflectforUpdatesqlwithGeom(KV, Constant.rsclouds_rscm_area_image_table, wherek);
//			int i=dao.update(updatesql, "jdbctemplate");
		}else {
			String sql = ReflectTools.ReflectforInsertsql(KV,Constant.rsclouds_rscm_area_image_table);
			dao.insert(sql, "jdbctemplate");
		}
	}

	/**
	 * @Description 根据任务id更新影像校正状态
	 **/
	public void updaterecordbyjobId(String jobId) {
		// TODO Auto-generated method stub
		String sql = "UPDATE " + Constant.rsclouds_rscm_area_image_table
				+ " SET iscorrect=true  WHERE jobid='" + jobId + "'";
		logger.info(sql);
		dao.update(sql, "jdbctemplate");
	}
	

	/**
	 * @Description 更新影像并行切割发布地址,
	 **/
	public void updaterecordbyjobIdforurl(String jobId,String url) {
		// TODO Auto-generated method stub
		String sql = "UPDATE " + Constant.rsclouds_rscm_area_image_table
				+ " SET img_url='"+url+"'  WHERE jobid='" + jobId + "'";
		logger.info(sql);
		dao.update(sql, "jdbctemplate");
	}
	
	/**
	 * @Description 根据记录id更新影像校正状态
	 **/
	public void updaterecordbyId(String id) {
		// TODO Auto-generated method stub
		
		String sql = "UPDATE " + Constant.rsclouds_rscm_area_image_table
				+ " SET iscorrect=true  WHERE id='" + id + "'";
		logger.info(sql);
		dao.update(sql, "jdbctemplate");
	}

	/**
	 * @Description 根据影像名称查询影像记录
	 **/
	public List queryPutInstorageforDataName(String name) {
		String sql="SELECT * FROM "+ Constant.rsclouds_rscm_area_image_table+" WHERE name='"+name+"' OR file_path like '%"+name+"\\\\' OR file_path like '%"+name+"\\\\\\\\'  OR file_path like '%"+name+"//' OR file_path like '%"+name+"/'";
		logger.info(sql);
		return dao.queryForBeanPropertyRowMapper(sql, "jdbctemplate", RsImageMetaData.class);
	}
	public List<Map<String,Object>> queryAdmincodeByGeom(String geom) {
		// TODO Auto-generated method stub
		String sql = "select admincode,cityadminname,adminname from admin123_simple_all " +
				" where st_intersects(geom,st_geomfromewkt('SRID=4326;"+geom+"'));";
		return dao.queryForList(sql, "jdbctemplate");
	}
}
