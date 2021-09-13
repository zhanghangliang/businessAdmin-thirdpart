/**
 * @Title: StudyTypeEnum.java
 * @Package StudyTypeEnum
 * @Description: TODO(用一句话描述该文件做什么)
 * @author cys
 * @date 2019年7月30日
 * @version V1.0
 */
package com.gov.wiki.business.enums;

/**
 * @ClassName: StudyTypeEnum
 * @Description: 学习资料类型枚举
 * @author cys
 * @date 2019年7月30日
 */
public enum StudyTypeEnum {
	folder(1, "文件夹"),
	document(2, "资料文件");

	private int key;
	
	private String value;
	
	private StudyTypeEnum(int key, String value) {
		this.key = key;
		this.value = value;
	}
	
	public static StudyTypeEnum getEnumByKey(Integer key) {
		if(key == null) {
			return null;
		}
		StudyTypeEnum ote = null;
		for (StudyTypeEnum e : StudyTypeEnum.values()) {  
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
        for (StudyTypeEnum sc : StudyTypeEnum.values()) {  
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