package com.cloudbox.mgmt.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.cloudbox.mgmt.entity.Area;
import com.cloudbox.table.Constant;
import com.rsclouds.jdbc.repository.JdbcRepository;
@Service
public class AreaRespository extends JdbcRepository<Area, String> {
	/**
	 * @Description 查询省级区域列表
	 * 
	 */
	public List<Map<String, Object>> AreaqueryforProvince() {
		String sql = "SELECT admincode,adminname as name FROM "
				+ Constant.rsclouds_area_table + " where proname =''";
		List<Map<String, Object>> list = getJdbcTemplate().queryForList(sql);
		return list;
	}

	/**
	 * @Description 查询市级级区域列表
	 * @param province
	 *            省份名称
	 */
	public  List<Map<String, Object>> AreaqueryforCity(String province) {
		String sql = "SELECT admincode,adminname as name  FROM "
				+ Constant.rsclouds_area_table + " where  cityadminname =''";
		if (province != null) {
			sql += "and proname='" + province + "'";
		}
		List<Map<String, Object>> list = getJdbcTemplate().queryForList(sql);
		return list;
	}

	/**
	 * @Description 查询县级区域列表
	 * @param city
	 *            市级名称
	 * @param cityid
	 *            市级编码
	 */
	public List<Map<String, Object>> AreaqueryforCounty(String city, String cityid) {
		cityid = cityid.substring(0, 4);
		String sql = "SELECT admincode ,adminname as name FROM "
				+ Constant.rsclouds_area_table + " where proname is not null ";
		if (city != null) {
			sql += "and cityadminname='" + city + "' and admincode like'"
					+ cityid + "%'";
		}
		List<Map<String, Object>> list = getJdbcTemplate().queryForList(sql);
		return list;
	}

	/**
	 * @Description 查询区域信息
	 * @param areaId
	 *            地区编码
	 */
	public List<Map<String, Object>> AreaqueryByareaId(String areaId) {
		// TODO Auto-generated method stub
		String sql = "SELECT admincode ,adminname as provancename ,proname as cityname,cityadminname as Countyname,ST_AsText(geom) as geom FROM "
				+ Constant.rsclouds_area_table
				+ " where admincode ='"
				+ areaId
				+ "'";
		List<Map<String, Object>> list = getJdbcTemplate().queryForList(sql);
		return list;
	}

	/**
	 * @Description 查询几何对象geom覆盖的区域编码列表
	 * @param geom
	 *            几何对象边框（bbox）
	 */
	public List<Map<String, Object>> AreaIdqueryByaGeom(String geom) {
		String sql = "SELECT admincode FROM " + Constant.rsclouds_area_table
				+ " where ST_Intersects(geom,ST_GeomFromText('" + geom
				+ "',4326))";
		List<Map<String, Object>> list = getJdbcTemplate().queryForList(sql);
		return list;

	}

	/**
	 * @Description 查询区域名称列表
	 * @param areaIdarray
	 *            区域编码数组
	 */
	public List<Map<String, Object>> AreaqueryForNameByIds(String areaIdarray) {
		String sql = "SELECT cityadminname as cityname ,proname as provancename,adminname as Countyname FROM "
				+ Constant.rsclouds_area_table
				+ " where admincode = any('"
				+ areaIdarray + "')";
		List<Map<String, Object>> list = getJdbcTemplate().queryForList(sql);
		return list;
	}
}
