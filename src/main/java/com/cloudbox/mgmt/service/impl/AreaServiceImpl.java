package com.cloudbox.mgmt.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudbox.mgmt.dao.AreaRespository;
import com.cloudbox.mgmt.service.AreaService;

@Service("areaqueryserver")
public class AreaServiceImpl implements AreaService {

	private Map<String, Object> map;

	private List<Map<String, Object>> list;

	@Autowired
	private AreaRespository areaquerymanage;

	/**
	 * @Description 查询省级区域列表
	 */
	@Override
	public Map<String, Object> AreaqueryforProvince() {
		// TODO Auto-generated method stub
		map = new HashMap<String, Object>();
		try {
			list = areaquerymanage.AreaqueryforProvince();
		} catch (Exception e) {
			e.printStackTrace();
			map.put("code", "2");
			map.put("message", "查询发生异常！");
		}
		if (list != null || list.size() != 0) {
			map.put("code", "1");
			map.put("message", "查询成功，返回列表！");
			map.put("list", list);
		} else {
			map.put("code", "0");
			map.put("message", "查询无结果！");
		}
		return map;
	}

	/**
	 * @Description 查询市级级区域列表
	 * @param province
	 *            省份名称
	 */
	@Override
	public Map<String, Object> AreaqueryforCity(String province) {
		map = new HashMap<String, Object>();
		if (province == null || "".equals(province)) {
			map.put("code", "0");
			map.put("message", "请输入查询市的父级省份名称或区号！");
			return map;
		}
		try {
			list = areaquerymanage.AreaqueryforCity(province);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("code", "2");
			map.put("message", "查询发生异常！");
		}
		if (list != null || list.size() != 0) {
			map.put("code", "1");
			map.put("message", "查询成功，返回列表！");
			map.put("list", list);
		} else {
			map.put("code", "0");
			map.put("message", "查询无结果！");
		}
		return map;
	}

	/**
	 * @Description 查询县级区域列表
	 * @param city
	 *            市级名称
	 * @param cityid
	 *            市级编码
	 */
	@Override
	public Map<String, Object> AreaqueryforCounty(String city, String cityid) {
		map = new HashMap<String, Object>();
		if (city == null || "".equals(city) || cityid == null
				|| "".equals(cityid)) {
			map.put("code", "0");
			map.put("message", "请输入查询市的父级省份名称或区号！");
			return map;
		}
		try {
			list = areaquerymanage.AreaqueryforCounty(city, cityid);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("code", "2");
			map.put("message", "查询发生异常！");
		}
		if (list != null || list.size() != 0) {
			map.put("code", "1");
			map.put("message", "查询成功，返回列表！");
			map.put("list", list);
		} else {
			map.put("code", "0");
			map.put("message", "查询无结果！");
		}
		return map;
	}

	/**
	 * @Description 查询区域信息
	 * @param areaId
	 *            地区编码
	 */
	@Override
	public Map<String, Object> AreaqueryByareaId(String areaId) {
		map = new HashMap<String, Object>();
		if (areaId == null || "".equals(areaId)) {
			map.put("code", "0");
			map.put("message", "请输入查询区号！");
			return map;
		}
		try {
			list = areaquerymanage.AreaqueryByareaId(areaId);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("code", "2");
			map.put("message", "查询发生异常！");
		}
		if (list != null || list.size() != 0) {
			map.put("code", "1");
			map.put("message", "查询成功，返回列表！");
			map.put("list", list);
		} else {
			map.put("code", "0");
			map.put("message", "查询无结果！");
		}
		return map;
	}

	/**
	 * @Description 查询区域名称列表
	 * @param areaIdarray
	 *            区域编码数组
	 */
	@Override
	public String AreaqueryForNameByIds(String areaIdarray) {
		String areaNames = "";
		list = areaquerymanage.AreaqueryForNameByIds(areaIdarray);
		if (list != null || list.size() != 0) {
			for (int i = 0; i < list.size(); i++) {
				String cityname = String.valueOf(list.get(i).get("cityname"));
				String provancename = String.valueOf(list.get(i).get("provancename"));
				String Countyname = String.valueOf(list.get(i).get("Countyname"));
				if (cityname == "县") {
					areaNames += provancename + Countyname;
				} else {
					areaNames += provancename + cityname + Countyname;
				}
				areaNames += ",";
			}
		}
		return areaNames;
	}
}
