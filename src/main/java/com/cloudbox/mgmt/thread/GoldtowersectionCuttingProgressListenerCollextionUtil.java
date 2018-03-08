package com.cloudbox.mgmt.thread;


import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.cloudbox.mgmt.service.DownloadRecordService;
import com.cloudbox.utils.PropertyOputils;
import com.cloudbox.utils.SpringContextUtil;
/**
 * @Description  Gtdata并行切割进度监听管理类，管理运行的并行切割进度监听线程，并计算平均切割进度
 * */
@Service("goldtowersectionCuttingProgressListenerCollextionUtil")
public class GoldtowersectionCuttingProgressListenerCollextionUtil {
    private Long  averagespeed;
    
	public synchronized Long getAveragespeed() {
		return averagespeed;
	}

	protected PropertyOputils PropertyOputils=(PropertyOputils) SpringContextUtil.getBean("averagespeed");
	protected DownloadRecordService downloadrecordstutasServer=(DownloadRecordService) SpringContextUtil.getBean("downloadRecordService");
	
	public GoldtowersectionCuttingProgressListenerCollextionUtil () {
		averagespeed=Long.valueOf(PropertyOputils.getKeyValue("CuttingAveragespeed"));
	}
	
	private Map<String, Object> CuttingProgressListenerMap;
	
	{
		this.CuttingProgressListenerMap = new HashMap<String, Object>();
	}

	public synchronized Map<String, Object> getCuttingProgressListenerMap() {
		return CuttingProgressListenerMap;
	}

	public void AddgoldtowersectionCuttingProgressListener(
			GoldtowersectionCuttingProgressListener goldtowersectionCuttingProgressListener ,String taskid) {
		this.CuttingProgressListenerMap.put(taskid, goldtowersectionCuttingProgressListener);
	}

	public void romovegoldtowersectionCuttingProgressListener(String taskid) {
		calculateaveragespeed(taskid);
		this.CuttingProgressListenerMap.remove(taskid);
	}

	private void calculateaveragespeed(String taskid) {
		// TODO Auto-generated method stub
		Timestamp nowTimes=new Timestamp(System.currentTimeMillis()); 
		downloadrecordstutasServer.updateCutEndTime(nowTimes.toString(), taskid);
		if (averagespeed==null||averagespeed.equals(0)) {
			averagespeed =  ((GoldtowersectionCuttingProgressListener) this.CuttingProgressListenerMap.get(taskid)).getAveragespeed();
		}else {
			averagespeed = (averagespeed + ((GoldtowersectionCuttingProgressListener) this.CuttingProgressListenerMap.get(taskid)).getAveragespeed()) / 2;
		}		PropertyOputils.writeProperties("CuttingAveragespeed", Long.toString(averagespeed));
	}
}
