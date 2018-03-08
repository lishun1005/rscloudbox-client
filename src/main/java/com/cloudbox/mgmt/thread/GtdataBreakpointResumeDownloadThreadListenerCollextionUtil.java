package com.cloudbox.mgmt.thread;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.cloudbox.mgmt.service.DownloadRecordService;
import com.cloudbox.utils.PropertyOputils;
import com.cloudbox.utils.SpringContextUtil;

/**
 * @Description  点续传数据下载进度监听管理类，管理运行的断点续传下载进度监听线程，并计算平均切割进度
 * */
@Service("GtdataFileBreakpointResumeThreadListenerCollextionUtil")
public class GtdataBreakpointResumeDownloadThreadListenerCollextionUtil {
	private Map<String, Object> BreakpointResumeThreadListenerMap;
	{
		this.BreakpointResumeThreadListenerMap = new HashMap<String, Object>();
	}
    private Long  averagespeed;
    
	public synchronized Long getAveragespeed() {
		return averagespeed;
	}

	protected PropertyOputils PropertyOputils=(PropertyOputils) SpringContextUtil.getBean("averagespeed");
	
	protected DownloadRecordService downloadrecordstutasServer=(DownloadRecordService) SpringContextUtil.getBean("downloadRecordService");
	
	public GtdataBreakpointResumeDownloadThreadListenerCollextionUtil () {
		averagespeed=Long.valueOf(PropertyOputils.getKeyValue("DownloadAveragespeed"));
	}

	public synchronized Map<String, Object> getBreakpointResumeThreadListenerMap() {
		return BreakpointResumeThreadListenerMap;
	}

	public void AddBreakpointResumeThreadListener(
			GtdataBreakpointResumeDownloadThreadListener GtdataFileBreakpointResumeThreadListener ,String taskid) {
		this.BreakpointResumeThreadListenerMap.put(taskid, GtdataFileBreakpointResumeThreadListener);
	}

	public void romoveBreakpointResumeThreadListener(String taskid) {
		calculateaveragespeed(taskid);
		this.BreakpointResumeThreadListenerMap.remove(taskid);
	}

	private void calculateaveragespeed(String taskid) {
		Timestamp nowTimes=new Timestamp(System.currentTimeMillis()); 
		downloadrecordstutasServer.updateDownloadEndTime(nowTimes.toString(), taskid);
		if (averagespeed==null||averagespeed.equals(0)) {
			averagespeed =  ((GtdataBreakpointResumeDownloadThreadListener) this.BreakpointResumeThreadListenerMap.get(taskid)).getAveragespeed();
		}else {
			averagespeed = (averagespeed + ((GtdataBreakpointResumeDownloadThreadListener) this.BreakpointResumeThreadListenerMap.get(taskid)).getAveragespeed()) / 2;
		}
		PropertyOputils.writeProperties("DownloadAveragespeed", Long.toString(averagespeed));
	}
}
