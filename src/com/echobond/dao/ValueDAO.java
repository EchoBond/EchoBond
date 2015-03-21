package com.echobond.dao;

import com.echobond.util.SQLiteDBUtil;

import android.content.Context;

public class ValueDAO {
	private Context ctx;
	private SQLiteDBUtil dbUtil;
	
	public ValueDAO(Context ctx) {
		super();
		this.ctx = ctx;
		this.dbUtil = SQLiteDBUtil.getInstance(ctx);
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
