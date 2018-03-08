package com.cloudbox.mgmt.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinarsgeo.rscloudmart.web.dao.entity.DownloadRecord;
import com.cloudbox.mgmt.service.CheckProcessService;
import com.cloudbox.mgmt.service.DownloadRecordService;
import com.cloudbox.mgmt.thread.CompressProgressMonitorListener;
import com.cloudbox.mgmt.thread.CompressProgressMonitorListenerCollectionUtil;
import com.cloudbox.mgmt.thread.GoldtowersectionCuttingProgressListenerCollextionUtil;
import com.cloudbox.mgmt.thread.GtdataBreakpointResumeDownloadThreadListener;
import com.cloudbox.mgmt.thread.GtdataBreakpointResumeDownloadThreadListenerCollextionUtil;
import com.cloudbox.mgmt.thread.TarGzCompressProgressMonitorListener;
import com.cloudbox.mgmt.thread.TarGzCompressProgressMonitorListenerCollectionUtil;
import com.cloudbox.utils.DateTools;
import com.cloudbox.utils.SpringContextUtil;
import com.cloudbox.utils.StringTools;


/**
 * @author lzw 2015-07-01
 * 
 * @Description   查询符合recordid的相关后台操作进程
 * 
 * @version V1.0
 */

@Controller 
public class DownloadRecordController extends BaseController{
	@Autowired
	private CheckProcessService checkProcessService;
	@Autowired
	@Qualifier("downloadRecordService")
	protected DownloadRecordService downloadRecordService;
	
	protected GtdataBreakpointResumeDownloadThreadListenerCollextionUtil GtdataFileBreakpointResumeThreadListenerCollextionUtil = (GtdataBreakpointResumeDownloadThreadListenerCollextionUtil) SpringContextUtil
			.getBean("GtdataFileBreakpointResumeThreadListenerCollextionUtil");//断点下载线程监控进度工具集
	
	protected TarGzCompressProgressMonitorListenerCollectionUtil TarGzCompressProgressCollectionUtil = (TarGzCompressProgressMonitorListenerCollectionUtil) SpringContextUtil
			.getBean("TarGzCompressProgressCollectionUtil");//targz压缩包解压线程监控进度工具集
	
	protected GoldtowersectionCuttingProgressListenerCollextionUtil goldtowersectionCuttingProgressListenerCollextionUtil = (GoldtowersectionCuttingProgressListenerCollextionUtil) SpringContextUtil
			.getBean("goldtowersectionCuttingProgressListenerCollextionUtil");//targz压缩包解压线程监控进度工具集
	
	@Value("#{bboxSystemProperties[decryptFilePath]}")
	private String decryptFilePath;
	
