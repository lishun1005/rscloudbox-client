package com.cloudbox.mgmt.thread;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.http.ConnectionClosedException;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.chinarsgeo.rscloudmart.web.dao.entity.DownloadRecord;
import com.chinarsgeo.rscloudmart.web.webservice.ICheckBboxDownloadRecordServer;
import com.cloudbox.mgmt.old.entity.RsImageMetaData;
import com.cloudbox.mgmt.service.DownloadRecordService;
import com.cloudbox.mgmt.service.GtdataOpServer;
import com.cloudbox.mgmt.service.HandleAreaImageService;
import com.cloudbox.utils.FileOP;
import com.cloudbox.utils.PropertyOputils;
import com.cloudbox.utils.RsmartGtdataEncryptionUtil;
import com.cloudbox.utils.SpringContextUtil;
import com.cloudbox.utils.StringTools;
/**
 * @author lzw 2015-06-19
 * 
 * @Description  断点续传，数据入库线程。 当数据未下载完成时，执行断点续传，当数据下载完成后，执行解压入库程序
 * 
 * @version V1.2
 * 
 * @updatatime 2015-12-25
 */
public class GtdataBreakpointResumeDownloadThread extends Thread {
	private static Logger logger = LoggerFactory.getLogger(GtdataBreakpointResumeDownloadThread.class);
	private Boolean runStatus;
	private int retryCount=1;
	
	public synchronized Boolean getRunStatus() {
		return runStatus;
	}

	private DownloadRecord downloadRecord;

	
	
	public synchronized DownloadRecord getDownloadRecord() {
		return downloadRecord;
	}

	protected ICheckBboxDownloadRecordServer checkbboxdownloaddownloadRecordServer	 = (ICheckBboxDownloadRecordServer) SpringContextUtil.getBean("checkbboxdownloadrecordServerRmiService");

	protected DownloadRecordService downloaddownloadRecordstutasServer = (DownloadRecordService) SpringContextUtil.getBean("downloadRecordService");
	
	private GtdataOpServer gtdatadataputinstorageserver=(GtdataOpServer) SpringContextUtil.getBean("gtdataopserver");
	protected GtdataBreakpointResumeDownloadThreadListenerCollextionUtil GtdataFileBreakpointResumeThreadListenerCollextionUtil = (GtdataBreakpointResumeDownloadThreadListenerCollextionUtil) SpringContextUtil
			.getBean("GtdataFileBreakpointResumeThreadListenerCollextionUtil");//断点下载线程监控进度工具集
	
	private Integer clientStorageVersion=Integer.valueOf(((PropertyOputils) SpringContextUtil.getBean("systemProperty")).getKeyValue("clientStorageVersion"));
	
	private HandleAreaImageService handleAreaImageService=(HandleAreaImageService) SpringContextUtil.getBean("handleAreaImageService");
	
	public GtdataBreakpointResumeDownloadThread(DownloadRecord downloadRecord) {
		this.downloadRecord = downloadRecord;
		this.runStatus = true;
	}

