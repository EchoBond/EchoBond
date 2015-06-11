package com.echobond.application;

import com.echobond.db.ChatDB;
import com.echobond.db.CommentDB;
import com.echobond.db.HomeThoughtDB;
import com.echobond.db.HotThoughtDB;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import android.app.Application;

public class MyApp extends Application {

	public static final String PREF_TYPE_LOGIN = "login";
	public static final String LOGIN_FIRST = "firstUse";
	public static final String LOGIN_TYPE = "loginUser_type";
	public static final String LOGIN_ID = "loginUser_id";
	public static final String LOGIN_PASS = "loginUser_pass";
	public static final String LOGIN_EMAIL = "loginUser_email";
	public static final String LOGIN_FBId = "loginUser_FBId";
	
	public static final String PREF_TYPE_SYSTEM = "system";
	public static final String SYS_APP_VERSION = "app_version";
	public static final String SYS_GCM_ID = "GCM_reg_id";

	
	
	public static final int LOADER_HOME = 0;
	public static final int LOADER_HOT = 1;
	public static final int LOADER_COMMENT = 2;
	public static final int LOADER_CHAT = 3;
	
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
	        .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
	        .diskCacheExtraOptions(480, 800, null)
	        //.taskExecutor(...)
	        //.taskExecutorForCachedImages(...)
	        .threadPoolSize(3) // default
	        .threadPriority(Thread.NORM_PRIORITY - 2) // default
	        .tasksProcessingOrder(QueueProcessingType.FIFO) // default
	        .denyCacheImageMultipleSizesInMemory()
	        .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
	        .memoryCacheSize(2 * 1024 * 1024)
	        .memoryCacheSizePercentage(13) // default
	        //.diskCache(new UnlimitedDiscCache(cacheDir)) // default
	        .diskCacheSize(50 * 1024 * 1024)
	        .diskCacheFileCount(100)
	        .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
	        .imageDownloader(new BaseImageDownloader(this)) // default
	        .imageDecoder(new BaseImageDecoder(true)) // default
	        .defaultDisplayImageOptions(opt) // default
	        .writeDebugLogs()
	        .build();
		ImageLoader.getInstance().init(conf);		
	}
	private void initDB(){
		CommentDB.getInstance(this);
		HomeThoughtDB.getInstance(this);
		HotThoughtDB.getInstance(this);
		ChatDB.getInstance(this);
	}
	
}
