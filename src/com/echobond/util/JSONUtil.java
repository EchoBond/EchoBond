package com.echobond.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

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
			dest = new JSONObject(fromObjectToString(src));
		} catch (JSONException e1){
			e1.printStackTrace();
		}
		return dest;
	}
	
	public static Object fromJSONToObject(JSONObject obj, Class<?> cls){
		return fromStringToObject(obj.toString(), cls);
	}
	
	@SuppressWarnings("rawtypes")
	public static List fromJSONToList(JSONObject obj, String name, TypeToken<?> token){
		JSONArray jArray = null;
		try {
			jArray = obj.getJSONArray(name);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return fromJSONArrayToList(jArray, token);
	}
	
	@SuppressWarnings("rawtypes")
	public static List fromJSONArrayToList(JSONArray array, TypeToken<?> token){
		ArrayList list = gson.fromJson(array.toString(), token.getType());
		return list;
	}
	
	@SuppressWarnings("rawtypes")
	public static JSONArray fromListToJSONArray(ArrayList list, TypeToken<?> token){
		JsonElement jElement = gson.toJsonTree(list, token.getType());
		JsonArray gArray = jElement.getAsJsonArray();
		JSONArray jArray = null;
		try {
			jArray = new JSONArray(gArray.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jArray;
	}
	
}
