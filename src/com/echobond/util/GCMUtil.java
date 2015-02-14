package com.echobond.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

import com.echobond.R;
import com.echobond.activity.MainPage;
import com.echobond.connector.GCMRegAsyncTask;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GCMUtil {

	private GoogleCloudMessaging gcm;
	private String regId;
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";	
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	
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
	 * Dispatching async task to register the device in GCM
	 * @param startPageFragment
	 * @param senderId
	 * @param gcm
	 */
	public void regInBackground(MainPage mainPage, String senderId, GoogleCloudMessaging gcm){
		new GCMRegAsyncTask().execute(mainPage, senderId, gcm);
	}
	
	/**
	 * Gets the current registration ID for application on GCM service.
	 * 
	 * If result is empty, the app needs to register.
	 *
	 * @return registration ID, or empty string if there is no existing
	 *         registration ID.
	 */
	private String getRegistrationId(Context context) {
	    final SharedPreferences prefs = getGCMPreferences(context);
	    String registrationId = prefs.getString(PROPERTY_REG_ID, "");
	    if (registrationId.isEmpty()) {
	        return "";
	    }
	    // Check if app was updated; if so, it must clear the registration ID
	    // since the existing regID is not guaranteed to work with the new
	    // app version.
	    int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
	    int currentVersion = getAppVersion(context);
	    if (registeredVersion != currentVersion) {
	        return "";
	    }
	    return registrationId;
	}
	
	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	private SharedPreferences getGCMPreferences(Context context) {
	    // This sample app persists the registration ID in shared preferences, but
	    // how you store the regID in your app is up to you.
	    return context.getSharedPreferences(context.getClass().getSimpleName(),
	            Context.MODE_PRIVATE);
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
	private void registerDevice(MainPage mainPage){
		gcm = GoogleCloudMessaging.getInstance(mainPage);
		String senderId = mainPage.getResources().getString(R.string.gcm_sender_id);
	    regId = getRegistrationId(mainPage);
	    regId = "";
	    if (regId.isEmpty()) {
	        GCMUtil.getInstance().regInBackground(mainPage, senderId, gcm);
	    }
	}
}
