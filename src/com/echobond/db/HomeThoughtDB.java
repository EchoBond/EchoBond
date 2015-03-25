package com.echobond.db;

import com.echobond.R;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class HomeThoughtDB extends MyDBHelper {
	
	/*Singleton pattern implementation*/
	private static HomeThoughtDB INSTANCE;
	public static HomeThoughtDB getInstance(Context ctx){
		if(null == INSTANCE){
			INSTANCE = new HomeThoughtDB(ctx.getApplicationContext());
		}
		return INSTANCE;
	}
	
	public HomeThoughtDB(Context ctx){
		super(ctx);
		Resources res = ctx.getResources();
		this.tblName = res.getString(R.string.tbl_home_t);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
	}
	
	public Cursor getHomeThoughts(){
		Cursor cursor = query(ctx.getResources().getString(R.string.sql_s_t_home), new String[]{"10","0"});
		return cursor;
	}
	
	public long addHomeThought(ContentValues values){
		return replace(tblName, values);
	}
	
}