package com.cloudbox.mgmt.thread;


import java.util.Date;
import java.util.Map;

import com.cloudbox.mgmt.dao.GtdataDataPutInstorageRespository;
import com.cloudbox.mgmt.service.DownloadRecordService;
import com.cloudbox.mgmt.service.impl.GtdataOpServerImpl;
import com.cloudbox.utils.SpringContextUtil;
/**
 * @Description  Gtdata并行切割进度监听类
 * */
public class GoldtowersectionCuttingProgressListener extends Thread {

	private Date beginTime;
	
	private boolean run;
	
	private String  jobid;
	
	private Long filesize; 
	
	private Integer max_layers;
	
	private Long averagespeed;
	
	private Long speed;
	
	private String taskid;
	
	private Integer progress;
	
	private long lasttime;
	
	private long alltime;
	
	private GtdataOpServerImpl gtdataopserver = (GtdataOpServerImpl) SpringContextUtil.getBean("gtdataopserver");
	
	private GtdataDataPutInstorageRespository gtdatadataputinstoragemanagement=(GtdataDataPutInstorageRespository) SpringContextUtil.getBean("GtdataDataPutInstorageRespository");
	
	private GoldtowersectionCuttingProgressListenerCollextionUtil goldtowersectionCuttingProgressListenerCollextionUtil=(GoldtowersectionCuttingProgressListenerCollextionUtil) SpringContextUtil.getBean("goldtowersectionCuttingProgressListenerCollextionUtil");
	
	protected DownloadRecordService downloadrecordstutasServer=(DownloadRecordService) SpringContextUtil.getBean("downloadRecordService");
			
	public GoldtowersectionCuttingProgressListener(String jobid,Long filesize, Integer max_layers ,String taskid) {
		this.beginTime = new Date();
		this.jobid=jobid;
		this.filesize=filesize;
		this.max_layers=max_layers;
		this.taskid=taskid;
		goldtowersectionCuttingProgressListenerCollextionUtil.AddgoldtowersectionCuttingProgressListener(this, taskid);
	}

	@Override
	public void run() {
		Map<String, Object> map;
		this.run=true;
		try {
			while (this.run) {
			  map = gtdataopserver.querygoldtowersectionCuttingProgress(jobid);
			  if (map.get("state").equals("SUCCEEDED")||map.get("state").equals("RUNNING")||map.get("state").equals("ACCEPTED")||map.get("state").equals("FAILED")) {
				  String url=(String) map.get("url");
				  String progress=  (String) map.get("progress");
				  this.progress=Integer.valueOf(progress);
				  System.out.println(jobid+":"+progress);
				  if (progress!=null) {
					  recordlastTime(progress);
				  }
				  if (progress.equals(100)||progress=="100"||Integer.valueOf(progress)==100) {
					  System.out.println("结束！");
					  this.run=false;
					  gtdatadataputinstoragemanagement.updaterecordbyjobIdforurl(jobid, url);
					  downloadrecordstutasServer.updateRecordStutasFordownloadstatus(4, taskid);
					  goldtowersectionCuttingProgressListenerCollextionUtil.romovegoldtowersectionCuttingProgressListener(taskid);
				  }
			  }else {
				  System.out.println(map.get("HttpStatusCode")+":"+map.get("errorMessage"));
			  }
			  sleep(3000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void recordlastTime(String progress) {
		try {
			Date currentTime = new Date();
			long changetime = (currentTime.getTime() - beginTime.getTime());
			if (changetime > 0) {
				speed =   (long) (filesize*Math.pow(2, max_layers)*(Integer.valueOf(progress)/100.00) /changetime);
				if (this.averagespeed == null) {
					this.averagespeed = speed;
				} else {
					this.averagespeed = (this.averagespeed + speed) / 2;
				}
				System.out.println(speed);
				if (speed!=0) {
					lasttime=(long) (filesize*Math.pow(2, max_layers)*(1-Integer.valueOf(progress)/100.00)/speed);
					alltime=(long) (filesize*Math.pow(2, max_layers)/speed);
				}else {
					lasttime=0;
					alltime=0;
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public synchronized Long getSpeed() {
		return speed;
	}

	public synchronized Long getAveragespeed() {
		return averagespeed;
	}

	public synchronized Integer getProgress() {
		return progress;
	}

	public synchronized long getLasttime() {
		return lasttime;
	}
	
	public synchronized long getAlltime() {
		return alltime;
	}

	public synchronized Date getBeginTime() {
		return beginTime;
	}
}