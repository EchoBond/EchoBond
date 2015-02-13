package com.echobond.entity;

import java.util.Map;

public class RawHttpResponse {
	private int code;
	private Map<?,?> headers;
	private String result;
	private String msg;
	public RawHttpResponse(int code, Map<?, ?> headers, String result,
			String msg) {
		this.code = code;
		this.headers = headers;
		this.result = result;
		this.msg = msg;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public Map<?, ?> getHeaders() {
		return headers;
	}
	public void setHeaders(Map<?, ?> headers) {
		this.headers = headers;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
