package com.echobond.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.JSONObject;

import com.echobond.R;
import com.echobond.util.GCMUtil;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

/**
 * 
 * @author aohuijun
 *
 */
public class StartPageFragment extends Fragment {
	
	private ImageButton loginEmail, signEmail;
	private FragmentTransaction transaction;
	Intent intent = new Intent();
	
	private UiLifecycleHelper uiLifecycleHelper;
	private LoginButton loginButton;
	private GoogleCloudMessaging gcm;
	private String regId;
	private String senderId;
	private Context ctx;
	private AtomicInteger msgId = new AtomicInteger();

    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    
	private Session.StatusCallback myStatuscallback = new Session.StatusCallback() {
	    @Override
	    public void call(Session session, SessionState state, Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }
	};
	
	/** called when request to facebook returned.*/
	private Request.Callback myRequestCallback = new Request.Callback() {
		
		@Override
		public void onCompleted(Response response) {
            /* handle the result */
        	GraphObject rs = response.getGraphObject();
        	if(null != rs){
        		JSONObject jObj = rs.getInnerJSONObject();
        		new AlertDialog.Builder(StartPageFragment.this.getActivity()).setMessage(jObj.toString()).show();
        	}
		}
	};
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    uiLifecycleHelper = new UiLifecycleHelper(getActivity(), myStatuscallback);
	    uiLifecycleHelper.onCreate(savedInstanceState);
	};
	
	@Override
	public void onResume() {
		super.onResume();
	    // For scenarios where the main activity is launched and user
	    // session is not null, the session state change notification
	    // may not be triggered. Trigger it if it's open/closed.
	    Session session = Session.getActiveSession();
	    if (session != null &&
	           (session.isOpened() || session.isClosed()) ) {
	        onSessionStateChange(session, session.getState(), null);
	    }
	    // For scenarios when google play is disabled upon resume
	    if(!GCMUtil.checkPlayServices(this.getActivity())){
	    	//download google play or enable it
	    }
		uiLifecycleHelper.onResume();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		uiLifecycleHelper.onPause();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		uiLifecycleHelper.onDestroy();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiLifecycleHelper.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiLifecycleHelper.onSaveInstanceState(outState);
	}
	
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		//already logged in
	    if (state.isOpened()) {
	    	requestReadPermissions();
	    	loginButton.setBackgroundResource(R.drawable.continue_facebook);
	    	loadUserData(session);
	    } 
	    //already logged out
	    else if (state.isClosed()) {
	    	loginButton.setBackgroundResource(R.drawable.continue_facebook);
	    }
	}
	
	private void requestReadPermissions(){
		Session session = Session.getActiveSession();
		if(null == session){
			Session.openActiveSession(getActivity(), true, myStatuscallback);
		}
		//asking permissions
		String[] pers = getResources().getString(R.string.facebook_permissions).split(",");
		ArrayList<String> readPermissions = new ArrayList<String>();
		for(int i=0;i<pers.length;i++){
			if(!session.isPermissionGranted(pers[i])){
				readPermissions.add(pers[i]);
			}
		}
	    loginButton.setReadPermissions(readPermissions);
	}
	
	private void loadUserData(Session session){
    	new Request(session, getResources().getString(R.string.facebook_root_path), null, HttpMethod.GET, myRequestCallback).executeAsync();
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
	    return getActivity().getSharedPreferences(getActivity().getClass().getSimpleName(),
	            Context.MODE_PRIVATE);
	}
	
	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	private static int getAppVersion(Context context) {
	    try {
	        PackageInfo packageInfo = context.getPackageManager()
	                .getPackageInfo(context.getPackageName(), 0);
	        return packageInfo.versionCode;
	    } catch (NameNotFoundException e) {
	        // should never happen
	        throw new RuntimeException("Could not get package name: " + e);
	    }
	}
	
	/**
	 * Registers the application with GCM servers asynchronously.
	 * <p>
	 * Stores the registration ID and app versionCode in the application's
	 * shared preferences.
	 */
	private void registerInBackground() {
	    new AsyncTask<Void,String,String>() {
	        @Override
	        protected String doInBackground(Void... params) {
	            String msg = "";
	            try {
	                if (gcm == null) {
	                    gcm = GoogleCloudMessaging.getInstance(ctx);
	                }
	                regId = gcm.register(senderId);
	                msg = "Device registered, registration ID=" + regId;

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
	                msg = "Error :" + ex.getMessage();
	                // If there is an error, don't just keep trying to register.
	                // Require the user to click a button again, or perform
	                // exponential back-off.
	            }
	            return msg;
	        }
	    }.execute(null, null, null);
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View startPageView = inflater.inflate(R.layout.authentication_startpage, container, false);
		
		loginButton = (LoginButton) startPageView.findViewById(R.id.loginFbBtn);
		
        loginEmail = (ImageButton)startPageView.findViewById(R.id.loginEmailBtn);
        signEmail = (ImageButton)startPageView.findViewById(R.id.signEmailBtn);
        
	    //1. modify login button UI according to FB session state
        /*
        Session session = Session.getActiveSession();
	    if(null == session || !session.isOpened()){
	    	loginButton.setBackgroundResource(R.drawable.continue_facebook);
	    } else {
	    	loginButton.setBackgroundResource(R.drawable.continue_facebook);
	    }
	    */
        loginButton.setBackgroundResource(R.drawable.continue_facebook);
	    loginButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
	    loginButton.setFragment(this);
	    //2. check google play services, also in onResume()
	    if(!GCMUtil.checkPlayServices(this.getActivity())){
	    	//download google play or enable it
	    }
	    //3. get registration id from SharedPreference or register device in gcm
	    ctx = getActivity().getApplicationContext();	    
	    gcm = GoogleCloudMessaging.getInstance(getActivity());
	    senderId = getResources().getString(R.string.gcm_sender_id);
        regId = getRegistrationId(ctx);
	    regId = "";
        if (regId.isEmpty()) {
            registerInBackground();
        }
        
        /*
        loginFacebook.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO REGISTRATION USING FACEBOOK ACCOUNT
			}
		});
		*/
        
        signEmail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				transaction = getFragmentManager().beginTransaction().replace(R.id.startContent, new SignUpPageFragment());
				transaction.addToBackStack(null);
				transaction.commit();
			}
		});
        
        loginEmail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				transaction = getFragmentManager().beginTransaction().replace(R.id.startContent, new LoginPageFragment());
				transaction.addToBackStack(null);
				transaction.commit();
			}
		});
        
        return startPageView;
	}

}
