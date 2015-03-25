package com.echobond.dao;

import android.content.Context;
import android.database.Cursor;

import com.echobond.db.UserDB;
import com.echobond.entity.User;

public class UserDAO {
	private Context ctx;
	private UserDB dbUtil;
	public UserDAO(Context ctx){
		this.ctx = ctx;
		dbUtil = UserDB.getInstance(ctx);
	}
	public void addLoginUser(User user){
//		dbUtil.insert(ctx.getResources().getString(R.string.tbl_lusr), JSONUtil.fromObjectToJSON(user));
	}
	public void loadLoginUser(){
//		dbUtil.query(ctx.getResources().getString(R.string.tbl_lusr), null, null, null);
	}
	public void addUser(User user){
		dbUtil.addUser(user.putValues());
	}
	public Cursor loadUserById(String id){
		return dbUtil.loadUserById(id);
	}
	public Cursor loadUserByUserName(String userName){
		return dbUtil.loadUserByUserName(userName);
	}
	public Context getCtx() {
		return ctx;
	}
	public void setCtx(Context ctx) {
		this.ctx = ctx;
	}
	public void close(){
		if(null != dbUtil){
			dbUtil.close();
		}
	}
}
