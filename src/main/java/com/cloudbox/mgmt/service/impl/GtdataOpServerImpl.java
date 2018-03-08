package com.cloudbox.mgmt.service.impl;

import java.io.File;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.cloudbox.mgmt.dao.GtdataDataPutInstorageRespository;
import com.cloudbox.mgmt.old.entity.RsImageMetaData;
import com.cloudbox.mgmt.service.GtdataOpServer;
import com.cloudbox.utils.GtdataFileUtil;
import com.cloudbox.utils.StringTools;

@Service("gtdataopserver")
public class GtdataOpServerImpl implements GtdataOpServer  {

	private static Logger logger = LoggerFactory.getLogger(GtdataOpServerImpl.class);
	
	
	@Value("#{bboxSystemProperties[gtDataUsername]}")
	public String gtDataUsername;
	
	@Value("#{imageCorrectProperty[baseUrl]}")
	public String imageCorrectUrl;
	
	@Value("#{imageCorrectProperty[callbackUrl]}")
	public String imageCorrectcallbackUrl;
	
	@Value("#{goldtowersectioncutting[baseUrl]}")
	public String goldtowersectionUrl;
	
	@Value("#{goldtowersectioncutting[callbackUrl]}")
	public String goldtowersectioncallbackUrl;
	
	@Value("#{bboxSystemProperties[Gtdatastorage]}")
	private String gtdatastorage;
	
	@Value("#{bboxSystemProperties[FileTempPath]}")
	private String fileTempPath;
	
	@Autowired
	@Qualifier("restTemplate")
	private RestTemplate restTemplate;
	
	@Autowired
	private GtdataDataPutInstorageRespository gtdatadataputinstoragemanagement;
	@Value("#{bboxSystemProperties[clientStorageVersion]}")
	private Integer clientStorageVersion;
	
    private  Map<String, Object> map;
    
	/**
	 * Description：Gtdata图像校正接口实现
	 * @param RsImageMetaData 影像信息类
	 */
    @Override
	public  Map<String,Object>  correctPic(RsImageMetaData RsImageMetaData){
		String inPath  =RsImageMetaData.getFile_path() +"/"+RsImageMetaData.getName() +".jpg";  //原图在gtdata的相对路径
		String outPath =RsImageMetaData.getFile_path() +"/"+ RsImageMetaData.getName()+"_800_800.png";    //处理图片结果请求路径
		String range=RsImageMetaData.getRange();
		range=range.replace("POLYGON((", "").replace("))", "");
		String[] pointListArray=range.split(",");
		String leftTop = pointListArray[0];          //左上点坐标
		String leftDown = pointListArray[1];     //左下点坐标
		String rightDown = pointListArray[2];  //右下点坐标
		String rightTop = pointListArray[3];       //右上点坐标
		
		String topLeftLng = leftTop.split(" ")[0]; //左上经度
		String topLeftLat  = leftTop.split(" ")[1]; //左上纬度
		
		String bottomLeftLng=leftDown.split(" ")[0];  //左下经度
		String bottomLeftLat=leftDown.split(" ")[1];  //左下纬度
		
		String bottomRightLng=rightDown.split(" ")[0];  //右下经度
		String bottomRightLat=rightDown.split(" ")[1];  //右下纬度
		
		String topRightLng=rightTop.split(" ")[0];//右上经度
		String topRightLat =rightTop.split(" ")[1];  //右上纬度
		String resourceUrl = MessageFormat.format(
				"{0}?op=WARPGCP&inpath={1}&outpath={2}&"+
		        "TopLeftLat={3}&TopLeftLng={4}&TopRightLat={5}&TopRightLng={6}&"+
				"BottomRightLat={7}&BottomRightLng={8}&BottomLeftLat={9}&BottomLeftLng={10}&callbackUrl={11}", imageCorrectUrl,gtDataUsername+"/"+inPath,gtDataUsername+"/"+outPath,topLeftLat,topLeftLng,
				topRightLat,topRightLng,bottomRightLat,bottomRightLng,bottomLeftLat,bottomLeftLng,imageCorrectcallbackUrl);
		System.out.println(resourceUrl);
		map= restGet(resourceUrl);
		return map;
	}
    /**
	 * Description：Gtdata正射影像并行切割接口实现
	 * @param 查看接口文档
	 */
    @Override
	public Map<String, Object> goldtowersectionCuttingbydefaultresolution(String inPath,Integer max_layers,String mapname,boolean watermark) {
    	String inpath =gtDataUsername+"/"+inPath;
		String url = MessageFormat.format("{0}?op=CUTTING&inpath={1}&max_layers={2}&mapname={3}&is_rgb={4}&rgb_project={5}",goldtowersectionUrl,inpath,max_layers,mapname,true,"WGS84");
		System.out.println(url);
		map = restGet(url);
		return map;
	}
    
