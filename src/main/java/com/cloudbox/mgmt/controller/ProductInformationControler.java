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

import com.cloudbox.mgmt.old.entity.RsImageMetaData;
import com.cloudbox.mgmt.service.ProductInformationService;
import com.cloudbox.utils.StringTools;

@Controller
public class ProductInformationControler {
	private Map<String, Object> map;
	@Autowired
	@Qualifier("productInformationserver")
	private ProductInformationService productInformationserver;

	/**
	 * @Description  根据记录id查询影像信息
	 * */
	@RequestMapping("/queryproductInformationByRecordid")
	@ResponseBody
	public Map<String, Object> queryproductInformationByRecordid(String recordid,String name) {
		Map<String,Object> resultMap=productInformationserver.queryproductInformationByRecordid(recordid);
		if("0".equals(resultMap.get("code").toString())){
			String recordidNew=productInformationserver.queryRecordIdByName(name);
			if(!"".equals(recordidNew)){
				resultMap=productInformationserver.queryproductInformationByRecordid(recordidNew);
			}
		}
		return resultMap;
	}
	/**
	 * @Description  根据地区范围查询影像信息
	 * */
	@RequestMapping("/queryproductInformationByGeom")
	@ResponseBody
	public Map<String, Object> queryproductInformationByGeom(String geom,
			String productsourceType,String productType,String cloudsrange,String Timerange,String productId,String resolutionrange) {
		
		// 修复时间查询的bug
		Timerange = StringTools.fixTimerange(Timerange);
		
		// TODO Auto-generated constructor stub
		try {
			productsourceType=URLDecoder.decode(productsourceType,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			map =new HashMap<String, Object>();
			map.put("code", "0");
			map.put("message", "中文编码解码出错!");
			return map;
		}
		return productInformationserver.queryproductInformationByGeom(geom,productsourceType,productType,cloudsrange, Timerange, productId,resolutionrange);
	}
	/**
	 * @Description  根据地区名称查询影像信息
	 * */
	@RequestMapping("/queryproductInformationByAreaName")
	@ResponseBody
	public Map<String, Object> queryproductInformationByAreaId(
			String provinceId, String cityId, String CountyId,
			String productsourceType,String productType,String cloudsrange,String Timerange,String productId,String resolutionrange) {
		
		// 修复时间查询的bug
		Timerange = StringTools.fixTimerange(Timerange);
		
		try {
			productsourceType=URLDecoder.decode(productsourceType,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			map =new HashMap<String, Object>();
			map.put("code", "0");
			map.put("message", "中文编码解码出错!");
			return map;
		}
		return productInformationserver.queryproductInformationByAreaId(
				provinceId, cityId, CountyId, productsourceType,productType,cloudsrange, Timerange, productId,resolutionrange);
	}
	/**
	 * @Description  根据记录id更新影像信息
	 * */
	@RequestMapping("/UpdateproductInformationById")
	@ResponseBody
	public Map<String, Object> UpdateproductInformationById(
			RsImageMetaData RsImageMetaData) {
		// TODO Auto-generated constructor stub
		return productInformationserver.UpdateproductInformationById(RsImageMetaData);
	}
	/**
	 * @Description  查询影像校正或并行切割的准被状态
	 * */
	@RequestMapping("/querydatacallbackopisreadybyId")
	@ResponseBody
	public boolean queryDataCallBackOpIsReadyById(
			String id,boolean checkwhat) {
		// TODO Auto-generated constructor stub
		return productInformationserver.queryDataCallBackOpIsReadyById(id,checkwhat);
	}
}
