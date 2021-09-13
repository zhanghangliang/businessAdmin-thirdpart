package com.gov.wiki.common.beans;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ResultBean<T> implements Serializable {

	private static final long serialVersionUID = 1L;


	public static final int SUCCESS = 0;

	public static final int FAIL = 1;

	
	@ApiModelProperty(value = "状态说明：success/fail")
	private String msg = "success";

	@ApiModelProperty(value = "返回状态码： 0 成功  1 失败")
	private int code = SUCCESS;
	
	@ApiModelProperty(value = "业务状态错误码，无错误返回0")
	private int errorCode;
	
	@ApiModelProperty(value = "业务状态说明，无错误无返回")
	private String errorMsg;
	
	@ApiModelProperty(value = "请求成功后返回的数据")
	private T data;
	private long totalCount;
	public ResultBean() {
		super();
	}

	public ResultBean(T data) {
		super();
		this.setData(data);
	}
	

	public ResultBean(Throwable e) {
		super();
		this.msg = e.toString();
		this.code = FAIL;
	}
	
	public static ResultBean<String> error(String msg){
		return new ResultBean<String>().setCode(FAIL).setMsg(msg);
	}

	public static ResultBean<String> error(int code2, String msg2) {
		return error("fail").setErrorCode(code2).setErrorMsg(msg2);
	}
}