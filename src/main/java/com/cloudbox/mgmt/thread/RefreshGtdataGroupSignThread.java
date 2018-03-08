package com.cloudbox.mgmt.thread;


import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

import com.cloudbox.utils.GtdataGroupVerifyUtil;

/**
 * Description:gtdata定时刷新登录sign和time
 *
 * @author ljw 2014-10-11
 * 
 * @version v1.0
 *
 */
public class RefreshGtdataGroupSignThread extends TimerTask
{
	public RefreshGtdataGroupSignThread()
	{
		
	}
	
	public void run()
	{
		try
		{
			Map<String, Object> map = new HashMap<String, Object>();
			map = GtdataGroupVerifyUtil.groupUserGetLoginedSign(GtdataGroupVerifyUtil.getSign(), GtdataGroupVerifyUtil.getSignTime());//更新
			if(1 == (Integer)map.get("result"))
			{
				GtdataGroupVerifyUtil.sign = (String) map.get("sign");
				GtdataGroupVerifyUtil.signTime = (String) map.get("time");
			}else 
			{
				GtdataGroupVerifyUtil.updateGroupUserGetSignAndTime();//登录
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
