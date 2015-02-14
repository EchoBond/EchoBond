package com.echobond.util;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

public class JSONUtil {
	
	private static Gson gson = new Gson();
	
	public static String fromObjectToString(Object src){
		String jsonStr = gson.toJson(src);
		return jsonStr;
	}
	
	public static Object fromStringToObject(String src, Class<?> cls){
		Object obj = gson.fromJson(src, cls);
		return obj;
	}
	
	public static JSONObject fromObjectToJSON(Object src){
		JSONObject dest = new JSONObject();
		try {
			dest = new JSONObject(JSONUtil.fromObjectToString(src));
		} catch (JSONException e1){
			e1.printStackTrace();
		}
		return dest;
	}
	
}
