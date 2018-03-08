package com.cloudbox.mgmt.controller;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cloudbox.mgmt.service.AreaService;

@Controller
public class AreaQueryController extends BaseController {

	private Map<String, Object> map;

	@Autowired
	@Qualifier("areaqueryserver")
	private AreaService areaqueryserver;

	/**
	 * 	@Description 查询省级区域列表
	 * 
	 */
	@RequestMapping("/AreaqueryforProvince")
	@ResponseBody
	public Map<String, Object> AreaqueryforProvince() {
		return areaqueryserver.AreaqueryforProvince();
	}

	/**
	 * 	@Description 查询市级级区域列表
	 *	@param province 省份名称
	 * 
	 */
	@RequestMapping("/AreaqueryforCity")
	@ResponseBody
	public Map<String, Object> AreaqueryforCity(String province) {
		try {
			province = URLDecoder.decode(province, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			map = new HashMap<String, Object>();
			map.put("code", "0");
			map.put("message", "中文编码解码出错!");
			return map;
		}
		return areaqueryserver.AreaqueryforCity(province);
	}

	/**
	 * 	@Description 查询县级区域列表
	 * 	@param city 市级名称
	 *	@param cityid 市级编码
	 */
	@RequestMapping("/AreaqueryforCounty")
	@ResponseBody
	public Map<String, Object> AreaqueryforCounty(String city, String cityid) {
		try {
			city = URLDecoder.decode(city, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			map = new HashMap<String, Object>();
			map.put("code", "0");
			map.put("message", "中文编码解码出错!");
			return map;
		}
		return areaqueryserver.AreaqueryforCounty(city, cityid);
	}

	/**
	 * 	@Description 查询区域信息
	 *	@param areaId 地区编码
	 */
	@RequestMapping("/AreaqueryByareaId")
	@ResponseBody
	public Map<String, Object> AreaqueryByareaId(String areaId) {
		return areaqueryserver.AreaqueryByareaId(areaId);
	}
	
	/**
	 * 	@Description 查询区域名称列表
	 * 	@param areaIdarray  区域编码数组 
	 */
	@RequestMapping("/AreaqueryForNameByIds")
	@ResponseBody
	public String AreaqueryForNameByIds(String areaIdarray) {
		String areaNames = areaqueryserver.AreaqueryForNameByIds(areaIdarray);
		return areaNames;
	}

}
