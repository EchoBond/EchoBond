package com.echobond.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.echobond.R;
import com.echobond.entity.RawHttpRequest;
import com.echobond.entity.RawHttpResponse;

/**
 * 
 * @author Luck
 *
 */
public class HTTPUtil {
	private static class HTTPUtilHolder{
		public static HTTPUtil INSTANCE = new HTTPUtil();
	}
	public static HTTPUtil getInstance(){
		return HTTPUtilHolder.INSTANCE;
	}
	public RawHttpResponse send(RawHttpRequest request) throws SocketTimeoutException, ConnectException{
		RawHttpResponse response = null;
		InputStream is = null;
		OutputStreamWriter osw = null;
		try {
			URL url = new URL(request.getUrl());
			HttpURLConnection conn;
			if(url.getProtocol().equalsIgnoreCase("https")){
				conn = (HttpsURLConnection) url.openConnection();
			} else {
				conn = (HttpURLConnection) url.openConnection();
			}
			//basic connection properties
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setConnectTimeout(1000);
			conn.setReadTimeout(1000);
			//headers settings
			setRequestHeaders(conn, request.getHeaders());
			//method setting
			conn.setRequestMethod(request.getMethod());
			if(request.getMethod().equalsIgnoreCase("POST")){
				//send post request
				osw = new OutputStreamWriter(conn.getOutputStream());
				osw.write(request.getParams().toString());
				osw.flush();
			} else {
				//send get request
				conn.connect();
			}
			//recv response
			int code = conn.getResponseCode();
			Map<?, ?> headers = conn.getHeaderFields();
			String result = conn.getResponseMessage();
			is = conn.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = "", msg = "";
			while((line = br.readLine()) != null){
				msg += line;
			}
			response = new RawHttpResponse(code, headers, result, msg);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(null != is) is.close();
				if(null != osw) osw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return response;
	}
	private void setRequestHeaders(URLConnection conn, Map<?, ?> headers){
		if(null == headers)
			return;
		Set<?> headerNames = headers.keySet();
		for (Object header: headerNames) {
			conn.setRequestProperty((String)header, (String)headers.get(header));
		}
	}
	
	public String composePreURL(Context ctx){
        String preUrl = ctx.getResources().getString(R.string.url_protocol) + "://" + 
    			ctx.getResources().getString(R.string.url_domain) + ":" + 
    			ctx.getResources().getString(R.string.url_port) + "/" + 
    			ctx.getResources().getString(R.string.url_sub) + "/";
        return preUrl;
	}
	
	public JSONObject sendRequest(String url, Object body, boolean isJSON){
		RawHttpRequest request = new RawHttpRequest(url, RawHttpRequest.HTTP_METHOD_POST, null, body, isJSON);
		RawHttpResponse response = null;
		JSONObject result = null;
		try{
			response = send(request);
		} catch (SocketTimeoutException e){
			e.printStackTrace();
		} catch (ConnectException e){
			e.printStackTrace();
		}
		if(null != response){
			try{
				result = new JSONObject(response.getMsg());
			} catch (JSONException e){
				e.printStackTrace();
			}
		}
		return result;
	}

}
