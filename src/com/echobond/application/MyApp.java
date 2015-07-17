package com.echobond.application;

import java.util.HashMap;
import java.util.Map;

import com.echobond.activity.AppSettingPage;
import com.echobond.activity.ChatPage;
import com.echobond.activity.CommentPage;
import com.echobond.activity.FollowingPage;
import com.echobond.activity.ImagePage;
import com.echobond.activity.IntroPage;
import com.echobond.activity.MainPage;
import com.echobond.activity.NewPostPage;
import com.echobond.activity.SearchPage;
import com.echobond.activity.StartPage;
import com.echobond.db.CategoryDB;
import com.echobond.db.ChatDB;
import com.echobond.db.CommentDB;
import com.echobond.db.CountryDB;
import com.echobond.db.GroupDB;
import com.echobond.db.HomeThoughtDB;
import com.echobond.db.HotThoughtDB;
import com.echobond.db.LanguageDB;
import com.echobond.db.TagDB;
import com.echobond.db.ThoughtTagDB;
import com.echobond.db.UserDB;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

public class MyApp extends Application implements Application.ActivityLifecycleCallbacks {

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
	public static final String SYS_GCM_REG_TIME = "GCM_reg_time";
	public static final String SYS_NOTIFY_TYPE = "notification_type";
	
	public static final int LOADER_HOME = 0;
	public static final int LOADER_HOT = 1;
	public static final int LOADER_COMMENT = 2;
	public static final int LOADER_CHAT = 3;
	public static final int LOADER_MSG_LIST = 4;
	public static final int LOADER_GROUP = 5;
	public static final int LOADER_TAG = 6;
	public static final int LOADER_USER_TAG = 7;
	public static final int LOADER_FOLLOW_GROUP = 8;
	public static final int LOADER_FOLLOW_TAG = 9;
	
	public static final int THOUGHT_POST = 0;
	public static final int THOUGHT_MESSAGE = 1;
	public static final int THOUGHT_BOOST = 2;
	public static final int THOUGHT_COMMENT = 3;
	public static final int THOUGHT_SHARE = 4;
	
	public static final int DEFAULT_OFFSET = 0;
	public static final int LIMIT_INIT = 10;
	public static final int LIMIT_INIT_FETCH = 1000;
	public static final int LIMIT_INCREMENT = 10;
	public static final long LOAD_INTERVAL = 2000;
	
	public static final int ACTIVITY_NULL = -1;
	public static final int ACTIVITY_APP_SETTING = 0;
	public static final int ACTIVITY_CHAT_PAGE = 1;
	public static final int ACTIVITY_COMMENT_PAGE = 2;
	public static final int ACTIVITY_FOLLOWING_PAGE = 3;
	public static final int ACTIVITY_IMAGE_PAGE = 4;
	public static final int ACTIVITY_INTRO_PAGE = 5;
	public static final int ACTIVITY_MAIN_PAGE = 6;
	public static final int ACTIVITY_NEW_POST_PAGE = 7;
	public static final int ACTIVITY_SEARCH_PAGE = 8;
	public static final int ACTIVITY_START_PAGE = 9;
	
	public static final int VIEW_MORE_FROM_SEARCH = 0;
	public static final int VIEW_MORE_FROM_PROFILE = 1;
	public static final int VIEW_MORE_FROM_POST = 2;
	
	public final static int COLOR_WHITE = 0;
	public final static int COLOR_DARK_RED = 1;
	public final static int COLOR_BRIGHT_RED = 2;
	public final static int COLOR_PINK = 3;
	public final static int COLOR_YELLOW = 4;
	public final static int COLOR_ORANGE = 5;
	public final static int COLOR_BLACK = 6;
	public final static int COLOR_CYAN = 7;
	public final static int COLOR_MINT = 8;
	public final static int COLOR_PURPLE = 9;
	public final static int COLOR_HORIZON = 10;
	public final static int COLOR_GREEN = 11;
	
	public static final String VIEW_MORE_GROUP = "Groups";
	public static final String VIEW_MORE_TAG = "Hashtags";
	
	public static final String BROADCAST_STARTUP = "StartApp";
	public static final String BROADCAST_NOTIFICATION = "newNotification";
	public static final String BROADCAST_UPDATE_PROFILE = "updateUserProfile";
	public static final String BROADCAST_UPDATE_MSGLIST = "msgListUpdate";
	public static final String BROADCAST_CHAT_WITH = "chatWith";
	public static final String BROADCAST_BOOST = "boostThought";
	public static final String BROADCAST_COMMENT = "commentThought";
	
	public static Map<String, Integer> notificationId;
	public static Integer currentNotificationId;
	
    public static final int ALARM_NOTIFICATION_ID = 1;
	
	public static final int NOTIFICATION_VIB_ALARM = 0;
	public static final int NOTIFICATION_ALARM = 1;
	public static final int NOTIFICATION_VIB = 2;
	public static final int NOTIFICATION_NONE = 3;
	
	private static int currentActivityIndex = ACTIVITY_NULL;
	private static Activity currentActivity;
	
