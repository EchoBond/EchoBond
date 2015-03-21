package com.echobond.dao;

import android.content.Context;

import com.echobond.entity.Thought;
import com.echobond.util.SQLiteDBUtil;

public class ThoughtDAO {
	private Context ctx;
	private SQLiteDBUtil dbUtil;
	public void addThought(Thought t){
		
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
