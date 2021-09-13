/**
 * @Title: StatusEnum.java 
 * @Package com.common.enums 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author cys 
 * @date 2019年11月5日 下午8:46:46 
 * @version V1.0 
 */
package com.gov.wiki.business.enums;

/**
 * @ClassName: StatusEnum 
 * @Description: 状态枚举
 * @author cys
 * @date 2019年11月5日 下午8:46:46
 */
public enum StatusEnum {
	save(0),
	agree(1),
	disagree(2);
	
	private int value;
	
	private StatusEnum(int val){
		this.value = val;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}