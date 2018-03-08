package com.cloudbox.timer;


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

import com.cloudbox.mgmt.service.FileDeleteExceptionService;
import com.cloudbox.utils.Threads;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

/**
 * */
@Lazy
public class FileDeleteExceptionTimerJob implements Runnable {
	private static Logger logger = LoggerFactory
			.getLogger(FileDeleteExceptionTimerJob.class);
	@Autowired
	protected FileDeleteExceptionService fileDeleteExceptionService;

	private int initialDelay = 0;
	private int period = 2;
	private int shutdownTimeout = Integer.MAX_VALUE;
	private ScheduledExecutorService scheduledExecutorService;


	@PostConstruct
	public void start() throws Exception {
		Validate.isTrue(period > 0);
		Runnable task = TaskUtils.decorateTaskWithErrorHandler(this, null, true);
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setNameFormat(
				" fileDeleteExceptionServiceTimerJob-%1$d").build());
		scheduledExecutorService.scheduleWithFixedDelay(task, initialDelay, period, TimeUnit.SECONDS);
	}

	@PreDestroy
	public void stop() {
		Threads.normalShutdown(scheduledExecutorService, shutdownTimeout, TimeUnit.SECONDS);
	}

	@Override
	public void run() {
		logger.info("FileDeleteExceptionTimerJob is start...");
		fileDeleteExceptionService.checkFileDeleteException();
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
