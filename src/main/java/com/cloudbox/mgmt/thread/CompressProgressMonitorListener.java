package com.cloudbox.mgmt.thread;


import java.util.Date;

import net.lingala.zip4j.progress.ProgressMonitor;

import com.cloudbox.utils.SpringContextUtil;

/**
 * @Description  zip4j解压进度监听类，最基础版测试监听解压包使用，已经废弃
 * */
public class CompressProgressMonitorListener extends Thread {
	private String  taskid;
	private Date beginTime;
	private boolean run = true;
	private ProgressMonitor ProgressMonitor;
	private long averagespeed;
	private long resttime;
	private CompressProgressMonitorListenerCollectionUtil CompressProgressCollectionUtil = (CompressProgressMonitorListenerCollectionUtil) SpringContextUtil
			.getBean("CompressProgressCollectionUtil");

	public CompressProgressMonitorListener(ProgressMonitor ProgressMonitor,String  taskid) {
		this.taskid=taskid;
		this.ProgressMonitor = ProgressMonitor;
		this.beginTime = new Date();
		CompressProgressCollectionUtil.AddProgressMonitorListern(this,taskid);
	}

	@Override
	public void run() {
		try {
			// TODO 更新进度条
			while (this.run) {
				if (this.ProgressMonitor.getPercentDone() == 100) {
					recordlastTime();
					this.run = false;
				} else {
					recordlastTime();
					Thread.sleep(100);
				}
			}
			CompressProgressCollectionUtil.romoveProgressMonitorListern(this.taskid);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void recordlastTime() {
		Date currentTime = new Date();
		long changetime = (currentTime.getTime() - beginTime.getTime());
		// System.out.println("时间差值:" + changetime);
		long WorkCompleted = this.ProgressMonitor.getTotalWork()
				* this.ProgressMonitor.getPercentDone() / 100;
		if (changetime > 0) {
			if (WorkCompleted != 0) {
				// System.out.println("完成的工作:" + WorkCompleted);
				long speed = WorkCompleted / changetime;
				this.averagespeed = (this.averagespeed + speed) / 2;
				// System.out.println("解压速度:" + speed);
				long lasttotal = this.ProgressMonitor.getTotalWork()
						- WorkCompleted;
				// System.out.println("所有多少:" +
				resttime = lasttotal / speed;
				System.err.println("剩余时间：" + resttime / 1000 + "秒" + resttime
						+ "毫秒");
			}
		}
	}

	public synchronized long getAveragespeed() {
		return averagespeed;
	}

	public synchronized long getResttime() {
		return resttime;
	}

	public int getPercentDone() {
		// TODO Auto-generated method stub
		return this.ProgressMonitor.getPercentDone();
	}
}
