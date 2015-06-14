package com.echobond.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

import com.echobond.R;
import com.echobond.activity.MainPage;
import com.echobond.application.MyApp;
import com.echobond.connector.GCMRegAsyncTask;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class GCMUtil {

	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	private final static int GCM_REG_EXPIRE_DAY = 30;
	
	private static class GCMUtilHolder{
		public static GCMUtil INSTANCE = new GCMUtil();
	}
	public static GCMUtil getInstance(){
		return GCMUtilHolder.INSTANCE;
	}
	
	/**
	 * Check the device to make sure it has the Google Play Services APK. If
	 * it doesn't, display a dialog that allows users to download the APK from
	 * the Google Play Store or enable it in the device's system settings.
	 */
	public static boolean checkPlayServices(Activity activity) {
	    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
	    if (resultCode != ConnectionResult.SUCCESS) {
	        if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
	            GooglePlayServicesUtil.getErrorDialog(resultCode, activity,
	                    PLAY_SERVICES_RESOLUTION_REQUEST).show();
	        } else {
	        	activity.finish();
	        }
	        return false;
	    }
	    return true;
	}
	
	/**
	 * Gets the current registration ID for application on GCM service.
	 * 
	 * If result is empty, the app needs to register.
	 *
	 * @return registration ID, or empty string if there is no existing
	 *         registration ID.
	 */
	private String getRegId(Context context) {
		String regId = (String) SPUtil.get(context, MyApp.PREF_TYPE_SYSTEM, MyApp.SYS_GCM_ID, "", String.class);
	    // Check if app was updated; if so, it must clear the registration ID
	    // since the existing regID is not guaranteed to work with the new
	    // app version.
	    int regVersion = (Integer) SPUtil.get(context, MyApp.PREF_TYPE_SYSTEM, MyApp.SYS_APP_VERSION, 1, Integer.class);
	    int currentVersion = getAppVersion(context);
	    int regDate = Integer.parseInt(((String) SPUtil.get(context, MyApp.PREF_TYPE_SYSTEM, MyApp.SYS_GCM_REG_TIME, 
	    		"2000-1-1", String.class)).replace("-", ""));
	    int today = Integer.parseInt(CommUtil.getDateAsString(null).replace("-", ""));
	    if (regVersion != currentVersion) {
	        return "";
	    } else if(today - regDate > GCM_REG_EXPIRE_DAY){
	    	return "";
	    }
	    return regId;
	}
	
	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	private int getAppVersion(Context context) {
	    try {
	        PackageInfo packageInfo = context.getPackageManager()
	                .getPackageInfo(context.getPackageName(), 0);
	        return packageInfo.versionCode;
	    } catch (NameNotFoundException e) {
	        // should never happen
	        throw new RuntimeException("Could not get package name: " + e);
	    }
	}
	
    //get registration id from SharedPreference or register device in gcm
	public void registerDevice(MainPage mainPage){
	    String regId = getRegId(mainPage.getApplicationContext());
	    if (regId.isEmpty()) {
	    	String url = HTTPUtil.getInstance().composePreURL(mainPage) + mainPage.getResources().getString(R.string.url_gcm_reg);
	    	new GCMRegAsyncTask().execute(mainPage, url);
	    }
	}
}
