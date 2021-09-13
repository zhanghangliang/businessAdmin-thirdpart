/**
 * @Title: OperTypeEnum.java
 * @Package com.jade.filesystem.enums
 * @Description: TODO(用一句话描述该文件做什么)
 * @author cys
 * @date 2019年7月30日
 * @version V1.0
 */
package com.gov.wiki.business.enums;

/**
 * @ClassName: OperTypeEnum
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author cys
 * @date 2019年7月30日
 */
public enum OperTypeEnum {
	ADD(1, "新增"),
	MODIFY(2, "修改"),
	DELETE(3, "删除");

	private int key;
	
	private String value;
	
	private OperTypeEnum(int key, String value) {
		this.key = key;
		this.value = value;
	}
	
	public static OperTypeEnum getEnumByKey(Integer key) {
		if(key == null) {
			return null;
		}
		OperTypeEnum ote = null;
		for (OperTypeEnum e : OperTypeEnum.values()) {  
            if (e.key == key) {  
                ote = e;
                break;
            }  
        }
		return ote;
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
	
	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}