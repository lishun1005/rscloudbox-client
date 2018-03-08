package com.cloudbox.mgmt.thread;


import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

/**
 * @Description  zip4j解压进度监听管理类 ，管理解压进度监听线程，并计算平均进度 ，最基础版测试监听解压包使用，已经废弃
 * */
@Service("CompressProgressCollectionUtil")
public class CompressProgressMonitorListenerCollectionUtil {

	private long averagespeed;
	private Map<String, Object> progressMonitorListernMap;
	{
		this.progressMonitorListernMap = new HashMap<String, Object>();
	}

	public synchronized long getAveragespeed() {
		return averagespeed;
	}

	public void setAveragespeed(long averagespeed) {
		this.averagespeed = averagespeed;
	}

	public Map<String, Object> getProgressMonitorListernMap() {
		return progressMonitorListernMap;
	}

	public void setProgressMonitorListernMap(
			Map<String, Object> progressMonitorListernMap) {
		this.progressMonitorListernMap = progressMonitorListernMap;
	}

	public void AddProgressMonitorListern(
			CompressProgressMonitorListener ProgressMonitorListern,String  taskid) {
		this.progressMonitorListernMap.put(taskid,ProgressMonitorListern);
	}

	public void romoveProgressMonitorListern(String  taskid) {
		calculateaveragespeed(taskid);
		this.progressMonitorListernMap.remove(taskid);
	}

	private void calculateaveragespeed(String id) {
		// TODO Auto-generated method stub
		averagespeed = (averagespeed + ((CompressProgressMonitorListener) this.progressMonitorListernMap
				.get(id)).getAveragespeed()) / 2;
		System.out.println(averagespeed);
	}

}
