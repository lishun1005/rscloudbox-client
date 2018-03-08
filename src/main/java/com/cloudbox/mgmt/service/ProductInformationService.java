package com.cloudbox.mgmt.service;


import java.util.Map;

import com.cloudbox.mgmt.old.entity.RsImageMetaData;

public interface ProductInformationService {
	/**
	* Description  根据地区范围查询影像信息
	* @param    geom   地区范围
	* @param    productsourceType  数据类型  原始影像等
	* @param    productType        影像类型 GF1等
	* @param    cloudsrange        云量范围
	* @param    Timerange          时间范围
	* @param    productId          产品id
	* @param    resolutionrange    分辨率范围
	* @return  
	* @author lzw 
	* @date 2016-5-9 上午11:22:57
	 */
	 public Map<String, Object> queryproductInformationByGeom(String geom,String productsourceType,String productType,String cloudsrange,String Timerange,String productId,String resolutionrange) ;
	/**
	* Description 根据地区名称查询影像信息
	
	* @param provinceId 省份id
	* @param cityId 城市id
	* @param CountyId 镇级id
	* <br>[provinceId,cityId,CountyId] 地区id,多选一
	* @param productsourceType  数据类型  原始影像等
	* @param productType        影像类型 GF1等
	* @param cloudsrange        云量范围
	* @param Timerange          时间范围
	* @param productId          产品id
	* @param resolutionrange    分辨率范围
	* @return  
	* @author lzw 
	* @date 2016-5-9 上午11:18:27
	 */
	 public Map<String, Object> queryproductInformationByAreaId(String provinceId, String cityId, String CountyId,
				String productsourceType,String productType,String cloudsrange,String Timerange,String productId, String resolutionrange);
	 /**
	 * Description: 根据记录id更新影像信息
	 * @param RsImageMetaData 更新实体
	 * @return  
	 * @author lzw 
	 * @date 2016-5-9 上午11:13:58
	  */
	 public Map<String, Object> UpdateproductInformationById(RsImageMetaData RsImageMetaData);
	/**
	* Description: 根据记录recordid查询影像信息
	* @param recordid 
	* @return  
	* @author lzw 
	* @date 2016-5-9 上午11:13:05
	 */
	public Map<String, Object> queryproductInformationByRecordid(String recordid);
	/**
	 * 
	* Description  查询影像校正或并行切割的准被状态
	* @param id
	* @param checkwhat
	* @return  
	* @author lzw 
	* @date 2016-5-9 上午11:24:29
	 */
	public boolean queryDataCallBackOpIsReadyById(String id, boolean checkwhat);
	/**
	* Description: 根据文件名查询recordid值
	* @param name 文件名[eq:GF2_PMS2_E113.3_N23.8_20150328_L1A0000724460]
	* @return  
	* @author lishun 
	* @date 2016-5-9 上午11:27:35
	 */
	String queryRecordIdByName(String name);
	 
	//获取数据模型的XML信息
	// public Map<String, Object> getXMLInfo(String id,String filepath,String file,String dataType) ;
	 
	 
	 
	 
}





