package com.echobond.util;


import com.echobond.R;

import android.content.Context;
import android.content.res.Resources;
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
		super(ctx, ctx.getResources().getString(R.string.url_domain), null, 2);
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

}
