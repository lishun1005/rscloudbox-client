package com.cloudbox.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * @Description  加密解密类
 * */
public class FileEncryptionUtils {
	public static Logger logger =LoggerFactory.getLogger(FileEncryptionUtils.class);
	public interface LgetLib extends Library{
		LgetLib Instance=(LgetLib)Native.loadLibrary("AESFile",LgetLib.class);
		boolean fileCryptoAES256(String in_file_path, String out_file_path, String key, int opt);
	}
	/**
	 * 
	* Description: 加密解密工具类
	* @param in_file_path 文件路径
	* @param out_file_path 加密/解密后文件路径
	* @param key 秘钥
	* @param opt 0:decrypt解密 1:encrypt 加密
	* @return boolean<br>
	* @author lishun 
	* @date 2016-7-6 上午9:05:37
	 */
	public static boolean fileCryptoAES256(String in_file_path, String out_file_path, String key, int opt){
		boolean result=false;
		try {
			result=LgetLib.Instance.fileCryptoAES256(in_file_path, out_file_path, key, opt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
