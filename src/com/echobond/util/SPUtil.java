package com.echobond.util;

import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SPUtil {
	@SuppressWarnings("unchecked")
	public static Object get(Context ctx, String name, String key, Object defValue, Class<?> cls) {
		SharedPreferences pref = ctx.getSharedPreferences(name, Activity.MODE_PRIVATE);
		Object value = null;
		if (cls == Integer.class)
			value = pref.getInt(key, (Integer)defValue);
		else if (cls == Boolean.class)
			value = pref.getBoolean(key, (Boolean) defValue);
		else if (cls == Float.class)
			value = pref.getFloat(key, (Float) defValue);
		else if (cls == Long.class)
			value = pref.getLong(key, (Long) defValue);
		else if (cls == String.class)
			value = pref.getString(key, (String) defValue);
		else if (cls == Set.class)
			value = pref.getStringSet(key, (Set<String>) defValue);
		return value;
	}
	
	@SuppressWarnings("unchecked")
	public static boolean put(Context ctx, String name, String key, Object value) {
		SharedPreferences pref = ctx.getSharedPreferences(name, Activity.MODE_PRIVATE);
		Editor editor = pref.edit();
		boolean result = false;
		if (value.getClass() == Boolean.class) {
			result = editor.putBoolean(key, (Boolean) value).commit();
		} else if (value.getClass() == Integer.class) {
			result = editor.putInt(key, (Integer) value).commit();
		} else if (value.getClass() == Float.class) {
			result = editor.putFloat(key, (Float) value).commit();
		} else if (value.getClass() == Long.class) {
			result = editor.putLong(key, (Long) value).commit();
		} else if (value.getClass() == String.class) {
			result = editor.putString(key, (String) value).commit();
		} else if (value.getClass() == Set.class) {
			result = editor.putStringSet(key, (Set<String>) value).commit();
		}
		return result;
	}

	public static boolean remove(Context ctx, String name, String key) {
		SharedPreferences pref = ctx.getSharedPreferences(name, Activity.MODE_PRIVATE);
		Editor editor = pref.edit();
		return editor.remove(key).commit();
	}
	
	public static boolean clear(Context ctx, String name) {
		SharedPreferences pref = ctx.getSharedPreferences(name, Activity.MODE_PRIVATE);
		Editor editor = pref.edit();
		return editor.clear().commit();
	}
}
