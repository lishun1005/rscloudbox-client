package com.cloudbox.utils;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.security.*;
import java.text.SimpleDateFormat;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * 字符串工具类
 * 
 * @author
 */
public class StringTools {

	public static String REGEX = "','";
	private static final String PASSWORD_CRYPT_KEY = "rsclouds";
	private final static String DES = "DES";

	public StringTools() {

	}

	/**
	 * 生成随即密码
	 * 
	 * @param pwd_len
	 *            生成的密码的总长度
	 * @return 密码的字符串
	 */
	public static String genRandomStr(int pwd_len) {
		// 35是因为数组是从0开始的，26个字母+10个数字
		final int maxNum = 36;
		int i; // 生成的随机数
		int count = 0; // 生成的密码的长度
		char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
				'l', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
				'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

		StringBuffer pwd = new StringBuffer("");
		Random r = new Random();
		while (count < pwd_len) {
			// 生成随机数，取绝对值，防止生成负数，

			i = Math.abs(r.nextInt(maxNum)); // 生成的数最大为36-1

			if (i >= 0 && i < str.length) {
				pwd.append(str[i]);
				count++;
			}
		}

		return pwd.toString();
	}

	/***************************************************************************
	 * 统一特殊字符过滤方法
	 * 
	 * @param str
	 *            ：需要过滤的字符串
	 * @return：过滤后的字符串
	 */
	public static String filter(String str) {
		if (str != null) {
			str = str.replaceAll(";", "");
			str = str.replaceAll("&", "&amp;");
			str = str.replaceAll("<", "&lt;");
			str = str.replaceAll(">", "&gt;");
			str = str.replaceAll("'", "");
			str = str.replaceAll("--", "");
			str = str.replaceAll("/", "");
			str = str.replaceAll("%", "");
			str = str.replaceAll("\"", "“");
		}
		return str;
	}

	/**
	 * 把特殊字符转换成HTML标签
	 * 
	 * @param str
	 *            ：需要转换的字符
	 * @return：转换后的字符
	 */
	public static String toHtml(String str) {
		String html = str;
		if (str == null || str.length() == 0) {
			return "";
		} else {
			html = html.replaceAll("&", "&amp;");
			html = html.replaceAll("<", "&lt;");
			html = html.replaceAll(">", "&gt;");
			html = html.replaceAll("\r\n", "\n");
			html = html.replaceAll("\n", "<br>\n");
			html = html.replaceAll("\"", "&quot;");
			html = html.replaceAll(" ", "&nbsp;");
			return html;
		}
	}

	/**
	 * 
	 * @author ygl 将数组替换为字符串
	 * @param ary
	 *            数组
	 * @param splitBy
	 *            分隔符
	 * @return
	 */
	public static String splitAry(Object[] ary, String splitBy) {
		String result = "";
		if (ary != null) {
			for (int i = 0; i < ary.length; i++) {
				result += String.valueOf(ary[i]) + splitBy;
			}
			if (!result.equals("")) {
				result = result.substring(0, result.length() - 1);
			}
		}
		return result;
	}

	/**
	 * 字符串替换，将 source 中的 oldString 全部换成 newString
	 * 
	 * @param source
	 *            源字符串
	 * @param oldString
	 *            老的字符串
	 * @param newString
	 *            新的字符串
	 * @return 替换后的字符串
	 */
	public static String Replace(String source, String oldString,
			String newString) {
		StringBuffer output = new StringBuffer();

		int lengthOfSource = source.length(); // 源字符串长度
		int lengthOfOld = oldString.length(); // 老字符串长度

		int posStart = 0; // 开始搜索位置
		int pos; // 搜索到老字符串的位置

		while ((pos = source.indexOf(oldString, posStart)) >= 0) {
			output.append(source.substring(posStart, pos));

			output.append(newString);
			posStart = pos + lengthOfOld;
		}

		if (posStart < lengthOfSource) {
			output.append(source.substring(posStart));
		}

		return output.toString();
	}

	/**
	 * 去除字符串两端空格。
	 * 
	 * @param str
	 *            需要处理的字符串
	 * @return 去掉了两端空格的字符串，如果str 为 null 则返回 ""
	 */
	public static String trim(String str) {
		if (str != null) {
			return str.trim();
		} else {
			return "";
		}
	}