	@Override
	public void onCreate() {
		initUIL();
		initDB();
		currentActivityIndex = ACTIVITY_NULL;
		currentActivity = null;
		registerActivityLifecycleCallbacks(this);
		notificationId = new HashMap<String, Integer>();
		currentNotificationId = 1;
	}
	
	@Override
	public void onTerminate() {
		super.onTerminate();
	}
	
	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}
	
	private void initUIL(){
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
	        //.memoryCacheSize(2 * 1024 * 1024)
	        //.memoryCacheSizePercentage(13) // default
	        //.diskCache(new UnlimitedDiscCache(cacheDir)) // default
	        .diskCacheSize(50 * 1024 * 1024)
	        //.diskCacheFileCount(100)
	        .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
	        .imageDownloader(new BaseImageDownloader(this)) // default
	        .imageDecoder(new BaseImageDecoder(true)) // default
	        .defaultDisplayImageOptions(getDefaultDisplayImageOptions(new DisplayImageOptions.Builder()).build()) // default
	        .writeDebugLogs()
	        .build();
		ImageLoader.getInstance().init(conf);		
	}
	
	private void initDB(){
		CommentDB.getInstance(this);
		HomeThoughtDB.getInstance(this);
		HotThoughtDB.getInstance(this);
		ChatDB.getInstance(this);
		GroupDB.getInstance(this);
		TagDB.getInstance(this);
		UserDB.getInstance(this);
		CategoryDB.getInstance(this);
		LanguageDB.getInstance(this);
		CountryDB.getInstance(this);
		ThoughtTagDB.getInstance(this);
	}
	
	public static DisplayImageOptions.Builder getDefaultDisplayImageOptions(DisplayImageOptions.Builder builder){
		return builder.cacheInMemory(true)
			.cacheOnDisk(true)
			.imageScaleType(ImageScaleType.EXACTLY);
	}
	
	@Override
	public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
		setCurrentActivity(activity);
		setCurrentActivityIndex(activity);
	}
	
	@Override
	public void onActivityDestroyed(Activity activity) {
		
	}
	
	@SuppressLint("NewApi")
	@Override
	public void onActivityPaused(Activity activity) {
		if(activity.isTaskRoot()){
			setCurrentActivity(null);
			setCurrentActivityIndex(null);
		}
	}
	
	@Override
	public void onActivityResumed(Activity activity) {
		setCurrentActivity(activity);
		setCurrentActivityIndex(activity);
	}
	
	@Override
	public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void onActivityStarted(Activity activity) {
	}
	
	@Override
	public void onActivityStopped(Activity activity) {
	}
	
	private void setCurrentActivity(Activity activity){
		currentActivity = activity;
	}
	
	private void setCurrentActivityIndex(Activity activity){
		if(null == activity){
			currentActivityIndex = -1;
		} else if(activity instanceof AppSettingPage){
			currentActivityIndex = ACTIVITY_APP_SETTING;
		} else if(activity instanceof ChatPage){
			currentActivityIndex = ACTIVITY_CHAT_PAGE;
		} else if(activity instanceof CommentPage){
			currentActivityIndex = ACTIVITY_COMMENT_PAGE;
		} else if(activity instanceof FollowingPage){
			currentActivityIndex = ACTIVITY_FOLLOWING_PAGE;
		} else if(activity instanceof ImagePage){
			currentActivityIndex = ACTIVITY_IMAGE_PAGE;
		} else if(activity instanceof IntroPage){
			currentActivityIndex = ACTIVITY_INTRO_PAGE;
		} else if(activity instanceof MainPage){
			currentActivityIndex = ACTIVITY_MAIN_PAGE;
		} else if(activity instanceof NewPostPage){
			currentActivityIndex = ACTIVITY_NEW_POST_PAGE;
		} else if(activity instanceof SearchPage){
			currentActivityIndex = ACTIVITY_SEARCH_PAGE;
		} else if(activity instanceof StartPage){
			currentActivityIndex = ACTIVITY_START_PAGE;
		}		
	}
	
	public static int getCurrentActivityIndex(){
		return currentActivityIndex;
	}
	
	public static Activity getCurrentActivity(){
		if(null == currentActivity){
			return currentActivity;
		} else if(currentActivity instanceof AppSettingPage){
			return (AppSettingPage)currentActivity;
		} else if(currentActivity instanceof ChatPage){
			return (ChatPage)currentActivity;
		} else if(currentActivity instanceof CommentPage){
			return (CommentPage)currentActivity;
		} else if(currentActivity instanceof FollowingPage){
			return (FollowingPage)currentActivity;
		} else if(currentActivity instanceof ImagePage){
			return (ImagePage)currentActivity;
		} else if(currentActivity instanceof IntroPage){
			return (IntroPage)currentActivity;
		} else if(currentActivity instanceof MainPage){
			return (MainPage)currentActivity;
		} else if(currentActivity instanceof NewPostPage){
			return (NewPostPage)currentActivity;
		} else if(currentActivity instanceof SearchPage){
			return (SearchPage)currentActivity;
		} else if(currentActivity instanceof StartPage){
			return (StartPage)currentActivity;
		}
		return currentActivity;
	}
	
}
