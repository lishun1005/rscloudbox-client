package com.cloudbox.mgmt.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cloudbox.mgmt.entity.Area;
import com.cloudbox.mgmt.old.entity.ApplicantDataList;
import com.cloudbox.mgmt.old.entity.ApplicantInformation;
import com.cloudbox.mgmt.old.entity.DownloadDataList;
import com.cloudbox.mgmt.old.entity.ExaminationApprovalRecords;
import com.cloudbox.table.Constant;
import com.cloudbox.utils.ReflectTools;
import com.rsclouds.jdbc.repository.JdbcRepository;
@Service
public class CheckProcessRespository extends JdbcRepository<Area, String> {
	@Autowired
	@Qualifier("basejdbcdao")
	private BaseJdbcDao dao;
	
	
	public boolean updateExaminationApprovalRecordsById(String id,Integer status,String examinationApprovalTime){
		String sql ="update examination_approval_records set examination_approval_status="+status
				+" ,examination_approval_time='"+examinationApprovalTime+"' WHERE id='"+id+"'";
		return dao.update(sql, "jdbctemplate")>0;
	}
	
	public boolean updateDownloadDataById(String id, Integer status){
		String sql ="update download_data_list set op_status="+status+" WHERE id='"+id+"'";
		return dao.update(sql, "jdbctemplate")>0;
	}
	public boolean addApplicantInformation(ApplicantInformation applicantInformation){
		Object[][] KV = ReflectTools.ReflectKV(applicantInformation);
		String sql = ReflectTools.ReflectforInsertsql(KV,
				Constant.applicant_information);
		System.out.println(sql);
		boolean flag=dao.update(sql, "jdbctemplate")>0;
		return flag;
	}
	
	public boolean addDownloadDataList(DownloadDataList downloadDataList){
		Object[][] KV = ReflectTools.ReflectKV(downloadDataList);
		String sql = ReflectTools.ReflectforInsertsql(KV,
				Constant.download_data_list);
		boolean flag=dao.update(sql, "jdbctemplate")>0;
		return flag;
	}
	public boolean addApplicantDataList(ApplicantDataList applicantDataList){
		Object[][] KV = ReflectTools.ReflectKV(applicantDataList);
		String sql = ReflectTools.ReflectforInsertsql(KV,
				Constant.applicant_data_list);
		
		System.out.println(sql);
		boolean flag=dao.update(sql, "jdbctemplate")>0;
		return flag;
	}
	public boolean addExaminationApprovalRecords(ExaminationApprovalRecords examinationApprovalRecords){
		Object[][] KV = ReflectTools.ReflectKV(examinationApprovalRecords);
		String sql = ReflectTools.ReflectforInsertsql(KV,
				Constant.examination_approval_records);
		boolean flag=dao.update(sql, "jdbctemplate")>0;
		return flag;
	}
	public List<Map<String, Object>> findAreaImageByRecordId(String recordId){
		String sqlString="select id from rscm_area_image where record_id='"+recordId+"'";
		return dao.queryForList(sqlString, "jdbctemplate");
	}
	
	public List<Map<String, Object>> findDownloadDataListByExaminationApprovalRecordId(String ExaminationApprovalRecordId){
		String sqlString="select ST_AsText(range) as range,id,examination_approval_record_id,file_name,image_product_type,op_status,downloadsize,file_directory,record_id,auto_tag from download_data_list ddl where examination_approval_record_id='"+ExaminationApprovalRecordId+"'";
		return dao.queryForList(sqlString, "jdbctemplate");
	}
	
	public Map<String, Object> findExampleDataLimit(){
		String sqlString="select * from example_data_limit ";
		List list=dao.queryForList(sqlString,"jdbctemplate");
		if(list.size()>0){
			return (Map<String, Object>)list.get(0);
		}
		return null;
	}
	
	public int findDownloadDataListCountByDate(String startDate,String endDate){
		String sqlString="select count(*) from examination_approval_records ear,download_data_list ddl"
						 +" where examination_approval_time>='"+startDate+"' and examination_approval_time<='"+endDate+"'"
						 +" and  ddl.examination_approval_record_id =ear.id " 
						 +" and examination_approval_status=1 and purpose=0 and (op_status=1 or op_status=2)";
		System.out.println(sqlString);
		return dao.queryForInt(sqlString, "jdbctemplate");
	}
	
