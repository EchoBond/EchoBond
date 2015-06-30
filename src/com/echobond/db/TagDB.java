package com.echobond.db;

import com.echobond.R;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TagDB extends MyDBHelper {
	
	/*Singleton pattern implementation*/
	private static TagDB INSTANCE;
	private static Context ctx;
	private String tagSelfTbl, tagLikeTbl;
	public static TagDB getInstance(Context context){
		if(null == INSTANCE){
			ctx = context;
			INSTANCE = new TagDB();
		}
		return INSTANCE;
	}
	
	public static TagDB getInstance(){
		if(null == INSTANCE){
			INSTANCE = new TagDB();
		}
		return INSTANCE;
	}
	
	public TagDB(){
		super(ctx);
		Resources res = ctx.getResources();
		this.tblName = res.getString(R.string.tbl_tag);
		this.tagLikeTbl = res.getString(R.string.tbl_ltag);
		this.tagSelfTbl = res.getString(R.string.tbl_tag_slf);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
	}
	public long addTag(ContentValues values){
		return replace(tblName, values);
	}
	
	public long addTagSelf(ContentValues values){
		return replace(tagSelfTbl, values);
	}
	
	public long addTagLike(ContentValues values){
		return replace(tagLikeTbl, values);
	}
	
	public Cursor loadTags(String[] args){
		return query(ctx.getResources().getString(R.string.sql_s_tag), args);
	}
	
	public Cursor loadTagsSelf(String[] args){
		return query(ctx.getResources().getString(R.string.sql_s_tag_self), args);
	}
	
	public Cursor loadTagsLike(String[] args){
		return query(ctx.getResources().getString(R.string.sql_s_tag_like), args);
	}
	
	public Cursor loadTagsWithSelf(String[] args){
		return query(ctx.getResources().getString(R.string.sql_s_tag_self_list), args);
	}
	
	public Cursor loadTagsWithLike(String[] args){
		return query(ctx.getResources().getString(R.string.sql_s_tag_like_list), args);
	}
	
	public long updateTag(ContentValues values, String where, String[] whereArgs){
		return update(tblName, values, where, whereArgs);
	}
	
	public Cursor loadTagsHome(String[] args){
		return query(ctx.getResources().getString(R.string.sql_s_tag_home), args);
	}
	
	public Cursor loadTagsHot(String[] args){
		return query(ctx.getResources().getString(R.string.sql_s_tag_hot), args);
	}
	
	public Cursor loadTagsByThought(String[] args){
		return query(ctx.getResources().getString(R.string.sql_s_tag_t), args);
	}
	
	public int removeSelfTags(String where, String[] args){
		return delete(tagSelfTbl, where, args);
	}
	
	public int removeLikedTags(String where, String[] args){
		return delete(tagLikeTbl, where, args);
	}
	
}
