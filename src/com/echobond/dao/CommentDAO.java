package com.echobond.dao;

import java.util.ArrayList;

import com.echobond.db.CommentDB;
import com.echobond.entity.Comment;

import android.content.Context;
import android.database.Cursor;

public class CommentDAO {
	private Context ctx;
	private CommentDB dbUtil;
	
	public CommentDAO(Context ctx) {
		super();
		this.ctx = ctx;
		this.dbUtil = CommentDB.getInstance(ctx);
	}
	public void addComment(Comment cmt){
		dbUtil.addComment(cmt.putValues());
	}
	public void addComments(ArrayList<Comment> cmts){
		for (Comment comment : cmts) {
			addComment(comment);
		}
	}
	public Cursor loadComments(int id){
		return dbUtil.loadComments(id);
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
