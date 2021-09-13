/**
 * @Title: Utility.java
 * @Package com.xiangtong.common.utils
 * @Description: TODO(用一句话描述该文件做什么)
 * @author cys
 * @date 2019年12月17日
 * @version V1.0
 */
package com.gov.wiki.common.utils;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {
	public static String getNowDateStr() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	public static String getMD5(String str) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] bytes = md.digest(str.getBytes("utf-8"));
			return toHex(bytes).toLowerCase();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static String toHex(byte[] bytes) {

		final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
		StringBuilder ret = new StringBuilder(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
			ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
		}
		return ret.toString();
	}
}