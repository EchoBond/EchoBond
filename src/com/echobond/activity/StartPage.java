package com.echobond.activity;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.connector.FBSignInAsyncTask;
import com.echobond.connector.ResetPassAsyncTask;
import com.echobond.connector.SignInAsyncTask;
import com.echobond.connector.SignUpAsyncTask;
import com.echobond.entity.User;
import com.echobond.fragment.LoginPageFragment.OnLoginSelectedListener;
import com.echobond.fragment.SignUpPageFragment.OnSignUpSelectedListener;
import com.echobond.fragment.LoginPageFragment;
import com.echobond.fragment.SignUpPageFragment;
import com.echobond.fragment.StartPageFragment;
import com.echobond.fragment.StartPageFragment.OnLoginClickListener;
import com.echobond.R;
import com.facebook.Session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.Toast;

/**
 * 
 * @author aohuijun
 * @editor liujunjie
 *
 */
public class StartPage extends FragmentActivity implements OnLoginClickListener, OnSignUpSelectedListener, OnLoginSelectedListener {
	
	private StartPageFragment startPageFragment;
	private SignUpPageFragment signUpPageFragment;
	private LoginPageFragment loginPageFragment;
	private String preUrl;
	private int fgIndex = 0;
	
	public static final int BUTTON_TYPE_SIGNUP = 0;
	public static final int BUTTON_TYPE_SIGNIN = 1;
	public static final int BUTTON_TYPE_FGTPW = 2;
	public static final int BUTTON_TYPE_FBSIGNIN = 3;
	
