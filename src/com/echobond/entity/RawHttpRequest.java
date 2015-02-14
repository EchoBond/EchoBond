package com.echobond.entity;

import java.util.HashMap;
import java.util.Map;

public class RawHttpRequest {
	
	public static final String HTTP_METHOD_POST = "POST";
	public static final String HTTP_METHOD_GET = "GET"; 
	
	private String url;
	private String method;
	private Map<String,String> headers;
	private Object params;
	
	public RawHttpRequest(String url, String method, Map<String, String> headers,
			Object params) {
		super();
		this.url = url;
		this.method = method;
		this.headers = headers;
		this.params = params;
		setJSON(false);
	}
	public RawHttpRequest(String url, String method, Map<String, String> headers,
			Object params, boolean isJSON){
		super();
		this.url = url;
		this.method = method;
		this.headers = headers;
		this.params = params;
		setJSON(isJSON);
	}
	public void setJSON(boolean isJSON){
		if(null == headers)
			headers = new HashMap<String, String>();
		if(isJSON){
			headers.put("content-type", "text/json;charset=UTF-8");			
		} else {
			headers.put("content-type", "text/plain;charset=UTF-8");
		}
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
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	public Object getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	
}
