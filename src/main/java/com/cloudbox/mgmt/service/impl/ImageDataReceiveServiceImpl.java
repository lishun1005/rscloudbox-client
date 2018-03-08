package com.cloudbox.mgmt.service.impl;

import java.io.File;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.chinarsgeo.rscloudmart.web.dao.entity.DownloadRecord;
import com.chinarsgeo.rscloudmart.web.webservice.ICheckBboxDownloadRecordServer;
import com.cloudbox.mgmt.service.DownloadRecordService;
import com.cloudbox.mgmt.service.ImageDataReceiveService;
import com.cloudbox.mgmt.thread.DowmloadThreadPoolExecutor;
import com.cloudbox.mgmt.thread.GtdataBreakpointResumeDownloadThread;
import com.cloudbox.utils.Base64;
import com.cloudbox.utils.FileEncryptionUtils;
import com.cloudbox.utils.PropertyOputils;
import com.cloudbox.utils.RSAEncrypt;
import com.cloudbox.utils.RsmartGtdataEncryptionUtil;
import com.cloudbox.utils.SpringContextUtil;
import com.cloudbox.utils.StringTools;

@Service("imageDataReceiveService")
public class ImageDataReceiveServiceImpl implements ImageDataReceiveService {
private static Logger logger = LoggerFactory.getLogger(ImageDataReceiveServiceImpl.class);
	
	private static  DowmloadThreadPoolExecutor pool;
	@Autowired
	@Qualifier("checkbboxdownloadrecordServerRmiService")
	protected  ICheckBboxDownloadRecordServer checkbboxdownloadrecordServer;
	@Autowired
	protected  DownloadRecordService downloadrecordstutasServer;
	@Autowired
	protected ICheckBboxDownloadRecordServer checkbboxdownloaddownloadRecordServer;
	/**
	 * 云盒用户名
	 */
	protected static String groupUserName;
	protected static String groupUserPassword;
	/**
	 * 加密文件保存路径
	 */
	
	@Value("#{bboxSystemProperties[decryptFilePath]}")
	private String decryptFilePath;
	
