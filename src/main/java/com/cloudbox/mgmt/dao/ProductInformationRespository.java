package com.cloudbox.mgmt.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cloudbox.mgmt.entity.AreaImage;
import com.cloudbox.mgmt.old.entity.RsImageMetaData;
import com.cloudbox.table.Constant;
import com.cloudbox.utils.ReflectTools;
import com.rsclouds.jdbc.repository.JdbcRepository;
@Service
public class ProductInformationRespository extends JdbcRepository<AreaImage, String> {
	@Autowired
	@Qualifier("basejdbcdao")
	private BaseJdbcDao dao;
	
	//通过四点坐标查询数据
	public List queryproductInformationByGeom(String geom, String table,
			String productType, String cloudsrange, String Timerange,
			String productId ,String ProductsourceTypeCode, String resolutionrange) {
		String sql = "select id, area_no, image_row_col, image_satellite_type,  image_start_resolution,image_end_resolution, "
				+ "image_spectrum_type, begin_time, update_time, ST_AsText(gemo) as gemo, range, num, "
				+ "is_cover,image_area, data_id, image_product_type, relation_no, "
				+ " name, image_cloudage,sensor_id,product_level,to_date(collect_start_time,'yyyy/mm/dd') as collect_start_time,to_date(collect_end_time,'yyyy/mm/dd') as collect_end_time,product_id,file_path,area_no_array,img_url,area_description from " + table;
		String wheresql = " where ";
		if (geom != null) {
			wheresql += "ST_Intersects(gemo,ST_GeomFromText('" + geom
					+ "'))" + " and ";
		}
		if (ProductsourceTypeCode != null) {
			wheresql += "image_product_type='" +ProductsourceTypeCode + "' and ";
			if(ProductsourceTypeCode=="0102"){
				wheresql+="  img_url!='' and ";
			}
		}
		if (productType != null) {
			String[] productTypearray = productType.split(",");
			String productTypesql = "(";
			for (int i = 0; i < productTypearray.length; i++) {
				productTypesql += "image_satellite_type ='"
						+ productTypearray[i] + "' or ";
			}
			productTypesql = productTypesql.substring(0,
					productTypesql.lastIndexOf("or"));
			wheresql += productTypesql + ") and ";
		}
		if (Timerange != null) {
			String[] Timerangearray = Timerange.split(",");
	/*		wheresql += "collect_time between '"
					+ Timerangearray[0]
					+ "' and '"
					+Timerangearray[1] + "' and ";*/
			wheresql += "collect_start_time >='"
					+ Timerangearray[0]
					+ "' and collect_start_time <= '"
					+Timerangearray[1] + " 23:59:59' and ";
		}
		if (resolutionrange != null) {
			String[] resolutionrangearray = resolutionrange.split(",");
			wheresql += "image_start_resolution >= '"
					+ resolutionrangearray[0]
					+ "' and image_end_resolution <='"
					+resolutionrangearray[1] + "' and ";
		}
		if (cloudsrange != null) {
			wheresql += "image_cloudage between " + cloudsrange + " and ";
		}
		if (productId != null) {
			wheresql += "data_id = '" + productId + "' and ";
		}
		wheresql+="iscorrect='TRUE'";
		if (wheresql.lastIndexOf("and") == -1) {
			wheresql = "";
		} else {
			wheresql = wheresql.substring(0, wheresql.lastIndexOf("and"));
		}
		
		sql += wheresql;
		System.out.println(sql);
		return dao.queryForBeanPropertyRowMapper(sql, "jdbctemplate",
				RsImageMetaData.class);
	}

