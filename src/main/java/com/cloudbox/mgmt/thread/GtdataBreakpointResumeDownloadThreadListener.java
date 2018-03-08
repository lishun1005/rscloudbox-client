package com.cloudbox.mgmt.thread;


import java.util.Date;

import com.cloudbox.utils.SpringContextUtil;
/**
 * @Description  断点续传数据下载进度监听类
 * */
public class GtdataBreakpointResumeDownloadThreadListener extends Thread {
	/**
	 * 任务开始时间
	 */
	private Date beginTime;
	private boolean run = true;
	/**
	 * 平均速度
	 */
	private Long averagespeed;
	/**
	 * 剩余时间
	 */
	private Long resttime;
	/**
	 * 文件总大小
	 */
	private Long totalWork;
	/**
	 * 断点下载每次开始下载的数据点
	 */
	private Long workCompletedBegin;
	/**
	 * 已经完成下载数据大小
	 */
	private Long workCompleted;
	/**
	 * 数据id
	 */
	private String  recordid;
	private GtdataBreakpointResumeDownloadThreadListenerCollextionUtil BreakpointResumeThreadListenerCollextionUtil = (GtdataBreakpointResumeDownloadThreadListenerCollextionUtil) SpringContextUtil.getBean("GtdataFileBreakpointResumeThreadListenerCollextionUtil");

	public GtdataBreakpointResumeDownloadThreadListener(long totalWork, long workCompleted,String recordid) {
		this.recordid=recordid;
		this.beginTime = new Date();
		this.totalWork = totalWork;
		this.workCompleted = workCompleted;
		this.workCompletedBegin	=workCompleted;
		BreakpointResumeThreadListenerCollextionUtil.AddBreakpointResumeThreadListener(this,recordid);
	}

	public void addWorkCompleted(long workCompleted) {
		synchronized (this.workCompleted) {
			this.workCompleted += workCompleted;
		}
	}

	@Override
	public void run() {
		try {
			while (this.run) {// TODO 更新进度条
				if (workCompleted.equals(totalWork) ) {
					recordlastTime();
					this.run = false;
				} else {
					recordlastTime();
					Thread.sleep(100);
				}
			}
			BreakpointResumeThreadListenerCollextionUtil.romoveBreakpointResumeThreadListener(recordid);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setRun(boolean run) {
		this.run = run;
	}

	private void recordlastTime() {
		try {
			Date currentTime = new Date();
			long changetime = (currentTime.getTime() - beginTime.getTime());
			if (changetime > 0) {
				if (workCompleted != 0) {
					long speed = (workCompleted-this.workCompletedBegin) / changetime;
					if (this.averagespeed == null) {
						this.averagespeed = speed;
					} else {
						this.averagespeed = (this.averagespeed + speed) / 2;
					}
					long lasttotal = totalWork - workCompleted;
					if (speed!=0) {
						resttime = lasttotal / speed;
						//System.err.println("任务:"+recordid+"--剩余时间：" + resttime / 1000 + "秒" + resttime+ "毫秒");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public  Long getAveragespeed() {
		return averagespeed;
	}
	public  Long getResttime() {
		return resttime;
	}
	
	public synchronized Date getBeginTime() {
		return beginTime;
	}

	public  int getPercentDone(){
		return  (int) Math.floor(((double) this.workCompleted)/((double)this.totalWork)*100);
	}
}
