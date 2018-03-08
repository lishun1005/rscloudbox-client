package com.cloudbox.mgmt.thread;


import java.text.SimpleDateFormat;
import java.util.Date;

import com.cloudbox.mgmt.service.CheckProcessService;
import com.cloudbox.mgmt.service.ImageDataReceiveService;
import com.cloudbox.utils.SpringContextUtil;

public class DownloadKeyAndDecryptionThread implements Runnable {

	private CheckProcessService checkProcessService  = (CheckProcessService) SpringContextUtil.getBean("checkProcessService");
	
	private ImageDataReceiveService imageDataReceiveService  = (ImageDataReceiveService) SpringContextUtil.getBean("imageDataReceiveService");
	
	private String downLoadName;
	private String fileDirectory;
	private String examinationApprovalRecordId;
	private String downloadDatalistId;
	/**
	 * @param downLoadName:下载秘钥的路径
	 * @param fileDirectory 存放加密文件的路径
	 * @param examinationApprovalRecordId 审批记录ID
	 * @param downloadDatalistId 数据下载列表ID
	 */
	public DownloadKeyAndDecryptionThread(String downLoadName,String fileDirectory,
			String examinationApprovalRecordId,String downloadDatalistId){
		this.downLoadName=downLoadName;
		this.fileDirectory=fileDirectory;
		this.examinationApprovalRecordId=examinationApprovalRecordId;
		this.downloadDatalistId=downloadDatalistId;
	}
	@Override
	public void run() {
		try {
			boolean flag=imageDataReceiveService
					.downloadKeyAndDecryption(downLoadName,fileDirectory,examinationApprovalRecordId);
			if(flag){
				checkProcessService.updateDownloadDataById(downloadDatalistId, 1);//修改状态为已解密
			}else{
				checkProcessService.updateDownloadDataById(downloadDatalistId, 3);//解密失败
			}
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			checkProcessService//修改状态为已审批(有一条数据解密成功就把该审批记录置为已审批)
				.updateExaminationApprovalRecordsById(examinationApprovalRecordId, 1,df.format(new Date()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