	/**
	 * 判断是否是数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String strNumber) {
		boolean bolResult = false;
		try {
			Double.parseDouble(strNumber);
			bolResult = true;
		} catch (NumberFormatException ex) {
			bolResult = false;
		}
		return bolResult;
	}

	/**
	 * 判断字符串是否为Null或空字符串
	 * 
	 * @param String
	 *            要判断的字符串
	 * @return String true-是空字符串,false-不是空字符串
	 */
	public static boolean nil(String s) {
		if (s == null || s.trim().equals("")) {
			return true;
		}
		return false;
	}

	/**
	 * 判断字符串数组是否为空
	 */
	public static boolean nil(String[] s) {
		return (s == null || s.length == 0);
	}

	/**
	 * 判断字符串数组是否为空
	 */
	public static boolean nilArrstr(String[] str) {
		boolean bl = true;
		if (str != null && str.length > 0) {
			int length = str.length;
			for (int i = 0; i < length; i++) {
				if (!nil(str[i])) {
					bl = false;
					break;
				}
			}
		}
		return bl;
	}

	/**
	 * 判断ArrayList是否为空
	 * 
	 */
	public static boolean isNilArray(List<String> list) {
		for (String ob : list) {
			if (ob != null && !"".equals(ob))
				return false;
		}
		return true;
	}

	/**
	 * 转换String到long
	 */
	public static long parseLong(String flag) {
		if (nil(flag))
			return 0;
		return Long.parseLong(flag);
	}

	/**
	 * 判断字符串是否全是数字字符.
	 * 
	 * @param input
	 *            输入的字符串
	 * @return 判断结果, true 为全数字, false 为还有非数字字符
	 */
	public static boolean isNumeric(String input) {
		if (nil(input)) {
			return false;
		}

		for (int i = 0; i < input.length(); i++) {
			char charAt = input.charAt(i);
			if (!Character.isDigit(charAt)) {
				return false;
			}
		}
		return true;
	}

