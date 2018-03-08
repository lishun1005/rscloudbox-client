package com.cloudbox.mgmt.service;

import com.chinarsgeo.rscloudmart.web.dao.entity.DownloadRecord;


public interface ImageDataReceiveService {
	/**
	 * 
	* Description: 影像数据接收服务
	* @return void<br>
	* @author lishun 
	* @date 2016年12月8日 上午9:23:14
	 */
	public void imageDataReceive();
	/**
	* Description:下载秘钥和解密 
	* @param downLoadName:/付费订单数据/1258353694767692//GF2_PMS2_E113.8_N23.8_20150123_L1A0001026725.tar.gz
	* @param fileDirectory 加密文件存放的路径
	* @param examinationApprovalRecordId
	* @return  true:解密成功；false：解密失败
	* @author lishun 
	* @date 2016-5-18 下午2:25:57
	 */
	boolean downloadKeyAndDecryption(String downLoadName, String fileDirectory,String examinationApprovalRecordId);
	/**
	 * 
	* Description:把未完成下载的数据直接加入线程池进行下载  
	* @param downloadRecord  
	* @author lishun 
	* @date 2016-8-5 上午9:42:27
	 */
	void addWorkThreadQueue(DownloadRecord downloadRecord);

	
}
