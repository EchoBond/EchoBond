package com.echobond.dao;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.echobond.db.HotThoughtDB;
public class HotThoughtDAO extends ContentProvider{
	
	public static final String PROVIDER_NAME = "com.echobond.contentprovider.hotthought";
	public static final Uri CONTENT_URI = Uri.parse("content://"+PROVIDER_NAME+"/hotThought");
	private static final int HOTTHOUGHT = 1;
	private static final UriMatcher uriMatcher;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(PROVIDER_NAME, "hotThought", HOTTHOUGHT);
	}
	private HotThoughtDB dbUtil;
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
		if(uriMatcher.match(uri) == HOTTHOUGHT){
			dbUtil.addHotThought(values);
			ContentResolver resolver = getContext().getContentResolver();
			resolver.notifyChange(uri, null);
		}
		return uri;
	}
	
	@Override
	public int bulkInsert(Uri uri, ContentValues[] values) {
		if(uriMatcher.match(uri) == HOTTHOUGHT){
			for (ContentValues contentValues : values) {
				dbUtil.addHotThought(contentValues);
			}
			ContentResolver resolver = getContext().getContentResolver();
			resolver.notifyChange(uri, null);
		}
		return 0;
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
			cursor.setNotificationUri(getContext().getContentResolver(), uri);
		}
		return cursor;
	}
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		if(uriMatcher.match(uri) == HOTTHOUGHT){
			dbUtil.addHotThought(values);
			ContentResolver resolver = getContext().getContentResolver();
			resolver.notifyChange(uri, null);
		}
		return 0;
	}
	
	public long addHotThought(ContentValues values){
		if(null == dbUtil){
			dbUtil = HotThoughtDB.getInstance(getContext());
		}
		long id = dbUtil.addHotThought(values);	
		return id;
	}
}
