package com.cloudbox.utils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Description: 个人中心的我的空间里，对文件的操作就是请求gtdata的操作，增加安全验证之后的接口
 * 
 * @author ljf 2015-12-18
 * 
 * @version v1.2
 */
public class RsmartGtdataEncryptionUtil {
	private static Logger logger = LoggerFactory.getLogger(RsmartGtdataEncryptionUtil.class);
	private static String agent_gtdataHost;
	static {
		try {
			String file = "Rsmartgtcloudos";
			agent_gtdataHost = new String(RSAEncrypt.decrypt(RSAEncrypt.loadPublicKeyByStrDefault(), Base64.decode(StringTools.getValueFromProperties(file, "agent_gtdataHost"))));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	* Description: 从指定位置大小获取加密数据
	* @param filePath
	* @param localFileLenght
	* @return Map<String,Object><br>
	* @author lishun 
	* @date 2016-9-8 下午3:29:21
	 */
	public static Map<String,Object> getDownDataStreamMap(String filePath,long localFileLenght) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String url = agent_gtdataHost+filePath+"?op=DOWNDATA&sign="+RsmartGtdataGroupVerifyUtil.getSign()
				+"&time="+RsmartGtdataGroupVerifyUtil.getSignTime();
		map = doGetFile(url,localFileLenght);
		return map;
	}
	/**
	* Description: 获取秘钥文件
	* @param filePath
	* @throws IOException  
	* @return Map<String,Object><br>
	* @author lishun 
	* @date 2016-9-8 下午3:30:02
	 */
	public static Map<String,Object> getDownKeyStreamMap(String filePath) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String url = agent_gtdataHost+filePath+"?op=DOWNKEY&sign="+RsmartGtdataGroupVerifyUtil.getSign()
				+"&time="+RsmartGtdataGroupVerifyUtil.getSignTime();
		map = doGetFile(url,0);
		return map;
	}
	/**
	 * 
	* Description: 获取文件大小
	* @param filePath
	* @return
	* @return long<br>
	*
	* @author lishun 
	 * @throws Exception 
	* @date 2016-9-8 下午3:30:21
	 */
	public static long getDownDataSize(String filePath) throws Exception{
		String url = agent_gtdataHost+filePath+"?op=DOWNDATA&sign="+RsmartGtdataGroupVerifyUtil.getSign()
				+"&time="+RsmartGtdataGroupVerifyUtil.getSignTime();
		return doGetFileSize(url);
	}
	/**
	 * 
	* Description: 请求加密
	* @param path 请求url路径<br>
	* 异常信息：<br>	
	* 			2008  Down file's num over limit<br>
				2001  请求参数错误<br>
				2003  <PATH>does not exist<br>
				3005  URL has been timeout<br>
				3006  Not login<br>
				3007  Sign is invalid<br>
	* @return Map<String,String><br>
	*
	* @author lishun 
	* @date 2016-9-8 上午10:55:37
	 */
	public static Map<String,String> prepareData(String path){
		Map<String,String> map = new HashMap<String,String>();
		String url = agent_gtdataHost+path+"?op=PREPAREDATA&sign="+RsmartGtdataGroupVerifyUtil.getSign()+"&time="
				+RsmartGtdataGroupVerifyUtil.getSignTime(); 
		try {
			String jsonresult=doGet(url);
			if(StringTools.nil(jsonresult)){
				map.put("result", "0");
				logger.info("prepareData failed,jsonresult is null,url:"+url+",jsonresult:"+jsonresult);
			}else{
				Map<String, Object> resultMap=JsonUtil.toMap(jsonresult);
				if(resultMap.get("GTDataException")!=null){
					map.put("result", "0");
					logger.info("prepareData failed,jsonresult is null,url:"+url+",jsonresult:"+jsonresult);
				}else if(resultMap.get("PREPAREDATA")!=null){
					if("true".equals(resultMap.get("PREPAREDATA").toString())){
						map.put("result", "1");
						map.put("message", "request prepareData file success");
					}else{
						map.put("result", "0");
						logger.info("prepareData failed,resultMap:"+resultMap);
					}
					
				}else{
					map.put("result", "0");
					logger.info("prepareData failed,jsonresult:"+jsonresult);
				}
			}
		} catch (Exception e) {
			map.put("result", "0");
			logger.info("prepareData failed:"+e.getClass().getSimpleName()+":"+e.getMessage());
			e.printStackTrace();
		}
		return map;
	}
	/**
	 * 
	* Description: 删除代理服务器数据
	* @param path 请求url路径<br>
	* 异常信息：<br>	
	* 			2001  请求参数错误<br>
				2003  <PATH>does not exist<br>
				3005  URL has been timeout<br>
				3006  Not login<br>
				3007  Sign is invalid<br>
	* @return Map<String,String><br>
	* @author lishun 
	* @date 2016-9-8 上午11:06:42
	 */
	public static Map<String,String> deleteData(String path){
		Map<String,String> map = new HashMap<String,String>();
		String url = agent_gtdataHost+path+"?op=DELETEDATA&sign="+RsmartGtdataGroupVerifyUtil.getSign()
				+"&time="+RsmartGtdataGroupVerifyUtil.getSignTime();
		try {
			String jsonresult = doGet(url);
			if(StringTools.nil(jsonresult)){
				map.put("result", "0");
				logger.info("deleteData failed,jsonresult is null,url:"+url+",jsonresult:"+jsonresult);
			}else{
				Map<String, Object> resultMap=JsonUtil.toMap(jsonresult);
				if(resultMap.get("GTDataException")!=null){
					map.put("result", "0");
					map.put("message", "deleteData failed,jsonresult is null,url:"+url+",jsonresult:"+jsonresult);
				}else if(resultMap.get("DELETEDATA")!=null){
					if("true".equals(resultMap.get("DELETEDATA").toString())){
						map.put("result", "1");
						map.put("message", "request delete file success");
					}else{
						map.put("result", "0");
						map.put("message", "deleteData resultMap;"+resultMap);
					}
				}else{
					map.put("result", "0");
					map.put("message", "deleteData failed;"+jsonresult);
				}
			}
		} catch (Exception e) {
			map.put("result", "0");
			map.put("message", "deleteData failed;"+e.getMessage());
		}
		return map;
	}
	/**
	 * 
	* Description: 从指定位置大小获取加密数据
	* @param url 
	* @param localFileLenght 数据开始下载点
	* 异常信息：<br>	
	* 			2009  Encrypt file is not ready <br>	
				2010  PREPAREDATA failed or not call, please retry <br>	
				2001   请求参数错误 <br>	
				2003  <PATH>does not exist<br>	
				3005  URL has been timeout <br>	
				3006  Not login <br>	
				3007  Sign is invalid <br>	
	* @return Map<String,Object><br>
	* @author lishun 
	 * @throws Exception 
	* @date 2016-9-8 上午11:18:27
	 */
	private static Map<String,Object> doGetFile(String url,long localFileLenght) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		InputStream in;
		HttpResponse response;
		try{
			UriComponents uriComponents = UriComponentsBuilder.fromUriString(url).build();
			HttpGet httpGet = createHttpGet(uriComponents.toUri().toString());
			httpGet.addHeader("Range", "bytes=" + localFileLenght + "-");
			response = new DefaultHttpClient().execute(httpGet);
			if(response!=null){
				int statusCode = response.getStatusLine().getStatusCode();
				in = response.getEntity().getContent();
				if(statusCode==200||206==statusCode){//query inputStream success
					map.put("result", "1");
					map.put("inputStream",in);
				}else{//query inputStream failed
					String jsonresult = inputStream2String(in);//query error code
					if(StringTools.nil(jsonresult)){
						map.put("result", "0");
						logger.info("doGetFile_get failed,jsonresult is null,url:"+url+",jsonresult:"+jsonresult);
					}else{
						Map<String, Object> resultMap=JsonUtil.toMap(jsonresult);
						if(resultMap.get("GTDataException")!=null){
							map.put("result", "0");
							logger.info("doGetFile_get failed,jsonresult is null,url:"+url+",jsonresult:"+jsonresult);
						}else{
							map.put("result", "0");
							logger.info("doGetFile_get failed,jsonresult:"+jsonresult);
						}
					}
				}
			}else{
				logger.info("doGetFile_get fail response is url:"+url+",response:"+response);
			}
		}catch(Exception e){
			logger.error("doGetFile_get failed:"+e.getClass().getSimpleName()+":"+e.getMessage());
			throw e;
		}
		return map;
	}
	/**
	 * 
	* Description: 获取文件大小
	* @param url
	* @return long<br>
	* 		 	-1：停止下载（参照异常信息）-2:文件正在加密中，-3:连接异常，重新请求，-4:其他异常,停止下载
	* 异常信息：<br>	
	* 			2009  Encrypt file is not ready <br>	
				2010  PREPAREDATA failed or not call, please retry <br>	
				2001   请求参数错误 <br>	
				2003  <PATH>does not exist<br>	
				3005  URL has been timeout <br>	
				3006  Not login <br>	
				3007  Sign is invalid <br>	
	* @author lishun 
	 * @throws Exception 
	* @date 2016-9-8 下午12:22:19
	 */
	private static long doGetFileSize(String url) throws Exception {
		InputStream in;
		HttpResponse response;
		try {
			UriComponents uriComponents = UriComponentsBuilder.fromUriString(
					url).build();
			HttpGet delete = createHttpGet(uriComponents.toUri().toString());
			response = new DefaultHttpClient().execute(delete);
			if (response != null) {
				int statusCode = response.getStatusLine().getStatusCode();
				if (200 == statusCode||206 == statusCode) {
					return response.getEntity().getContentLength();
				}else{
					in=response.getEntity().getContent();
					String jsonresult=inputStream2String(in);
					if(StringTools.nil(jsonresult)){
						logger.info("doGetFileSie failed,jsonresult is null,url:"+url+",jsonresult:"+jsonresult);
						return -3;
					}else{
						Map<String, Object> resultMap=JsonUtil.toMap(jsonresult);
						if(resultMap.get("GTDataException")!=null){
							Map<String, Object> resultMapDetail=JsonUtil.toMap(resultMap.get("GTDataException"));
							String errorCode=resultMapDetail.get("errorCode").toString();
							if("2009".equals(errorCode)){
								return -2;//encrypting....
							}else{
								logger.info("doGetFileSie failed,jsonresult:"+jsonresult);
								return -1;
							}
						}else{
							logger.info("doGetFileSie failed,jsonresult:"+jsonresult);
							return -3;//返回错误信息为空，继续请求
						}
					}
				}
			}
			else{
				logger.info("getFileSize  fail response is url:"+url+",response:"+response);
				return -3;//返回错误信息为空，继续请求
			}
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 
	* Description: 获取返回json信息
	* @param url
	* @return String<br>
	* @author lishun 
	* @date 2016-9-8 下午3:43:36
	 */
	private static String doGet(String url) throws Exception {
		InputStream in=null;
		String str = "";
		try {
			UriComponents uriComponents = UriComponentsBuilder.fromUriString(url).build();
			HttpGet httpGet = createHttpGet(uriComponents.toUri().toString());
			HttpResponse response = new DefaultHttpClient().execute(httpGet);
			if (response != null) {
				in = response.getEntity().getContent();
				if (in != null) {
					str = inputStream2String(in);
				}else{
					logger.info("doGet failed,InputStream is null ");
				}
			}else{
				logger.info("doGet failed,response is null ");
			}
		} catch (Exception e) {
			throw e;
		}finally{
			if(in!=null){
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
		return str;
	}
	/**
	 * 
	* Description: 文件流转换字符串
	* @param in
	* @throws IOException  
	* @return String<br>
	* @author lishun 
	* @date 2016-9-8 下午2:42:07
	 */
	public static String inputStream2String(InputStream in) throws IOException {
		StringBuffer out = new StringBuffer();
		BufferedReader br = new BufferedReader(new InputStreamReader(in,"utf-8"));
		char[] b = new char[4096];
		for (int n; (n = br.read(b)) != -1;) {
			out.append(new String(b, 0, n));
		}
		return out.toString();
	}

	/**
	 * 
	* Description: create HttpGet
	* @param url
	* @return  
	* @return HttpGet<br>
	*
	* @author lishun 
	* @date 2016-9-8 下午3:42:16
	 */
	private static HttpGet createHttpGet(String url){
		HttpGet httpGet=new HttpGet(url);
		httpGet.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60000); 
		httpGet.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
		return  httpGet;
	}
}
