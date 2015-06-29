package com.echobond.db;

import com.echobond.R;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class LanguageDB extends MyDBHelper {
	
	/*Singleton pattern implementation*/
	private static LanguageDB INSTANCE;
	private static Context ctx;
	public static LanguageDB getInstance(Context context){
		if(null == INSTANCE){
			ctx = context;
			INSTANCE = new LanguageDB();
		}
		return INSTANCE;
	}
	public static LanguageDB getInstance(){
		if(null == INSTANCE){
			INSTANCE = new LanguageDB();
		}
		return INSTANCE;
	}
	
	public LanguageDB(){
		super(ctx);
		Resources res = ctx.getResources();
		this.tblName = res.getString(R.string.tbl_language);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
	}

	public Cursor loadAllLanguages(){
		return query(ctx.getResources().getString(R.string.sql_s_language), null);
	}
	
	public Cursor loadLanguage(String[] args){
		return query(ctx.getResources().getString(R.string.sql_s_language_id), args);
	}
	
	public long addLanguage(ContentValues values){
		return replace(tblName, values);
	}
}
