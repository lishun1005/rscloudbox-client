package com.cloudbox.mgmt.service.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cloudbox.Enum.tableEnum;
import com.cloudbox.mgmt.dao.ProductInformationRespository;
import com.cloudbox.mgmt.old.entity.RsImageMetaData;
import com.cloudbox.mgmt.service.ProductInformationService;
import com.cloudbox.utils.XmlModelMap;

@Service("productInformationserver")
public class ProductInformationServiceImpl implements ProductInformationService {
	@Autowired
	@Qualifier("xmlModelMap")
	private XmlModelMap xmlModelMap;
	
	private Map<String, Object> map;
	private List list;
	@Autowired
	private ProductInformationRespository productInformationmanagement;

	@Override
	public Map<String, Object> queryproductInformationByGeom(String geom,String productsourceType,String productType,String cloudsrange,String Timerange,String productId,String resolutionrange) {
		map=new HashMap<String, Object>();
		try {
			tableEnum productsourceEnum = tableEnum.gettableEnum(productsourceType);
			list=productInformationmanagement.queryproductInformationByGeom(geom, productsourceEnum.getTablename(),productType, cloudsrange, Timerange, productId, productsourceEnum.getProductsourceTypeCode(),resolutionrange);
			if (list!=null&&list.size()!=0) {
				map.put("code", "1");
				map.put("message", "查询成功！");
				map.put("list", list);
				map.put("HtmltableInit", productsourceEnum.getHtmltableInit());
			}else {
				map.put("code", "0");
				map.put("message", "查询结果为空！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("code", "0");
			map.put("message", "后台查询发生异常！");
		}
		return map;
	}
	
	@Override
	public Map<String, Object> queryproductInformationByAreaId(String provinceId, String cityId, String CountyId,
			String productsourceType,String productType,String cloudsrange,String Timerange,String productId,String resolutionrange) {
		map=new HashMap<String, Object>();
		try {
			tableEnum productsourceEnum = tableEnum.gettableEnum(productsourceType);
			list=productInformationmanagement.queryproductInformationByAreaId(provinceId, cityId, CountyId, productsourceEnum.getTablename(),productType,cloudsrange, Timerange, productId,productsourceEnum.getProductsourceTypeCode(),resolutionrange);
			if (list!=null&&list.size()!=0) {
				map.put("code", "1");
				map.put("message", "查询成功！");
				map.put("list", list);
				map.put("HtmltableInit", productsourceEnum.getHtmltableInit());
			}else {
				map.put("code", "0");
				map.put("message", "查询结果为空！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("code", "0");
			map.put("message", "后台查询发生异常！");
		}
		return map;
	}
	
	@Override
	public Map<String, Object> UpdateproductInformationById(RsImageMetaData RsImageMetaData) {
		map=new HashMap<String, Object>();
		try {
			int k=productInformationmanagement.UpdateproductInformationById(RsImageMetaData);
			if (k!=0) {
				map.put("code", "0");
			}else {
				map.put("code", "1");
				map.put("message", "插入数据成功！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("code", "0");
			map.put("message", "后台插入数据发生异常！");
		}
		return map;
	}

	@Override
	public Map<String, Object> queryproductInformationByRecordid(String recordid) {
		map=new HashMap<String, Object>();
		try {
			list=productInformationmanagement.queryproductInformationByRecordid(recordid);
			if (list!=null&&list.size()!=0) {
				map.put("code", "1");
				map.put("message", "查询成功！");
				map.put("list", list);
				RsImageMetaData firstData=(RsImageMetaData) list.get(0);
				if (firstData.getImage_product_type().equals("0101")) {
					map.put("HtmltableInit","originalimageresultList");
				}else {
					map.put("HtmltableInit","orthoimageresultList");
				}
			}else {
				map.put("code", "0");
				map.put("message", "查询结果为空！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("code", "0");
			map.put("message", "后台查询发生异常！");
		}
		return map;
	}
	
	@Override
	public boolean queryDataCallBackOpIsReadyById(String id,boolean checkwhat) {
		try {
		    Map	map=productInformationmanagement.queryDataCallBackOpIsReadyById(id);
			if (map!=null&&!map.isEmpty()) {
				if (checkwhat) {
					if ((Boolean) map.get("iscorrect")==true) {
						return true;
					}
				}else {
					if (map.get("img_url")!=null) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	@Override
	public String queryRecordIdByName(String name) {
		return productInformationmanagement.queryRecordIdByName(name);
	}
	/*@Override
	 public Map<String, Object> getXMLInfo(String id,String filepath,String name,String dataType) {
		// TODO Auto-generated method stub
		map=new HashMap<String, Object>();
		
		dataType="ZY-3";
		filepath="G:/原始数据/原始数据/国产卫星/ZY3/ZY3_NAD_E119.3_N25.9_20140614_L1A0001724356";
		name="ZY3_TLC_E119.3_N25.9_20140614_L1A0001724356-NAD";
		
		
		
		//productInformationmanagement.getXMLMetaDataModel();
		String ClassName = (String) xmlModelMap.getXmlModel(dataType);
		//拼接xml路径全名
		String fullName=filepath+"\\"+name+".xml";
		
		
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(Class.forName(ClassName));
			Unmarshaller shaller = context.createUnmarshaller();
			BaseProductMetaData baseProductMetaData=
					(BaseProductMetaData)shaller.unmarshal(new File(fullName));

			
			map.put("code", 1);
			map.put("xmlModel", baseProductMetaData);
		
			
		} catch (Exception e) {
			// TODO: handle exception
			
			map.put("code", "0");
			map.put("message", "后台查询发生异常！");
		}
		return map;
	}
*/
	
	
	
}
