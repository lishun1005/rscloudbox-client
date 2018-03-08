package com.cloudbox.timer;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.support.TaskUtils;

import com.cloudbox.mgmt.service.ImageDataReceiveService;
import com.cloudbox.utils.Threads;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
/**
 * @Description 每天18:20定时执行查询最新影像数据并执行推送入库流程
 * */
@Lazy
public class CheckBboxDownloadRecordTimerJob implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(CheckBboxDownloadRecordTimerJob.class);
	@Autowired
	@Qualifier("imageDataReceiveService")
	protected ImageDataReceiveService imageDataReceiveService;
	
	private long initialDelay = 0;
	private int period = 0;
	private int shutdownTimeout = Integer.MAX_VALUE;
	private ScheduledExecutorService scheduledExecutorService;

	@PostConstruct
	public void start() throws Exception {
		Validate.isTrue(period > 0);
		// 任何异常不会中断schedule执行, 由Spring的TaskUtils的LOG_AND_SUPPRESS_ERROR_HANDLER进行处理
		Runnable task = TaskUtils.decorateTaskWithErrorHandler(this, null, true);
		// 创建单线程的SechdulerExecutor,并用guava的ThreadFactoryBuilder设定生成线程的名称
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder()
						.setNameFormat("checkbboxdownloadrecordtimerJob-%1$d").build());
		long oneDay = 24 * 60 * 60;
		period=(int) oneDay;
		initialDelay = (getTimeMillis("18:30:01") - System.currentTimeMillis())/1000;
		initialDelay = initialDelay > 0 ? initialDelay : oneDay + initialDelay;
		scheduledExecutorService.scheduleAtFixedRate(task,initialDelay,period,TimeUnit.SECONDS);
	}
	@PreDestroy
	public void stop() {
		Threads.normalShutdown(scheduledExecutorService, shutdownTimeout,TimeUnit.SECONDS);
	}
	/**
	 * 数据推送处理
	 */
	@Override
	public void run() {
		logger.info("Task Timing is start in 18:30:01......");
		try {
			imageDataReceiveService.imageDataReceive();;
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


	/** * 获取指定时间对应的毫秒数 * @param time "HH:mm:ss" * @return */
	private static long getTimeMillis(String time) {
		try {
			DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
			DateFormat dayFormat = new SimpleDateFormat("yy-MM-dd");
			Date curDate = dateFormat.parse(dayFormat.format(new Date()) + " "
					+ time);
			return curDate.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

}
