package com.echobond.dao;

import com.echobond.db.TagDB;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class TagDAO extends ContentProvider{
	
	public static final String PROVIDER_NAME = "com.echobond.contentprovider.tag";
	//content://authority/path/id
	public static final Uri CONTENT_URI = Uri.parse("content://"+PROVIDER_NAME+"/tag");
	private static final int TAG = 1;
	private static final UriMatcher uriMatcher;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(PROVIDER_NAME, "tag", TAG);
	}

	public void close(){
		if(null != TagDB.getInstance()){
			TagDB.getInstance().close();
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
		if(uriMatcher.match(uri) == TAG){
			TagDB.getInstance().addTag(values);
		}
		return uri;
	}
	
	@Override
	public int bulkInsert(Uri uri, ContentValues[] values) {
		if(uriMatcher.match(uri) == TAG){
			for (ContentValues contentValues : values) {
				TagDB.getInstance().addTag(contentValues);
			}
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
		if(uriMatcher.match(uri) == TAG){
			cursor = TagDB.getInstance().loadTags(selectionArgs);
			cursor.setNotificationUri(getContext().getContentResolver(), uri);
		}
		return cursor;
	}
	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		if(uriMatcher.match(uri) == TAG){
			TagDB.getInstance().updateTag(values, selection, selectionArgs);
			ContentResolver resolver = getContext().getContentResolver();
			resolver.notifyChange(uri, null);
		}
		return 0;
	}
	
	public Cursor loadTagsByHomeThought(String args[]){
		return TagDB.getInstance().loadTagsHome(args);
	}
	
	public Cursor loadTagsByHotThought(String args[]){
		return TagDB.getInstance().loadTagsHot(args);
	}

}
