package com.echobond.db;

import com.echobond.R;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class GroupDB extends MyDBHelper {
	
	/*Singleton pattern implementation*/
	private static GroupDB INSTANCE;
	private static Context ctx;
	public static GroupDB getInstance(Context context){
		if(null == INSTANCE){
			ctx = context;
			INSTANCE = new GroupDB();
		}
		return INSTANCE;
	}
	public static GroupDB getInstance(){
		if(null == INSTANCE){
			INSTANCE = new GroupDB();
		}
		return INSTANCE;
	}
	
	public GroupDB(){
		super(ctx);
		Resources res = ctx.getResources();
		this.tblName = res.getString(R.string.tbl_grp);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
	}

	public long addGroup(ContentValues values){
		return replace(tblName, values);
	}
	
	public Cursor loadGroups(String[] args){
		return query(ctx.getResources().getString(R.string.sql_s_grp), args);
	}
	
	public long updateGroup(ContentValues values, String where, String[] whereArgs){
		return update(tblName, values, where, whereArgs);
	}
	
	public Cursor loadGroupHome(String[] args){
		return query(ctx.getResources().getString(R.string.sql_s_grp_home), args);
	}
	
	public Cursor loadGroupHot(String[] args){
		return query(ctx.getResources().getString(R.string.sql_s_grp_hot), args);
	}
}
