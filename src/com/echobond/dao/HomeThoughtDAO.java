package com.echobond.dao;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.echobond.db.HomeThoughtDB;

public class HomeThoughtDAO extends ContentProvider{
	
	public static final String PROVIDER_NAME = "com.echobond.contentprovider.homethought";
	//content://authority/path/id
	public static final Uri CONTENT_URI = Uri.parse("content://"+PROVIDER_NAME+"/homeThought");
	private static final int HOMETHOUGHT = 1;
	private static final UriMatcher uriMatcher;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(PROVIDER_NAME, "homeThought", HOMETHOUGHT);
	}
	private HomeThoughtDB dbUtil;

	public void close(){
		if(null != dbUtil){
			dbUtil.close();
		}
	}
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		
		return 0;
	}
	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		if(uriMatcher.match(uri) == HOMETHOUGHT){
			dbUtil.addHomeThought(values);
			ContentResolver resolver = getContext().getContentResolver();
			resolver.notifyChange(uri, null);
		}
		return uri;
	}
	
	@Override
	public int bulkInsert(Uri uri, ContentValues[] values) {
		if(uriMatcher.match(uri) == HOMETHOUGHT){
			for (ContentValues contentValues : values) {
				dbUtil.addHomeThought(contentValues);
			}
			ContentResolver resolver = getContext().getContentResolver();
			resolver.notifyChange(uri, null);
		}
		return 0;
	}
	
	@Override
	public boolean onCreate() {
		dbUtil = HomeThoughtDB.getInstance(getContext());
		dbUtil.getReadableDatabase();
		return true;
	}
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Cursor cursor = null;
		if(uriMatcher.match(uri) == HOMETHOUGHT){
			cursor = dbUtil.getHomeThoughts();
			cursor.setNotificationUri(getContext().getContentResolver(), uri);
		}
		return cursor;
	}
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		
		return 0;
	}
}
