package com.echobond.application;

import com.echobond.db.CommentDB;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import android.app.Application;

public class MyApp extends Application {

	public static final int LOADER_HOME = 0;
	public static final int LOADER_HOT = 1;
	public static final int LOADER_COMMENT = 2;
	
	@Override
	public void onCreate() {
		initUIL();
		initDB();
	}
	@Override
	public void onTerminate() {
		super.onTerminate();
	}
	
	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
	}
	
	private void initUIL(){
		DisplayImageOptions opt = new DisplayImageOptions.Builder()
			.cacheInMemory(true)
			.cacheOnDisk(true)
			.imageScaleType(ImageScaleType.EXACTLY)
			.build();		
		ImageLoaderConfiguration conf = new ImageLoaderConfiguration.Builder(this)
			.defaultDisplayImageOptions(opt)
			.build();
		ImageLoader.getInstance().init(conf);		
	}
	private void initDB(){
		CommentDB.getInstance(this);
	}
	
}
