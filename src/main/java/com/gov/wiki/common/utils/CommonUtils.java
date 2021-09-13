package com.gov.wiki.common.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class CommonUtils {

	public static String getNonceStr(int length){
		String string = "qwertyuioplkjhgfdsazxcvbnm123456789POIUYTREWQASDFGHJKLMNBVCXZ";
		StringBuffer sb = new StringBuffer();
	    int len = string.length();
	    for (int i = 0; i < length; i++) {
	        sb.append(string.charAt(getRandom(len-1)));
	    }
	    return sb.toString();
	}
	/**
	 * 获取随机数，[0,i)
	 * @param i
	 * @return
	 */
	public static int getRandom(int i){
		return (int) (Math.random()*i);
	}
	public static List<Long> stingsToListLongs(String[] split) {
		
		List<Long> list = new ArrayList<>(split.length);
		for (String string : split) {
			if(string == null){
				continue;
			}
			string = string.trim();
			if(StringUtils.isNotBlank(string)){
				list.add(Long.valueOf(string));
			}
		}
		return list;
	}
	public static boolean checkLongsNotRepeat(List<Long> ids) {
		Collections.sort(ids);
		for (int i = 0; i < ids.size() - 1; i++) {
			if(ids.get(i).equals(ids.get(i+1))){
				return false;
			}
		}
		return true;
	}
	
	public static void main(String[] args) {
		String code = getNonceStr(6);
		System.out.println(code);
	}
}
