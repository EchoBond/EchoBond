package com.echobond.util;

import android.app.Activity;

import com.echobond.connector.GCMRegAsyncTask;
import com.echobond.fragment.StartPageFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GCMUtil {

	private static class GCMUtilHolder{
		public static GCMUtil INSTANCE = new GCMUtil();
	}
	public static GCMUtil getInstance(){
		return GCMUtilHolder.INSTANCE;
	}
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	
	
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
	
	public void regInBackground(StartPageFragment startPageFragment, String senderId, GoogleCloudMessaging gcm){
		new GCMRegAsyncTask().execute(startPageFragment, senderId, gcm);
	}
}
