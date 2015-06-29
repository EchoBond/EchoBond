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
	private static Context ctx;
	public static UserDB getInstance(Context context){
		if(null == INSTANCE){
			ctx = context;
			INSTANCE = new UserDB();
		}
		return INSTANCE;
	}
	
	public static UserDB getInstance(){
		if(null == INSTANCE){
			return new UserDB();
		}
		return INSTANCE;
	}
	
	public UserDB(){
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
	
	public Cursor loadUserById(String[] args){
		return query(ctx.getResources().getString(R.string.sql_s_usr_id), args);
	}
	
	public Cursor loadUserByUserName(String userName){
		return query(ctx.getResources().getString(R.string.sql_s_usr_uname), new String[]{userName});
	}
	
	public Cursor loadUserByGroup(String[] args){
		return query(ctx.getResources().getString(R.string.sql_s_usr_grp), args);
	}
	
	public Cursor loadUserBySelfTag(String[] args){
		return query(ctx.getResources().getString(R.string.sql_s_usr_self_tag), args);
	}

	public Cursor loadUserByLikeTag(String[] args){
		return query(ctx.getResources().getString(R.string.sql_s_usr_like_tag), args);
	}

	public Cursor loadUserByKeyword(String[] args){
		String sql = ctx.getResources().getString(R.string.sql_s_usr_keyword) + 
				"'%" + args[0] + "%' ORDER BY _id DESC LIMIT ? OFFSET ?"; 
		return query(sql, new String[]{args[1], args[2]});
	}
	
}
