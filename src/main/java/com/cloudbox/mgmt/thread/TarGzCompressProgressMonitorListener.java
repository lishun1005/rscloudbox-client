package com.cloudbox.mgmt.thread;


import java.util.Date;

import com.cloudbox.mgmt.old.entity.MyFileInputStream;
import com.cloudbox.utils.SpringContextUtil;
/**
 * @Description  tar.gz包解压进度监听类
 * */
public class TarGzCompressProgressMonitorListener extends Thread {
	private String  taskid;
	private Date beginTime;
	private boolean run = true;
	private MyFileInputStream fileInputStream;
	private Long averagespeed;
	private Long resttime;
	private TarGzCompressProgressMonitorListenerCollectionUtil TarGzCompressProgressCollectionUtil = (TarGzCompressProgressMonitorListenerCollectionUtil) SpringContextUtil
			.getBean("TarGzCompressProgressCollectionUtil");

	public TarGzCompressProgressMonitorListener(MyFileInputStream fileInputStream,String  taskid) {
		this.taskid=taskid;
		this.beginTime = new Date();
		this.fileInputStream=fileInputStream;
		TarGzCompressProgressCollectionUtil.AddTarGzCompressProgressMonitorListener(this,taskid);
	}
	@Override
	public void run() {
		try {
			// TODO 更新进度条
			while (this.run) {
				
				if (fileInputStream.getReadbytes()>=fileInputStream.getAllbytes()) {
					recordlastTime();
					this.run = false;
				} else {
					recordlastTime();
					Thread.sleep(100);
				}
			}
			TarGzCompressProgressCollectionUtil.romoveTarGzCompressProgressMonitorListener(this.taskid);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	private void recordlastTime() {
		Date currentTime = new Date();
		long changetime = (currentTime.getTime() - beginTime.getTime());
		// System.out.println("时间差值:" + changetime);
		if (changetime > 0) {
			if (fileInputStream.getReadbytes() != 0) {
				// System.out.println("完成的工作:" + WorkCompleted);
				long speed = fileInputStream.getReadbytes() / changetime;
				if (this.averagespeed == null) {
					this.averagespeed = speed;
				} else {
					this.averagespeed = (this.averagespeed + speed) / 2;
				}
				long lasttotal = (long)(fileInputStream.getAllbytes()- fileInputStream.getReadbytes());
				// System.out.println("所有多少:" +
				if (lasttotal<0) {
					lasttotal=0;	
				}
				if (speed!=0) {
				 resttime = lasttotal / speed;
				}
				//System.err.println("剩余时间：" + resttime / 1000 + "秒" + resttime
						//+ "毫秒");
			}
		}
	}
	public synchronized Long getAveragespeed() {
		return averagespeed;
	}

	public synchronized long getResttime() {
		return resttime;
	}
	
	public synchronized Date getBeginTime() {
		return beginTime;
	}
	public int getPercentDone() {
		// TODO Auto-generated method stub
		return  (int) ((double) fileInputStream.getReadbytes()/fileInputStream.getAllbytes()*100);
	}
}
