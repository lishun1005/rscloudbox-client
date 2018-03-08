package com.cloudbox.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 2016-03-18
 * @author lishun
 *
 */
public class JsonUtil {
	/**
	 * 把json字符串转换Map
	 * @param strJson
	 * @return
	 */
	public static Map<String, Object> toMap(Object strJson){
		JSONObject jsonObject = JSONObject.fromObject(strJson);
		Map<String, Object> map = new HashMap<String, Object>();
		for (Iterator iter = jsonObject.keys(); iter.hasNext();) {
			String key = (String) iter.next();
			map.put(key, jsonObject.get(key));
		}
		return map;
	}
	
	/**
	 * 把json字符串转换ListMap
	 * @param strJson
	 * @return
	 */
	public static List<Map<String, Object>> toListMap(Object strJson){
		JSONArray jArray=JSONArray.fromObject(strJson);
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		for (Object object : jArray) {
			Map<String, Object> map = toMap(object.toString());
			list.add(map);
		}
		return list;
	}
}
