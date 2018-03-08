package com.cloudbox.mgmt.service.impl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.chinarsgeo.rscloudmart.web.webservice.IGetEdgeDataInfomationsServer;
import com.cloudbox.mgmt.dao.GtdataDataPutInstorageRespository;
import com.cloudbox.mgmt.old.entity.GeojsonMultiPolygonDto;
import com.cloudbox.mgmt.old.entity.RsImageMetaData;
import com.cloudbox.mgmt.service.HandleAreaImageService;
import com.cloudbox.utils.Base64;
import com.cloudbox.utils.RSAEncrypt;
import com.cloudbox.utils.StringTools;
@Service("handleAreaImageService")
public class HandleAreaImageServiceImpl implements HandleAreaImageService{
	private static Logger logger = LoggerFactory.getLogger(HandleAreaImageServiceImpl.class);
	
	@Autowired
	@Qualifier("getEdgeDataInfomationsServerRmiService")
	protected IGetEdgeDataInfomationsServer getEdgeDataInfomationsRmiServer;
	@Autowired
	@Qualifier("GtdataDataPutInstorageRespository")
	private GtdataDataPutInstorageRespository gtdataDataPutInstorageManagement;

	protected static String groupUserName;
	protected static String groupUserPassword;
	@Value("#{bboxSystemProperties[RsmartUrl]}")
	private String rsmartUrl;
	
