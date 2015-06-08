package com.echobond.db;

import com.echobond.R;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class HotThoughtDB extends MyDBHelper {
	
	/*Singleton pattern implementation*/
	private static HotThoughtDB INSTANCE;
	private static Context ctx;
	public static HotThoughtDB getInstance(Context context){
		if(null == INSTANCE){
			ctx = context;
			INSTANCE = new HotThoughtDB();
		}
		return INSTANCE;
	}
	public static HotThoughtDB getInstance(){
		if(null == INSTANCE){
			INSTANCE = new HotThoughtDB();
		}
		return INSTANCE;
	}
	
	public HotThoughtDB(){
		super(ctx);
		Resources res = ctx.getResources();
		this.tblName = res.getString(R.string.tbl_hot_t);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
	}
	
	public Cursor getHotThoughts(String[] args){
		Cursor cursor = query(ctx.getResources().getString(R.string.sql_s_t_hot), args);
		return cursor;
	}
	
	public long addHotThought(ContentValues values){
		return replace(tblName, values);
	}
	public int updateHotThought(ContentValues values, String where, String[] whereArgs){
		return update(tblName, values, where, whereArgs);
	}
	
}