    /**
	 * Description：Gtdata正射影像并行切割接口实现
	 * @param 查看接口文档
	 */
	@Override
	public Map<String, Object> goldtowersectionCutting(String inPath,Integer max_layers,String mapname,double max_resolution,double  min_resolution,
			boolean watermark) {
		String inpath =gtDataUsername+"/"+inPath;
		String url = MessageFormat.format("{0}?op=CUTTING&inpath={1}&max_layers={2}&" +
				"mapname={3}&max_resolution={4}&min_resolution={5}&watermark={6}" +
				"&callbackUrl={7}",goldtowersectionUrl,inpath,max_layers,mapname,max_resolution,min_resolution,watermark,goldtowersectioncallbackUrl);
		map = restGet(url);
		return map;
	}
	
	@Override
	public Map<String, Object> querygoldtowersectionCuttingProgress(String jobid) {
		String url = MessageFormat.format("{0}/{1}?op=PROGRESS",goldtowersectionUrl,jobid);
		System.out.println( url);
		map = restGet(url);
		return map;
	}
    
	/**
	 * Description：rest方式Get请求
	 * @param resourceUrl
	 * @return
	 *
	 */
	protected Map<String, Object> restGet(String resourceUrl) {
		MultiValueMap<String, Object> parameter = new LinkedMultiValueMap<String,Object>();
		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String, Object>>(parameter);
		return rest(resourceUrl, HttpMethod.GET, httpEntity);
	}
	
	/**
	 * Description：rest方式请求
	 * @param resourceUrl
	 * @param httpMethod
	 * @param httpEntity
	 * @return
	 *
	 */
	protected <T> Map<String, Object> rest(String resourceUrl, HttpMethod httpMethod, HttpEntity<T> httpEntity) {
		try {
			ResponseEntity<HashMap> responseEntity = restTemplate.exchange(resourceUrl, httpMethod, httpEntity,
					HashMap.class);

			Map<String, Object> success = new HashMap<String, Object>();
			success.put("HttpStatusCode", 200);// 调用成功
			if (responseEntity.getBody() != null && !responseEntity.getBody().isEmpty()) {
				success.putAll(responseEntity.getBody());
			}
			return success;
		} catch (HttpStatusCodeException e) {
			e.printStackTrace();
			logger.info("call imagecut rest api ["+resourceUrl+"] throw exception: http status code "
					+e.getStatusCode().value()+", response body "+e.getResponseBodyAsString());
			Map<String, Object> error = new HashMap<String, Object>();
			error.put("HttpStatusCode", e.getStatusCode().value());// 调用失败
			if (e.getResponseBodyAsString() != null && !"".equals(e.getResponseBodyAsString())) {
				System.out.println(e.getResponseBodyAsString());
			}
			return error;
		}
	}
	/**
	 * Description：Gtdata图像校正回调接口实现
	 * @param jobId 任务id
	 * @param status 状态
	 */
	@Override
	public void recordCorrectResult(String jobId, String status) {
		if (status.equals("success")) {
			gtdatadataputinstoragemanagement.updaterecordbyjobId(jobId);
		}
	}
	
	@Override
	public void InPutInstorage(List list,String record,String fileAbsolutePath) {
		Map<String, Object> map2 =new HashMap<String, Object>();
		for (int i = 0; i < list.size(); i++) {
			RsImageMetaData rsImageMetaData=(RsImageMetaData) list.get(i);
			if (clientStorageVersion==0) {//客户端存储版本 0：gtdata版本(影像数据存储在gtdata) 1:本地版(影像数据存储在本地)
				try {//Gt-data创建上传目录,当目录存在时视为更新数据,删除重新上传
					map2 = GtdataFileUtil.createNewDirectory(gtdatastorage, rsImageMetaData.getName());
					if (!map2.get("result").equals(1)) {
						logger.info("Gtdata初次尝试创建文件失败！");
						GtdataFileUtil.deleteDirectoryOrFile(gtdatastorage+rsImageMetaData.getName());
						map2.clear();
						map2 = GtdataFileUtil.createNewDirectory(gtdatastorage, rsImageMetaData.getName());
						if (!map2.get("result").equals(1)) {
							logger.info("Gtdata第二次尝试创建文件失败，抱错！");
						}
					}
					File tempFile = new File(fileTempPath + File.separator + rsImageMetaData.getName());
					File[] files = tempFile.listFiles();
					for (int j = 0; j < files.length; j++) {
							map2 = GtdataFileUtil.uploadFile(gtdatastorage + rsImageMetaData.getName() + "/" + files[j].getName(), files[j]);
							if (!map2.get("result").equals(1)) {
								GtdataFileUtil.deleteDirectoryOrFile(gtdatastorage + rsImageMetaData.getName() + "/" + files[j].getName());
								map2.clear();
								map2 = GtdataFileUtil.uploadFile(gtdatastorage + rsImageMetaData.getName() + "/" + files[j].getName(), files[j]);					
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			rsImageMetaData.setFile_path(fileAbsolutePath.substring(0,fileAbsolutePath.lastIndexOf(File.separator)));
			rsImageMetaData.setRecord_id(record);
			rsImageMetaData.setArea_no("123123");
			rsImageMetaData.setIscorrect(true);
			rsImageMetaData.setAuto_tag(1);//默认是自动推送
			gtdatadataputinstoragemanagement.dataInputRecord(rsImageMetaData);
		}
	}
}
