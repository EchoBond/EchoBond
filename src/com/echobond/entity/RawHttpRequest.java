package com.echobond.entity;

import java.util.Map;

public class RawHttpRequest {
	
	public static final String HTTP_METHOD_POST = "POST";
	public static final String HTTP_METHOD_GET = "GET"; 
	
	private String url;
	private String method;
	private Map<?,?> headers;
	private Object params;
	
	public RawHttpRequest(String url, String method, Map<?, ?> headers,
			Object params) {
		super();
		this.url = url;
		this.method = method;
		this.headers = headers;
		this.params = params;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public Map<?, ?> getHeaders() {
		return headers;
	}
	public void setHeaders(Map<?, ?> headers) {
		this.headers = headers;
	}
	public Object getparams() {
		return params;
	}
	public void setparams(String params) {
		this.params = params;
	}
	
}