	static {
		String file = "Rsmartgtcloudos";
		try {
			groupUserName = new String(RSAEncrypt.decrypt(RSAEncrypt.loadPublicKeyByStrDefault(), Base64.decode(StringTools.getValueFromProperties(file, "groupUserName"))));
			groupUserPassword =  new String(RSAEncrypt.decrypt(RSAEncrypt.loadPublicKeyByStrDefault(), Base64.decode(StringTools.getValueFromProperties(file, "groupUserPassword"))));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	@Override
	public Map<String, String> checkDataInfomationIsSell(String imageId) {
		Map<String,String> resultMap=new HashMap<String, String>();
		Map<String, Object> map	=getEdgeDataInfomationsRmiServer.getEdgeDataInfomationByImageId(imageId, groupUserName, groupUserPassword);
		if (map.get("code")!=null&&"0".equals(map.get("code").toString())) {
			resultMap.put("code", "0");
			resultMap.put("message", "用户名或密码错误!name:"+groupUserName+"，pw:"+groupUserPassword);			
		}else if(map.get("code")!=null&&"-1".equals(map.get("code").toString())){
			resultMap.put("code", "-1");
			resultMap.put("message", "imageId="+imageId+",影像已下架");	
		}else{
			resultMap.put("code", "1");
		}
		return resultMap;
	}
	
	@Override
	public List<RsImageMetaData> getEdgeDataInfomationByImageid(String imageId,String FileAbsolutePath) {
		Map<String, Object> map	=getEdgeDataInfomationsRmiServer.getEdgeDataInfomationByImageId(imageId, groupUserName, groupUserPassword);
		if (map.get("code")!=null&&map.get("code").equals(0)) {
			logger.info("用户名或密码错误!name:"+groupUserName+"，pw:"+groupUserPassword);
			return null;
		}else{
			return MapTransformRsImageMetaData(map,FileAbsolutePath);
		}
	}
	/**
	* Description: 解析集市元数据并转换为云盒数据
	* @param map 集市元数据
	* @param fileAbsolutePath 保存缩略图的路径
	* @return List:{同一个imageId，不同分辨率的元数据是一致的}
	* @author lishun 
	* @date 2016-5-5 上午10:28:35
	 */
	private List<RsImageMetaData> MapTransformRsImageMetaData(Map<String, Object> map,String fileAbsolutePath) {
		List<RsImageMetaData> list =new ArrayList<RsImageMetaData>();
		String[] resolutions=  map.get("image_resolution").toString().replace("{", "").replace("}", "").split(",");
		String[] image_spectrum_types=  map.get("image_spectrum_type_display").toString().replace("{", "").replace("}", "").split(",");
		String data_id=map.get("data_id").toString();
		String Image_row_col=map.get("image_row_col").toString();
		String Image_satellite_type=map.get("image_satellite_type").toString();
		String Begin_time=map.get("image_take_time").toString();
		String collect_start_time=map.get("image_start_time").toString();
		String Collect_end_time=map.get("image_end_time").toString();
		//String Update_time=map.get("image_product_time").toString();
		//String Image_product_type=map.get("image_product_type").toString();
		String Image_cloudage=map.get("image_cloudage").toString();
		String Sensor_id=map.get("image_sensor_id").toString();
		String Product_level=map.get("image_product_level").toString();
		String Product_id=map.get("image_product_id").toString();
		String name =map.get("name").toString();
		String thumbnail_path =map.get("thumbnail_path").toString();
		//http://www.rscloudmart.com/image//data/GF2/2015/12/14/GF2_PMS1_E116.3_N34.6_20151214_L1A0001244455/GF2_PMS1_E116.3_N34.6_20151214_L1A0001244455_800_800.png
		String thumbnail_image_url=rsmartUrl+thumbnail_path+"_800_800.png";
		downloadThumbnail(thumbnail_image_url,name+"_800_800.png",
				fileAbsolutePath.substring(0,fileAbsolutePath.lastIndexOf(File.separator))+File.separator);
		for (int i = 0; i < resolutions.length; i++) {
			RsImageMetaData rsImageMetaData =new RsImageMetaData();
			String id=UUID.randomUUID().toString();
			rsImageMetaData.setId(id);
			rsImageMetaData.setData_id(data_id);
			rsImageMetaData.setImage_row_col(Image_row_col);
			rsImageMetaData.setImage_satellite_type(Image_satellite_type);
			rsImageMetaData.setImage_start_resolution(Double.valueOf(resolutions[i]));
			rsImageMetaData.setImage_end_resolution(Double.valueOf(resolutions[i]));
			rsImageMetaData.setImage_spectrum_type(image_spectrum_types[i]);
			rsImageMetaData.setBegin_time(Begin_time);
			rsImageMetaData.setCollect_start_time(collect_start_time);
			rsImageMetaData.setCollect_end_time(Collect_end_time);
			rsImageMetaData.setUpdate_time(map.get("image_product_time").toString());
			String range= (String) map.get("range");
			ObjectMapper mapper = new ObjectMapper();
			GeojsonMultiPolygonDto aa = null;
			try {
				aa = mapper.readValue(range, GeojsonMultiPolygonDto.class);
				aa.WritedToTextFormatString();
			} catch (Exception e) {
				e.printStackTrace();
			}
			String rangetext=aa.WritedToTextFormatString();
			rsImageMetaData.setGemo(rangetext);
			StringBuilder area_no_array=new StringBuilder();
			List<Map<String,Object>> mapAdmincode=gtdataDataPutInstorageManagement
					.queryAdmincodeByGeom(rangetext);//获取地区编号
			for (Map<String, Object> map2 : mapAdmincode) {
				area_no_array.append(",").append(map2.get("admincode"));
			}
			rsImageMetaData.setArea_no_array("{"+area_no_array.substring(1)+"}");
			String image_area="ST_Area(ST_GeomFromText('"+rangetext+"'),true)";
			rsImageMetaData.setImage_area(image_area);
			rsImageMetaData.setRange(rangetext);
			rsImageMetaData.setNum(1);
			rsImageMetaData.setIs_cover(0);
			rsImageMetaData.setImage_product_type(map.get("image_product_type").toString());
			rsImageMetaData.setRelation_no("");
			rsImageMetaData.setImage_cloudage(Double.valueOf(Image_cloudage));
			rsImageMetaData.setSensor_id(Sensor_id);
			rsImageMetaData.setProduct_level(Product_level);
			rsImageMetaData.setProduct_id(Product_id);
			rsImageMetaData.setName(name);
			list.add(rsImageMetaData);
		}
		return list;
	}
	/**
	 * 下载集市缩略图
	* Description: 
	* @param urlStr 地址
	* @param fileName 文件名
	* @param savePath  保存位置
	* @return void<br>
	*
	* @author lishun 
	* @date 2016-8-19 下午2:58:44
	 */
	public static void downloadThumbnail(String urlStr,String fileName,String savePath){
		BufferedInputStream bis=null;
		FileOutputStream fos=null;
		try {
			URL url = new URL(urlStr);  
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
			conn.setConnectTimeout(10000);
			conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");//防止屏蔽程序抓取而返回403错误
			bis=new BufferedInputStream(conn.getInputStream());
			File saveDir = new File(savePath);
			if(!saveDir.exists()){
				saveDir.mkdirs();
			}
			File file = new File(saveDir+File.separator+fileName);
			fos = new FileOutputStream(file);  
			byte[] b = new byte[1024];
			int len=0;
			while((len=bis.read(b))!=-1){
				fos.write(b, 0, len);
			}
		} catch (Exception e) {
			logger.info("downloadThumbnail failed by url="+urlStr);
			e.printStackTrace();
		}finally{
			if(bis!=null){ 
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(fos!=null){
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
