package com.echobond.dao;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.echobond.db.HotThoughtDB;
import com.echobond.entity.Thought;

public class HotThoughtDAO extends ContentProvider{
	
	public static final String PROVIDER_NAME = "com.echobond.contentprovider.hotthought";
	public static final Uri CONTENT_URI = Uri.parse("content://"+PROVIDER_NAME+"/hotThought");
	private static final int HOTTHOUGHT = 1;
	private static final UriMatcher uriMatcher;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(PROVIDER_NAME, "homeThought", HOTTHOUGHT);
	}
	private HotThoughtDB dbUtil;
	public void addThought(Thought t){
		
	}

	public void close(){
		if(null != dbUtil){
			dbUtil.close();
		}
	}
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean onCreate() {
		dbUtil = HotThoughtDB.getInstance(getContext());
		dbUtil.getReadableDatabase();
		return true;
	}
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Cursor cursor = null;
		if(uriMatcher.match(uri) == HOTTHOUGHT){
			cursor = dbUtil.getHotThoughts();
		}
		return cursor;
	}
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}
}