	public static boolean hasChineseCode(String str) {
		// int length = str.length();
		int length = -1;
		if (str == null)
			str = "";
		try {
			length = new String(str.getBytes(), "8859_1").length();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return length != str.length();
	}

	/**
	 * 在list中查询以prefix开头的子项，返回符合匹配的list
	 * 
	 * @param list
	 * @param prefix
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List getMatches(List list, String prefix) {
		String prefix_upper = prefix.toUpperCase();
		List<String> matches = new ArrayList<String>();
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			String name = (String) iter.next();
			String name_upper = name.toUpperCase();
			if (name_upper.startsWith(prefix_upper)) {
				matches.add(name);
			}
		}
		return matches;
	}

	/**
	 * 此方法将给出的字符串source使用delim划分为单词数组。
	 * 
	 * @param source
	 *            需要进行划分的原字符串
	 * @param delim
	 *            单词的分隔字符串
	 * @return 划分以后的数组，如果source为null的时候返回以source为唯一元素的数组，
	 *         如果delim为null则使用逗号作为分隔字符串。
	 * @since 0.1
	 */
	public static String[] split(String source, String delim) {
		String[] wordLists;
		if (nil(source)) {
			wordLists = new String[1];
			wordLists[0] = source;
			return wordLists;
		}
		if (delim == null) {
			delim = ",";
		}
		if (source.indexOf(REGEX) != -1)
			delim = REGEX;
		StringTokenizer st = new StringTokenizer(source, delim);
		int total = st.countTokens();
		wordLists = new String[total];
		for (int i = 0; i < total; i++) {
			wordLists[i] = st.nextToken();
		}
		return wordLists;
	}

	public static String castNullToString(Object o) {
		return o == null ? "" : o.toString();
	}

	public static String array2String(String[] str, String regex) {
		String result = "";
		if (nil(regex))
			regex = StringTools.REGEX;
		if (!nil(str)) {
			StringBuilder sb = new StringBuilder();
			int length = str.length;
			for (int i = 0; i < length; i++) {
				if (i == length - 1)
					sb.append(str[i].trim());
				else
					sb.append(str[i].trim() + regex);
			}
			result = sb.toString();
		}
		return result;
	}

	/**
	 * MD5(规则稍修改)加密
	 * 
	 * @param str
	 *            原字符串
	 * @return 加密后的字符串
	 */
	public static String string4Md5String(String str) {
		String s = str;
		if (s == null) {
			return "";
		} else {
			String value = null;
			MessageDigest md5 = null;
			try {
				md5 = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException ex) {
				ex.printStackTrace();
			}
			sun.misc.BASE64Encoder baseEncoder = new sun.misc.BASE64Encoder();
			try {
				value = baseEncoder.encode(md5.digest(s.getBytes("utf-8")));
			} catch (Exception ex) {
			}
			return getMd5StringAppend(str, value);
		}
	}

	/**
	 * 修改MD5加密规则(在密文的第(原文长%密文长)处插入字符);
	 * 
	 * @param str
	 *            原字符串
	 * @param val
	 *            经过MD5加密后的字符串
	 * @return 经过规则加密后的新MD5字符串
	 */
	private static String getMd5StringAppend(String str, String val) {
		String value = str;
		if (str != null && !"".equals(str)) {
			char[] ch = { 'Q', 'A', 'Z', 'W', 'S', 'X', 'E', 'D', 'C', 'R',
					'F', 'V' };
			int l1 = str.charAt(0);
			int length = str.length();
			int vlength = val.length();
			StringBuilder sb = new StringBuilder(val);
			sb = sb.insert(length % vlength, ch[l1 % ch.length]);
			value = sb.toString();
		}
		return value;
	}

	public static boolean validateString(String str, String type) {
		boolean bl = false;
		str = str.trim();
		if (type != null && type.equals("string")) {
			bl = true;
		} else if (type != null && type.equals("alphanum")) {
			bl = validateAlphaNum(str);
		} else if (type != null
				&& (type.equals("number") || type.equals("integer") || type
						.equals("float")) || type.equals("double")
				|| type.equals("long")) {
			bl = isNumber(str);
		} else if (type != null && type.equals("email")) {
			bl = validateMail(str);
		} else if (type != null && type.equals("name")) {
			bl = validateName(str);
		} else if (type != null && type.equals("title")) {
			bl = validateTitle(str);
		} else if (type != null && type.equals("ip")) {
			bl = validateIp(str);
		} else if (type != null && type.equals("code")) {
			bl = validateCode(str);
		} else if (type != null && type.equals("pcard")) {
			bl = validatePcard(str);
		} else if (type != null && type.equals("phone")) {
			bl = validatePhone(str);
		} else if (type != null && type.equals("mobile")) {
			bl = validateTelPhone(str);
		} else if (type != null && type.equals("otime")) {
			bl = validateOnlyTime(str);
		} else {
			bl = true;
		}
		return bl;
	}

	// 验证手机号码
	private static boolean validateTelPhone(String str) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher matcher = p.matcher(str);
		return matcher.matches();
	}

	// 验证电话号码
	private static boolean validatePhone(String str) {
		Pattern p = Pattern.compile("^(\\d{3,4}([\\s|-]{0,1})\\d{3,8})$");
		Matcher matcher = p.matcher(str);
		return matcher.matches();
	}

