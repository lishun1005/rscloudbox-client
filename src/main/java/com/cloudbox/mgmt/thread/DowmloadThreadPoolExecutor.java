package com.cloudbox.mgmt.thread;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lzw 2015-12-19
 * 
 * @Description 影像数据下载线程池
 * 
 * @version V1.2
 **/
public class DowmloadThreadPoolExecutor extends ThreadPoolExecutor{
	public static Logger logger=LoggerFactory.getLogger(DowmloadThreadPoolExecutor.class);
	/**
	 * 记录运行中线程队列
	 */
	private LinkedBlockingQueue<Runnable> workBlockingQueue=new  LinkedBlockingQueue<Runnable>();
	
	public DowmloadThreadPoolExecutor(int corePoolSize, int maximumPoolSize,
			long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}
	@Override
	protected void beforeExecute(Thread t, Runnable r) {
		super.beforeExecute(t, r);
		GtdataBreakpointResumeDownloadThread gtdataBreakpointResumeDownloadThread=
				(GtdataBreakpointResumeDownloadThread)r;
		t.setName("poolThread_"+gtdataBreakpointResumeDownloadThread.getDownloadRecord().getName());//rename threadName
		workBlockingQueue.add(gtdataBreakpointResumeDownloadThread);//保存在运行的线程
		logger.info("Before the task execution");
	}
	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		super.afterExecute(r, t);
		workBlockingQueue.remove((GtdataBreakpointResumeDownloadThread)r);//移除关闭的线程
		logger.info("After the task execution");
	}
	//执行任务完成,需要执行关闭操作才会调用这个方法
	@Override
	protected void terminated() {
		super.terminated();
		logger.info("pool Thread done");
	}
	
	/**
	 * 
	* Description: 正在运行的线程
	* @return LinkedBlockingQueue<Runnable><br>
	*
	* @author lishun 
	* @date 2016-7-19 上午9:26:15
	 */
	public LinkedBlockingQueue<Runnable> getWorkBlockingQueue() {
		return workBlockingQueue;
	}
}