package com.cloudbox.mgmt.thread;



import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

import com.cloudbox.utils.RsmartGtdataGroupVerifyUtil;

/**
 * Description:gtdata定时刷新登录sign和time
 *
 * @author ljw 2014-10-11
 * 
 * @version v1.0
 *
 */
public class RsmartRefreshGtdataGroupSignThread extends TimerTask
{
	public RsmartRefreshGtdataGroupSignThread()
	{
		
	}
	
	public void run()
	{
		try
		{
			Map<String, Object> map = new HashMap<String, Object>();
			map = RsmartGtdataGroupVerifyUtil.groupUserGetLoginedSign(RsmartGtdataGroupVerifyUtil.getSign(), RsmartGtdataGroupVerifyUtil.getSignTime());//更新
			if(1 == (Integer)map.get("result"))
			{
				RsmartGtdataGroupVerifyUtil.sign = (String) map.get("sign");
				RsmartGtdataGroupVerifyUtil.signTime = (String) map.get("time");
			}else 
			{
				RsmartGtdataGroupVerifyUtil.updateGroupUserGetSignAndTime();//登录
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
