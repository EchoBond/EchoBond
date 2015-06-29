package com.echobond.db;

import com.echobond.R;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CategoryDB extends MyDBHelper {
	
	/*Singleton pattern implementation*/
	private static CategoryDB INSTANCE;
	private static Context ctx;
	public static CategoryDB getInstance(Context context){
		if(null == INSTANCE){
			ctx = context;
			INSTANCE = new CategoryDB();
		}
		return INSTANCE;
	}
	public static CategoryDB getInstance(){
		if(null == INSTANCE){
			INSTANCE = new CategoryDB();
		}
		return INSTANCE;
	}
	
	public CategoryDB(){
		super(ctx);
		Resources res = ctx.getResources();
		this.tblName = res.getString(R.string.tbl_ctgr);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
	}
	
	public Cursor loadAllCategories(){
		return query(ctx.getResources().getString(R.string.sql_s_category), null);
	}
	
	public Cursor loadCategory(String[] args){
		return query(ctx.getResources().getString(R.string.sql_s_category_id), args);
	}
	
	public long addCategory(ContentValues values){
		return replace(tblName, values);
	}
	
}
