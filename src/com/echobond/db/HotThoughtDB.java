package com.echobond.db;

import com.echobond.R;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class HotThoughtDB extends MyDBHelper {
	
	/*Singleton pattern implementation*/
	private static HotThoughtDB INSTANCE;
	public static HotThoughtDB getInstance(Context ctx){
		if(null == INSTANCE){
			INSTANCE = new HotThoughtDB(ctx.getApplicationContext());
		}
		return INSTANCE;
	}
	
	public HotThoughtDB(Context ctx){
		super(ctx);
		Resources res = ctx.getResources();
		this.tblName = res.getString(R.string.tbl_hot_t);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
	}
	
	public Cursor getHotThoughts(){
		Cursor cursor = query(tblName, false, null, null, null, null, null, "_id DESC", null);
		return cursor;
	}
	
}
