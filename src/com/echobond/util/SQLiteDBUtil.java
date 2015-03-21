package com.echobond.util;


import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.R;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDBUtil extends SQLiteOpenHelper {
	private Context ctx;
	private static SQLiteDBUtil INSTANCE;
	public static SQLiteDBUtil getInstance(Context ctx){
		if(null == INSTANCE){
			INSTANCE = new SQLiteDBUtil(ctx.getApplicationContext());
		}
		return INSTANCE;
	}
	
	public SQLiteDBUtil(Context ctx){
		super(ctx, ctx.getResources().getString(R.string.db_name), null, 2);
		this.ctx = ctx;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		Resources res = ctx.getResources();
		String[] strings = res.getStringArray(R.array.create_tables);
		for (String string : strings) {
			db.execSQL(string);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
	}
	
	public long insert(String table, JSONObject obj){
		SQLiteDatabase db = getWritableDatabase();
		return db.insert(table, null, bindValues(obj));
	}

	public int update(String table, JSONObject obj, String where, String[] whereArgs){
		SQLiteDatabase db = getWritableDatabase();
		return db.update(table, bindValues(obj), where, whereArgs);
	}
	
	public int delete(String table, JSONObject obj, String where, String[] whereArgs){
		SQLiteDatabase db = getWritableDatabase();
		return db.delete(table, where, whereArgs);
	}
	
	public Cursor query(String table, boolean distinct, String columns[], String where, String[] whereArgs, String groupBy, String having, String orderBy, String limit){
		SQLiteDatabase db = getReadableDatabase();
		return db.query(distinct, table, columns, where, whereArgs, groupBy, having, orderBy, limit);
	}
	
	public Cursor query(String table, String[] columns, String where, String[] whereArgs, String limit){
		return query(table, true, columns, where, whereArgs, null, null, null, limit);
	}
	
	public Cursor query(String table, String[] columns, String where, String[] whereArgs){
		return query(table, columns, where, whereArgs, null);
	}
	
	private ContentValues bindValues(JSONObject obj){
		ContentValues values = new ContentValues();
		Iterator<String> keys = obj.keys();
		try{
			while(keys.hasNext()){
				String key = keys.next();
				Object value = obj.get(key);
				if(null == value){
					values.putNull(key);
				}
				else if(value.getClass() == Boolean.class){
					values.put(key, (Boolean)value);
				} else if(value.getClass() == Byte.class){
					values.put(key, (Byte)value);
				} else if(value.getClass() == Byte[].class){
					values.put(key, (byte[])value);
				} else if(value.getClass() == Double.class){
					values.put(key, (Double)value);
				} else if(value.getClass() == Float.class){
					values.put(key, (Float)value);
				} else if(value.getClass() == Long.class){
					values.put(key, (Long)value);
				} else if(value.getClass() == Short.class){
					values.put(key, (Short)value);
				} else if(value.getClass() == String.class){
					values.put(key, (String)value);
				} 
			}
		} catch (JSONException e){
			e.printStackTrace();
		}
		return values;
	}
	
}
