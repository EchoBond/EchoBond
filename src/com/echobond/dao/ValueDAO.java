package com.echobond.dao;

import com.echobond.db.MyDBHelper;

import android.content.Context;

public class ValueDAO {
	private Context ctx;
	private MyDBHelper dbUtil;
	
	public ValueDAO(Context ctx) {
		super();
		this.ctx = ctx;
//		this.dbUtil = MyDBHelper.getInstance(ctx);
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
