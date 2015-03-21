package com.echobond.dao;

import android.content.Context;

import com.echobond.R;
import com.echobond.entity.User;
import com.echobond.util.JSONUtil;
import com.echobond.util.SQLiteDBUtil;

public class UserDAO {
	private Context ctx;
	private SQLiteDBUtil dbUtil;
	public UserDAO(Context ctx){
		this.ctx = ctx;
		dbUtil = SQLiteDBUtil.getInstance(ctx);
	}
	public void addLoginUser(User user){
		dbUtil.insert(ctx.getResources().getString(R.string.tbl_lusr), JSONUtil.fromObjectToJSON(user));
	}
	public void loadLoginUser(){
		dbUtil.query(ctx.getResources().getString(R.string.tbl_lusr), null, null, null);
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
