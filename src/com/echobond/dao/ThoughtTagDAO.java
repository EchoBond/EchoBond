package com.echobond.dao;

import com.echobond.db.ThoughtTagDB;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class ThoughtTagDAO extends ContentProvider{
	
	public static final String PROVIDER_NAME = "com.echobond.contentprovider.thoughttag";
	public static final Uri CONTENT_URI = Uri.parse("content://"+PROVIDER_NAME+"/thoughtTag");

	private static final int THOUGHT_TAG = 1;
	private static final UriMatcher uriMatcher;

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(PROVIDER_NAME, "thoughtTag", THOUGHT_TAG);
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
		if(uriMatcher.match(uri) == THOUGHT_TAG){
			ThoughtTagDB.getInstance().addThoughtTag(values);
		}
		return uri;
	}
	@Override
	public int bulkInsert(Uri uri, ContentValues[] values) {
		if(uriMatcher.match(uri) == THOUGHT_TAG){
			for (ContentValues contentValues : values) {
				ThoughtTagDB.getInstance().addThoughtTag(contentValues);				
			}
		}
		return 0;
	}
	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Cursor cursor = null;
		if(uriMatcher.match(uri) == THOUGHT_TAG){
			return cursor;
		}
		return null;
	}
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
