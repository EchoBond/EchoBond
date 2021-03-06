package com.echobond.fragment;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.R;
import com.echobond.activity.StartPage;
import com.echobond.entity.User;
import com.echobond.intf.StartPageFragmentsSwitchAsyncTaskCallback;
import com.echobond.util.CommUtil;
import com.echobond.util.GCMUtil;
import com.facebook.FacebookException;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.OnErrorListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;
/**
 * 
 * @author AO Huijun
 * @author Luck
 *
 */
public class StartPageFragment extends Fragment {
	
	private StartPageFragmentsSwitchAsyncTaskCallback mClickListener;
	private ImageView loginEmail, signEmail;
	private LoginButton loginButton;

	private UiLifecycleHelper uiLifecycleHelper;
	private JSONObject FBAccount;
	
	public static final String THREAD_TIMEOUT_NAME = "thread_timeout";
    
	private Session.StatusCallback myStatuscallback = new Session.StatusCallback() {
	    @Override
	    public void call(Session session, SessionState state, Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }
	};
	
	/** called when request to Facebook returned.*/
	private Request.Callback myRequestCallback = new Request.Callback() {
		
		@Override
		public void onCompleted(Response response) {
			((StartPage)getActivity()).loading(false);
            /* handle the result */
        	GraphObject rs = response.getGraphObject();
        	/*
        	boolean fullRead = true;
        	Session session = Session.getActiveSession();
        	String[] pers = getResources().getString(R.string.facebook_permissions).split(",");
        	for(int i=0;i<pers.length;i++){
        		if(!session.isPermissionGranted(pers[i]))
        			fullRead = false;
        	}
			if(!fullRead){
				session.closeAndClearTokenInformation();
				session = Session.openActiveSession(getActivity(), true, myStatuscallback);
				session.requestNewReadPermissions(new NewPermissionsRequest(getActivity(), pers));
			}
			else */if(null != rs){
        		loginButton.setClickable(false);
        		if(null == FBAccount){
        			FBAccount = rs.getInnerJSONObject();
		    		User fbUser = new User();
		    		try{
		    			fbUser.setEmail(FBAccount.getString("email"));
			    		fbUser.setFBId(FBAccount.getString("id"));
			    		fbUser.setFirstName(FBAccount.getString("first_name"));
			    		fbUser.setLastName(FBAccount.getString("last_name"));
			    		fbUser.setName(FBAccount.getString("name"));
			    		fbUser.setTimeZone(FBAccount.getInt("timezone"));
			    		fbUser.setLocale(FBAccount.getString("locale"));
			    		fbUser.setGender(FBAccount.getString("gender"));
		    		} catch (JSONException e){
		    			e.printStackTrace();
		    		}
		    		mClickListener.onButtonSelected(StartPage.BUTTON_TYPE_FBSIGNIN, fbUser);
        		}
        	}
        	else {
        		loginButton.setClickable(true);
    			Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.hint_network_issue), Toast.LENGTH_LONG).show();
        	}
		}
	};
    
	private Thread timeOutThread = new Thread() {
		
		@Override
		public void run() {
			if(!interrupted()){
				//still opening , must be timed out, restart activity
				if(Session.getActiveSession() != null && Session.getActiveSession().getState() == SessionState.OPENING){
					if(CommUtil.isThreadRunning(THREAD_TIMEOUT_NAME))
						return;
					Session.getActiveSession().closeAndClearTokenInformation();
					Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.hint_network_issue), Toast.LENGTH_LONG).show();
					Intent intent = new Intent();
					intent.setClass(StartPageFragment.this.getActivity(), StartPage.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					getActivity().finish();
				}
			}
		}
	};
	
	public StartPageFragment(){}
	
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
		if(state == SessionState.OPENING){
			if(!CommUtil.isThreadRunning(THREAD_TIMEOUT_NAME)){
				loginButton.setClickable(false);
				new Handler().postAtTime(timeOutThread, SystemClock.uptimeMillis() + 6000);
			}
		}
		else if (state.isOpened()) {
			((StartPage)getActivity()).loading(true);
			loginButton.setClickable(true);
			loadUserFBData(session);
	    }
	    else if(state.isClosed()){
	    	((StartPage)getActivity()).loading(false);
	    	loginButton.setClickable(true);
	    	if(null != exception && state == SessionState.CLOSED_LOGIN_FAILED){
	    		Bundle bd = getActivity().getIntent().getExtras();
	    		if(null != bd && bd.getBoolean("logout")){
	    			bd.remove("logout");
	    		} else {
	    			Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.hint_network_issue), Toast.LENGTH_LONG).show();
	    		}
	    	}
    		session.closeAndClearTokenInformation();
	    }
	}
	
	/**
	 * need to be called OUTSIDE the ASYNC session state callback
	 * 
	 */
	private void requestReadPermissions(){
		//asking permissions
		String[] pers = getResources().getString(R.string.facebook_permissions).split(",");
		ArrayList<String> readPermissions = new ArrayList<String>();
		for(int i=0;i<pers.length;i++){
			readPermissions.add(pers[i]);
		}
	    loginButton.setReadPermissions(readPermissions);
	}
	
	private void loadUserFBData(Session session){
		if(session != null && session.isOpened()){
	    	Request request = new Request(session, getResources().getString(R.string.facebook_root_path), null, HttpMethod.GET, myRequestCallback);
	    	request.executeAsync();
		}
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {		
		View startPageView = inflater.inflate(R.layout.fragment_startpage, container, false);
		loginButton = (LoginButton) startPageView.findViewById(R.id.button_login_Fb);
        loginEmail = (ImageView)startPageView.findViewById(R.id.button_signin_email);
        signEmail = (ImageView)startPageView.findViewById(R.id.button_signup_email);
        timeOutThread.setName(THREAD_TIMEOUT_NAME);
        
        requestReadPermissions();
        loginButton.setBackgroundResource(R.drawable.button_facebook_login);
	    loginButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
	    loginButton.setFragment(this);
	    
	    loginButton.setLoginBehavior(SessionLoginBehavior.SSO_WITH_FALLBACK);
	    loginButton.setOnErrorListener(new OnErrorListener() {
			
			@Override
			public void onError(FacebookException error) {
				
			}
		});

        signEmail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mClickListener.onFragmentSelected(StartPage.FRAG_SIGNUP);
			}
		});
        
        loginEmail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mClickListener.onFragmentSelected(StartPage.FRAG_LOGIN);
			}
		});
        
        //A valid session, auto login
        Session session = Session.getActiveSession();
        if (session != null) {
            if (!session.isClosed()) {
            	onSessionStateChange(session, session.getState(), null);
            }
        }
        return startPageView;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mClickListener = (StartPageFragmentsSwitchAsyncTaskCallback) activity;
		} catch (ClassCastException e) {
			// should never happen in normal cases
			throw new ClassCastException(activity.toString() + "must implement OnLoginClickListener. ");
		}
	}
	
	public Runnable getTimeOutrunnable() {
		return timeOutThread;
	}
	
	public LoginButton getLoginButton(){
		return loginButton;
	}
	
}
