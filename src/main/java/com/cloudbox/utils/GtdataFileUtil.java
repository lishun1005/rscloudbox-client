package com.cloudbox.utils;



import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.FileRequestEntity;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.cloudbox.utils.file.FileTools;

/**
 * Description: 个人中心的我的空间里，对文件的操作就是请求gtdata的操作，增加安全验证之后的接口
 * 
 * @author ljw 2014-11-03
 * 
 * @version v1.1
 */
public class GtdataFileUtil {
	// private static String gtdataHost = "http://192.168.2.6:8001/gtdata/v1/";

	private static String gtdataHost;

	private static String groupUserHost;

	public static String getGtdataHost() {
		return gtdataHost;
	}

	public static String getGroupUserHost() {
		return groupUserHost;
	}

	/**
	 * 
	 * Description：根据目录查询该目录的所有文件和文件夹，如test文件夹 ，格式：/test
	 * 
	 * @param path
	 *            要查询的目录
	 * @param username
	 *            登录用户名字，用于去掉用户的路径
	 * @return
	 * @throws Exception
	 * 
	 */
	public static Map<String, Object> getAllFileByPath(String path,
			String username) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		List<GtdataFile> list = new ArrayList<GtdataFile>();
		String url = gtdataHost + path + "?op=LIST&recursive=false&sign="+ GtdataGroupVerifyUtil.getSign() + "&time="+ GtdataGroupVerifyUtil.getSignTime();
		String jsonresult = doGet(url);
		if (StringTools.nil(jsonresult)) {
			map.put("result", 99);// 文件不存在
			map.put("message", "查询错误！");
			return map;
		} else {
			JSONObject jsonObject = JSONObject.fromObject(jsonresult);
			// 有2种情况：错误GTDataException和files
			if (jsonresult.indexOf("GTDataException") >= 0)// 报错了
			{
				if (jsonresult.indexOf("2003") >= 0) {
					map.put("result", 98);// 文件不存在
					map.put("message", "文件夹不存在！");
					return map;
				} else {
					map.put("result", 99);
					map.put("message", "查询错误！");
					return map;
				}
			} else if (jsonresult.indexOf("files") >= 0) {
				JSONArray jsonArray = JSONArray.fromObject(JSONObject
						.fromObject(jsonresult).get("files"));
				for (int i = 0; i < jsonArray.size(); i++) {
					GtdataFile gtdataFile = new GtdataFile();
					JSONObject jsonObject2 = (JSONObject) jsonArray.get(i);
					String pathStr = jsonObject2.getString("path").substring(
							username.length() + 1);
					gtdataFile.setPath(pathStr);
					gtdataFile.setSize(jsonObject2.getString("size"));
					String timeString = jsonObject2.getString("time");
					gtdataFile.setTime(DateTools.timeToDateFormat(
							Long.parseLong(timeString + "000"),
							"yyyy-MM-dd HH:mm:ss"));
					gtdataFile.setFilename(gtdataFile.getPath().substring(
							gtdataFile.getPath().lastIndexOf("/") + 1));
					list.add(gtdataFile);
				}

				map.put("result", 1);// 文件不存在
				map.put("message", "查询成功！");
				map.put("list", list);
				return map;
			} else // 空文件夹
			{
				map.put("result", 1);
				map.put("message", "查询成功！");
				map.put("list", list);
				return map;
			}
		}
	}

	/**
	 * 
	 * Description：根据URL查询该目录的所有文件和文件夹，如test文件夹
	 * ，格式：http://192.168.2.7:8001/gtdata/v1/hello?op=LIST 查询hello文件夹
	 * 
	 * @param url
	 *            请求url
	 * @return
	 * @throws Exception
	 * 
	 */
	public static Map<String, Object> getAllFileByURL(String url)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		List<GtdataFile> list = new ArrayList<GtdataFile>();
		url = url + "&user=" + GtdataGroupVerifyUtil.user
				+ "&recursive=false&sign=" + GtdataGroupVerifyUtil.getSign()
				+ "&time=" + GtdataGroupVerifyUtil.getSignTime();
		String jsonresult = doGet(url);

		if (StringTools.nil(jsonresult)) {
			map.put("result", 99);// 文件不存在
			map.put("message", "查询错误！");
			return map;
		} else {
			JSONObject jsonObject = JSONObject.fromObject(jsonresult);
			// 有2种情况：错误GTDataException和files
			if (jsonresult.indexOf("GTDataException") >= 0)// 报错了
			{
				if (jsonresult.indexOf("2003") >= 0) {
					map.put("result", 98);// 文件不存在
					map.put("message", "文件夹不存在！");
					return map;
				} else {
					map.put("result", 99);// 文件不存在
					map.put("message", "查询错误！");
					return map;
				}
			} else if (jsonresult.indexOf("files") >= 0) {
				JSONArray jsonArray = JSONArray.fromObject(JSONObject
						.fromObject(jsonresult).get("files"));
				for (int i = 0; i < jsonArray.size(); i++) {
					GtdataFile gtdataFile = new GtdataFile();
					JSONObject jsonObject2 = (JSONObject) jsonArray.get(i);
					String pathStr = jsonObject2.getString("path");
					gtdataFile.setPath(pathStr);
					gtdataFile.setSize(jsonObject2.getString("size"));
					String timeString = jsonObject2.getString("time");
					gtdataFile.setTime(DateTools.timeToDateFormat(
							Long.parseLong(timeString + "000"),
							"yyyy-MM-dd HH:mm:ss"));
					gtdataFile.setFilename(gtdataFile.getPath().substring(
							gtdataFile.getPath().lastIndexOf("/") + 1));
					list.add(gtdataFile);
				}

				map.put("result", 1);
				map.put("message", "查询成功！");
				map.put("list", list);
				return map;
			} else // 空文件夹
			{
				map.put("result", 1);
				map.put("message", "查询成功！");
				map.put("list", list);
				return map;
			}
		}
	}

	/**
	 * 
	 * Description：根据目录查询该目录的所有文件和文件夹，包括子文件夹，子文件夹的文件和文件夹都会被查询出来，如test文件夹
	 * ，格式：test 包括子文件夹包括子文件夹包括子文件夹包括子文件夹包括子文件夹
	 * 
	 * @param path
	 *            要查询的目录
	 * @param username
	 *            登录用户名字，用于去掉用户的路径
	 * @return
	 * @throws Exception
	 * 
	 */
	public static Map<String, Object> getAllFileByPathWithChildPath(
			String path, String username) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		List<GtdataFile> list = new ArrayList<GtdataFile>();
		String url = gtdataHost + path + "?op=LIST&recursive=true&sign="
				+ GtdataGroupVerifyUtil.getSign() + "&time="
				+ GtdataGroupVerifyUtil.getSignTime();
		String jsonresult = doGet(url);
		if (StringTools.nil(jsonresult)) {
			map.put("result", 99);// 文件不存在
			map.put("message", "查询错误！");
			return map;
		} else {
			JSONObject jsonObject = JSONObject.fromObject(jsonresult);
			// 有2种情况：错误GTDataException和files
			if (jsonresult.indexOf("GTDataException") >= 0)// 报错了
			{
				if (jsonresult.indexOf("2003") >= 0) {
					map.put("result", 98);// 文件不存在
					map.put("message", "文件夹不存在！");
					return map;
				} else {
					map.put("result", 99);
					map.put("message", "查询错误！");
					return map;
				}
			} else if (jsonresult.indexOf("files") >= 0) {
				JSONArray jsonArray = JSONArray.fromObject(JSONObject
						.fromObject(jsonresult).get("files"));
				list = formatJsonToFilesList(jsonArray, username);
				map.put("result", 1);// 文件不存在
				map.put("message", "查询成功！");
				map.put("list", list);
				return map;
			} else // 空文件夹
			{
				map.put("result", 1);
				map.put("message", "查询成功！");
				map.put("list", list);
				return map;
			}
		}
	}

	/**
	 * 
	 * Description：在LIST的包含子文件夹的查询中，要递归获取子文件夹信息
	 * 
	 * @param jsonArray
	 *            查询的目录json数组
	 * @return
	 * 
	 */
	private static List<GtdataFile> formatJsonToFilesList(JSONArray jsonArray,
			String username) {
		List<GtdataFile> listAll = new ArrayList<GtdataFile>();
		try {
			for (int i = 0; i < jsonArray.size(); i++) {
				GtdataFile gtdataFile = new GtdataFile();
				JSONObject jsonObject2 = (JSONObject) jsonArray.get(i);
				// 如果是文件夹，就添加到gtdataFile 的child中
				if (null != jsonObject2.get("files"))// 是子目录
				{
					JSONArray jsonArray2 = JSONArray.fromObject(jsonObject2
							.getJSONArray("files"));
					List<GtdataFile> child = new ArrayList<GtdataFile>();
					child = formatJsonToFilesList(jsonArray2, username);
					gtdataFile.setChild(child);
				}
				String pathStr = jsonObject2.getString("path").substring(
						username.length() + 1);
				gtdataFile.setPath(pathStr);
				gtdataFile.setSize(jsonObject2.getString("size"));
				String timeString = jsonObject2.getString("time");
				gtdataFile.setTime(DateTools.timeToDateFormat(
						Long.parseLong(timeString + "000"),
						"yyyy-MM-dd HH:mm:ss"));
				gtdataFile.setFilename(gtdataFile.getPath().substring(
						gtdataFile.getPath().lastIndexOf("/") + 1));
				listAll.add(gtdataFile);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return listAll;
		}
		return listAll;
	}

	/**
	 * 
	 * Description：根据目录查询该目录的所有文件的大小，包含子文件夹的文件目录大小，如test/hello 文件夹
	 * ，格式：http://192.168.2.7:8001/gtdata/v1/hello?op=LIST 查询hello文件夹
	 * 
	 * @param path
	 *            要查询的文件夹路径 如 test/hello
	 * @param gtdataHost
	 *            gtdata的ip ，如 http://192.168.2.7:8001/gtdata/v1/
	 * @return
	 * @throws Exception
	 * 
	 */
	public static Map<String, Object> getAllFileSizeByPath(String path,
			String gtdataHost) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> dMap = new HashMap<String, Object>();
		Long allSize = 0L;
		String firstUrl = gtdataHost + path + "?op=LIST&user="
				+ GtdataGroupVerifyUtil.user + "&recursive=false&sign="
				+ GtdataGroupVerifyUtil.getSign() + "&time="
				+ GtdataGroupVerifyUtil.getSignTime();
		dMap = getAllFileByURL(firstUrl);
		if (1 != (Integer) dMap.get("result")) {
			return dMap;
		}
		List<GtdataFile> firstList = (List<GtdataFile>) dMap.get("list");
		if (null == firstList || firstList.size() <= 0) {
			allSize = 0L;
			map.put("result", 1);
			map.put("message", "查询文件夹大小成功！！");
			map.put("size", allSize);
			return map;
		}
		for (int i = 0; i < firstList.size(); i++) {
			if (StringTools.nil(firstList.get(i).getSize())) {
				continue;
			}
			Long childSize = Long.parseLong(firstList.get(i).getSize());
			// System.out.println(childSize);
			if (-1 == childSize)// 如果是文件夹继续循环
			{
				String childpath = firstList.get(i).getPath().substring(1);
				Map<String, Object> cMap = getAllFileSizeByPath(childpath,
						gtdataHost);// 递归
				if (1 == (Integer) dMap.get("result")) {
					allSize = allSize + (Long) cMap.get("size");
				}
			} else {
				allSize = allSize + childSize;
			}
		}
		map.put("result", 1);// 文件不存在
		map.put("message", "查询文件夹大小成功！！");
		map.put("size", allSize);
		return map;
	}

	/**
	 * 
	 * Description：判断文件或文件夹是否存在
	 * 
	 * @param path
	 *            文件或文件夹的路径，如，/2/2/1.txt
	 * @return
	 * @throws Exception
	 * 
	 */
	public static Map<String, Object> isFileOrDirExist(String path)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		List<GtdataFile> list = new ArrayList<GtdataFile>();

		String url = gtdataHost + path + "?op=LIST&user="
				+ GtdataGroupVerifyUtil.user + "&recursive=false&sign="
				+ GtdataGroupVerifyUtil.getSign() + "&time="
				+ GtdataGroupVerifyUtil.getSignTime();
		String jsonresult = doGet(url);
		if (StringTools.nil(jsonresult)) {
			map.put("result", 99);// 文件不存在
			map.put("message", "查询错误！");
			return map;
		} else {
			JSONObject jsonObject = JSONObject.fromObject(jsonresult);
			// 有2种情况：错误GTDataException和files
			if (jsonresult.indexOf("GTDataException") >= 0)// 报错了
			{
				if (jsonresult.indexOf("2003") >= 0) {
					map.put("result", 98);// 文件不存在
					map.put("message", "文件夹不存在！");
					return map;
				} else {
					map.put("result", 99);// 文件不存在
					map.put("message", "查询错误！");
					return map;
				}
			} else if (jsonresult.indexOf("path") >= 0
					&& jsonresult.indexOf("size") >= 0
					&& jsonresult.indexOf("time") >= 0) {
				map.put("result", 1);// 文件或文件夹已存在
				map.put("message", "已存在同名文件或文件夹！");
				return map;
			} else // 空文件夹
			{
				map.put("result", 99);//
				map.put("message", "查询出错！");
				return map;
			}
		}
	}

	/**
	 * 
	 * Description：创建新文件夹
	 * 
	 * @param beforePath
	 *            前面的路径 如 ："/hello/1"
	 * @param newNewDirectoryName
	 *            新文件夹名字 如： "新文件夹"
	 * @return
	 * @throws Exception
	 * 
	 */
	public static Map<String, Object> createNewDirectory(String beforePath,
			String newNewDirectoryName) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		String url = gtdataHost + beforePath;
		if (newNewDirectoryName != null && !newNewDirectoryName.equals("")) {
			if (url.lastIndexOf("/") != (url.length() - 1))
				url += "/";
			url += newNewDirectoryName;
		}

		url += "?op=MKDIRS&sign=" + GtdataGroupVerifyUtil.getSign() + "&time="
				+ GtdataGroupVerifyUtil.getSignTime();

		String jsonresult = doPut(url);
		if (StringTools.nil(jsonresult)) {
			map.put("result", 99);
			map.put("message", "创建错误！系统内部错误！");
			return map;
		} else {
			JSONObject jsonObject = JSONObject.fromObject(jsonresult);
			// 有2种情况：错误GTDataException和files
			if (jsonresult.indexOf("GTDataException") >= 0)// 报错了
			{
				if (jsonresult.indexOf("2002") >= 0) {
					map.put("result", 97);
					map.put("message", "目录已存在！");
					return map;
				} else if (jsonresult.indexOf("2003") >= 0) {
					map.put("result", 98);
					map.put("message", "父路径不存在！");
					return map;
				} else {
					map.put("result", 99);
					map.put("message", "创建错误！系统内部错误！");
					return map;
				}
			} else if (jsonresult.indexOf("MKDIRS") >= 0) {
				boolean flag = jsonObject.getBoolean("MKDIRS");
				if (flag) {
					map.put("result", 1);
					map.put("message", "创建成功！");
					return map;
				} else {
					map.put("result", 96);
					map.put("message", "创建失败！未知错误！");
					return map;
				}
			} else {
				map.put("result", 99);
				map.put("message", "创建错误！系统内部错误！");
				return map;
			}
		}
	}

	/**
	 * 
	 * Description：创建新文件夹
	 * 
	 * @param beforePath
	 *            前面的路径 如 ："/hello/1"
	 * @param newNewDirectoryName
	 *            新文件夹名字 如： "新文件夹"
	 * @param gtdataHost
	 * 
	 * @return
	 * @throws Exception
	 * 
	 */
	public static Map<String, Object> createNewDirectory(String beforePath,
			String newNewDirectoryName, String gtdataHost) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		String url = gtdataHost + beforePath;
		if (newNewDirectoryName != null && !newNewDirectoryName.equals(""))
			url += "/" + newNewDirectoryName;

		url += "?op=MKDIRS&sign=" + GtdataGroupVerifyUtil.getSign() + "&time="
				+ GtdataGroupVerifyUtil.getSignTime();

		String jsonresult = doPut(url);
		if (StringTools.nil(jsonresult)) {
			map.put("result", 99);
			map.put("message", "创建错误！系统内部错误！");
			return map;
		} else {
			JSONObject jsonObject = JSONObject.fromObject(jsonresult);
			// 有2种情况：错误GTDataException和files
			if (jsonresult.indexOf("GTDataException") >= 0)// 报错了
			{
				if (jsonresult.indexOf("2002") >= 0) {
					map.put("result", 97);
					map.put("message", "目录已存在！");
					return map;
				} else if (jsonresult.indexOf("2003") >= 0) {
					map.put("result", 98);
					map.put("message", "父路径不存在！");
					return map;
				} else {
					map.put("result", 99);
					map.put("message", "创建错误！系统内部错误！");
					return map;
				}
			} else if (jsonresult.indexOf("MKDIRS") >= 0) {
				boolean flag = jsonObject.getBoolean("MKDIRS");
				if (flag) {
					map.put("result", 1);
					map.put("message", "创建成功！");
					return map;
				} else {
					map.put("result", 96);
					map.put("message", "创建失败！未知错误！");
					return map;
				}
			} else {
				map.put("result", 99);
				map.put("message", "创建错误！系统内部错误！");
				return map;
			}
		}
	}

	/**
	 * 
	 * Description：上传文件之前要上传md5，如果md5返回true继续上传文件
	 * 
	 * @param pathWithFileName
	 *            保存文件的路径，包含文件名。如: /新建文件夹/1.txt
	 * @param file
	 *            文件File实体
	 * @return
	 * @throws Exception
	 * 
	 */
	public static Map<String, Object> uploadFileMD5(String pathWithFileName,
			File file) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 拿到文件的md5值
		String fileMd5 = MD5Calculate.fileMD5(file.getAbsolutePath());
		String url = gtdataHost + pathWithFileName + "?op=MD5&value=" + fileMd5
				+ "&size=" + file.length() + "&sign="
				+ GtdataGroupVerifyUtil.getSign() + "&time="
				+ GtdataGroupVerifyUtil.getSignTime();
		String jsonresult = doPut(url);
		if (StringTools.nil(jsonresult)) {
			map.put("result", 99);
			map.put("message", "上传错误！系统内部错误！");
			return map;
		} else {
			JSONObject jsonObject = JSONObject.fromObject(jsonresult);
			// 有2种情况：错误GTDataException和files
			if (jsonresult.indexOf("GTDataException") >= 0)// 报错了
			{
				if (jsonresult.indexOf("2001") >= 0) {
					map.put("result", 97);
					map.put("message", "参数错误！");
					return map;
				} else if (jsonresult.indexOf("2002") >= 0) {
					map.put("result", 98);
					map.put("message", "文件或目录已存在！");
					return map;
				} else if (jsonresult.indexOf("2003") >= 0) {
					map.put("result", 98);
					map.put("message", "文件或目录不存在！");
					return map;
				} else if (jsonresult.indexOf("2004") >= 0) {
					map.put("result", 98);
					map.put("message", "MD5值不存在！");
					return map;
				} else {
					map.put("result", 99);
					map.put("message", "上传错误！系统内部错误！");
					return map;
				}
			} else if (jsonresult.indexOf("MD5") >= 0) {
				boolean flag = jsonObject.getBoolean("MD5");
				if (flag) {
					map.put("result", 1);
					map.put("message", "MD5值已存在，文件上传成功！");
					return map;
				} else {
					map.put("result", 96);
					map.put("message", "上传失败！未知错误！");
					return map;
				}
			} else {
				map.put("result", 99);
				map.put("message", "上传错误！系统内部错误！");
				return map;
			}
		}
	}

	/**
	 * 
	 * Description：上传文件之前要上传md5，如果md5返回true继续上传文件
	 * 
	 * @param pathWithFileName
	 *            保存文件的路径，包含文件名。如: /新建文件夹/1.txt
	 * @param file
	 *            文件File实体
	 * @param gtdataHost
	 *            自定义的gtdataHost
	 * @return
	 * @throws Exception
	 * 
	 */
	public static Map<String, Object> uploadFileMD5(String pathWithFileName,
			File file, String gtdataHost) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 拿到文件的md5值
		String fileMd5 = MD5Calculate.fileMD5(file.getAbsolutePath());
		String url = gtdataHost + pathWithFileName + "?op=MD5&value=" + fileMd5
				+ "&size=" + file.length() + "&sign="
				+ GtdataGroupVerifyUtil.getSign() + "&time="
				+ GtdataGroupVerifyUtil.getSignTime();
		String jsonresult = doPut(url);
		if (StringTools.nil(jsonresult)) {
			map.put("result", 99);
			map.put("message", "上传错误！系统内部错误！");
			return map;
		} else {
			JSONObject jsonObject = JSONObject.fromObject(jsonresult);
			// 有2种情况：错误GTDataException和files
			if (jsonresult.indexOf("GTDataException") >= 0)// 报错了
			{
				if (jsonresult.indexOf("2001") >= 0) {
					map.put("result", 97);
					map.put("message", "参数错误！");
					return map;
				} else if (jsonresult.indexOf("2002") >= 0) {
					map.put("result", 98);
					map.put("message", "文件或目录已存在！");
					return map;
				} else if (jsonresult.indexOf("2003") >= 0) {
					map.put("result", 98);
					map.put("message", "文件或目录不存在！");
					return map;
				} else if (jsonresult.indexOf("2004") >= 0) {
					map.put("result", 98);
					map.put("message", "MD5值不存在！");
					return map;
				} else {
					map.put("result", 99);
					map.put("message", "上传错误！系统内部错误！");
					return map;
				}
			} else if (jsonresult.indexOf("MD5") >= 0) {
				boolean flag = jsonObject.getBoolean("MD5");
				if (flag) {
					map.put("result", 1);
					map.put("message", "MD5值已存在，文件上传成功！");
					return map;
				} else {
					map.put("result", 96);
					map.put("message", "上传失败！未知错误！");
					return map;
				}
			} else {
				map.put("result", 99);
				map.put("message", "上传错误！系统内部错误！");
				return map;
			}
		}
	}

	/**
	 * 
	 * Description：上传文件
	 * 
	 * @param pathWithFileName
	 *            上传路径，包括文件名 如："/hello/1.txt"
	 * @param file
	 *            要上传的文件File对象
	 * @return
	 * @throws Exception
	 * 
	 */
	public static Map<String, Object> uploadFile(String pathWithFileName,
			File file) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 先判断当前目录是否存在同名文件
		map = isFileOrDirExist(pathWithFileName);

		if (1 == (Integer) map.get("result")) {
			// file.delete();
			map.put("result", 97);
			map.put("message", "已存在相同名字的文件！上传失败！");
			return map;
		}

		// 先上传md5值
		map = uploadFileMD5(pathWithFileName, file);
		if (1 == (Integer) map.get("result"))// 文件已经存在的，文件上传成功了，不需要执行下去，但是没有验证文件或文件夹是否存在
		{
			// file.delete();
			map.put("result", 1);
			map.put("message", "文件上传成功！");
			return map;
		}
		String fileMd5 = MD5Calculate.fileMD5(file.getAbsolutePath());
		String url = gtdataHost + pathWithFileName + "?op=CREATE&md5="
				+ fileMd5 + "&overwrite=true&sign="
				+ GtdataGroupVerifyUtil.getSign() + "&time="
				+ GtdataGroupVerifyUtil.getSignTime();
		String jsonresult = doPutFile(url, file);
		// TODO 调curl进行上传,只能在Linux环境运行
		/*
		 * String curlString = MessageFormat.format("curl -T {0} -X PUT {1}",
		 * file.getPath(), "\"" + url + "\""); String jsonresult =
		 * ExecLinuxCmd.exec(curlString);
		 */
		// file.delete();
		if (StringTools.nil(jsonresult)) {
			map.put("result", 99);
			map.put("message", "上传错误！系统内部错误！");
			return map;
		} else {
			JSONObject jsonObject = JSONObject.fromObject(jsonresult);
			// 有2种情况：错误GTDataException和files
			if (jsonresult.indexOf("GTDataException") >= 0)// 报错了
			{
				if (jsonresult.indexOf("2002") >= 0) {
					map.put("result", 97);
					map.put("message", "文件已存在哦！");
					return map;
				} else if (jsonresult.indexOf("2003") >= 0) {
					map.put("result", 98);
					map.put("message", "父路径不存在！");
					return map;
				} else {
					map.put("result", 99);
					map.put("message", "上传错误！系统内部错误！");
					return map;
				}
			} else if (jsonresult.indexOf("CREATE") >= 0) {
				boolean flag = jsonObject.getBoolean("CREATE");
				if (flag) {
					map.put("result", 1);
					map.put("message", "上传文件成功！");
					return map;
				} else {
					map.put("result", 96);
					map.put("message", "上传失败！未知错误！");
					return map;
				}
			} else {
				map.put("result", 99);
				map.put("message", "上传错误！系统内部错误！");
				return map;
			}
		}
	}

	/**
	 * 
	 * Description：上传文件
	 * 
	 * @param pathWithFileName
	 *            上传路径，包括文件名 如："/hello/1.txt"
	 * @param file
	 *            要上传的文件File对象
	 * @param gtdataHost
	 *            自定义的ip
	 * @return
	 * @throws Exception
	 * 
	 */
	public static Map<String, Object> uploadFile(String pathWithFileName,
			File file, String gtdataHost) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 先判断当前目录是否存在同名文件
		map = isFileOrDirExist(pathWithFileName);

		if (1 == (Integer) map.get("result")) {
			// file.delete();
			map.put("result", 97);
			map.put("message", "已存在相同名字的文件！上传失败！");
			return map;
		}

		// 先上传md5值
		map = uploadFileMD5(pathWithFileName, file, gtdataHost);
		if (1 == (Integer) map.get("result"))// 文件已经存在的，文件上传成功了，不需要执行下去，但是没有验证文件或文件夹是否存在
		{
			// file.delete();
			map.put("result", 1);
			map.put("message", "文件上传成功！");
			return map;
		}
		String fileMd5 = MD5Calculate.fileMD5(file.getAbsolutePath());
		pathWithFileName = java.net.URLEncoder
				.encode(pathWithFileName, "utf-8");

		String url = gtdataHost + pathWithFileName + "?op=CREATE&md5="
				+ fileMd5 + "&overwrite=true&sign="
				+ GtdataGroupVerifyUtil.getSign() + "&time="
				+ GtdataGroupVerifyUtil.getSignTime();

		// String jsonresult = doPutFile(url, file);
		// TODO 调curl进行上传,只能在Linux环境运行
		String curlString = MessageFormat.format("curl -T {0} -X PUT {1}",
				file.getPath(), "\"" + url + "\"");
		String jsonresult = ExecLinuxCmd.exec(curlString);

		// file.delete();
		if (StringTools.nil(jsonresult)) {
			map.put("result", 99);
			map.put("message", "上传错误！系统内部错误！");
			return map;
		} else {
			JSONObject jsonObject = JSONObject.fromObject(jsonresult);
			// 有2种情况：错误GTDataException和files
			if (jsonresult.indexOf("GTDataException") >= 0)// 报错了
			{
				if (jsonresult.indexOf("2002") >= 0) {
					map.put("result", 97);
					map.put("message", "文件已存在哦！");
					return map;
				} else if (jsonresult.indexOf("2003") >= 0) {
					map.put("result", 98);
					map.put("message", "父路径不存在！");
					return map;
				} else {
					map.put("result", 99);
					map.put("message", "上传错误！系统内部错误！");
					return map;
				}
			} else if (jsonresult.indexOf("CREATE") >= 0) {
				boolean flag = jsonObject.getBoolean("CREATE");
				if (flag) {
					map.put("result", 1);
					map.put("message", "上传文件成功！");
					return map;
				} else {
					map.put("result", 96);
					map.put("message", "上传失败！未知错误！");
					return map;
				}
			} else {
				map.put("result", 99);
				map.put("message", "上传错误！系统内部错误！");
				return map;
			}
		}
	}

	/**
	 * 
	 * Description：下载文件
	 * 
	 * @param filePath
	 *            文件路径
	 * @param request
	 *            请求
	 * @param response
	 *            响应
	 * @throws Exception
	 * 
	 */
	public static void downloadFile(String filePath,
			HttpServletRequest request, HttpServletResponse response,
			String filename) throws Exception {
		String url = gtdataHost + filePath + "?op=OPEN&sign="
				+ GtdataGroupVerifyUtil.getSign() + "&time="
				+ GtdataGroupVerifyUtil.getSignTime();
		Map<String, Object> map = doGetFileMap(url);
		if (map != null) {
			InputStream in = (InputStream) map.get("in");
			// String type = (String)map.get("type");
			Long length = (Long) map.get("length");

			// int contentLengh = in.available();
			int contentLengh = Integer.parseInt(length.toString());
			// response.setContentType(type);
			response.setContentLength(contentLengh);
			response.setContentType("application/octet-stream");
			response.setHeader("Content-disposition", "attachment; filename="
					+ new String(filename.getBytes("utf-8"), "ISO8859-1"));

			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;
			bis = new BufferedInputStream(in);
			bos = new BufferedOutputStream(response.getOutputStream());
			byte[] buff = new byte[2048];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
			bis.close();
			bos.close();
		}
	}

	/**
	 * 
	 * Description：下载文件,获取到文件输入流
	 * 
	 * @param filePath
	 *            文件路径
	 * @throws Exception
	 * 
	 */
	public static InputStream getDownloadFile(String filePath) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String url = gtdataHost + filePath + "?op=OPEN&sign="
				+ GtdataGroupVerifyUtil.getSign() + "&time="
				+ GtdataGroupVerifyUtil.getSignTime();
		return doGetFile(url);
	}

	/**
	 * 
	 * Description：读取图片文件，返回文件流
	 * 
	 * @param filePath
	 *            文件路径
	 * @param request
	 *            请求
	 * @param response
	 *            响应
	 * @throws Exception
	 * 
	 */
	public static void showDownloadFile(String filePath,
			HttpServletRequest request, HttpServletResponse response,
			String filename) {
		try {

			String url = gtdataHost + filePath + "?op=OPEN&sign="
					+ GtdataGroupVerifyUtil.getSign() + "&time="
					+ GtdataGroupVerifyUtil.getSignTime();

			InputStream in = doGetFile(url);
			if (null == in) {
				return;
			}
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;
			bis = new BufferedInputStream(in);
			bos = new BufferedOutputStream(response.getOutputStream());
			byte[] buff = new byte[2048];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
			bis.close();
			bos.close();
		} catch (Exception e) {
			return;
		}
	}

	/**
	 * 
	 * Description：下载文件,获取到文件输入流,通用的方法
	 * 
	 * @param filePath
	 *            文件路径
	 * @throws Exception
	 * 
	 */
	public static InputStream getDownloadRSFile(String url) throws Exception {
		url = url + "&sign=" + GtdataGroupVerifyUtil.getSign() + "&time="
				+ GtdataGroupVerifyUtil.getSignTime();
		return doGetFile(url);
	}

	/**
	 * 
	 * Description：重命名文件夹或文件
	 * 
	 * @param oldPath
	 *            旧路径,如:文件夹 "/hello/1" 文件："/hello/1.txt"
	 * @param newPath
	 *            新路径,如:文件夹 "/hello/2" 文件："/hello/2.txt"
	 * @return
	 * @throws Exception
	 * 
	 */
	public static Map<String, Object> renameDirectoryOrFile(String oldPath,
			String newPath) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		String url = gtdataHost + URLEncoder.encode(oldPath, "UTF-8")
				+ "?op=MOVE&destination=/" + newPath + "&sign="
				+ GtdataGroupVerifyUtil.getSign() + "&time="
				+ GtdataGroupVerifyUtil.getSignTime();
		String jsonresult = doPut4renameDirorFile(url);
		if (StringTools.nil(jsonresult)) {
			map.put("result", 99);
			map.put("message", "重命名错误！系统内部错误！");
			return map;
		} else {
			JSONObject jsonObject = JSONObject.fromObject(jsonresult);
			// 有2种情况：错误GTDataException和files
			if (jsonresult.indexOf("GTDataException") >= 0)// 报错了
			{
				if (jsonresult.indexOf("2002") >= 0) {
					map.put("result", 97);
					map.put("message", "已存在相同名字的文件夹或文件！！");
					return map;
				} else if (jsonresult.indexOf("2003") >= 0) {
					map.put("result", 98);
					map.put("message", "文件或文件夹不存在！重命名失败！");
					return map;
				} else {
					map.put("result", 99);
					map.put("message", "重命名错误！系统内部错误！");
					return map;
				}
			} else if (jsonresult.indexOf("MOVE") >= 0) {
				boolean flag = jsonObject.getBoolean("MOVE");
				if (flag) {
					map.put("result", 1);
					map.put("message", "重命名成功！");
					return map;
				} else {
					map.put("result", 96);
					map.put("message", "重命名失败！未知错误！");
					return map;
				}
			} else {
				map.put("result", 99);
				map.put("message", "重命名错误！系统内部错误！");
				return map;
			}
		}
	}

	/**
	 * 
	 * Description：删除文件或文件夹
	 * 
	 * @param path
	 *            删除的路径 如文件夹："/hello/1" 文件："/hello/1.txt"
	 * @return
	 * @throws Exception
	 * 
	 */
	public static Map<String, Object> deleteDirectoryOrFile(String path)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		String url = gtdataHost + path + "?op=DELETE&sign="
				+ GtdataGroupVerifyUtil.getSign() + "&time="
				+ GtdataGroupVerifyUtil.getSignTime();
		// UriComponents uriComponents =
		// UriComponentsBuilder.fromUriString(url).build();
		String jsonresult = doDelete(url);
		if (StringTools.nil(jsonresult)) {
			map.put("result", 99);
			map.put("message", "删除错误！系统内部错误！");
			return map;
		} else {
			JSONObject jsonObject = JSONObject.fromObject(jsonresult);
			// 有2种情况：错误GTDataException和files
			if (jsonresult.indexOf("GTDataException") >= 0)// 报错了
			{
				if (jsonresult.indexOf("2003") >= 0) {
					map.put("result", 98);
					map.put("message", "文件或文件夹不存在！删除失败");
					return map;
				} else {
					map.put("result", 99);
					map.put("message", "删除错误！系统内部错误！");
					return map;
				}
			} else if (jsonresult.indexOf("DELETE") >= 0) {
				boolean flag = jsonObject.getBoolean("DELETE");
				if (flag) {
					map.put("result", 1);
					map.put("message", "删除成功！");
					return map;
				} else {
					map.put("result", 96);
					map.put("message", "删除失败！未知错误！");
					return map;
				}
			} else {
				map.put("result", 99);
				map.put("message", "删除错误！系统内部错误！");
				return map;
			}
		}
	}

	/**
	 * 
	 * Description：复制文件或文件夹到指定路径，复制会保留原文件，
	 * 
	 * @param oldPath
	 *            旧的文件路径 如文件夹 "/hello/1" 文件 "/hello/1.txt"
	 * @param newPath
	 *            新文件夹路径 如文件夹 "/hello/2/1"(将1复制到2目录下) 文件 "/hello/2/1.txt"
	 * @return
	 * @throws Exception
	 * 
	 */
	public static Map<String, Object> copyDirectoryOrFile(String oldPath,
			String newPath) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		String url = gtdataHost + oldPath + "?op=COPY&destination=/" + newPath
				+ "&sign=" + GtdataGroupVerifyUtil.getSign() + "&time="
				+ GtdataGroupVerifyUtil.getSignTime();
		// System.out.println(url);
		String jsonresult = doPut(url);
		if (StringTools.nil(jsonresult)) {
			map.put("result", 99);
			map.put("message", "复制错误！系统内部错误！");
			return map;
		} else {
			JSONObject jsonObject = JSONObject.fromObject(jsonresult);
			// 有2种情况：错误GTDataException和files
			if (jsonresult.indexOf("GTDataException") >= 0)// 报错了
			{
				if (jsonresult.indexOf("2002") >= 0) {
					map.put("result", 97);
					map.put("message", "目标文件夹已存在同名文件或文件夹！复制失败");
					return map;
				} else if (jsonresult.indexOf("2003") >= 0) {
					map.put("result", 98);
					map.put("message", "文件或文件夹不存在！复制失败");
					return map;
				} else if (jsonresult.indexOf("2006") >= 0) {
					map.put("result", 95);
					map.put("message", "用户云盘空间不够！复制失败");
					return map;
				} else {
					map.put("result", 99);
					map.put("message", "复制错误！系统内部错误！");
					return map;
				}
			} else if (jsonresult.indexOf("COPY") >= 0) {
				boolean flag = jsonObject.getBoolean("COPY");
				if (flag) {
					map.put("result", 1);
					map.put("message", "复制成功！");
					return map;
				} else {
					map.put("result", 96);
					map.put("message", "复制失败！未知错误！");
					return map;
				}
			} else {
				map.put("result", 99);
				map.put("message", "复制错误！系统内部错误！");
				return map;
			}
		}
	}

	/**
	 * 
	 * Description：移动文件或文件夹到指定路径，不会保留原文件（先复制再删除）
	 * 
	 * @param oldPath
	 *            旧的文件路径 如文件夹 "/hello/1" 文件 "/hello/1.txt"
	 * @param newPath
	 *            新文件夹路径 如文件夹 "/hello/2/1"(将1复制到2目录下) 文件 "/hello/2/1.txt"
	 * @return
	 * @throws Exception
	 * 
	 */
	public static Map<String, Object> moveDirectoryOrFile(String oldPath,
			String newPath) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		String url = gtdataHost + oldPath + "?op=COPY&destination=/" + newPath
				+ "&sign=" + GtdataGroupVerifyUtil.getSign() + "&time="
				+ GtdataGroupVerifyUtil.getSignTime();
		String jsonresult = doPut(url);
		if (StringTools.nil(jsonresult)) {
			map.put("result", 99);
			map.put("message", "移动失败！系统内部错误！");
			return map;
		} else {
			JSONObject jsonObject = JSONObject.fromObject(jsonresult);
			// 有2种情况：错误GTDataException和files
			if (jsonresult.indexOf("GTDataException") >= 0)// 报错了
			{
				if (jsonresult.indexOf("2002") >= 0) {
					map.put("result", 97);
					map.put("message", "目标文件夹已存在同名文件或文件夹！移动失败");
					return map;
				} else if (jsonresult.indexOf("2003") >= 0) {
					map.put("result", 98);
					map.put("message", "文件或文件夹不存在！移动失败");
					return map;
				} else {
					map.put("result", 99);
					map.put("message", "移动失败！系统内部错误！");
					return map;
				}
			} else if (jsonresult.indexOf("COPY") >= 0) {
				boolean flag = jsonObject.getBoolean("COPY");
				if (flag) {
					// 再删除文件
					deleteDirectoryOrFile(oldPath);
					map.put("result", 1);
					map.put("message", "移动成功！");
					return map;
				} else {
					map.put("result", 96);
					map.put("message", "移动失败！未知错误！");
					return map;
				}
			} else {
				map.put("result", 99);
				map.put("message", "移动错误！系统内部错误！");
				return map;
			}
		}
	}

	/**
	 * 
	 * Description：创建多级文件夹
	 * 
	 * @param dirs
	 *            1/2/3
	 * @param fatherDir
	 *            父亲目录
	 * @param gtdataHost
	 *            服务器ip
	 * @return
	 * 
	 */
	public static Map<String, Object> createSomeDirectoys(String dirs,
			String fatherDir, String gtdataHost) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String[] arrpath = dirs.split("/");
			for (int i = 0; i < arrpath.length; i++) {

				map = createNewDirectory(fatherDir, arrpath[i], gtdataHost);
				fatherDir += arrpath[i] + "/";
			}
			map.put("result", 1);
			map.put("message", "创建成功！");
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", 0);
			map.put("message", "创建失败了！请检查路径！");
			return map;
		}
	}

	public static void main(String[] args) {
		try {
			// Map<String, Object> map = uploadFile("/lijianwei/111.png", new
			// File("C:\\Users\\lijianwei\\Desktop\\bike.png"));
			// System.out.println(map.get("message"));
			// System.out.println(System.currentTimeMillis());
			/*
			 * uploadFile( "/rscloudmart/data/1/2.jpg", new File(
			 * "C:\\Users\\lijianwei\\Desktop\\新建文件夹\\GF1_PMS1_E116.4_N38.9_20131127_L1A0000117602\\GF1_PMS1_E116.4_N38.9_20131127_L1A0000117602-PAN1.jpg"
			 * ));
			 */

			// Map<String, Object> map = getAllFileSizeByPath("1/订单数据",
			// "http://192.168.2.7:8001/gtdata/v1/");
			// Map<String, Object> map =
			// copyDirectoryOrFile("rscloudmart/production/20140829/太湖水华分布遥感监测图",
			// "1/1/太湖水华分布遥感监测图");
			Map<String, Object> map = createSomeDirectoys(
					"rscloudmart/data/GF1/thumbnail/2014112801/GF1_PMS1_E63.1_N57.6_20141124_L1A0000475666",
					"", gtdataHost);
			System.out.println(map);
			// System.out.println(map.get("size"));

		} catch (Exception e) {
			e.printStackTrace();
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

		for (int i = 0; i < 5; i++) {

			try {
				// System.out.println(url);
				UriComponents uriComponents = UriComponentsBuilder
						.fromUriString(url).build();
				HttpGet httpGet = new HttpGet(uriComponents.toUri().toString());

				/*
				 * httpGet.setHeader("Content-Type", "application/json;");
				 * httpGet.setHeader("Accept-Encoding", "gzip,deflate,sdch");
				 * httpGet.setHeader("Accept-Language:", "zh-CN,zh;");
				 */

				response = new DefaultHttpClient().execute(httpGet);
				if (response != null) {
					response.getAllHeaders();
					int statusCode = response.getStatusLine().getStatusCode();
					// if(statusCode==200){
					in = response.getEntity().getContent();
					if (in != null) {
						str = inputStream2String(in);
						in.close();
					}
					i = 5;
					// }
				}
			} catch (IOException e) {
				e.printStackTrace();
				// return str;
			}
		}
		return str;
	}

	/**
	 * 
	 * Description：get下载文件基础请求
	 * 
	 * @param url
	 *            请求地址
	 * @return 请求成功后的结果
	 * 
	 */
	public static InputStream doGetFile(String url) {
		InputStream in;
		HttpResponse response;
		try {
			UriComponents uriComponents = UriComponentsBuilder.fromUriString(
					url).build();
			HttpGet delete = new HttpGet(uriComponents.toUri().toString());
			response = new DefaultHttpClient().execute(delete);
			if (response != null) {
				int statusCode = response.getStatusLine().getStatusCode();
				in = response.getEntity().getContent();

				if (200 == response.getStatusLine().getStatusCode()) {
					return in;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	/**
	 * 
	 * Description：get下载文件基础请求
	 * 
	 * @param url
	 *            请求地址
	 * @return 请求成功后的结果
	 * 
	 */
	public static Map<String, Object> doGetFileMap(String url) {
		InputStream in;
		HttpResponse response;
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			UriComponents uriComponents = UriComponentsBuilder.fromUriString(
					url).build();
			HttpGet delete = new HttpGet(uriComponents.toUri().toString());
			// HttpGet delete = new HttpGet(url);

			response = new DefaultHttpClient().execute(delete);
			if (response != null) {
				int statusCode = response.getStatusLine().getStatusCode();
				in = response.getEntity().getContent();
				if (200 == response.getStatusLine().getStatusCode()) {
					map.put("in", in);
					map.put("length", response.getEntity().getContentLength());
					// map.put("type", response.getEntity().getContentType());
					return map;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	/**
	 * 
	 * Description：get打包下载zip文件基础请求
	 * 
	 * @param url
	 *            请求地址
	 * @return 请求成功后的结果
	 * 
	 */
	public static Map<String, Object> doGetZipFileMap(String path,Long localFileLenght) {
		String url = groupUserHost + "?op=ZIPDOWNLOAD&sign="
				+ GtdataGroupVerifyUtil.getSign() + "&time="
				+ GtdataGroupVerifyUtil.getSignTime();
		InputStream inputStream;
		HttpResponse response;
		try {
			HttpPost httpost = new HttpPost(url);
			List <NameValuePair> nvps = new ArrayList <NameValuePair>();
			//提交两个参数及值
			nvps.add(new BasicNameValuePair("path", path));
			//设置表单提交编码为UTF-8
			UrlEncodedFormEntity pEntity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
			httpost.setEntity(pEntity);
			//httpost.addHeader("Accept", "image/jpeg, application/x-ms-application, image/gif, application/xaml+xml, image/pjpeg, application/x-ms-xbap, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, application/x-shockwave-flash, */*");   
		  /*  httpost.addHeader("Accept-Language","zh-CN");   
		    httpost.addHeader("User-Agent","Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Trident/4.0; QQWubi 87; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0)");   
		    httpost.addHeader("Accept-Encoding","gzip, deflate");   
		    httpost.addHeader("Host","you never be know");   
		    httpost.addHeader("Connection","Keep-Alive");
			httpost.addHeader("User-Agent", "NetFox");*/
			httpost.addHeader("Range", "bytes=" + localFileLenght + "-");
			DefaultHttpClient httpclient = new DefaultHttpClient();
			httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60000); 
			httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
			response = new DefaultHttpClient().execute(httpost);
			HttpEntity entity = response.getEntity();
			if (response != null) {
				Map<String, Object> map=new HashMap<String, Object>();
				int statusCode = response.getStatusLine().getStatusCode();
				if (entity != null && (statusCode == 200 || statusCode == 206)) {
					inputStream=entity.getContent();
					map.put("in", inputStream);
					map.put("length", response.getEntity().getContentLength());
					return map;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}
	public static long doGetZipFileLength(String path) {
		String url = groupUserHost + "?op=ZIPDOWNLOAD&sign="
				+ GtdataGroupVerifyUtil.getSign() + "&time="
				+ GtdataGroupVerifyUtil.getSignTime();
		HttpResponse response;
		try {
			HttpPost httpost = new HttpPost(url);
			List <NameValuePair> nvps = new ArrayList <NameValuePair>();
			//提交两个参数及值
			nvps.add(new BasicNameValuePair("path", path));
			//设置表单提交编码为UTF-8
			UrlEncodedFormEntity pEntity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
			httpost.setEntity(pEntity);
			DefaultHttpClient httpclient = new DefaultHttpClient();
			httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60000); 
			httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
			response = new DefaultHttpClient().execute(httpost);
			HttpEntity entity = response.getEntity();
			if (response != null) {
				int statusCode = response.getStatusLine().getStatusCode();
				if (entity != null && statusCode == 200) {
					return entity.getContentLength();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return 0;
	}
	/**
	 * 
	 * Description：delete基础请求
	 * 
	 * @param url
	 *            请求地址
	 * @return 请求成功后的结果
	 * 
	 */
	public static String doDelete(String url) {
		InputStream in;
		String str = "";
		HttpResponse response;
		try {
			UriComponents uriComponents = UriComponentsBuilder.fromUriString(
					url).build();
			HttpDelete delete = new HttpDelete(uriComponents.toUri().toString());

			response = new DefaultHttpClient().execute(delete);
			if (response != null) {
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
	 * Description：put基础请求，一般命令，查询文件等使用
	 * 
	 * @param url
	 * @return
	 * 
	 */
	public static String doPut(String url) {
		String ret = "";
		for (int i = 0; i < 5; i++) {
			try {
				UriComponents uriComponents = UriComponentsBuilder
						.fromUriString(url).build();
				HttpPut request = new HttpPut(uriComponents.toUri().toString());
				// HttpPut request = new HttpPut(url);
				DefaultHttpClient client = new DefaultHttpClient();
				HttpResponse response = client.execute(request);
				if (response != null) {
					StatusLine statusLine = (StatusLine) response
							.getStatusLine();
					int statusCode = statusLine.getStatusCode();
					// if(statusCode==200){
					HttpEntity entity = response.getEntity();
					InputStream inputStream = entity.getContent();
					if (inputStream != null) {
						ret = inputStream2String(inputStream);
						inputStream.close();
					}
					// }
					i = 5;
				}
			} catch (IOException e) {
				e.printStackTrace();
				// return ret;
			}
		}
		return ret;
	}

	/**
	 * 
	 * Description：put基础请求，一般命令，查询文件等使用
	 * 
	 * @param url
	 * @return
	 * 
	 */
	public static String doPut4renameDirorFile(String url) {
		String ret = "";
		for (int i = 0; i < 5; i++) {
			try {
				// UriComponents uriComponents =
				// UriComponentsBuilder.fromUriString(url).build();
				// HttpPut request = new
				// HttpPut(uriComponents.toUri().toString());
				HttpPut request = new HttpPut(url);
				DefaultHttpClient client = new DefaultHttpClient();
				HttpResponse response = client.execute(request);
				if (response != null) {
					StatusLine statusLine = (StatusLine) response
							.getStatusLine();
					int statusCode = statusLine.getStatusCode();
					// if(statusCode==200){
					HttpEntity entity = response.getEntity();
					InputStream inputStream = entity.getContent();
					if (inputStream != null) {
						ret = inputStream2String(inputStream);
						inputStream.close();
					}
					// }
					i = 5;
				}
			} catch (IOException e) {
				e.printStackTrace();
				// return ret;
			}
		}
		return ret;
	}

	/**
	 * 
	 * Description：上传文件的put基础方法，
	 * 
	 * @param url
	 *            上传的路径，如 "/hello/1.txt"
	 * @param file
	 *            File对象，要上传的文件
	 * @return
	 * 
	 */
	public static String doPutFile(String url, File file) {

		// 创建httpclient工具对象
		HttpClient client = new HttpClient();
		// client.getParams().setContentCharset("utf-8");
		String responseString = "";
		// InputStream body = null;
		/* httpURLConnection.setChunkedStreamingMode(int chunkLength); */
		try {
			UriComponents uriComponents = UriComponentsBuilder.fromUriString(
					url).build();
			PutMethod myPost = new PutMethod(uriComponents.toUri().toString());
			// myPost.setContentChunked(true);
			// 设置请求头部类型
			// body = new FileInputStream(file);
			String MimeType = FileTools.getMimeType(file.getAbsolutePath());
			myPost.setRequestEntity(new FileRequestEntity(file, MimeType));
			// myPost.setRequestBody(body);
			client.executeMethod(myPost);
			responseString = inputStream2String(myPost
					.getResponseBodyAsStream());
		} catch (Exception e) {
			e.printStackTrace();
			return responseString;
		}
		return responseString;

	}

	/**
	 * 
	 * Description:打包压缩和下载
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 * 
	 */

	public static void zipDownLoad(String filePath, HttpServletRequest request,
			HttpServletResponse response, String filename) throws Exception {
		Map<String, Object> map = doGetZipFileMap(filePath,(long) 0);
		if (map != null) {
			InputStream in = (InputStream) map.get("in");
			// String type = (String)map.get("type");
			Long length = (Long) map.get("length");
			// int contentLengh = in.available();
			int contentLengh = Integer.parseInt(length.toString());
			// response.setContentType(type);
			response.setContentLength(contentLengh);
			response.setContentType("application/octet-stream");
			response.setHeader("Content-disposition", "attachment; filename="
					+ new String(filename.getBytes("utf-8"), "ISO8859-1"));
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;
			bis = new BufferedInputStream(in);
			bos = new BufferedOutputStream(response.getOutputStream());
			byte[] buff = new byte[2048];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
			bis.close();
			bos.close();
		}
	}
	public static Map<String, Object> zipDownLoadBackMap(String filePath) throws Exception {
		return doGetZipFileMap(filePath,(long) 0);
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
		return out.toString();
	}

	/**
	 * 
	 * Description：写入文件
	 * 
	 * @param file
	 * @param in
	 * @throws Exception
	 * 
	 */
	public static void writeFile(File file, InputStream in) throws Exception {
		OutputStream os = new FileOutputStream(file);
		int bytesRead = 0;
		byte[] buffer = new byte[8192];
		// System.out.println(bytesRead = in.read(buffer, 0, 8192));
		while ((bytesRead = in.read(buffer, 0, 8192)) != -1) {
			os.write(buffer, 0, bytesRead);
		}
		os.close();
		in.close();
	}

	static {
		try {
			String file = "gtcloudos";
			gtdataHost = StringTools.getValueFromProperties(file, "gtdataHost");
			groupUserHost = StringTools.getValueFromProperties(file,
					"groupUserHost");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
