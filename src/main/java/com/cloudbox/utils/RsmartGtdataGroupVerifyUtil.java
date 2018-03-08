package com.cloudbox.utils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.cloudbox.mgmt.thread.RsmartRefreshGtdataGroupSignThread;

/**
 * Description: gtdata组用户登录，验证等操作
 * 
 * @author ljw 2014-11-03
 * @author zjw 2015-03-17 添加普通用户登录功能,以实现用户直接访问gtdata下载，其他功能依然用组用户实现。
 * 
 * @version v1.0
 * 
 */
public class RsmartGtdataGroupVerifyUtil {
    // private static String gtdataHost = "http://192.168.2.6:8001/gtdata/v1/";
	public static String groupUserHost;//配置文件上内网ip
	public static String gtdataOutHost;//配置文件上外网ip，普通用户使用 ，不用
	public static String commonUserHost;//普通用户认证ip，不用
	public static String user;//配置文件上   
	public static String password;//配置文件上
	public static String userSize;//用户注册云盘的容量byte
	
    public static String sign;//组用户登录签名
    public static String signTime;//组用户登录失败
    public static String token;//组用户登录令牌
    
    static {
        try {
        	String file = "Rsmartgtcloudos";
            groupUserHost = new String(RSAEncrypt.decrypt(RSAEncrypt.loadPublicKeyByStrDefault(), Base64.decode(StringTools.getValueFromProperties(file, "groupUserHost"))));
            commonUserHost =new String(RSAEncrypt.decrypt(RSAEncrypt.loadPublicKeyByStrDefault(), Base64.decode(StringTools.getValueFromProperties(file, "commonUserHost"))));
            gtdataOutHost =new String(RSAEncrypt.decrypt(RSAEncrypt.loadPublicKeyByStrDefault(), Base64.decode(StringTools.getValueFromProperties(file, "gtdataOutHost")))); 
            user =new String(RSAEncrypt.decrypt(RSAEncrypt.loadPublicKeyByStrDefault(), Base64.decode(StringTools.getValueFromProperties(file, "groupUserName")))); ;
            password =new String(RSAEncrypt.decrypt(RSAEncrypt.loadPublicKeyByStrDefault(), Base64.decode( StringTools.getValueFromProperties(file, "groupUserPassword"))));
            userSize= new String(RSAEncrypt.decrypt(RSAEncrypt.loadPublicKeyByStrDefault(), Base64.decode(StringTools.getValueFromProperties(file, "userSize")))); 
            updateGroupUserGetSignAndTime();//先登录获取sign和time
            //启动定时更新线程
            Timer timer = new Timer();
            timer.schedule(new RsmartRefreshGtdataGroupSignThread(), 100,1000*60*10);//10分钟刷新一次
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
   
    public static String getSign()
	{
    	//如果为空，就自动登录获取
    	if(StringTools.nil(sign))
    	{
    		updateGroupUserGetSignAndTime();
    	}
		return sign;
	}
	public static String getSignTime()
	{
		//如果为空，就自动登录获取
    	if(StringTools.nil(signTime))
    	{
    		updateGroupUserGetSignAndTime();
    	}
		return signTime;
	}
	
	/**
	 * 
	 * Description：集成登录的方法，一次性更新sign和Time
	 * @return       
	 *
	 */
	public static void updateGroupUserGetSignAndTime()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		try
		{
			map = groupUserGetToken();
			if(1 == (Integer)map.get("result"))
			{
				token = (String) map.get("token");
				String time = (String) map.get("time");
				//登录
				map = groupUserLogin(time,  token);
				if(1 == (Integer)map.get("result"))
				{
					sign = (String) map.get("sign");
					signTime = (String) map.get("time");
				}else if(96 == (Integer)map.get("result"))//已经登录了,但是没有sign和time，就退出重新登录获取
				{
					map = groupUserLogout(token);
					if(1 ==  (Integer)map.get("result"))
					{
						 updateGroupUserGetSignAndTime();//再遍历一次，就到了上面登录
					}
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	 
	
	/**
     * 
     * Description：组用户获取令牌
     * @return  {"time" : "1413186359","token" : "d7483483f86bb262dac0e4da4cd1681e"}
     *
     */
    public static Map<String, Object> groupUserGetToken()
    {
        Map<String, Object> map = new HashMap<String, Object>();
        try
		{
	        String url = groupUserHost+"?op=GETTOKEN&user="+user;
	        String jsonresult = doGet(url);
	        if (StringTools.nil(jsonresult)) {
	            map.put("result", 99);
	            map.put("message", "组用户获取令牌失败！");
	            return map;
	        } else {
	            if (jsonresult.indexOf("time") >= 0 && jsonresult.indexOf("token") >= 0)
	            {
	            	JSONObject jsonObject = JSONObject.fromObject(jsonresult);
	                map.put("result", 1);
	                map.put("message", "获取组用户令牌成功！");
	                map.put("time", jsonObject.getString("time"));
	                map.put("token", jsonObject.getString("token"));
	                return map;
	            } else 
	            {
	            	 map.put("result", 99);
	                 map.put("message", "组用户获取令牌失败！");
	                 return map;
	            }
	        }
		} catch (Exception e)
		{
			e.printStackTrace();
			map.put("result", 99);
            map.put("message", "组用户获取令牌失败！");
            return map;
		}
    }

   /**
    * Description：组用户登录
    * @param time Gettoken返回的时间
    * @param token Gettoken返回的token
    * @return       
    *
    */
    public static Map<String, Object> groupUserLogin(String time, String token)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        try
		{
	        String url = groupUserHost+"?op=LOGIN&user="+user+"&time="+time+"&token="+token+"&pass="+PubFun.MD5(PubFun.MD5(password)+token);
	        String jsonresult = doGet(url);
	        if (StringTools.nil(jsonresult)) {
	            map.put("result", 99);
	            map.put("message", "组用户登录失败！");
	            return map;
	        } else {
	            if (jsonresult.indexOf("sign") >= 0 && jsonresult.indexOf("time") >= 0)
	            {
	            	JSONObject jsonObject = JSONObject.fromObject(jsonresult);
	                map.put("result", 1);//
	                map.put("message", "组用户登录成功！");
	                map.put("time", jsonObject.getString("time"));
	                map.put("sign", jsonObject.getString("sign"));
	                return map;
	            } else if (jsonresult.indexOf("GTDataException") >= 0)// 报错了
	            {
	                if (jsonresult.indexOf("3002") >= 0) {
	                    map.put("result", 98);
	                    map.put("message", "用户名或密码错误！");
	                    return map;
	                } else if (jsonresult.indexOf("3003") >= 0) {
	                    map.put("result", 97);
	                    map.put("message", "Token令牌不正确！");
	                    return map;
	                }else if (jsonresult.indexOf("3004") >= 0) {
	                    map.put("result", 96);
	                    map.put("message", "Token令牌已经登录了！");
	                    return map;
	                } else if (jsonresult.indexOf("3005") >= 0) {
	                    map.put("result", 95);
	                    map.put("message", "请求登录url已经失效了！");
	                    return map;
	                }else {
	                    map.put("result", 99);
	                    map.put("message", "组用户登录失败！");
	                    return map;
	                }
	            } else 
	            {
	            	 map.put("result", 99);
	            	 map.put("message", "组用户登录失败！");
	                 return map;
	            }
	        }
		} catch (Exception e)
		{
			e.printStackTrace();
			map.put("result", 99);
			map.put("message", "组用户登录失败！");
            return map;
		}
    }
    
    /**
     * Description：获取到组用户已经登录的sign，该方法可能是用于更新sign的
     * @param sign Login（或getsign）返回的Sign签名
     * @param time Login（或getsign）返回的时间
     * @return       
     *
     */
     public static Map<String, Object> groupUserGetLoginedSign(String sign, String time)
     {
         Map<String, Object> map = new HashMap<String, Object>();
         try
 		{
 	        String url = groupUserHost+"?op=GETSIGN&sign="+sign+"&time="+time;
 	        String jsonresult = doGet(url);
 	        if (StringTools.nil(jsonresult)) {
 	            map.put("result", 99);
 	            map.put("message", "组用户获取登录sign失败！");
 	            return map;
 	        } else {
 	            if (jsonresult.indexOf("sign") >= 0 && jsonresult.indexOf("time") >= 0)
 	            {
 	            	JSONObject jsonObject = JSONObject.fromObject(jsonresult);
 	                map.put("result", 1);//
 	                map.put("message", "组用户获取sign成功！");
 	                map.put("time", jsonObject.getString("time"));
 	                map.put("sign", jsonObject.getString("sign"));
 	                return map;
 	            } else if (jsonresult.indexOf("GTDataException") >= 0)// 报错了
 	            {
 	                if (jsonresult.indexOf("3005") >= 0) {
 	                    map.put("result", 98);
 	                    map.put("message", "请求url已经失效了！");
 	                    return map;
 	                } else if (jsonresult.indexOf("3006") >= 0) {
 	                    map.put("result", 97);
 	                    map.put("message", "该sign没有登录！");
 	                    return map;
 	                }else if (jsonresult.indexOf("3007") >= 0) {
 	                    map.put("result", 96);
 	                    map.put("message", "sign无效！");
 	                    return map;
 	                }else {
 	                    map.put("result", 99);
 	                    map.put("message", "组用户获取登录sign失败！");
 	                    return map;
 	                }
 	            } else 
 	            {
 	            	 map.put("result", 99);
 	            	 map.put("message", "组用户获取登录sign失败！");
 	                 return map;
 	            }
 	        }
 		} catch (Exception e)
 		{
 			e.printStackTrace();
 			map.put("result", 99);
 			map.put("message", "组用户获取登录sign失败！");
            return map;
 		}
     }
    
     /**
      * Description：组用户退出登录
      * @param token Gettoken返回的token
      * @return       
      *
      */
      public static Map<String, Object> groupUserLogout(String token)
      {
          Map<String, Object> map = new HashMap<String, Object>();
          try
  		{
  	        String url = groupUserHost+"?op=LOGOUT&token="+token;
  	        String jsonresult = doGet(url);
  	        if (StringTools.nil(jsonresult)) {
  	            map.put("result", 99);
  	            map.put("message", "组用户退出登录失败！");
  	            return map;
  	        } else {
  	            if (jsonresult.indexOf("GTDataException") >= 0)// 报错了
  	            {
	                map.put("result", 99);
	                map.put("message", "组用户退出登录失败！");
	                return map;
  	            } else 
  	            {
  	            	 map.put("result", 1);
  	            	 map.put("message", "组用户退出登录成功！");
  	                 return map;
  	            }
  	        }
  		} catch (Exception e)
  		{
  			e.printStackTrace();
  			map.put("result", 99);
  			map.put("message", "组用户退出登录出错！");
            return map;
  		}
      }
      
      /**
       * 
       * Description：组用户注册普通用户  ，该api需要组用户登录先，然后组用户的sign和time注册普通用户
       * @param user  普通用户名
       * @param pass  MD5（密码） ,请传已经加密的
       * @param total 普通用户空间的总尺寸（单位bytes）
       * @param sign  Login（或getsign）返回的Sign签名
       * @param time  Login（或getsign）返回的时间
       * @return       
       *
       */
	   public static Map<String, Object> groupUserRegisterCommonUser(String user, String pass, String total, String sign, String time)
	   {
	      Map<String, Object> map = new HashMap<String, Object>();
	      try
		  {
	        String url = groupUserHost+"?op=GREG&user="+user+"&time="+time+"&sign="+sign+"&pass="+pass+"&total="+total;
	        String jsonresult = doGet(url);
	        if (StringTools.nil(jsonresult)) {
	            map.put("result", 99);
	            map.put("message", "普通用户注册失败！");
	            return map;
	        } else {
	            if (jsonresult.indexOf("GREG") >= 0)
	            {
	            	JSONObject jsonObject = JSONObject.fromObject(jsonresult);
	            	if(true == (jsonObject.getBoolean("GREG")))
	            	{
	            		map.put("result", 1);//
		                map.put("message", "普通用户注册gtdata成功！");
		                return map;
	            	}
	            	map.put("result", 99);
	 	            map.put("message", "普通用户注册失败！");
	 	            return map;
	            } else if (jsonresult.indexOf("GTDataException") >= 0)// 报错了
	            {
	                if (jsonresult.indexOf("2001") >= 0) {
	                    map.put("result", 98);
	                    map.put("message", "请求参数错误！");
	                    return map;
	                } else if (jsonresult.indexOf("2007") >= 0) {
	                    map.put("result", 94);
	                    map.put("message", "该普通用户已经注册！");
	                    return map;
	                }else if (jsonresult.indexOf("3007") >= 0) {
	                    map.put("result", 97);
	                    map.put("message", "sign无效！");
	                    return map;
	                }else if (jsonresult.indexOf("3006") >= 0) {
	                    map.put("result", 96);
	                    map.put("message", "组用户没有登录！");
	                    return map;
	                } else if (jsonresult.indexOf("3005") >= 0) {
	                    map.put("result", 95);
	                    map.put("message", "请求url已经失效了！");
	                    return map;
	                }else {
	                    map.put("result", 99);
	                    map.put("message", "普通用户注册失败！");
	                    return map;
	                }
	            } else 
	            {
	            	 map.put("result", 99);
	                 map.put("message", "普通用户注册失败！");
	                 return map;
	            }
	        }
		} catch (Exception e)
		{
			e.printStackTrace();
			map.put("result", 99);
			map.put("message", "普通用户注册失败！");
	        return map;
		}
	   }
      
	   /**
       * 
       * Description：组用户删除普通用户，该api需要组用户登录先，然后组用户的sign和time删除普通用户
       * @param user  普通用户名
       * @param sign  Login（或getsign）返回的Sign签名
       * @param time  Login（或getsign）返回的时间
       * @return       
       *
       */
	   public static Map<String, Object> groupUserDeleteCommonUser(String user, String sign, String time)
	   {
	      Map<String, Object> map = new HashMap<String, Object>();
	      try
		  {
	        String url = groupUserHost+"?op=GDELUSER&user="+user+"&time="+time+"&sign="+sign;
	        String jsonresult = doGet(url);
	        if (StringTools.nil(jsonresult)) {
	            map.put("result", 99);
	            map.put("message", "普通用户删除失败！");
	            return map;
	        } else {
	            if (jsonresult.indexOf("GDELUSER") >= 0)
	            {
	            	JSONObject jsonObject = JSONObject.fromObject(jsonresult);
	            	if(true == (jsonObject.getBoolean("GDELUSER")))
	            	{
	            		map.put("result", 1);//
		                map.put("message", "普通用户删除成功！");
		                return map;
	            	}
	            	map.put("result", 99);
	                map.put("message", "普通用户删除失败！");
	                return map;
	            } else if (jsonresult.indexOf("GTDataException") >= 0)// 报错了
	            {
	                if (jsonresult.indexOf("2001") >= 0) {
	                    map.put("result", 98);
	                    map.put("message", "请求参数错误！");
	                    return map;
	                } else if (jsonresult.indexOf("3007") >= 0) {
	                    map.put("result", 97);
	                    map.put("message", "sign无效！");
	                    return map;
	                }else if (jsonresult.indexOf("3006") >= 0) {
	                    map.put("result", 96);
	                    map.put("message", "组用户没有登录！");
	                    return map;
	                } else if (jsonresult.indexOf("3005") >= 0) {
	                    map.put("result", 95);
	                    map.put("message", "请求url已经失效了！");
	                    return map;
	                }else {
	                    map.put("result", 99);
	                    map.put("message", "普通用户删除失败！");
	                    return map;
	                }
	            } else 
	            {
	            	 map.put("result", 99);
	            	 map.put("message", "普通用户删除失败！");
	                 return map;
	            }
	        }
		} catch (Exception e)
		{
			e.printStackTrace();
			map.put("result", 99);
			map.put("message", "普通用户删除失败！");
	        return map;
		}
	   }
	   /**
       * 
       * Description：组用户修改组用户，自己的密码，该api需要组用户登录先，然后组用户的sign和time修改
       * @param newpass  MD5（新密码）  已加密的
       * @param sign  Login（或getsign）返回的Sign签名
       * @param time  Login（或getsign）返回的时间
       * @return       
       *
       */
	   public static Map<String, Object> groupUserChangePassword(String newpass, String sign, String time)
	   {
	      Map<String, Object> map = new HashMap<String, Object>();
	      try
		  {
	        String url = groupUserHost+"?op=CHANGEPWD&newpass="+newpass+"&time="+time+"&sign="+sign;
	        String jsonresult = doGet(url);
	        if (StringTools.nil(jsonresult)) {
	            map.put("result", 99);
	            map.put("message", "组用户修改自己的密码失败！");
	            return map;
	        } else {
	            if (jsonresult.indexOf("CHANGEPWD") >= 0)
	            {
	            	JSONObject jsonObject = JSONObject.fromObject(jsonresult);
	            	if(true == (jsonObject.getBoolean("CHANGEPWD")))
	            	{
	            		map.put("result", 1);//
		                map.put("message", "组用户修改自己的密码成功！");
		                return map;
	            	}
	            	map.put("result", 99);
	                map.put("message", "组用户修改自己的密码失败！");
	                return map;
	            } else if (jsonresult.indexOf("GTDataException") >= 0)// 报错了
	            {
	                if (jsonresult.indexOf("2001") >= 0) {
	                    map.put("result", 98);
	                    map.put("message", "请求参数错误！");
	                    return map;
	                } else if (jsonresult.indexOf("3007") >= 0) {
	                    map.put("result", 97);
	                    map.put("message", "sign无效！");
	                    return map;
	                }else if (jsonresult.indexOf("3006") >= 0) {
	                    map.put("result", 96);
	                    map.put("message", "组用户没有登录！");
	                    return map;
	                } else if (jsonresult.indexOf("3005") >= 0) {
	                    map.put("result", 95);
	                    map.put("message", "请求url已经失效了！");
	                    return map;
	                }else {
	                    map.put("result", 99);
	                    map.put("message", "组用户修改自己的密码失败！");
	                    return map;
	                }
	            } else 
	            {
	            	 map.put("result", 99);
	            	 map.put("message", "组用户修改自己的密码失败！");
	                 return map;
	            }
	        }
		} catch (Exception e)
		{
			e.printStackTrace();
			map.put("result", 99);
			map.put("message", "组用户修改自己的密码失败！");
	        return map;
		}
	   }
	   
	  /**
       * 
       * Description：组用户修改普通用户的密码，该api需要组用户登录先，然后组用户的sign和time修改普通用户的密码
       * @param user  普通用户名
       * @param newpass  MD5（新密码）  已加密的
       * @param sign  Login（或getsign）返回的Sign签名
       * @param time  Login（或getsign）返回的时间
       * @return       
       *
       */
	   public static Map<String, Object> groupUserChangeCommonUserPassword(String user, String newpass, String sign, String time)
	   {
	      Map<String, Object> map = new HashMap<String, Object>();
	      try
		  {
	        String url = groupUserHost+"?op=GCHANGEPWD&newpass="+newpass+"&time="+time+"&sign="+sign+"&user="+user;
	        String jsonresult = doGet(url);
	        if (StringTools.nil(jsonresult)) {
	            map.put("result", 99);
	            map.put("message", "组用户修改普通用户的密码失败！");
	            return map;
	        } else {
	            if (jsonresult.indexOf("GCHANGEPWD") >= 0)
	            {
	            	JSONObject jsonObject = JSONObject.fromObject(jsonresult);
	            	if(true == (jsonObject.getBoolean("GCHANGEPWD")))
	            	{
	            		map.put("result", 1);//
		                map.put("message", "组用户修改普通用户的密码成功！");
		                return map;
	            	}
	            	map.put("result", 99);
	                map.put("message", "组用户修改普通用户的密码失败！");
	                return map;
	            } else if (jsonresult.indexOf("GTDataException") >= 0)// 报错了
	            {
	                if (jsonresult.indexOf("2001") >= 0) {
	                    map.put("result", 98);
	                    map.put("message", "请求参数错误！");
	                    return map;
	                } else if (jsonresult.indexOf("3007") >= 0) {
	                    map.put("result", 97);
	                    map.put("message", "sign无效！");
	                    return map;
	                }else if (jsonresult.indexOf("3006") >= 0) {
	                    map.put("result", 96);
	                    map.put("message", "组用户没有登录！");
	                    return map;
	                } else if (jsonresult.indexOf("3005") >= 0) {
	                    map.put("result", 95);
	                    map.put("message", "请求url已经失效了！");
	                    return map;
	                }else {
	                    map.put("result", 99);
	                    map.put("message", "组用户修改普通用户的密码失败！");
	                    return map;
	                }
	            } else 
	            {
	            	 map.put("result", 99);
	            	 map.put("message", "组用户修改普通用户的密码失败！");
	                 return map;
	            }
	        }
		} catch (Exception e)
		{
			e.printStackTrace();
			map.put("result", 99);
			map.put("message", "组用户修改普通用户的密码失败！");
	        return map;
		}
	 }
	 
	  /**
       * 
       * Description：组用户修改普通用户容量，该api需要组用户登录先，然后组用户的sign和time修改普通用户的容量
       * @param user  普通用户名
       * @param newsize  新容量尺寸（单位bytes）
       * @param sign  Login（或getsign）返回的Sign签名
       * @param time  Login（或getsign）返回的时间
       * @return       
       *
       */
	   public static Map<String, Object> groupUserChangeCommonUserSize(String user, String newsize, String sign, String time)
	   {
	      Map<String, Object> map = new HashMap<String, Object>();
	      try
		  {
	        String url = groupUserHost+"?op=GCHANGESIZE&newsize="+newsize+"&time="+time+"&sign="+sign+"&user="+user;
	        String jsonresult = doGet(url);
	        if (StringTools.nil(jsonresult)) {
	            map.put("result", 99);
	            map.put("message", "组用户修改普通用户的容量失败！");
	            return map;
	        } else {
	            if (jsonresult.indexOf("GCHANGESIZE") >= 0)
	            {
	            	JSONObject jsonObject = JSONObject.fromObject(jsonresult);
	            	if(true == (jsonObject.getBoolean("GCHANGESIZE")))
	            	{
	            		map.put("result", 1);//
		                map.put("message", "组用户修改普通用户的容量成功！");
		                return map;
	            	}
	            	map.put("result", 99);
	                map.put("message", "组用户修改普通用户的容量失败！");
	                return map;
	            } else if (jsonresult.indexOf("GTDataException") >= 0)// 报错了
	            {
	                if (jsonresult.indexOf("2001") >= 0) {
	                    map.put("result", 98);
	                    map.put("message", "请求参数错误！");
	                    return map;
	                } else if (jsonresult.indexOf("3007") >= 0) {
	                    map.put("result", 97);
	                    map.put("message", "sign无效！");
	                    return map;
	                }else if (jsonresult.indexOf("3006") >= 0) {
	                    map.put("result", 96);
	                    map.put("message", "组用户没有登录！");
	                    return map;
	                } else if (jsonresult.indexOf("3005") >= 0) {
	                    map.put("result", 95);
	                    map.put("message", "请求url已经失效了！");
	                    return map;
	                }else {
	                    map.put("result", 99);
	                    map.put("message", "组用户修改普通用户的容量失败！");
	                    return map;
	                }
	            } else 
	            {
	            	 map.put("result", 99);
	            	 map.put("message", "组用户修改普通用户的容量失败！");
	                 return map;
	            }
	        }
		} catch (Exception e)
		{
			e.printStackTrace();
			map.put("result", 99);
			map.put("message", "组用户修改普通用户的容量失败！");
	        return map;
		}
	   }
	   
	 /**
       * 
       * Description：组用户获取普通用户容量等信息，该api需要组用户登录先，然后组用户的sign和time查询普通用户信息
       * @param user  普通用户名
       * @param sign  Login（或getsign）返回的Sign签名
       * @param time  Login（或getsign）返回的时间
       * @return       
       *
       */
	   public static Map<String, Object> groupUserGetCommonUserInfo(String user,  String sign, String time)
	   {
	      Map<String, Object> map = new HashMap<String, Object>();
	      try
		  {
	        String url = groupUserHost+"?op=GGETUSERINFO&time="+time+"&sign="+sign+"&user="+user;
	        String jsonresult = doGet(url);
	        //System.out.println(jsonresult);
	        if (StringTools.nil(jsonresult)) {
	            map.put("result", 99);
	            map.put("message", "组用户获取普通用户的信息失败！");
	            return map;
	        } else {
	            if (jsonresult.indexOf("UserInfo") >= 0)
	            {
	            	JSONObject jsonObject = JSONObject.fromObject(jsonresult);
	            	JSONObject jsonObject2 = jsonObject.getJSONObject("UserInfo");
	            	map.put("Group", jsonObject2.getString("Group"));
	            	map.put("Role", jsonObject2.getString("Role"));
	            	map.put("TotalSize", jsonObject2.getString("TotalSize"));
	            	map.put("UsedSize", jsonObject2.getString("UsedSize"));
	            	map.put("UserName", jsonObject2.getString("UserName"));
	            	map.put("result", 1);
	                map.put("message", "组用户获取普通用户的信息成功！");
	                return map;
	            } else if (jsonresult.indexOf("GTDataException") >= 0)// 报错了
	            {
	                if (jsonresult.indexOf("2001") >= 0) {
	                    map.put("result", 98);
	                    map.put("message", "请求参数错误！");
	                    return map;
	                } else if (jsonresult.indexOf("3007") >= 0) {
	                    map.put("result", 97);
	                    map.put("message", "sign无效！");
	                    return map;
	                }else if (jsonresult.indexOf("3006") >= 0) {
	                    map.put("result", 96);
	                    map.put("message", "组用户没有登录！");
	                    return map;
	                } else if (jsonresult.indexOf("3005") >= 0) {
	                    map.put("result", 95);
	                    map.put("message", "请求url已经失效了！");
	                    return map;
	                }else {
	                    map.put("result", 99);
	                    map.put("message", "组用户获取普通用户的信息失败！");
	                    return map;
	                }
	            } else 
	            {
	            	 map.put("result", 99);
	            	 map.put("message", "组用户获取普通用户的信息失败！");
	                 return map;
	            }
	        }
		} catch (Exception e)
		{
			e.printStackTrace();
			map.put("result", 99);
			map.put("message", "组用户获取普通用户的信息失败！");
	        return map;
		}
	 }
		   
    /**
     * 
     * Description：get基础请求
     * 
     * @param url
     *            请求地址
     * @return 请求成功后的结果
     * 
     */
    public static String doGet(String url) {
        InputStream in;
        String str = "";

        HttpResponse response;
        try {
        	//System.out.println(url);
            HttpGet httpGet = new HttpGet(url);

            /*
             * httpGet.setHeader("Content-Type", "application/json;");
             * httpGet.setHeader("Accept-Encoding", "gzip,deflate,sdch");
             * httpGet.setHeader("Accept-Language:", "zh-CN,zh;");
             */

            response = new DefaultHttpClient().execute(httpGet);
            if (response != null) {
                response.getAllHeaders();
                int statusCode = response.getStatusLine().getStatusCode();
                in = response.getEntity().getContent();

                if (in != null) {
                    str = inputStream2String(in);
                    in.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return str;
        }

        return str;
    }

 
    /**
     * 
     * Description:将输入流转为字符串
     * 
     * @param in
     * @return
     * @throws IOException
     * 
     */
    public static String inputStream2String(InputStream in) throws IOException {
        StringBuffer out = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(in,
                "utf-8"));
        char[] b = new char[4096];
        for (int n; (n = br.read(b)) != -1;) {
            out.append(new String(b, 0, n));
        }
        //System.out.println(out.toString());
        return out.toString();
    }
    
    /**
	 * 
	 * Description：获取用户个人登录重定向路径
	 * 
	 * @param file
	 * @param in
	 * @throws Exception
	 * 
	 */
	public static String getuserLoginUrl(String username,String userTime,String userToken,String userPassword) throws Exception {
//		“http://<HOST>:<PORT>/?op=LOGIN[&user=<user name>][&time=<time>][&token=<token>][&pass=<password>]"
		String url = commonUserHost + "?op=LOGIN&user="+username+"&time="+userTime+"&token="+userToken+"&pass="+PubFun.MD5(userPassword+userToken);
		return url;
	}
    
    public static void main(String[] args)
	{
    	//System.out.println(groupUserDeleteCommonUser("1122", GtdataGroupVerifyUtil.getSign(), GtdataGroupVerifyUtil.getSignTime()));
    	//System.out.println(groupUserRegisterCommonUser("1", PubFun.MD5("ljw123"), "5000000000", GtdataGroupVerifyUtil.getSign(), GtdataGroupVerifyUtil.getSignTime()));
    	//System.out.println(groupUserDeleteCommonUser("1", GtdataGroupVerifyUtil.getSign(), GtdataGroupVerifyUtil.getSignTime()));
    	//System.out.println(groupUserGetCommonUserInfo("1",GtdataGroupVerifyUtil.getSign(), GtdataGroupVerifyUtil.getSignTime()));
    	//System.out.println(groupUserRegisterCommonUser("WinD", PubFun.MD5("li556600"), "18446744073709551616", GtdataGroupVerifyUtil.getSign(), GtdataGroupVerifyUtil.getSignTime()));
    	//System.out.println(groupUserDeleteCommonUser("WinD", GtdataGroupVerifyUtil.getSign(), GtdataGroupVerifyUtil.getSignTime()));
    	System.out.println(RsmartGtdataGroupVerifyUtil.getSign()+","+RsmartGtdataGroupVerifyUtil.getSignTime());
	}

}
