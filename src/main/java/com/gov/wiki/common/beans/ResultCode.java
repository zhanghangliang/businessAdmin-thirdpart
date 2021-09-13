package com.gov.wiki.common.beans;

/**
 * 返回状态码和状态解释的枚举
 * 在codeMsg中占位符使用{index}，和国际化中使用一致。
 * @author csxx_wmw
 *
 */
public enum ResultCode {

	/**
	 * 请求成功
	 */
	SUCCESS(0,"SUCCESS"),
	ERROR(-1,"ERROR"),
	PARAM_NULL(1,"参数【{0}】为空"), 
	TOKEN_ERROR(2,"TOKEN错误"), 
	TOKEN_NOT_EMPTY(3,"TOKEN不存在"),
	DATA_NOT_EXIST(5,"【{0}】不存在"),
	COMMON_ERROR(4,"{0}");
	
	private int code;
	private String codeMsg;
	
	private ResultCode(int code,String codeMsg) {
		this.code = code;
		this.codeMsg = codeMsg;
	}

	
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getCodeMsg() {
		return codeMsg;
	}

	public void setCodeMsg(String codeMsg) {
		this.codeMsg = codeMsg;
	}
}