	// 验证邮箱
	private static boolean validateMail(String str) {
		Pattern pattern = Pattern
				.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\"
						+ ".[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)"
						+ "+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	// 验证邮编
	private static boolean validateCode(String str) {
		Pattern pattern = Pattern.compile("^([1-9]\\d{2}(|\\s))\\d{3}$");
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	// 验证IP
	private static boolean validateIp(String str) {
		Pattern pattern = Pattern
				.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\"
						+ "d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\"
						+ ".((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\."
						+ "((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	// 验证字母或数字
	private static boolean validateAlphaNum(String str) {
		Pattern pattern = Pattern.compile("^[\\p{Alnum}]*$");
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	// 验证身份证
	private static boolean validatePcard(String str) {
		Pattern pattern = Pattern
				.compile("^([1-9]\\d{5}[1-2]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}((\\d)|[xX]))|([1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$)");
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	// 验证姓名
	private static boolean validateName(String str) {
		Pattern pattern = Pattern
				.compile("^([\\u4E00-\\u9FFF]{1,}|([a-zA-Z]{1,}\\s{0,1}){1,})$");
		Matcher matcher = pattern.matcher(str.replace(" ", ""));
		return matcher.matches();
	}

	// 验证标题
	private static boolean validateTitle(String str) {
		Pattern pattern = Pattern
				.compile("^(([\\u4E00-\\u9FFF]{1,}\\s{0,1}[，、]{0,1}){1,}|([a-zA-Z]{1,}\\s{0,1}\\,{0,1}){1,})$");
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	// 验证时间(格式：hh:mm)
	private static boolean validateOnlyTime(String str) {
		return !nil(str)
				&& str.trim().matches("([0-1][0-9]|2[0-3]):([0-5][0-9])");
	}

	/**
	 * 
	 * 二行制转字符串
	 * 
	 * @param b
	 * 
	 * @return
	 */

	public static String byte2hex(byte[] b) {

		String hs = "";

		String stmp = "";

		for (int n = 0; n < b.length; n++) {

			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));

			if (stmp.length() == 1)

				hs = hs + "0" + stmp;

			else

				hs = hs + stmp;

		}

		return hs.toUpperCase();

	}

	public static byte[] hex2byte(byte[] b) {

		if ((b.length % 2) != 0)

			throw new IllegalArgumentException("长度不是偶数");

		byte[] b2 = new byte[b.length / 2];

		for (int n = 0; n < b.length; n += 2) {

			String item = new String(b, n, 2);

			b2[n / 2] = (byte) Integer.parseInt(item, 16);

		}

		return b2;

	}

	/**
	 * 加密
	 * 
	 * @param src
	 *            数据源
	 * @param key
	 *            密钥，长度必须是8的倍数
	 * @return 返回加密后的数据
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] src, byte[] key) throws Exception {
		// DES算法要求有一个可信任的随机数源
		SecureRandom sr = new SecureRandom();
		// 从原始密匙数据创建DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);
		// 创建一个密匙工厂，然后用它把DESKeySpec转换成
		// 一个SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);
		// Cipher对象实际完成加密操作
		Cipher cipher = Cipher.getInstance(DES);
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
		// 现在，获取数据并加密
		// 正式执行加密操作
		return cipher.doFinal(src);
	}

	/**
	 * 解密
	 * 
	 * @param src
	 *            数据源
	 * @param key
	 *            密钥，长度必须是8的倍数
	 * @return 返回解密后的原始数据
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] src, byte[] key) throws Exception {
		// DES算法要求有一个可信任的随机数源
		SecureRandom sr = new SecureRandom();
		// 从原始密匙数据创建一个DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);
		// 创建一个密匙工厂，然后用它把DESKeySpec对象转换成
		// 一个SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);
		// Cipher对象实际完成解密操作
		Cipher cipher = Cipher.getInstance(DES);
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
		// 现在，获取数据并解密
		// 正式执行解密操作
		return cipher.doFinal(src);
	}

	public static String encryStr(String str) throws Exception {
		String encrystr = "", tmp = "";
		int key1 = 2;
		int key2 = 999999;
		for (int i = 0; i < str.length(); i++) {
			tmp = t1(str.charAt(i) * key1 + key2).toLowerCase();
			encrystr += (tmp);
		}
		return encrystr;
	}

	// 写一个方法实现：把一个十进制的数转换成为16进制的数
	public static String t1(int a) {
		String str = "";
		// 1:用a去除以16，得到商和余数
		int sun = a / 16;
		int yuShu = a % 16;
		str = "" + shuZhiToZhiMu(yuShu);
		while (sun > 0) {
			// 2：继续用商去除以16，得到商和余数
			yuShu = sun % 16;
			sun = sun / 16;
			// 3：如果商为0，那么就终止
			// 4：把所有的余数倒序排列
			str = shuZhiToZhiMu(yuShu) + str;
		}
		// System.out.println("16进制==="+str);
		return str;
	}

	public static String shuZhiToZhiMu(int a) {
		switch (a) {
		case 10:
			return "A";
		case 11:
			return "B";
		case 12:
			return "C";
		case 13:
			return "D";
		case 14:
			return "E";
		case 15:
			return "F";
		}
		return "" + a;
	}

	public static String encodeString(String str) throws IOException {
		sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
		String encodedStr = new String(encoder.encodeBuffer(str.getBytes()));
		return encodedStr.trim();
	}

	public static String decodeString(String str) throws IOException {
		sun.misc.BASE64Decoder dec = new sun.misc.BASE64Decoder();
		String value = new String(dec.decodeBuffer(str));
		return value;
	}

	/**
	 * 
	 * Description：解码：对js前台传输过来的字符串进行解码操作
	 * 
	 * @param @param target
	 * @param @return
	 * @return String
	 * @throws
	 */
	public static String decode(String target) {
		String dataname = "";
		if (null == target || "".equals(target)) {
			return "";
		}
		try {
			dataname = java.net.URLDecoder.decode(target, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return dataname;
	}

	/**
	 * 
	 * Description：将路径中的中文进行url编码
	 * 
	 * @param str
	 *            文件路径
	 * @return
	 * @throws Exception
	 * 
	 */
	public static String changeStringToURLDecode(String str) throws Exception {
		// 路径如果有中文就进行编码
		String[] currStrings = str.split("/");
		str = "";
		for (int i = 0; i < currStrings.length; i++) {
			str += "/" + URLEncoder.encode(currStrings[i], "utf-8");
		}

		return str.substring(1);
	}

	/**
	 * 
	 * Description：如果是字符串是null，将null转为"",否则返回源字符串
	 * 
	 * @param string
	 * @return
	 * 
	 */
	public static String changeNullToEmpty(String string) {
		if (StringTools.nil(string)) {
			return "";
		}
		return string;

	}

	/**
	 * 
	 * Description：得到配置文件中的变量名称为variableName字段的值
	 * 
	 * @param fileName
	 *            文件名如zkyg.config.system
	 * @param variableName
	 *            配置文件的变量名称
	 * @return String 配置文件中的变量名称variableName字段的值
	 * @throws 配置文件中的变量名称variableName字段的值
	 */
	public static String getValueFromProperties(String fileName,String variableName) {
		// 读取保存路径
		ResourceBundle.clearCache();// 如果该配置文件启动后会更改，需要调用这个来清空缓存，不然读不了修改的内容
		ResourceBundle bd = ResourceBundle.getBundle(fileName);
		String value = bd.getString(variableName) != null&& bd.getString(variableName) != "" ? bd.getString(variableName) : null;
		return value;
	}

	/**
	 * 
	 * Description：将字符串长度设置为对应长度
	 * 
	 * @param length
	 *            目标长度
	 * @param source
	 *            原始字符串
	 * @return
	 * 
	 */
	public static String subStringToLength(int length, String source) {
		if (!StringTools.nil(source)) {
			if (source.length() >= length) {
				source = source.substring(0, length) + "...";
			}
		}
		return source;
	}

	/**
	 * Description: 修复timerange，如其值为"2016-01-8,2016-02-6"，
	 * 则将其变为"2016-01-08,2016-02-06"
	 * 
	 * @param timerange 初始值
	 * @author huangxj
	 * @date 2016-7-26 下午3:06:03
	 */
	public static String fixTimerange(String Timerange) {

		String[] timeArr = Timerange.split(",");
		String[] startTimeDetail = timeArr[0].split("-");
		String[] endTimeDetail = timeArr[1].split("-");
		//起始月
		if (startTimeDetail[1].length() == 1) {

			startTimeDetail[1] = "0" + startTimeDetail[1];
		}
		//起始日
		if (startTimeDetail[2].length() == 1) {

			startTimeDetail[2] = "0" + startTimeDetail[2];
		}
		//结束月
		if (endTimeDetail[1].length() == 1) {

			endTimeDetail[1] = "0" + endTimeDetail[1];
		}
		//结束日
		if (endTimeDetail[2].length() == 1) {

			endTimeDetail[2] = "0" + endTimeDetail[2];
		}
		String startTime = "";
		String endTime = "";
		for (int i = 0; i < 3; i++) {

			startTime = startTime + "-" + startTimeDetail[i];
			endTime = endTime + "-" + endTimeDetail[i];
		}
		startTime = startTime.substring(1);
		endTime = endTime.substring(1);
		Timerange = startTime + "," + endTime;
		
		return Timerange;
	}
	
	public static void main(String[] args) throws Exception {

	}

}
