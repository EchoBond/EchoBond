package com.echobond.dao;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.echobond.db.HotThoughtDB;

public class HotThoughtDAO extends ContentProvider{
	
	public static final String PROVIDER_NAME = "com.echobond.contentprovider.hotthought";
	//content://authority/path/id
	public static final Uri CONTENT_URI = Uri.parse("content://"+PROVIDER_NAME+"/hotThought");
	private static final int HOTTHOUGHT = 1;
	private static final UriMatcher uriMatcher;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(PROVIDER_NAME, "hotThought", HOTTHOUGHT);
	}

	public void close(){
		if(null != HotThoughtDB.getInstance()){
			HotThoughtDB.getInstance().close();
		}
	}
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		switch(uriMatcher.match(uri)){
		case HOTTHOUGHT:
			return HotThoughtDB.getInstance().removeAllHotThoughts();
		}
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
			HotThoughtDB.getInstance().addHotThought(values);
			/* don't update here, update manually
			ContentResolver resolver = getContext().getContentResolver();
			resolver.notifyChange(uri, null);
			*/
		}
		return uri;
	}
	
	@Override
	public int bulkInsert(Uri uri, ContentValues[] values) {
		if(uriMatcher.match(uri) == HOTTHOUGHT){
			for (ContentValues contentValues : values) {
				HotThoughtDB.getInstance().addHotThought(contentValues);
			}
			/* don't update here, update manually
			ContentResolver resolver = getContext().getContentResolver();
			resolver.notifyChange(uri, null);
			*/
		}
		return 0;
	}
	
	@Override
	public boolean onCreate() {
		return false;
	}
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Cursor cursor = null;
		if(uriMatcher.match(uri) == HOTTHOUGHT){
			cursor = HotThoughtDB.getInstance().getHotThoughts(selectionArgs);
			cursor.setNotificationUri(getContext().getContentResolver(), uri);
		}
		return cursor;
	}
	
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		if(uriMatcher.match(uri) == HOTTHOUGHT){
			HotThoughtDB.getInstance().updateHotThought(values, selection, selectionArgs);
			//ContentResolver resolver = getContext().getContentResolver();
			//resolver.notifyChange(uri, null);
		}
		return 0;
	}
}
