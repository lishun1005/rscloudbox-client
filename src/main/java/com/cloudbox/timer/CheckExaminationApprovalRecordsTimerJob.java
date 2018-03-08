package com.cloudbox.timer;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.support.TaskUtils;

import com.chinarsgeo.rscloudmart.web.webservice.ICheckApproveStatusServer;
import com.cloudbox.mgmt.service.CheckProcessService;
import com.cloudbox.mgmt.thread.DownloadKeyAndDecryptionThread;
import com.cloudbox.utils.Threads;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
/**
 * @Description  定时器,定时执行查询未审批的记录
 * */
public class CheckExaminationApprovalRecordsTimerJob implements Runnable {
	/**
	 * 解密线程池，最多4个线程在执行解密
	 */
	private static ThreadPoolExecutor threadPoolExecutor=
			new ThreadPoolExecutor(4, 5, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>()){
		@Override
		protected void beforeExecute(Thread t, Runnable r) {
			logger.info("staring decryptionFile");
		};
		@Override
		protected void afterExecute(Runnable r, Throwable t) {
			logger.info("ending decryptionFile");
		};
	};
	private static Logger logger = LoggerFactory.getLogger(CheckExaminationApprovalRecordsTimerJob.class);
	private long initialDelay = 0;
	private int period = 0;
	private int shutdownTimeout = Integer.MAX_VALUE;
	private ScheduledExecutorService scheduledExecutorService;
	
	@Autowired
	protected CheckProcessService checkProcessService;
	@Autowired
	protected ICheckApproveStatusServer checkApproveStatusServer;
	
	@PostConstruct
	public void start() throws Exception {
		Validate.isTrue(period > 0);
		// 任何异常不会中断schedule执行, 由Spring的TaskUtils的LOG_AND_SUPPRESS_ERROR_HANDLER進行处理
		Runnable task = TaskUtils.decorateTaskWithErrorHandler(this, null, true);
		// 创建单线程的SechdulerExecutor,并用guava的ThreadFactoryBuilder设定生成线程的名称
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder()
						.setNameFormat("CheckExaminationApprovalRecordsTimerJob-%1$d").build());
		scheduledExecutorService.scheduleAtFixedRate(task, initialDelay,period, TimeUnit.SECONDS);
	}

	@PreDestroy
	public void stop() {
		Threads.normalShutdown(scheduledExecutorService, shutdownTimeout,TimeUnit.SECONDS);
	}

	/**
	 * 定时执行查询未审批的记录
	 */
	@Override
	public void run() {
		logger.debug("query data is check");
		try {
			
			//定时执行查询待审批的记录
			List<Map<String,Object>> list=checkProcessService.listCheckProcess("0",null,null);
			for (Map<String, Object> map : list) {
				String approveId=String.valueOf(map.get("earId"));
				int examinationApprovalStatus=checkApproveStatusServer.getApproveStatusById(approveId);
				if(examinationApprovalStatus==1){
					decryptionFile(approveId);
				}else if(examinationApprovalStatus==2){
					SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					checkProcessService.updateExaminationApprovalRecordsById(approveId, 2,df.format(new Date()));
				}
			}
			
			//定时执行查询待重审的记录
			List<Map<String,Object>> list2=checkProcessService.listCheckProcess("4",null,null);
			for (Map<String, Object> map : list2) {
				String approveId=String.valueOf(map.get("earId"));
				int examinationApprovalStatus=checkApproveStatusServer.getApproveStatusById(approveId);
				if(examinationApprovalStatus==1){
					decryptionFile(approveId);
				}else if(examinationApprovalStatus==2){
					SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					checkProcessService.updateExaminationApprovalRecordsById(approveId, 2,df.format(new Date()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 
	* Description: 对审批通过的记录进行解密;只对自动推送[auto_tag=1]的记录进行解密
	* @param examinationApprovalRecordId 审批记录id
	* @author lishun 
	* @date 2016-5-8 下午2:57:36
	 */
	public void decryptionFile(String examinationApprovalRecordId){
		try {
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			List<Map<String, Object>> listAreaImage =
					checkProcessService.findDownloadDataListByExaminationApprovalRecordId(examinationApprovalRecordId);
			for (Map<String, Object> map : listAreaImage) {
				if(map.get("auto_tag")!=null){
					if(Integer.valueOf(map.get("auto_tag").toString())==1){
						String downLoadName=String.valueOf(map.get("file_name"));
						String fileDirectory=String.valueOf(map.get("file_directory"));
						String downloadDatalistId=String.valueOf(map.get("id"));
						DownloadKeyAndDecryptionThread th=
								new DownloadKeyAndDecryptionThread(downLoadName,
										fileDirectory,examinationApprovalRecordId,downloadDatalistId);
						threadPoolExecutor.execute(th);
						checkProcessService//审批记录置为解密中
							.updateExaminationApprovalRecordsById(examinationApprovalRecordId, 3,df.format(new Date()));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * 设置任务初始启动延时时间.
	 */
	public void setInitialDelay(int initialDelay) {
		this.initialDelay = initialDelay;
	}

	/**
	 * 设置任务间隔时间,单位秒.
	 */
	public void setPeriod(int period) {
		this.period = period;
	}

	/**
	 * 设置normalShutdown的等待时间, 单位秒.
	 */
	public void setShutdownTimeout(int shutdownTimeout) {
		this.shutdownTimeout = shutdownTimeout;
	}

}
