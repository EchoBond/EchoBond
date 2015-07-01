package com.echobond.db;

import com.echobond.R;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;

public class ThoughtTagDB extends MyDBHelper {
	
	/*Singleton pattern implementation*/
	private static ThoughtTagDB INSTANCE;
	private static Context ctx;
	public static ThoughtTagDB getInstance(Context context){
		if(null == INSTANCE){
			ctx = context;
			INSTANCE = new ThoughtTagDB();
		}
		return INSTANCE;
	}
	
	public static ThoughtTagDB getInstance(){
		if(null == INSTANCE){
			INSTANCE = new ThoughtTagDB();
		}
		return INSTANCE;
	}
	
	public ThoughtTagDB(){
		super(ctx);
		Resources res = ctx.getResources();
		this.tblName = res.getString(R.string.tbl_t_tag);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
	}
	
	public long addThoughtTag(ContentValues values){
		return replace(tblName, values);
	}
	
}
