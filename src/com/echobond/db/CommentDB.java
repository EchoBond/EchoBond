package com.echobond.db;

import com.echobond.R;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CommentDB extends MyDBHelper {
	
	/*Singleton pattern implementation*/
	private static CommentDB INSTANCE;
	private static Context ctx;
	public static CommentDB getInstance(Context context){
		if(null == INSTANCE){
			ctx = context;
			INSTANCE = new CommentDB();
		}
		return INSTANCE;
	}
	public static CommentDB getInstance(){
		if(null == INSTANCE){
			INSTANCE = new CommentDB();
		}
		return INSTANCE;
	}
	
	public CommentDB(){
		super(ctx);
		Resources res = ctx.getResources();
		this.tblName = res.getString(R.string.tbl_t_cmt);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
	}
	
	public long addComment(ContentValues values){
		return replace(tblName, values);
	}
	
	public Cursor loadComments(int id){
		return query(ctx.getResources().getString(R.string.sql_s_cmt), new String[]{id+""});
	}
	
}
