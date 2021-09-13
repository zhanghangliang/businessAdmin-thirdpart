package com.gov.wiki.common.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: LevelUtil 
 * @Description: 计算层级
 * @author cys
 * @date 2019年11月14日 上午10:15:41
 */
public class LevelUtil {
	
	public final static String SEPARATOR = "!";
	
	public final static String ROOT = "0";
	
	public static String calculateLevel(String parentLevel, int seq){
		if(StringUtils.isBlank(parentLevel)){
			return String.format("%04d", seq);
		} else {
			String seqStr = String.format("%04d", seq);
			List<String> joinStr = new ArrayList<String>();
			joinStr.add(parentLevel);
			joinStr.add(seqStr);
			return String.join(SEPARATOR, joinStr);
		}
	}
	
	public static void main(String[] args) {
		String prefix = "SD20200304";
		String billNum = "SD202003040002";
		billNum = billNum.substring(prefix.length());
		System.out.println(billNum);
		int num = StringUtils.isNotBlank(billNum)?Integer.parseInt(billNum) + 1:1;
		System.out.println(num);
		System.out.println(prefix + String.format("%04d", num));
	}
}
