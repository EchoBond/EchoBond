package com.echobond.db;


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

public abstract class MyDBHelper extends SQLiteOpenHelper {
	protected Context ctx;
	protected String tblName;
	private static final int DB_VERSION = 1;
	public MyDBHelper(Context ctx){
		super(ctx, ctx.getResources().getString(R.string.db_name), null, DB_VERSION);
		this.ctx = ctx;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		Resources res = ctx.getResources();
		db.execSQL(res.getString(R.string.sql_c_home_t));
		db.execSQL(res.getString(R.string.sql_c_hot_t));
		db.execSQL(res.getString(R.string.sql_c_t_tags));
		db.execSQL(res.getString(R.string.sql_c_t_comment));
		db.execSQL(res.getString(R.string.sql_c_msg));
		db.execSQL(res.getString(R.string.sql_c_user));
		db.execSQL(res.getString(R.string.sql_c_luser));
		db.execSQL(res.getString(R.string.sql_c_category));
		db.execSQL(res.getString(R.string.sql_c_group));
		db.execSQL(res.getString(R.string.sql_c_country));
		db.execSQL(res.getString(R.string.sql_c_tag));
		db.execSQL(res.getString(R.string.sql_c_user_tag));
		db.execSQL(res.getString(R.string.sql_c_flw_grp));
		db.execSQL(res.getString(R.string.sql_c_lk_tag));
		db.execSQL(res.getString(R.string.sql_c_tag_slf));
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
	}
	
	protected long insert(String table, JSONObject obj){
		SQLiteDatabase db = getWritableDatabase();
		return db.insert(table, null, bindValues(obj));
	}

	protected int update(String table, JSONObject obj, String where, String[] whereArgs){
		SQLiteDatabase db = getWritableDatabase();
		return db.update(table, bindValues(obj), where, whereArgs);
	}
	
	protected int delete(String table, JSONObject obj, String where, String[] whereArgs){
		SQLiteDatabase db = getWritableDatabase();
		return db.delete(table, where, whereArgs);
	}
	
	protected Cursor query(String sql, String[] args){
		SQLiteDatabase db = getReadableDatabase();
		return db.rawQuery(sql, args);
	}
	
	protected Cursor query(String table, boolean distinct, String columns[], String where, String[] whereArgs, String groupBy, String having, String orderBy, String limit){
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(distinct, table, columns, where, whereArgs, groupBy, having, orderBy, limit);
		return cursor;
	}
	
	protected Cursor query(String table, String[] columns, String where, String[] whereArgs, String limit){
		return query(table, true, columns, where, whereArgs, null, null, null, limit);
	}
	
	protected Cursor query(String table, String[] columns, String where, String[] whereArgs){
		return query(table, columns, where, whereArgs, null);
	}
	
	protected ContentValues bindValues(JSONObject obj){
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
