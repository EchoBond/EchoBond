package com.echobond.dao;

import android.content.Context;

import com.echobond.db.MyDBHelper;

public class ImageDAO {
	private Context ctx;
	private MyDBHelper dbUtil;
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
