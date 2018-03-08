package com.cloudbox.mgmt.thread;


import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.cloudbox.mgmt.service.DownloadRecordService;
import com.cloudbox.utils.PropertyOputils;
import com.cloudbox.utils.SpringContextUtil;
/**
 * @Description   tar.gz包解压进度监听管理类 ，
 * */
@Service("TarGzCompressProgressCollectionUtil")
public class TarGzCompressProgressMonitorListenerCollectionUtil {

	private Long averagespeed;
	
	protected PropertyOputils PropertyOputils=(PropertyOputils) SpringContextUtil.getBean("averagespeed");
	
	protected DownloadRecordService downloadrecordstutasServer=(DownloadRecordService) SpringContextUtil.getBean("downloadRecordService");
	
	public TarGzCompressProgressMonitorListenerCollectionUtil() {
		
		averagespeed=Long.valueOf(PropertyOputils.getKeyValue("CompressAveragespeed"));
	}
	
	private Map<String, Object> TarGzprogressMonitorListernMap;
	{
		this.TarGzprogressMonitorListernMap = new HashMap<String, Object>();
	}

	public synchronized Long getAveragespeed() {
		return averagespeed;
	}

	public void setAveragespeed(Long averagespeed) {
		this.averagespeed = averagespeed;
	}


	public synchronized Map<String, Object> getTarGzprogressMonitorListernMap() {
		return TarGzprogressMonitorListernMap;
	}

	public synchronized void setTarGzprogressMonitorListernMap(
			Map<String, Object> tarGzprogressMonitorListernMap) {
		TarGzprogressMonitorListernMap = tarGzprogressMonitorListernMap;
	}

	public void AddTarGzCompressProgressMonitorListener(
			TarGzCompressProgressMonitorListener TarGzCompressProgressMonitorListener,String  taskid) {
		this.TarGzprogressMonitorListernMap.put(taskid,TarGzCompressProgressMonitorListener);
	}

	public void romoveTarGzCompressProgressMonitorListener(String  taskid) {
		calculateaveragespeed(taskid);
		this.TarGzprogressMonitorListernMap.remove(taskid);
	}

	private void calculateaveragespeed(String id) {
		// TODO Auto-generated method stub
		Timestamp nowTimes=new Timestamp(System.currentTimeMillis()); 
		downloadrecordstutasServer.updateTargzEndTime(nowTimes.toString(), id);
		if (this.averagespeed == null || this.averagespeed.equals(0)) {
			this.averagespeed = ((TarGzCompressProgressMonitorListener) this.TarGzprogressMonitorListernMap.get(id)).getAveragespeed();
		} else {
			this.averagespeed = (this.averagespeed + ((TarGzCompressProgressMonitorListener) this.TarGzprogressMonitorListernMap.get(id)).getAveragespeed()) / 2;
		}
		PropertyOputils.writeProperties("CompressAveragespeed", Long.toString(averagespeed));
	}

}