	@Value("#{bboxSystemProperties[startUseDateStr]}")
	private String startUseDateStr;
	
	
	/**
	 * @Description 初始化用户名、密码。
	 * 
	 **/
	static {
		String file = "Rsmartgtcloudos";
		try {
			groupUserName = new String(RSAEncrypt.decrypt(RSAEncrypt.loadPublicKeyByStrDefault(), Base64.decode(StringTools.getValueFromProperties(file, "groupUserName"))));
			groupUserPassword = new String(RSAEncrypt.decrypt(RSAEncrypt.loadPublicKeyByStrDefault(), Base64.decode(StringTools.getValueFromProperties(file, "groupUserPassword"))));
			int maximumPoolSize=Integer.valueOf(((PropertyOputils) SpringContextUtil.getBean("systemProperty")).getKeyValue("maximumPoolSize"));
			pool=new DowmloadThreadPoolExecutor(maximumPoolSize, maximumPoolSize,
					60L,TimeUnit.SECONDS,new LinkedBlockingQueue <Runnable>()); //线程池,设置并发线程数目
		}catch(NumberFormatException e){
			logger.info("配置文件gtcloudos.properties的maximumPoolSize必须为正整数");
			e.printStackTrace();
			throw e;
		}catch (Exception e) {
			e.printStackTrace();
		}
		Thread threadPoolTaskQueueListener=new Thread(){//每30秒轮询一次，打印出线程池中的等待线程以及运行线程数量
			public void run() {
				while(true){
					try {
						int taskWorkingSize=pool.getWorkBlockingQueue().size();
						int taskQueue=pool.getQueue().size();
						if(taskWorkingSize>0){
							logger.info("The ThreadPool has {} task working",pool.getWorkBlockingQueue().size());	
						}
						if(taskQueue>0){
							logger.info("The ThreadPool has {} task queue waiting",pool.getQueue().size());
						}
						Thread.sleep(30000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		};
		threadPoolTaskQueueListener.setName("ThreadPoolTaskQueueListener");
		threadPoolTaskQueueListener.start();
	}
	@Override
	public void imageDataReceive() {
		Map<String, Object> map = checkbboxdownloadrecordServer // 使用RIM通知云盒后端请求遥感云盒集市后台最新数据记录
				.queryBboxdownloadNewrecord(groupUserName, groupUserPassword);
		if (!map.get("code").equals(0)) {
			Timestamp recordtime = new Timestamp(System.currentTimeMillis());//获取当前时间
			List<DownloadRecord> list = (List<DownloadRecord>) map.get("list");// 获取数据记录列表，并启动后台自动入库程序
			if(list==null||list.size()<1){
				logger.info("集市没有用户{}的最新数据",groupUserName);
			}else{//有最新数据
				for (DownloadRecord downloadRecord : list) {//将全部downloadRecord插入到数据库中
					if (downloadrecordstutasServer.qureyRecordbyrecordid(downloadRecord.getId()) == 0) {//如果客户端数据库中还没这条推送记录的话执行
						boolean confirmHaveDownload=true;//标记是否要对这条推送记录的数据进行下载
						List<Map<String, Object>> listTemp=downloadrecordstutasServer
								.queryDownrecordByName(downloadRecord.getName());//同个名称的数据可能存在多条推送记录
						Map<String, Object> savaDownloadRecordMap=null;
						for (Map<String, Object> downloadRecordMap : listTemp) {//通过name判断数据是否已经下载到当前的云盒客户端
							if(downloadRecordMap.get("downloadstatus")!=null){//若本机存在与该条推送记录所指向的数据相同的记录，且该数据的下载状态不为空的话执行
								Integer downloadStatus=(Integer)downloadRecordMap.get("downloadstatus");
								if(downloadStatus==4){//若该推送记录所对应的数据已下载到了客户端所对应的服务器上，则将其标记为不要下载
									savaDownloadRecordMap=downloadRecordMap;
									confirmHaveDownload=false;break;
								}
							}
						}
						if(confirmHaveDownload){
							//将记录插入到数据库中
							downloadrecordstutasServer.insertRecordStutas(downloadRecord.getId(),
									recordtime.toString(),downloadRecord.getImage_type(),downloadRecord.getName(),
									downloadRecord.getDownloadfilepath(),downloadRecord.getImage_product_id(),null,0l,null);
						}else{//已经下载到本地就无需下载
							downloadrecordstutasServer.insertRecordStutas(downloadRecord.getId(),
									recordtime.toString(),downloadRecord.getImage_type(),downloadRecord.getName(),
									(String)savaDownloadRecordMap.get("downloadfilepath"),downloadRecord.getImage_product_id(),4,
									(Long)savaDownloadRecordMap.get("downloadsize"),
									((Timestamp)savaDownloadRecordMap.get("taskendtime")).toString());
							//使用rmi通知云盒后台，将该条推送记录所对应的数据标记为已下载
							checkbboxdownloaddownloadRecordServer.updaterecordfordownloadstatus(true, downloadRecord.getId());
							logger.info("name={} have download ",downloadRecord.getName());//已经下载
						}
					}else{
						//数据库中已有该条记录
						logger.debug("id={} have exist",downloadRecord.getName());
					}
				}//end for
				List<Map<String,Object>> byNameList=//获取推送记录的name值
						downloadrecordstutasServer.getDownloadrecordGroupByName(null);//传个空值表示查找那个还未被下载的记录，按数据名称分组
				for (Map<String, Object> mapTemp : byNameList) {
					String id=mapTemp.get("id").toString();
					DownloadRecord downloadRecord=
							downloadrecordstutasServer.queryRecordStutasByRecordid(id);
					downloadRecord.setId(id);
					addToThreadPool(downloadRecord);//将所有未下载的推送记录加入到线程池中准备下载
				}
			}
			
		}else{
			logger.info("UserName or Password error UserName={},Password={}",groupUserName,groupUserPassword);
		}
	}
	/**
	* Description:添加到线程池 
	* @param downloadRecord  
	* @author lishun 
	* @date 2016年12月8日 下午2:44:02
	 */
	public void addToThreadPool(DownloadRecord downloadRecord){
		BlockingQueue<Runnable> waitThreadQueue = pool.getQueue();//Returns the task queue 
		LinkedBlockingQueue<Runnable> workThreadQueue =pool.getWorkBlockingQueue();//Returns the running work
		GtdataBreakpointResumeDownloadThread downloadThread = 
				new GtdataBreakpointResumeDownloadThread(downloadRecord);//创建下载线程，准备下载推送过来的数据
		if (!waitThreadQueue.contains(downloadThread)&&!workThreadQueue.contains(downloadThread)) {
			logger.info("a_workThread:recordId={},name={} add to workThreadQueue",downloadRecord.getId(),downloadRecord.getName());
			pool.execute(downloadThread);//运行下载线程
		}else{
			logger.debug("i_workThread:recordId={},name={} in waitThreadQueue or workThreadQueue",downloadRecord.getId(),downloadRecord.getName());
		}
	}
	@Override
	public void addWorkThreadQueue(DownloadRecord downloadRecord){
		GtdataBreakpointResumeDownloadThread downloadThread = new GtdataBreakpointResumeDownloadThread(downloadRecord);
		pool.execute(downloadThread);
	}
	@Override
	public boolean downloadKeyAndDecryption(String downLoadName,String fileDirectory,String examinationApprovalRecordId) {
		boolean flag=false;
		String fileNamePostfix=downLoadName.substring(downLoadName.lastIndexOf("/")+1);//带后缀的文件名
		String fileName=downLoadName.substring(downLoadName.lastIndexOf("/")+1);//不带后缀的文件名
		if(fileName.indexOf(".tar.gz")!=-1){//判断是否有后缀名
			fileName=fileName.substring(0,fileName.indexOf(".tar.gz"));
		}
		if (fileDirectory == null || fileDirectory.equals("")) {
			logger.info("请求路径或文件不存在！");
			return flag;
		}
		String decryptDir=decryptFilePath+File.separator+examinationApprovalRecordId;
		if(!new File(decryptDir).exists()){//创建保存解密文件的文件夹
			new File(decryptDir).mkdirs();
		}
		String encryptfileName=null;//加密保存文件路径
		String decryptFileName = null;//保存解密文件的路径
		if((fileDirectory.lastIndexOf("/")+1)!=fileDirectory.length()){
			encryptfileName = fileDirectory+File.separator+fileNamePostfix+".encrypt";
			
			decryptFileName = decryptDir+File.separator+fileNamePostfix;
		}else{
			encryptfileName = fileDirectory+fileNamePostfix+".encrypt";
			decryptFileName = decryptDir+File.separator+fileNamePostfix;
		}
		try {
			Map<String,Object> map = RsmartGtdataEncryptionUtil.getDownKeyStreamMap(downLoadName);//下载秘钥
			if("1".equals(map.get("result"))){
				logger.info("downLoadName={},获取秘钥成功",downLoadName);
				File fTemp=new File(fileDirectory+File.separator+examinationApprovalRecordId);//创建存放解密文件的文件夹
				if(!fTemp.exists()){
					fTemp.mkdir();
				}
				InputStream fileInputStream =(InputStream)map.get("inputStream");
				String key = RsmartGtdataEncryptionUtil.inputStream2String(fileInputStream);//System.out.println("in_file_path:"+encryptfileName+";out_file_path:"+decryptFileName+";out_file_path"+key);
				flag=FileEncryptionUtils.fileCryptoAES256(encryptfileName, decryptFileName, key, 0);//解密文件
				return flag;
			}else{
				logger.info("downLoadName={},获取秘钥失败:{}",downLoadName,map);
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return flag;
		}
		
	}
}
