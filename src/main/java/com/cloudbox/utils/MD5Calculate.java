package com.cloudbox.utils;


import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Calculate {
	public static String byteArrayToHex(byte[] byteArray) {
		StringBuilder hs = new StringBuilder();
		String stmp = "";
		for (int n = 0; n < byteArray.length; n++) {
			stmp = (Integer.toHexString(byteArray[n] & 0XFF));
			if (stmp.length() == 1) {
				hs.append("0" + stmp);
			} else {
				hs.append(stmp);
			}
			if (n < byteArray.length - 1) {
				hs.append("");
			}
		}
		return hs.toString();
	}

	public static String fileMD5(String inputFile) throws IOException {
		// 缓冲区大小（这个可以抽出一个参数）
		int bufferSize = 256 * 1024;
		FileInputStream fileInputStream = null;
		DigestInputStream digestInputStream = null;
		try {
			// 拿到一个MD5转换器（同样，这里可以换成SHA1）
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			// 使用DigestInputStream
			fileInputStream = new FileInputStream(inputFile);
			
			digestInputStream = new DigestInputStream(fileInputStream,
					messageDigest);
			// read的过程中进行MD5处理，直到读完文件
			byte[] buffer = new byte[bufferSize];
			while (digestInputStream.read(buffer) > 0)
				;
			// 获取最终的MessageDigest
			messageDigest = digestInputStream.getMessageDigest();
			// 拿到结果，也是字节数组，包含16个元素
			byte[] resultByteArray = messageDigest.digest();
			// 同样，把字节数组转换成字符串
			return byteArrayToHex(resultByteArray);
		} catch (NoSuchAlgorithmException e) {
			return null;
		} finally {
			try {
				digestInputStream.close();
			} catch (Exception e) {
			}
			try {
				fileInputStream.close();
			} catch (Exception e) {
			}
		}
	}
	
	public static String fileByteMD5(){
		MessageDigest messageDigest;
		DigestInputStream digestInputStream = null;
		InputStream in = null;
		int bufferSize = 256 * 1024;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			byte[] strByte = "qwertyuiopasdfghjklzxcvbnm".getBytes();
			in = new ByteArrayInputStream(strByte );			
			digestInputStream = new DigestInputStream(in, messageDigest);
			
			byte[] buffer = new byte[bufferSize];
			while (digestInputStream.read(buffer) > 0)
				;
			messageDigest = digestInputStream.getMessageDigest();
			byte[] resultByteArray = messageDigest.digest();
			System.out.println( byteArrayToHex(resultByteArray));
			
			strByte = "qwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnm".getBytes();
			in = new ByteArrayInputStream(strByte );			
			digestInputStream = new DigestInputStream(in, messageDigest);	
			while (digestInputStream.read(buffer) > 0)
				;
			messageDigest = digestInputStream.getMessageDigest();
			resultByteArray = messageDigest.digest();
			return byteArrayToHex(resultByteArray);
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				digestInputStream.close();
			} catch (Exception e) {}
			try {
				in.close();
			} catch (Exception e) {}
		}
		
	}
	
	public static void main(String[] args)throws IOException{
		System.out.println(System.currentTimeMillis());
		System.out.println(fileMD5("D://nanlin//smal//R00006790//C0001c5d6.png"));
		System.out.println(System.currentTimeMillis());
		System.out.println(fileByteMD5());
	}
}
