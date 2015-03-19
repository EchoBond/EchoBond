package com.echobond.dao;

import android.content.Context;

import com.echobond.entity.User;

public class UserDAO {
	private Context ctx;
	public UserDAO(){}
	public UserDAO(Context ctx){
		this.ctx = ctx;
	}
	public void addLoginUser(User user){
		
	}
	public Context getCtx() {
		return ctx;
	}
	public void setCtx(Context ctx) {
		this.ctx = ctx;
	}
	
}
