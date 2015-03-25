package com.echobond.dao;

import java.util.ArrayList;

import com.echobond.db.ThoughtTagDB;
import com.echobond.entity.Tag;

import android.content.Context;

public class ThoughtTagDAO {
	private Context ctx;
	private ThoughtTagDB dbUtil;
	public ThoughtTagDAO(Context ctx){
		this.ctx = ctx;
		dbUtil = ThoughtTagDB.getInstance(ctx);
	}
	public void addThoughtTags(int thoughtId, ArrayList<Tag> tags){
		dbUtil.addThoughtTags(thoughtId, tags);
	}
	public Context getCtx() {
		return ctx;
	}
	public void setCtx(Context ctx) {
		this.ctx = ctx;
	}
	public ThoughtTagDB getDbUtil() {
		return dbUtil;
	}
	public void setDbUtil(ThoughtTagDB dbUtil) {
		this.dbUtil = dbUtil;
	}
	
}
