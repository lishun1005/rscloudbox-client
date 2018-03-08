package com.cloudbox.mgmt.controller;


import java.io.File;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinarsgeo.rscloudmart.web.dao.entity.DownloadRecord;
import com.chinarsgeo.rscloudmart.web.webservice.ICheckBboxDownloadRecordServer;
import com.chinarsgeo.rscloudmart.web.webservice.IGetEdgeDataInfomationsServer;
import com.cloudbox.mgmt.service.DownloadRecordService;
import com.cloudbox.mgmt.service.GtdataGetDataService;
import com.cloudbox.mgmt.service.HandleAreaImageService;
import com.cloudbox.mgmt.service.ImageDataReceiveService;
import com.cloudbox.utils.FileOP;
import com.cloudbox.utils.PropertyOputils;
import com.cloudbox.utils.RsmartGtdataEncryptionUtil;
import com.cloudbox.utils.SpringContextUtil;
import com.cloudbox.utils.StringTools;
/**
 * 影像数据接收控制器
 * @author lishun
 *
 */
@Controller
public class ImageDataReceiveController extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(ImageDataReceiveController.class);
	@Autowired
	@Qualifier("imageDataReceiveService")
	protected ImageDataReceiveService imageDataReceiveService;

	@Autowired
	@Qualifier("getEdgeDataInfomationsServerRmiService")
	protected IGetEdgeDataInfomationsServer getEdgeDataInfomationsRmiServer;
	@Autowired
	private GtdataGetDataService gtdataGetDataService;
	
	@Value("#{bboxSystemProperties[clientStorageVersion]}")
	private Integer clientStorageVersion;
	
	@Value("#{bboxSystemProperties[FileTempPath]}")
	private String fileTempPath;
	
	@Autowired
	protected  DownloadRecordService downloadrecordServer;

	@Autowired
	protected ICheckBboxDownloadRecordServer checkbboxdownloaddownloadRecordServer;
	
	private HandleAreaImageService handleAreaImageServiceImpl=(HandleAreaImageService) SpringContextUtil.getBean("handleAreaImageService");

	/**
	 * @Description  模拟定时器触发事件。
	 * */
	@RequestMapping("/CheckNewProductsPathlist")
	public String CheckNewProductsPathlist() {
		imageDataReceiveService.imageDataReceive();;
		return "/checkProcess/checkNewProductsPathlist";
	}
	/**
	 * @Description  重新推送数据
	 * step 
	 *  1 删除代理服务器的文件
	 *  2 删除客户端数据库数据
	 *  3 删除客户端本地文件数据
	 *  4 修改后端把该数据置为未下载状态
	 * */
	@RequestMapping("/rePushData")
	@ResponseBody
	public String rePushData(String id) {
		DownloadRecord downloadRecord=downloadrecordServer.queryRecordStutasByRecordid(id);
		if(downloadRecord!=null){
			String fileName=downloadRecord.getName()+".tar.gz";
			String onlinefilePath=downloadRecord.getDownloadfilepath()+File.separator+fileName;
			Map<String,String> map=RsmartGtdataEncryptionUtil.deleteData(onlinefilePath);//删除代理服务器的文件
			logger.info("exception_delete Proxy Server encrypt file,message："+map);
			downloadrecordServer.deleteById(id);
			String FileAbsolutePath=fileTempPath+File.separator +downloadRecord.getName()+File.separator+ fileName;
			File localFile = new File(FileAbsolutePath+".encrypt");
			if(localFile.delete()){
				logger.info("exception_delete local file success"); 
			}else{
				logger.info("exception_delete local file fail"); 
			}
			checkbboxdownloaddownloadRecordServer.updaterecordfordownloadstatus(false, id);
			return "rePushData success";
		}else{
			return "id="+id+" data not exist";
		}
	}
	/**
	 * 
	* Description: 更新云盒后端影像的下载状态
	* @param status
	* @param id  
	* @author lishun 
	* @date 2016年12月8日 上午9:45:05
	 */
	@RequestMapping("/updateImageDataStatus")
	@ResponseBody
	public void updateImageDataStatus(Boolean status,String id){
		int result=checkbboxdownloaddownloadRecordServer.updaterecordfordownloadstatus(status, id);
		logger.info("update result="+result); 
	}
	@RequestMapping("/delData")
	public void delData(String onlinefilePath) {
		System.out.println(onlinefilePath);
		Map<String,String> map=RsmartGtdataEncryptionUtil.deleteData(onlinefilePath);//删除代理服务器的文件
		logger.info("exception_delete Proxy Server encrypt file,message："+map);
	}
	/**
	 * 
	* Description:手动下载集市缩略图 
	* @param imageId
	* @param imageName
	* @return  
	* @return String<br>
	*
	* @author lishun 
	* @date 2016-8-19 下午2:59:19
	 */
	@RequestMapping("/downloadThumbnail")
	@ResponseBody
	public String downloadThumbnail(String imageId,String imageName){
		StringBuilder sb=new StringBuilder();
		String FileAbsolutePath=sb.append(fileTempPath).append(File.separator).append(imageName).append(File.separator).toString();
		handleAreaImageServiceImpl.getEdgeDataInfomationByImageid(imageId, FileAbsolutePath);
		return "success";
	}
	@RequestMapping(value = "/getImage")
	public void getImageFile(String filepath, String name) {
		if (filepath == null || filepath == "" || name == null || name == "") {
		}
		HttpServletResponse httpServletResponse = this.getResponse();
		if (clientStorageVersion == 1) {
			FileOP.getFileStreamInResponse(filepath, name, httpServletResponse,"_800_800.png");
		} else {// 获取文件来自于Gt-data
			gtdataGetDataService.getFileStreamInResponse(filepath, name,httpServletResponse, "_800_800.png");
		}
	}
}
