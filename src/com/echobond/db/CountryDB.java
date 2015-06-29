package com.echobond.db;

import com.echobond.R;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CountryDB extends MyDBHelper {
	
	/*Singleton pattern implementation*/
	private static CountryDB INSTANCE;
	private static Context ctx;
	public static CountryDB getInstance(Context context){
		if(null == INSTANCE){
			ctx = context;
			INSTANCE = new CountryDB();
		}
		return INSTANCE;
	}
	public static CountryDB getInstance(){
		if(null == INSTANCE){
			INSTANCE = new CountryDB();
		}
		return INSTANCE;
	}
	
	public CountryDB(){
		super(ctx);
		Resources res = ctx.getResources();
		this.tblName = res.getString(R.string.tbl_country);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
	}
	
	public Cursor loadAllCountries(){
		return query(ctx.getResources().getString(R.string.sql_s_country), null);
	}
	
	public Cursor loadCountry(String[] args){
		return query(ctx.getResources().getString(R.string.sql_s_country_id), args);
	}
	
	public long addCountry(ContentValues values){
		return replace(tblName, values);
	}
}