	public List<Map<String,Object>> listCheckProcess(String id,String examinationApprovalStatus,
			String keyWord,Integer purpose){
		String sql ="select *,ear.id as earId,ai.id as aiId,ear.id as earId from examination_approval_records ear" +
				" left join applicant_information ai on ear.applicant_information_id=ai.id ";
		String sqlWhere=" where 1=1 ";
		if(id!=null&&!"".equals(id)){
			sqlWhere+=" and  ear.id='"+id+"'";
		}
		if(examinationApprovalStatus!=null&&!"".equals(examinationApprovalStatus)){
			sqlWhere+=" and ear.examination_approval_status="+examinationApprovalStatus+"";
		}
		if(keyWord!=null&&!"".equals(keyWord)){
			sqlWhere+=" and ai.applicant_name like '%"+keyWord+"%'";
		}
		if(purpose!=null&&purpose!=-1){
			sqlWhere+=" and ear.purpose ="+purpose;
		}
		//System.out.println(sql+sqlWhere+" order by ear.applicant_time");
		return dao.queryForList(sql+sqlWhere+" order by ear.applicant_time desc", "jdbctemplate");
	}
	public List<Map<String,Object>> filterAreaImage(Object[] ids){
		String arrs="";
		for(int i=0;i<ids.length;i++){
			arrs=arrs+"'"+ids[i]+"',";
		}
		
		/*
		 * 只要两个rscm_area_image记录中的image_satellite_type及file_path一样，则他两
		 * 归为同一个downloadrecord记录，取的rscm_area_image_id取面积最大的那一个
		 */
		String sql="select c.image_satellite_type,sum(c.image_area) as sum_area,count(c.image_satellite_type) as sum_cou from (" +
				" select a.id,a.image_satellite_type,a.image_area,a.file_path from rscm_area_image a " +
				" where image_area = " +
				" (select max(image_area) from rscm_area_image where file_path = a.file_path and a.image_satellite_type=image_satellite_type) " +
				" and a.id in("+arrs.substring(0,arrs.length()-1)+") order by a.file_path) as c group by image_satellite_type order by  sum(c.image_area)";
		//System.out.println(sql);
		return dao.queryForList(sql, "jdbctemplate");
	}
	
	
	public String getMaxImageArea(String id){
		String sqlString="select file_path,image_satellite_type from rscm_area_image where  id ='"+id+"'";
		List<Map<String, String>> list=
				dao.queryForList(sqlString, "jdbctemplate");
		if(list!=null&&list.size()>0){
			Map<String, String> map=list.get(0);
			String imageSatelliteType=map.get("image_satellite_type");
			String file_path=map.get("file_path");
			if(imageSatelliteType!=null&&!"".equals(imageSatelliteType)
					&&file_path!=null&&!"".equals(file_path)){
				String sqlString2="select id from rscm_area_image where image_area=" +
						"(select max(image_area) from rscm_area_image where " +
						"image_satellite_type='"+imageSatelliteType+"' and file_path='"+file_path+"')" +
						"and image_satellite_type='"+imageSatelliteType+"' and file_path='"+file_path+"';";
				Map<String, String> map2=(Map<String, String>)dao
							.queryForList(sqlString2, "jdbctemplate").get(0);//面积可能一样大
				return map2.get("id");
			}
			return id;
		}else{
			return "";
		}
		
	}
	public List<Map<String,Object>> getIdsBySatelliteType(Object[] ids,String satelliteType){
		String arrs="";
		for(int i=0;i<ids.length;i++){
			arrs=arrs+"'"+ids[i]+"',";
		}
		String sql="select id from rscm_area_image where  id in("+arrs.substring(0,arrs.length()-1)+") " +
				"and image_satellite_type='"+satelliteType+"'";
		//System.out.println(sql);
		return dao.queryForList(sql, "jdbctemplate");
	}
	
	public List<Map<String,Object>> getapplicantDataListByIds(String[] ids){
		String arrs="";
		for(int i=0;i<ids.length;i++){
			arrs=arrs+"'"+ids[i]+"',";
		}
		String sql="select * from applicant_data_list where  id in("+arrs.substring(0,arrs.length()-1)+") ";
		//System.out.println(sql);
		return dao.queryForList(sql, "jdbctemplate");
	}
	public Integer getapplicantAllNum(String[] ids){
		String arrs="";
		for(int i=0;i<ids.length;i++){
			arrs=arrs+"'"+ids[i]+"',";
		}
		String sql="select sum(num) from applicant_data_list  " +
				"where id in("+arrs.substring(0,arrs.length()-1)+");";
		//System.out.println(sql);
		return dao.queryForInt(sql, "jdbctemplate");
	}
	public Map<String,Object> getApplicantInformationById(String id){
		String sql ="select * from applicant_information where id='"+id+"'";
		return dao.queryForMap(sql, "jdbctemplate");
	}
	
	public Map<String,Object> getrscmAreaImageById(String id){
		String sql ="select id,area_no, image_row_col,image_satellite_type,image_start_resolution," +
				" image_spectrum_type,begin_time,update_time,rai.num,is_cover,data_id,image_product_type," +
				" relation_no,rai.name ,image_cloudage ,sensor_id,product_level ,collect_start_time ," +
				" file_path ,product_id ,iscorrect,jobid ,img_url,image_area,record_id,collect_end_time ," +
				" image_end_resolution,tip,srid,area_description,image_size,auto_tag,dr.downloadsize" +
				" from rscm_area_image rai" +
				" left join  downloadrecord dr on dr.recordid= rai.record_id" +
				" where id='"+id+"'";
		List list=dao.queryForList(sql, "jdbctemplate");
		if(list.size()>0){
			return dao.queryForMap(sql, "jdbctemplate");
		}
		return null;
	}
	
	public Map<String,String> checkExaminationApprovalRecordsByRecordId(String recordId){
		Map<String,String> map=new HashMap<String, String>();
		String sql="select * from download_data_list where record_id='"+recordId+"'";
		List<Map<String,Object>> list=dao.queryForList(sql, "jdbctemplate");
		if(list!=null&&list.size()>0){
			list.clear();
			sql="select * from download_data_list where record_id='"+recordId+"' and op_status=0";
			list=dao.queryForList(sql, "jdbctemplate");
			if(list!=null&&list.size()>0){
				map.put("val", list.get(0).get("examination_approval_record_id").toString());
				map.put("code", "0");
			}else{
				map.put("code", "1");
			}
		}else{
			map.put("code", "1");
		}
		return map;
	}
}
