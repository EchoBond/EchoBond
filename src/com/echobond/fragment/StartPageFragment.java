package com.echobond.fragment;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.R;
import com.echobond.activity.StartPage;
import com.echobond.entity.Gender;
import com.echobond.entity.User;
import com.echobond.util.GCMUtil;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.widget.LoginButton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

/**
 * 
 * @author AO Huijun & LIU Junjie
 *
 */
public class StartPageFragment extends Fragment {
	
	private OnLoginClickListener mClickListener;
	private ImageButton loginEmail, signEmail;
	
	private UiLifecycleHelper uiLifecycleHelper;
	private LoginButton loginButton;

	private JSONObject FBAccount;
    
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
            /* handle the result */
        	GraphObject rs = response.getGraphObject();
        	if(null != rs){
        		/* First load */
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
			    		Gender gender = new Gender();
			    		gender.setName(FBAccount.getString("gender"));
			    		fbUser.setGender(gender);
		    		} catch (JSONException e){
		    			e.printStackTrace();
		    		}
		    		mClickListener.OnButtonSelected(StartPage.BUTTON_TYPE_FBSIGNIN, fbUser);
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
		//already logged in
	    if (state.isOpened()) {
	    	//first log in
	    	if(null == FBAccount){
	    		loadUserFBData(session);
	    	}
	    	loginButton.setBackgroundResource(R.drawable.continue_facebook);
	    } 
	    //already logged out
	    /*
	    else if (state.isClosed()) {
	    	loginButton.setBackgroundResource(R.drawable.continue_facebook);
	    }
	    */
	}
	
	/**
	 * need to be called OUTSIDE the ASYNC session state callback
	 */
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
	
	private void loadUserFBData(Session session){
    	new Request(session, getResources().getString(R.string.facebook_root_path), null, HttpMethod.GET, myRequestCallback).executeAsync();
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View startPageView = inflater.inflate(R.layout.authentication_startpage, container, false);
		loginButton = (LoginButton) startPageView.findViewById(R.id.loginFbBtn);
        loginEmail = (ImageButton)startPageView.findViewById(R.id.loginEmailBtn);
        signEmail = (ImageButton)startPageView.findViewById(R.id.signEmailBtn);
        
        requestReadPermissions();
        loginButton.setBackgroundResource(R.drawable.continue_facebook);
	    loginButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);	    
	    loginButton.setFragment(this);
        
        signEmail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mClickListener.OnFragmentSelected(StartPage.FRAG_SIGNUP);
			}
		});
        
        loginEmail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mClickListener.OnFragmentSelected(StartPage.FRAG_LOGIN);
			}
		});
        
        return startPageView;
	}
	
	public interface OnLoginClickListener {
		public int OnFragmentSelected(int index);
		public void OnButtonSelected(int type, User user);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mClickListener = (OnLoginClickListener) activity;
		} catch (ClassCastException e) {
			// should never happen in normal cases
			throw new ClassCastException(activity.toString() + "must implement OnLoginClickListener. ");
		}
	}
}