	@Override
	public void run() {
		Map<String,String> checkImage=handleAreaImageService
				.checkDataInfomationIsSell(downloadRecord.getImage_product_id());
		if("1".equals(checkImage.get("code"))){
			String fileName=downloadRecord.getName()+".tar.gz";//数据名
			String onlinefilePath=downloadRecord.getDownloadfilepath()+File.separator+fileName;//下载地址
			Map<String, String> resultMap=RsmartGtdataEncryptionUtil//请求加密数据
					.prepareData(onlinefilePath);
			if (resultMap!=null&&"1".equals(resultMap.get("result"))){//encryption success,request download
				logger.info("{}/{}.tar.gz prepareData done",downloadRecord.getDownloadfilepath(),downloadRecord.getName());
				while (getRunStatus()) {
					try {
						String FileTempPath=((PropertyOputils) SpringContextUtil.getBean("systemProperty")).getKeyValue("FileTempPath");
						String FileAbsolutePath=FileTempPath+File.separator +downloadRecord.getName()+File.separator+ fileName;
						FileBreakpointResume(onlinefilePath, FileAbsolutePath, fileName);//下载数据
					} catch (Exception e) {
						e.printStackTrace();
					}  
				}
			}else{
				logger.info("request encryption fail:{}",resultMap);//请求加密失败，等待定时器重新加入线程池
			}
		}else{//集市影像数据已下架或已删除
			logger.info(checkImage.get("message"));
			List<Map<String,Object>> recodrIdList=downloaddownloadRecordstutasServer//获取该name对应的所有推送记录
					.queryDownrecordByName(downloadRecord.getName());
			for (Map<String, Object> mapTemp : recodrIdList) {
				checkbboxdownloaddownloadRecordServer//使用rmi通知云盒后端将该name所对应的所有推送记录设置为已下载
						.updaterecordfordownloadstatus(true, String.valueOf(mapTemp.get("recordid")));
			}
			downloaddownloadRecordstutasServer.deleteByName(downloadRecord.getName());
		}
		
		
	}
	/**
	 * 
	* Description: 断点续传
	* @param onlinefilePath 中转服务器文件路径
	* @param FileAbsolutePath 本地文件保存路径
	* @param key  文件名
	* @author lishun 
	* @date 2016-5-5 上午10:47:34
	 */
	private void FileBreakpointResume(String onlinefilePath, String FileAbsolutePath,String key) {
		RandomAccessFile randomAccessFile = null;//随机访问文件
		GtdataBreakpointResumeDownloadThreadListener listener=null;//监听数据下载的进度
		InputStream  inputStream=null;
		BufferedInputStream bs=null;
		File localFile=null;//保存到本地的文件
		try {
			String FileParentPath=FileAbsolutePath.substring(0,FileAbsolutePath.lastIndexOf(File.separator));//本地文件保存的文件夹路径
			File filefolder=new File(FileParentPath);//如果目录不存在则创建该目录
			if (!filefolder.exists()) {
				filefolder.mkdirs();
			}
			localFile = new File(FileAbsolutePath+".encrypt");
			randomAccessFile = new RandomAccessFile(localFile, "rws");//下载到本地的加密文件
			Long netFileLenght = RsmartGtdataEncryptionUtil.getDownDataSize(onlinefilePath);//获取代理服务器上文件的大小
			logger.info("o_file:onlinefilePath={}",onlinefilePath);
			Long localFileLenght = localFile.length();//获取本地已下载的文件大小
			if(netFileLenght==-1){//文件未加密或文件不存在
				logger.info("downloadRecordId={},name={};please prepareData first",downloadRecord.getId(),downloadRecord.getName());
				deleteProxyFileAndLocalFile(onlinefilePath,localFile);
				this.runStatus=false;
				return;
			}else if(netFileLenght==-2){
				logger.info("d_encrypting:downloadRecordId={},name={};data encrypting...sleep 10s",downloadRecord.getId(),downloadRecord.getName());//文件加密中。。。
				Thread.sleep(10000);
				return;
			}else if(netFileLenght==-3){//超时，等待
				logger.info("connect timeout,sleep 10s");//请求中转服务器超时
				Thread.sleep(10000);
				return;
			}
			downloaddownloadRecordstutasServer.updateRecordStutasFordownloadsize(netFileLenght, downloadRecord.getName());
			if (localFileLenght < netFileLenght) {//如果还未下载完的则继续下载
				downloaddownloadRecordstutasServer.updateRecordStutasFordownloadstatus(0, downloadRecord.getName());//更新状态为下载中
				listener=new GtdataBreakpointResumeDownloadThreadListener(netFileLenght,localFileLenght,downloadRecord.getId());//监听器，实时监听下载进度
				listener.setName("listener_"+downloadRecord.getName());
				Map<String, Object> returnMap=RsmartGtdataEncryptionUtil.getDownDataStreamMap(onlinefilePath,localFileLenght);//请求代理服务器进行下载，文件指针从localFileLenght后面开始，实现断点续传
				if (returnMap!=null && "1".equals(returnMap.get("result"))) {
					logger.info("d_starting:name={},download starting",downloadRecord.getName());
					inputStream=(InputStream) returnMap.get("inputStream");//获得输入流
					bs=new BufferedInputStream(inputStream);
					if (inputStream!=null) {
						randomAccessFile.seek(localFileLenght);//将本地的文件指针也定位到localFileLenght后面一位，也就是从最后面写起
						listener.start();//开启监听器
						int b = 0;
						byte buffer[] = new byte[2048];
						while ((b = inputStream.read(buffer)) != -1) {
							randomAccessFile.write(buffer, 0, b);//将数据入流的数据写入本地文件
							listener.addWorkCompleted(b);
						}
						logger.info("d_done:name={},dowmload done",downloadRecord.getName());
						downloaddownloadRecordstutasServer //下载完成，往数据库里写入下载完成的时间
						.updateDownloadEndTime(new Timestamp(System.currentTimeMillis()).toString(), downloadRecord.getName());
					}else {
						logger.info("return inputStream is null");
						return;
					}
				}else{
					logger.info("getFile failed:{}",returnMap);
				}
			} else if (localFileLenght.longValue()==netFileLenght.longValue()) {//已经完成下载
				logger.info("start input metadata..");
				randomAccessFile.close();
				insertMetadata(FileAbsolutePath, filefolder, onlinefilePath, key);//往数据库记录数据的具体信息
			} else {
				deleteProxyFileAndLocalFile(onlinefilePath,localFile);
				logger.info("fatal error:localFileLenght >netFileLenght or ");
			}
			
		}catch (SocketTimeoutException e) {//服务器响应超时
			logger.info("retry reason:{}:{}",e.getClass().getSimpleName(),e.getMessage());
			retryDownload(onlinefilePath,localFile,listener);
		}catch (ConnectTimeoutException e) {//连接超时
			logger.info("retry reason:{}:{}",e.getClass().getSimpleName(),e.getMessage());
			retryDownload(onlinefilePath,localFile,listener);
		}catch (ConnectionClosedException e) {//服务器server数据读写错误
			logger.info("retry reason:{}:{}",e.getClass().getSimpleName(),e.getMessage());
			retryDownload(onlinefilePath,localFile,listener);
		}catch (Exception e) {
			if(e instanceof HttpHostConnectException || e instanceof SocketException){//网络中断
				try {
					logger.info("message：network interrupt;sleeping 60s,{}",e.getMessage());
					Thread.sleep(60000);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}else{//其他异常
				e.printStackTrace();
				this.runStatus=false;
				logger.info("message:{},{},other exception",downloadRecord.getId(),downloadRecord.getName());
				deleteProxyFileAndLocalFile(onlinefilePath,localFile);
			}
		}finally {
			try {
				if (randomAccessFile != null) {
					randomAccessFile.close();
				}
				if(inputStream!=null){
					bs.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	* Description:重试下载，重试五次，第一次10s后重试,后面重试每次间隔时间递增10s,
	* @param onlinefilePath
	* @param localFile  
	* @return void<br>
	* @author lishun 
	* @date 2016-11-10 下午2:23:35
	 */
	public void retryDownload(String onlinefilePath,File localFile,GtdataBreakpointResumeDownloadThreadListener listener){
		if(listener!=null){
			listener.setRun(false);//stop downloadListener 
		}
		try {
			switch (retryCount) {
				case 1: Thread.sleep(10000);
					logger.info("httpConnectClose_retry download after 10 second");break;
				case 2: Thread.sleep(20000);
					logger.info("httpConnectClose_retry download after 20 second");break;
				case 3: Thread.sleep(30000);
					logger.info("httpConnectClose_retry download after 30 second");break;
				case 4: Thread.sleep(40000);
					logger.info("httpConnectClose_retry download after 40 second");break;
				case 5: Thread.sleep(50000);
					logger.info("httpConnectClose_retry download after 50 second");break;
				default:this.runStatus=false;
					deleteProxyFileAndLocalFile(onlinefilePath,localFile);
					logger.info("retry download 5 times fail,delete file");
					break;
			}
			retryCount++;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	* Description: 重试失败,删除文件
	* @param onlinefilePath
	* @param localFile  
	* @author lishun 
	* @date 2016-8-16 上午10:37:35
	 */
	public void deleteProxyFileAndLocalFile(String onlinefilePath,File localFile){
		downloaddownloadRecordstutasServer.deleteByName(downloadRecord.getName());
		if(localFile.delete()){
			logger.info("exception_delete local file success"); 
		}else{
			logger.info("exception_delete local file fail"); 
		}
		Map<String,String> map=RsmartGtdataEncryptionUtil.deleteData(onlinefilePath);
		if("0".equals(map.get("result"))){//添加删除代理服务器文件失败记录
			downloaddownloadRecordstutasServer.addFileDeleteException(onlinefilePath, downloadRecord.getName(), 1);
		}
		logger.info(map.toString());
	}
	/**
	 * 
	* Description: 请求集市的元数据入库到云盒客户端<br>
	* step:<br>
	* 	 	1:获取边部数据getEdgeDataInfomationByImageid <br>
	* 		2:入库 InPutInstorage<br>
	* 		3:更新云盒后端文件下载状态 updatedownloadRecordfordownloadstatus<br>
	* 		4:修改推送状态，推送时间。。。<br>
	* 		5:删除代理服务器的加密数据 deleteData<br>
	* @param FileAbsolutePath 本地文件保存路径
	* @param filefolder 本地文件保存的文件夹对象
	* @param onlinefilePath 中转服务器文件路径
	* @param key  文件名  
	* @author lishun 
	* @date 2016-5-5 上午10:19:08
	 */
	public void insertMetadata(String FileAbsolutePath,File filefolder,String onlinefilePath,String key){
		try {
			downloaddownloadRecordstutasServer.updateRecordStutasByName(4, downloadRecord.getName());//设置下载状态为完成
			List<RsImageMetaData> list	=	handleAreaImageService//根据产品id获取影像的具体信息
					.getEdgeDataInfomationByImageid(downloadRecord.getImage_product_id(),FileAbsolutePath);
			if(list!=null){
				gtdatadataputinstorageserver.InPutInstorage(list,downloadRecord.getId().toString(),FileAbsolutePath);//将获得的影像信息插入到数据库中
				List<Map<String,Object>> recodrIdList=downloaddownloadRecordstutasServer//获取该name对应的所有推送记录
						.queryDownrecordByName(downloadRecord.getName());
				for (Map<String, Object> mapTemp : recodrIdList) {
					int flag=checkbboxdownloaddownloadRecordServer//使用rmi通知云盒后端将该name所对应的所有推送记录设置为已下载
							.updaterecordfordownloadstatus(true, String.valueOf(mapTemp.get("recordid")));
					if(flag>0){
						logger.info("update bboxBackup downloadRecord downloadStatus success by id={}",downloadRecord.getId());
					}else{
						logger.info("update bboxBackup downloadRecord downloadStatus failed by id={}",downloadRecord.getId());
					}
				}
				downloaddownloadRecordstutasServer//更新解压完成的时间
					.updateTargzEndTime(new Timestamp(System.currentTimeMillis()).toString(), downloadRecord.getName());
				downloaddownloadRecordstutasServer//更新入库完成的时间
					.updateInputstorageEndTime(new Timestamp(System.currentTimeMillis()).toString(), downloadRecord.getName());
				downloaddownloadRecordstutasServer//更新任务结束时间
				.updateTaskEndTime(new Timestamp(System.currentTimeMillis()).toString(), downloadRecord.getName());
				
				downloaddownloadRecordstutasServer.updateDownloadEndTime(new Timestamp(System.currentTimeMillis()).toString(), downloadRecord.getName());//更新下载完成时间
			}else{
				logger.info("云盒后端未找到产品:imageid={}的数据，未能入库数据到客户端",downloadRecord.getImage_product_id().toString());
			}
			if (clientStorageVersion==0) {
				logger.info("Current version is gtdata version;delete local tempFile;{}",filefolder);
				FileOP.deleteAll(filefolder);
			}
		} catch (Exception e) {
			logger.info("input metadata fail;detele file"); 
			downloaddownloadRecordstutasServer.deleteByName(downloadRecord.getName());
			File localFile = new File(FileAbsolutePath+".encrypt");
			if(localFile.delete()){
				logger.info("exception_delete local file success"); 
			}else{
				logger.info("exception_delete local file fail"); 
			}
			e.printStackTrace();
		}finally{
			Map<String,String> map=RsmartGtdataEncryptionUtil.deleteData(onlinefilePath);//删除代理服务器上的加密数据
			if("0".equals(map.get("result"))){//添加删除代理服务器文件失败记录
				downloaddownloadRecordstutasServer.addFileDeleteException(onlinefilePath, downloadRecord.getName(), 1);
			}
			logger.info(map.toString());
			this.runStatus=false;//任务完成，结束循环
		}
		
	}
	
	@Override
	public synchronized boolean equals(Object obj) {
	  if(!(obj instanceof GtdataBreakpointResumeDownloadThread)){
			return false;
		}else{
			GtdataBreakpointResumeDownloadThread p = (GtdataBreakpointResumeDownloadThread) obj;
			return  this.downloadRecord.getId().equals(p.getDownloadRecord().getId()) ;
		}
	}
}
