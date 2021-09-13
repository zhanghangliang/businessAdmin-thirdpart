package com.gov.wiki.common.utils;

import java.util.UUID;
import org.springframework.util.Assert;

public abstract class PasswordUtil {

	public static String encryption(String pwd) {
		Assert.hasText(pwd, "密码不能为空");
		return getSaltMD5(pwd);
	}
	
	/**
	 * 	校验密码
	 * @param pwd
	 * @param encode
	 * @return
	 */
	public static boolean checkEncryption(String pwd,String encode) {
		return getSaltverifyMD5(pwd, encode);
	}
	
	public static String getSaltMD5(String password) {
		// 生成一个16位的随机数
		// 生成最终的加密盐
		String salt = UUID.randomUUID().toString().substring(0,16);
		System.out.println(salt);
		password = MD5.getMD5(password+salt);
		char[] cs = new char[48];
		for (int i = 0; i < 48; i += 3) {
			cs[i] = password.charAt(i / 3 * 2);
			char c = salt.charAt(i / 3);
			cs[i + 1] = c;
			cs[i + 2] = password.charAt(i / 3 * 2 + 1);
		}
		return String.valueOf(cs);
	}
 
	/**
	 * 验证加盐后是否和原文一致
	 */
	public static boolean getSaltverifyMD5(String password, String md5str) {
		char[] cs1 = new char[32];
		char[] cs2 = new char[16];
		for (int i = 0; i < 48; i += 3) {
			cs1[i / 3 * 2] = md5str.charAt(i);
			cs1[i / 3 * 2 + 1] = md5str.charAt(i + 2);
			cs2[i / 3] = md5str.charAt(i + 1);
		}
		String Salt = new String(cs2);
		return MD5.getMD5(password + Salt).equals(String.valueOf(cs1));
	}
	
	public static void main(String[] args) {
		System.out.println(PasswordUtil.encryption("123"));
	}
}
