package com.echobond.connector;

import java.io.IOException;

import com.echobond.activity.MainPage;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.content.Context;
import android.os.AsyncTask;

/**
 * 
 * @author Luck
 *
 */
public class GCMRegAsyncTask extends AsyncTask<Object, Integer, String> {
	
	private MainPage mainPage;
	
	/**
	 * Registers the application with GCM servers asynchronously.
	 * <p>
	 * Stores the registration ID and app versionCode in the application's
	 * shared preferences.
	 */
    @Override
    protected String doInBackground(Object... params) {
    	String regId = "";
    	this.mainPage = (MainPage) params[0];
    	String senderId = (String) params[1];
        GoogleCloudMessaging gcm = (GoogleCloudMessaging) params[2];
        Context ctx = mainPage.getApplicationContext();
        try {
            if (gcm == null) {
                gcm = GoogleCloudMessaging.getInstance(ctx);
            }
            regId = gcm.register(senderId);
            //msg = "Device registered, registration ID=" + regId;

            // You should send the registration ID to your server over HTTP,
            // so it can use GCM/HTTP or CCS to send messages to your app.
            // The request to your server should be authenticated if your app
            // is using accounts.
            
            //sendRegistrationIdToBackend();

            // For this demo: we don't need to send it because the device
            // will send upstream messages to a server that echo back the
            // message using the 'from' address in the message.

            // Persist the regID - no need to register again.
            
            //storeRegistrationId(ctx, regId);
        } catch (IOException ex) {
            //msg = "Error :" + ex.getMessage();
            // If there is an error, don't just keep trying to register.
            // Require the user to click a button again, or perform
            // exponential back-off.
        }
        return regId;
    }
    
    @Override
    protected void onPostExecute(String result) {
    	//fragment.setRegId(result);
    	//fragment.onGCMRegComplete();
    }
}
