package com.echobond.application;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import android.app.Application;

public class MyApplication extends Application {
	@Override
	public void onCreate() {
		initUIL();
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
	
}