	public static final int FRAG_START = 0;
	public static final int FRAG_SIGNUP = 1;
	public static final int FRAG_LOGIN = 2;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);
        //avoid dead loop of Facebook logout<->login
    	Session session = Session.getActiveSession();
    	if(null != session)
    		session.closeAndClearTokenInformation();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(null == startPageFragment || null == signUpPageFragment || null == loginPageFragment){
	    	startPageFragment = new StartPageFragment();
	    	signUpPageFragment = new SignUpPageFragment();
	    	loginPageFragment = new LoginPageFragment();
	    	transaction.add(R.id.startContent, startPageFragment);
	    	transaction.add(R.id.startContent, signUpPageFragment);
	    	transaction.add(R.id.startContent, loginPageFragment);
	    	transaction.hide(signUpPageFragment);
	    	transaction.hide(loginPageFragment);
	    	transaction.show(startPageFragment).commit();
        }
        preUrl = this.getResources().getString(R.string.url_protocol) + "://" + 
    			getResources().getString(R.string.url_domain) + ":" + 
    			getResources().getString(R.string.url_port) + "/" + 
    			getResources().getString(R.string.url_sub) + "/";
    }
    
	@Override
	public int OnFragmentSelected(int index) {
		this.fgIndex = index;
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		switch (fgIndex) {
		case FRAG_START:
			transaction.hide(signUpPageFragment).hide(loginPageFragment).show(startPageFragment).commit();
			break;
		case FRAG_SIGNUP:
			transaction.hide(startPageFragment).hide(loginPageFragment).show(signUpPageFragment).commit();
			break;
		case FRAG_LOGIN:
			transaction.hide(startPageFragment).hide(signUpPageFragment).show(loginPageFragment).commit();
			break;
		default:
			break;
		}
		return fgIndex;
	}
    
	@Override
	public void OnButtonSelected(int type, User user) {
		String url = "";
        switch (type) {
		case BUTTON_TYPE_SIGNUP:
			url = preUrl + getResources().getString(R.string.url_signup);
			new SignUpAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, user, url, this);
			break;
		case BUTTON_TYPE_SIGNIN:
			url = preUrl + getResources().getString(R.string.url_signin);
			new SignInAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, user, url, this);
			break;
		case BUTTON_TYPE_FGTPW:
			url = preUrl + getResources().getString(R.string.url_reset_pass);
			new ResetPassAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, user.getEmail(), url, this);
			break;
		case BUTTON_TYPE_FBSIGNIN:
			url = preUrl + getResources().getString(R.string.url_fb_signin);
			new FBSignInAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, user, url, this);
			break;
		default:
			break;
		}

	}
    
    public void onSignInResult(JSONObject result){
    	try {
    		if(null == result){
				Toast.makeText(this, getResources().getString(R.string.network_issue), Toast.LENGTH_LONG).show();
    		}
    		else if(result.getInt("exists") == 0){
				Toast.makeText(this, getResources().getString(R.string.signin_not_exists), Toast.LENGTH_LONG).show();
			} else if(result.getInt("passMatch") == 0){
				Toast.makeText(this, getResources().getString(R.string.signin_wrong_pass), Toast.LENGTH_LONG).show();	
			} else {
				SharedPreferences pref = getSharedPreferences("isFirstUse", Context.MODE_PRIVATE);
				boolean isFirstUse = pref.getBoolean("isFirstUse", true);
				/* 
				 * If it is detected that the mobile loads the app for the 1st time, 
				 * the app loads the introduction activity. Otherwise it jumps straight 
				 * to the main page. 
				*/ 
				Intent intent = new Intent();
				if (isFirstUse) {
					intent.setClass(this, IntroPage.class);
				} else {
					intent.setClass(this, MainPage.class);
				}				
				Editor editor = pref.edit();
				editor.putBoolean("isFirstUse", false);
				editor.commit();
				startActivity(intent);
				finish();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
    }
    
    public void onSignUpResult(JSONObject result){
    	try {
    		if(null == result){
				Toast.makeText(this, getResources().getString(R.string.network_issue), Toast.LENGTH_LONG).show();
    		}
    		else{
				if(result.getInt("exists") == 0){
					Toast.makeText(this, getResources().getString(R.string.signup_exst_unvrfd_new_mail), Toast.LENGTH_LONG).show();
					Intent intent = new Intent();
					intent.setClass(this, IntroPage.class);
					startActivity(intent);
					finish();
				}
				if(result.getInt("exists") == 1 && result.getInt("verified") == 1){
					Toast.makeText(this, getResources().getString(R.string.signup_exst_vrfd), Toast.LENGTH_LONG).show();
				} 
				if(result.getInt("exists") == 1 && result.getInt("verified") == 0 && result.getInt("email") == 0){
					Toast.makeText(this, getResources().getString(R.string.signup_exst_unvrfd_new_mail), Toast.LENGTH_LONG).show();				
				}
				if(result.getInt("exists") == 1 && result.getInt("verified") == 0 && result.getInt("email") == 1 && result.getInt("expired") == 1){
					Toast.makeText(this, getResources().getString(R.string.signup_exst_unvrfd_new_mail), Toast.LENGTH_LONG).show();	
				}
				if(result.getInt("exists") == 1 && result.getInt("verified") == 0 && result.getInt("email") == 1 && result.getInt("expired") == 0){
					Toast.makeText(this, getResources().getString(R.string.signup_exst_unvrfd_mailed), Toast.LENGTH_LONG).show();	
				}
    		}
		} catch (JSONException e) {
			e.printStackTrace();
		}
    }
    
    public void onFBSignInResult(JSONObject result){
    	try{
			if(null == result){
				Toast.makeText(this, getResources().getString(R.string.network_issue), Toast.LENGTH_LONG).show();
			}
			else {
				if(result.getInt("new") == 0){
					
				}
				else {
					
				}
				//make sure it is a valid session instead of a cache
				Session session = Session.getActiveSession();
				if(null != session && session.isOpened()){
					Intent intent = new Intent();
					intent.setClass(StartPage.this, MainPage.class);
					startActivity(intent);
					finish();
				}
				else{
					
				}
			}
    	} catch (JSONException e){
    		e.printStackTrace();
    	}
    	
    }

    public void onResetPassResult(JSONObject result){
		try{
			if(null == result){
				Toast.makeText(this, getResources().getString(R.string.network_issue), Toast.LENGTH_LONG).show();
			}			
			else if(result.getInt("accExists") == 0){
				Toast.makeText(this, getResources().getString(R.string.resetpass_not_reg), Toast.LENGTH_LONG).show();
			} else if(result.getInt("hadReset") == 1 && result.getInt("reset") == 0 && result.getInt("expire") == 0){
				Toast.makeText(this, getResources().getString(R.string.resetpass_resetting), Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(this, getResources().getString(R.string.resetpass_now), Toast.LENGTH_LONG).show();
			}
		} catch (JSONException e){
			e.printStackTrace();
		}    	
    }

    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	//pressed back key
    	if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
    		//in the start fragment
    		if(fgIndex == 0){
    			// Click twice to leave the app. 
	    		if ((System.currentTimeMillis() - exitTime) > 2000) {
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.hint_quit), Toast.LENGTH_SHORT).show();
					exitTime = System.currentTimeMillis();
				} else {
					finish();
					System.exit(0);
				}
				return true;
			}
        	//in other fragments
        	else { 
        		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            	transaction.hide(signUpPageFragment);
            	transaction.hide(loginPageFragment);
            	transaction.show(startPageFragment).commit();
    			fgIndex = 0;
        	}
    	}
		//return super.onKeyDown(keyCode, event);
    	return true;
	}
    
}