	/**
	 * 下载解密文件
	 * @param request
	 * @param response
	 * @param fileName
	 * @throws Exception
	 */
	@RequestMapping("/downDecryptionFile")
	public void downfile(HttpServletRequest request,HttpServletResponse response,
			String fileName,String downloadDatalistId,String fileDirectory,String examinationApprovalRecordId) throws Exception{
		if(fileName.lastIndexOf("/")!=-1){
			fileName=fileName.substring(fileName.lastIndexOf("/")+1);
		}
	    Long fileID = ServletRequestUtils.getLongParameter(request, "id", 0);
	    OutputStream fos = null;
	    InputStream fis = null;
	    if(null != fileID){
	    	long downloaderLength=0;//已经下载文件大小
	    	long fileLength=0;//文件大小
	    	String absolutePath=decryptFilePath+File.separator+examinationApprovalRecordId+File.separator+fileName;
		    File downFiles=new File(absolutePath); 
		    fileLength=downFiles.length();
            if(!downFiles.exists()){
            	logger.info("filePath="+absolutePath+"不存在");
                return;
            }
            try {
                fis = new FileInputStream(downFiles);
                fos = response.getOutputStream();
                setFileDownloadHeader(request,response, fileName);
                int byteRead = 0;
                byte[] buffer = new byte[8192];
                while((byteRead=fis.read(buffer,0,8192))!=-1){
                	downloaderLength+=byteRead;
                	fos.write(buffer,0,byteRead);
                }
                fos.close();
            } catch (Exception e) {
            	e.printStackTrace();
            	logger.info("download file failed");
            }
            finally{
            	if(fis!=null) fis.close();
        		if(downloaderLength==fileLength){//下载完毕
        			logger.info("delete file path="+absolutePath);
        			boolean flag=FileUtils.deleteQuietly(new File(decryptFilePath+File.separator+examinationApprovalRecordId));//下载完成后删除解密文件
                	checkProcessService.updateDownloadDataById(downloadDatalistId, 2);//修改为已下载，若想重新下载再走审批流程
                	if(flag){
                		logger.info("delete success");
                	}else{
                		logger.info("delete fail");
                	}
        		}
            }
        }
	 
	} 
	public  void setFileDownloadHeader(HttpServletRequest request,HttpServletResponse response,
			String fileName) {
	    try {
	        //中文文件名支持
	        String encodedfileName = null;
	        String agent = request.getHeader("USER-AGENT");
	        if(null != agent && -1 != agent.indexOf("MSIE")){//IE
	            encodedfileName = java.net.URLEncoder.encode(fileName,"UTF-8");
	        }else if(null != agent && -1 != agent.indexOf("Mozilla")){
	            encodedfileName = new String (fileName.getBytes("UTF-8"),"iso-8859-1");
	        }else{
	            encodedfileName = java.net.URLEncoder.encode(fileName,"UTF-8");
	        }
	        response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedfileName + "\"");
	    } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	    }
	}
	@RequestMapping("/queryDownloadRecord")
	@ResponseBody
	public Map<String, Object> queryDownloadRecord() {
		return downloadRecordService.queryRecordStutas();
	}
	/**
	 * Description：查询数据推送的整体进度。
	 * 
	 * @param recordid
	 *            纪录id
	 * @return map 
	 *            返回推送状态
	 */
	@RequestMapping(value = "/ALLProgressMonitorListener")
	@ResponseBody
	public Map<String, Object> recordALLProgressMonitorListener(String recordid,String name) {
		Map<String, Object> map = new HashMap<String, Object>();
		GtdataBreakpointResumeDownloadThreadListener GtdataFileBreakpointResumeThreadListener = (GtdataBreakpointResumeDownloadThreadListener) GtdataFileBreakpointResumeThreadListenerCollextionUtil
				.getBreakpointResumeThreadListenerMap().get(recordid);
		DownloadRecord  downloadRecord=downloadRecordService
				.queryRecordStutasByRecordid(recordid);
		if (GtdataFileBreakpointResumeThreadListener!=null) {//推送中状态
			map.put("code", "2");
			map.put("PPercentDone", GtdataFileBreakpointResumeThreadListener.getPercentDone());
			map.put("PBeginTime",DateTools.DateToStrchina(GtdataFileBreakpointResumeThreadListener.getBeginTime()));
			map.put("PAveragespeed",GtdataFileBreakpointResumeThreadListener.getAveragespeed());
			if (GtdataFileBreakpointResumeThreadListener.getResttime() != null) {
				map.put("PResttime",GtdataFileBreakpointResumeThreadListener.getResttime());
			}
			map.put("recordid", recordid);
			map.put("name", name);
		}else{
			map =downloadRecordService.queryrecordstutasfordownloadstatus(recordid);
			if (map.get("code").equals("0")) {//recordid查询不到数据,再通过name查找数据
				return map;
			}else {
				List list =  (List) map.get("list");
				Map<String, Object> mapparamter=  (Map<String, Object>) list.get(0);
				Integer downloadstatus=(Integer) mapparamter.get("downloadstatus");
				if (downloadstatus==null||downloadstatus.equals("")) {
					map.clear();
					map.put("recordid", recordid);
					map.put("code", "1");
					map.put("DownloadAveragespeed", GtdataFileBreakpointResumeThreadListenerCollextionUtil.getAveragespeed());
					map.put("CompressAveragespeed", TarGzCompressProgressCollectionUtil.getAveragespeed());
					map.put("CuttingAveragespeed", goldtowersectionCuttingProgressListenerCollextionUtil.getAveragespeed());
					map.put("Msg", "等待中。。");
				}else if (downloadstatus.equals(4)) {
					map.clear();
					map.put("PEndTime", DateTools.DateToStrchina(downloadRecord.getDownloadendtime()));
					map.put("CEndTime", DateTools.DateToStrchina(downloadRecord.getTargzendtime()));
					map.put("CutEndTime", DateTools.DateToStrchina(downloadRecord.getCutendtime()));
					map.put("recordid", recordid);
					map.put("InputStorageEndTime",DateTools.DateToStrchina( downloadRecord.getInputstorageendtime()));
					map.put("TaskEndTime",DateTools.DateToStrchina(downloadRecord.getTaskendtime()));
					map.put("code", "6");
					map.put("Msg", "任务结束！");
				}else if(downloadstatus.equals(0)){
					Map<String,Object> resultMap=getDownrecordStatusByName(name);
					if(resultMap!=null){
						return resultMap;
					}
				}
				map.put("name", name);
			}
		}
		return map;
	}
	/**
	* Description:通过name获取获取推送状态 
	* @param name eq:GF2_PMS2_E113.9_N23.8_20151219_L1A0001253966
	* @return  Map
	* @author lishun 
	* @date 2016-5-11 上午10:07:02
	 */
	public Map<String,Object> getDownrecordStatusByName(String name){
		List<Map<String,Object>> list=downloadRecordService.queryDownrecordByName(name);
		Map<String,Object> resultMap=new HashMap<String, Object>();
		for (Map<String, Object> map : list) {
			String recordIdNew=String.valueOf(map.get("recordid"));
			Integer downloadstatus=(Integer)map.get("downloadstatus");
			DownloadRecord  downloadRecord=downloadRecordService
					.queryRecordStutasByRecordid(recordIdNew);
			if(downloadstatus==4){//若存在name值已经下载完成就使用存在的name值的下载记录信息
				System.out.println("download data");
				resultMap.clear();
				resultMap.put("PEndTime", DateTools.DateToStrchina(downloadRecord.getDownloadendtime()));
				resultMap.put("CEndTime", DateTools.DateToStrchina(downloadRecord.getTargzendtime()));
				if (downloadRecord.getCutendtime()!=null) {
					map.put("CutEndTime", DateTools.DateToStrchina(downloadRecord.getCutendtime()));
				}else {
					map.put("CutEndTime", null);
				}
				resultMap.put("InputStorageEndTime",DateTools.DateToStrchina( downloadRecord.getInputstorageendtime()));
				resultMap.put("TaskEndTime",DateTools.DateToStrchina(downloadRecord.getTaskendtime()));
				resultMap.put("code", "6");
				resultMap.put("recordid", recordIdNew);
				resultMap.put("Msg", "任务结束！");
				resultMap.put("name", name);
				return resultMap;
			}
			GtdataBreakpointResumeDownloadThreadListener GtdataFileBreakpointResumeThreadListener = (GtdataBreakpointResumeDownloadThreadListener) GtdataFileBreakpointResumeThreadListenerCollextionUtil
					.getBreakpointResumeThreadListenerMap().get(recordIdNew);
			if(GtdataFileBreakpointResumeThreadListener!=null){//若在下载中的话，就返回存在的name值的下载状态
				System.out.println("other data");
				resultMap.clear();
				resultMap.put("code", "2");
				resultMap.put("PPercentDone", GtdataFileBreakpointResumeThreadListener.getPercentDone());
				resultMap.put("PBeginTime",DateTools.DateToStrchina(GtdataFileBreakpointResumeThreadListener.getBeginTime()));
				resultMap.put("PAveragespeed",GtdataFileBreakpointResumeThreadListener.getAveragespeed());
				if (GtdataFileBreakpointResumeThreadListener.getResttime() != null) {
					resultMap.put("PResttime",GtdataFileBreakpointResumeThreadListener.getResttime());
				}
				resultMap.put("name", name);
				resultMap.put("recordid", recordIdNew);
				return resultMap;
			}
		}
		return null;
	}
	/**
	 * Description：测试，可删除。。zip4j解压进度查询。
	 * 
	 * @param recordid
	 *            纪录id
	 * @return map 
	 *            返回recordid解压进度状态
	 */
	@RequestMapping(value = "/CompressProgressMonitorListener")
	@ResponseBody
	public Map<String, Object> CompressProgressMonitorListener(String recordid) {
		Map<String, Object> map = new HashMap<String, Object>();
		CompressProgressMonitorListenerCollectionUtil CompressProgressCollectionUtil = (CompressProgressMonitorListenerCollectionUtil) SpringContextUtil.getBean("CompressProgressCollectionUtil");
		map.put("Averagespeed",CompressProgressCollectionUtil.getAveragespeed());
		CompressProgressMonitorListener ProgressMonitorListern = (CompressProgressMonitorListener) CompressProgressCollectionUtil
				.getProgressMonitorListernMap().get(recordid);
		if (ProgressMonitorListern!=null) {
			map.put("code", "1");
			map.put("PAveragespeed", ProgressMonitorListern.getAveragespeed());
			map.put("Resttime", ProgressMonitorListern.getResttime() / 1000+ "秒" + ProgressMonitorListern.getResttime() % 1000 + "毫秒");
		}else {
			map.put("code", "0");
			map.put("Msg", "该解压线程已结束！");
		}
		return map;
	}
	
	/**
	 * Description：tar.gz解压进度查询。
	 * 
	 * @param recordid
	 *            纪录id
	 * @return map 
	 *            返回recordid解压进度状态
	 */
	@RequestMapping(value = "/TarGzCompressProgressMonitorListener")
	@ResponseBody
	public Map<String, Object> TarGzCompressProgressMonitorListener(String recordid) {
		Map<String, Object> map = new HashMap<String, Object>();
		TarGzCompressProgressMonitorListenerCollectionUtil TarGzCompressProgressCollectionUtil = (TarGzCompressProgressMonitorListenerCollectionUtil) SpringContextUtil.getBean("TarGzCompressProgressCollectionUtil");
		TarGzCompressProgressMonitorListener TarGzCompressProgressMonitorListener = (TarGzCompressProgressMonitorListener) TarGzCompressProgressCollectionUtil.getTarGzprogressMonitorListernMap().get(recordid);
		if (TarGzCompressProgressMonitorListener!=null) {
			map.put("code", "1");
			map.put("PAveragespeed", TarGzCompressProgressMonitorListener.getAveragespeed());
			map.put("PPercentDone", TarGzCompressProgressMonitorListener.getPercentDone());
			map.put("Resttime", TarGzCompressProgressMonitorListener.getResttime() / 1000+ "秒" + TarGzCompressProgressMonitorListener.getResttime() % 1000 + "毫秒");
		}else {
			map.put("code", "0");
			map.put("Averagespeed", TarGzCompressProgressCollectionUtil.getAveragespeed());
			map.put("Msg", "该解压线程已结束！");
		}
		return map;
	}
	
	/**
	 * Description：监控下载进度。
	 * 
	 * @param recordid
	 *            纪录id
	 * @return map 
	 *            返回recordid下载进度状态
	 */
	@RequestMapping(value = "/FiledownloadBreakpointResumeThreadListener")
	@ResponseBody
	public Map<String, Object> FileBreakpointResumeThreadListener(String recordid) {
		Map<String, Object> map = new HashMap<String, Object>();
		GtdataBreakpointResumeDownloadThreadListenerCollextionUtil GtdataFileBreakpointResumeThreadListenerCollextionUtil = (GtdataBreakpointResumeDownloadThreadListenerCollextionUtil) SpringContextUtil.getBean("GtdataFileBreakpointResumeThreadListenerCollextionUtil");
		map.put("Averagespeed",GtdataFileBreakpointResumeThreadListenerCollextionUtil.getAveragespeed());
		GtdataBreakpointResumeDownloadThreadListener GtdataFileBreakpointResumeThreadListener = (GtdataBreakpointResumeDownloadThreadListener) GtdataFileBreakpointResumeThreadListenerCollextionUtil.getBreakpointResumeThreadListenerMap().get(recordid);
		if (GtdataFileBreakpointResumeThreadListener!=null) {
			map.put("code", "1");
			map.put("PAveragespeed",GtdataFileBreakpointResumeThreadListener.getAveragespeed());
			map.put("PPercentDone",GtdataFileBreakpointResumeThreadListener.getPercentDone());
			if (GtdataFileBreakpointResumeThreadListener.getResttime() != null) {
				map.put("Resttime",GtdataFileBreakpointResumeThreadListener.getResttime()/ 1000+ "秒"+ GtdataFileBreakpointResumeThreadListener.getResttime() % 1000 + "毫秒");
			}
		}else {
			map.put("code", "0");
			map.put("Msg", "该断点下载线程已结束！");
		}
		return map;
	}
	
}
