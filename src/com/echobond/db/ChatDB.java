package com.echobond.db;

import com.echobond.R;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ChatDB extends MyDBHelper {
	
	/*Singleton pattern implementation*/
	private static ChatDB INSTANCE;
	private static Context ctx;
	public static ChatDB getInstance(Context context){
		if(null == INSTANCE){
			ctx = context;
			INSTANCE = new ChatDB();
		}
		return INSTANCE;
	}
	public static ChatDB getInstance(){
		if(null == INSTANCE){
			INSTANCE = new ChatDB();
		}
		return INSTANCE;
	}
	
	public ChatDB(){
		super(ctx);
		Resources res = ctx.getResources();
		this.tblName = res.getString(R.string.tbl_msg);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
	}

	public long addMsg(ContentValues values){
		return replace(tblName, values);
	}

	public Cursor countMsg(String[] args){
		return query(ctx.getResources().getString(R.string.sql_s_count_msg), args);
	}
	
	public Cursor loadMsg(String[] args){
		return query(ctx.getResources().getString(R.string.sql_s_msg), args);
	}
	
	public Cursor loadMsgList(String[] args){
		return query(ctx.getResources().getString(R.string.sql_s_msg_list), args);
	}
	
}