	//通过地区id查询
	public List queryproductInformationByAreaId(String provinceId,
			String cityId, String CountyId, String table, String productType,
			String cloudsrange, String Timerange, String productId,String ProductsourceTypeCode, String resolutionrange) {
		String sql = "select id, area_no, image_row_col, image_satellite_type, image_start_resolution,image_end_resolution,"
				+ "image_spectrum_type, begin_time, update_time, ST_AsText(gemo) as gemo, range, num, "
				+ "is_cover, image_area, data_id, image_product_type, relation_no, "
				+ " name, image_cloudage,sensor_id,product_level,to_date(collect_start_time,'yyyy/mm/dd') as collect_start_time,to_date(collect_end_time,'yyyy/mm/dd') as collect_end_time,product_id,file_path,area_no_array ,img_url,area_description from " + table;
		String wheresql = " where ";
		if (ProductsourceTypeCode != null) {
			wheresql += "image_product_type='" +ProductsourceTypeCode + "' and ";
			if(ProductsourceTypeCode=="0102"){
				wheresql+=" img_url!='' and ";
			}
		}
		if (CountyId != null) {
			wheresql += "'"+CountyId+"'= ANY(area_no_array)  and ";
		} else if (cityId != null) {
			wheresql += "'"+cityId+"'= ANY(area_no_array)  and ";
		} else if (provinceId != null) {
			wheresql += "'"+provinceId+"'= ANY(area_no_array)  and ";
		} else {
			return null;
		}
		if (productType != null) {
			String[] productTypearray = productType.split(",");
			String productTypesql = "(";
			for (int i = 0; i < productTypearray.length; i++) {
				productTypesql += "image_satellite_type ='"
						+ productTypearray[i] + "' or ";
			}
			productTypesql = productTypesql.substring(0,
					productTypesql.lastIndexOf("or"));
			wheresql += productTypesql + ") and ";
		}
		if (Timerange != null) {
			String[] Timerangearray = Timerange.split(",");
	/*		wheresql += "collect_time between '"
					+ Timerangearray[0]
					+ "' and '"
					+Timerangearray[1] + "' and ";*/
			wheresql += "collect_start_time >='"
					+ Timerangearray[0]
					+ "' and collect_start_time <= '"
					+Timerangearray[1] + " 23:59:59' and ";
		}
		if (resolutionrange != null) {
			String[] resolutionrangearray = resolutionrange.split(",");
			wheresql += "image_start_resolution >= '"
					+ resolutionrangearray[0]
					+ "' and image_end_resolution <='"
					+resolutionrangearray[1] + "' and ";
		}
		if (cloudsrange != null) {
			wheresql += "image_cloudage between " + cloudsrange + " and ";
		}
		if (productId != null) {
			wheresql += "data_id = '" + productId + "' and ";
		}
		if (wheresql.lastIndexOf("and") == -1) {
			wheresql = "";
		} else {
			wheresql = wheresql.substring(0, wheresql.lastIndexOf("and"));
		}
		sql += wheresql +" order by  image_start_resolution";
		System.out.println(sql); 
		return dao.queryForBeanPropertyRowMapper(sql, "jdbctemplate",
				RsImageMetaData.class);
	}

	public int UpdateproductInformationById(RsImageMetaData RsImageMetaData) {
		Object[][] KV = ReflectTools.ReflectKV(RsImageMetaData);
		String[] whereK = { RsImageMetaData.getId() };
		String sql = ReflectTools.ReflectforUpdatesql(KV,
				Constant.rsclouds_rscm_area_image_table, whereK);
		return dao.update(sql, "jdbctemplate");
	}

	public List<RsImageMetaData> queryproductInformationByRecordid(String recordid) {
		String sql = "select id, area_no, image_row_col, image_satellite_type, image_start_resolution,image_end_resolution,"
				+ "image_spectrum_type, begin_time, update_time, ST_AsText(gemo) as gemo, range, num, "
				+ "is_cover, image_area, data_id, image_product_type, relation_no, "
				+ " name, image_cloudage,sensor_id,product_level,to_date(collect_start_time,'yyyy/mm/dd') as collect_start_time,to_date(collect_end_time,'yyyy/mm/dd') as collect_end_time,product_id,file_path,area_no_array,img_url,area_description from " 
				+ Constant.rsclouds_rscm_area_image_table+" where record_id='"+recordid+"'";
		System.out.println(sql);
		return dao.queryForBeanPropertyRowMapper(sql, "jdbctemplate", RsImageMetaData.class);
	}
	
	public Map<String,Object> queryproductInformationById(String id) {
		String sql = "select id, area_no,dl.name as dlName,dl.downloadfilepath as downloadFilepath,image_row_col,dl.downloadsize, rai.record_id,image_satellite_type, image_start_resolution,image_end_resolution,"
				+ "image_spectrum_type,rai.auto_tag, begin_time, update_time, ST_AsText(gemo) as gemo,ST_AsText(range) as range, rai.num, "
				+ "is_cover,image_size, image_area, data_id, image_product_type, relation_no, "
				+ " rai.name, image_cloudage,sensor_id,product_level,to_date(collect_start_time,'yyyy/mm/dd') as collect_start_time,to_date(collect_end_time,'yyyy/mm/dd') as collect_end_time,product_id,file_path,area_no_array,img_url,area_description from " 
				+ Constant.rsclouds_rscm_area_image_table+
				" rai left join downloadrecord dl on dl.recordid=rai.record_id "
				+" where id ='"+id+"'";
		
		System.out.println(sql);
		List list=dao.queryForList(sql, "jdbctemplate");
		if(list.size()>0){
			return (Map<String,Object>)dao.queryForList(sql, "jdbctemplate").get(0);
		}
		return null;
	}
	
	public Map queryDataCallBackOpIsReadyById(String id) {
		String sql = "select img_url,iscorrect from " 
				+ Constant.rsclouds_rscm_area_image_table+" where id='"+  id+"'";
		return dao.queryForMap(sql, "jdbctemplate");
	}

	public String queryRecordIdByName(String name) {
		String sql="select record_id from "+ Constant.rsclouds_rscm_area_image_table
				+" where name='"+name+"'";
		List<Map<String,Object>> list=dao.queryForList(sql, "jdbctemplate");
		if(list!=null&&list.size()>0){
			return String.valueOf(list.get(0).get("record_id"));
		}
		return "";
	}
}
