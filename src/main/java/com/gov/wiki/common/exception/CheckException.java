package com.gov.wiki.common.exception;

public class CheckException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private int code = 0;
	
	private String message1;

	public CheckException(String message,int i) {
		super(message);
		this.code = i;
	}

	public CheckException(Throwable cause,int i) {
		super(cause.getMessage(),cause);
		this.code = i;
	}

	public CheckException(String message, Throwable cause,int i) {
		super(message, cause);
		this.code = i;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage1() {
		return message1;
	}

	public void setMessage1(String message1) {
		this.message1 = message1;
	}
}