package com.echobond.connector;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.R;
import com.echobond.activity.MainPage;
import com.echobond.entity.RawHttpRequest;
import com.echobond.entity.RawHttpResponse;
import com.echobond.util.HTTPUtil;
import com.echobond.util.SPUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.content.Context;
import android.os.AsyncTask;

/**
 * This task is to handle registration of user's device in GCM as well as update in server<br>
 * Params (Object): activity(MainPage), url<br>
 * @version 1.0
 * @author Luck
 * 
 */
public class GCMRegAsyncTask extends AsyncTask<Object, Integer, JSONObject> {
	
	private MainPage context;
	
	/**
	 * Registers the application with GCM servers asynchronously.
	 * <p>
	 * Stores registration ID and app versionCode in shared preferences.
	 */
    @Override
    protected JSONObject doInBackground(Object... params) {
    	//params
    	JSONObject result = new JSONObject();
    	this.context = (MainPage) params[0];
    	String url = (String) params[1];
    	//GCM configs
    	String regId = "";
    	String senderId = context.getResources().getString(R.string.gcm_sender_id);
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
        Context ctx = context.getApplicationContext();
        //HTTP
        String method = RawHttpRequest.HTTP_METHOD_POST;
        JSONObject body = new JSONObject();
        RawHttpRequest request = new RawHttpRequest(url, method, null, null, true);
        RawHttpResponse response = null;
        try {
            if (gcm == null) {
                gcm = GoogleCloudMessaging.getInstance(ctx);
            }
            regId = gcm.register(senderId);

            // You should send the registration ID to your server over HTTP,
            // so it can use GCM/HTTP or CCS to send messages to your app.
            // The request to your server should be authenticated if your app
            // is using accounts.
            body.put("userId", (String)SPUtil.get(context, "login", "loginUser_id", "", String.class));
            body.put("email", (String)SPUtil.get(context, "login", "loginUser_email", "", String.class));
            body.put("regId", regId);
            response = HTTPUtil.getInstance().send(request);

            // Persist the regID - no need to register again.
            SPUtil.put(context, "system", "GCM_reg_id", regId);
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
        try {
        	if(null != response){
        		result.put("response", response.getMsg());
        		result.put("regId", regId);
        	}
		} catch (JSONException e) {
			e.printStackTrace();
		}
        return result;
    }
    
    @Override
    protected void onPostExecute(JSONObject result) {
    	context.onRegFinished(result);
    }
}
