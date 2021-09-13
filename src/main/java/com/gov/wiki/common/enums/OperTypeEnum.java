/**
 * @Title: OperTypeEnum.java
 * @Package com.jade.filesystem.enums
 * @Description: TODO(用一句话描述该文件做什么)
 * @author cys
 * @date 2019年7月30日
 * @version V1.0
 */
package com.gov.wiki.common.enums;

/**
 * @ClassName: OperTypeEnum
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author cys
 * @date 2019年7月30日
 */
public enum OperTypeEnum {
	QUERY(1, "查询"),
	ADD(2, "新增"),
	MODIFY(3, "修改"),
	DELETE(4, "删除"),
	LOGIN(5, "登录"),
	REGISTER(6, "注册"),
	LOGOUT(7, "注销"),
	MERGE(8, "合并");

	private int key;
	
	private String value;
	
	private OperTypeEnum(int key, String value) {
		this.key = key;
		this.value = value;
	}
	
	/**
	 * @Title: getVal
	 * @Description: 通过key值获取value值
	 * @param ec
	 * @return String 返回类型
	 * @throws
	 */
	public static String getVal(int ec) {  
        for (OperTypeEnum sc : OperTypeEnum.values()) {  
            if (sc.key == ec) {  
                return sc.value;  
            }  
        }  
        return null;  
    }
	
	/**
	 * @return the key
	 */
	public int getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(int key) {
		this.key = key;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

}