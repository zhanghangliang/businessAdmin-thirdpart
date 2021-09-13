package com.gov.wiki.common.utils;

import java.util.UUID;

/**
 * @ClassName: UUIDLong 
 * @Description: 获取UUID
 * @author cys 
 * @date 2018年11月22日 下午3:10:37
 */
public class UUIDLong {
	/**
	 * @Title: longUUID 
	 * @Description: 获取长整型ID
	 * @param 设定文件 
	 * @return long    返回类型 
	 * @throws
	 */
	public static long longUUID(){
		return UUID.randomUUID().getMostSignificantBits();
	}
	
	/**
	 * @Title: absLongUUID 
	 * @Description: 获取正的长整型ID
	 * @param 设定文件 
	 * @return long    返回类型 
	 * @throws
	 */
	public static long absLongUUID() {
		while(true){
			long r = longUUID();
			if(r > 0){
				return r;
			}
		}
	}

	public static void main(String[] args) {
		System.out.println(absLongUUID());
	}
}