package com.echobond.db;

import com.echobond.R;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserDB extends MyDBHelper {
	
	/*Singleton pattern implementation*/
	private static UserDB INSTANCE;
	public static UserDB getInstance(Context ctx){
		if(null == INSTANCE){
			INSTANCE = new UserDB(ctx.getApplicationContext());
		}
		return INSTANCE;
	}
	
	public UserDB(Context ctx){
		super(ctx);
		Resources res = ctx.getResources();
		this.tblName = res.getString(R.string.tbl_usr);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
	}
	
	public long addUser(ContentValues values){
		return replace(tblName, values);
	}
	
	public Cursor loadUserById(String id){
		return query(ctx.getResources().getString(R.string.sql_s_usr_id), new String[]{id});
	}
	
	public Cursor loadUserByUserName(String userName){
		return query(ctx.getResources().getString(R.string.sql_s_usr_uname), new String[]{userName});
	}
}
