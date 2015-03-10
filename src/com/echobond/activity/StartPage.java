package com.echobond.activity;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.connector.FBSignInAsyncTask;
import com.echobond.connector.ResetPassAsyncTask;
import com.echobond.connector.SignInAsyncTask;
import com.echobond.connector.SignUpAsyncTask;
import com.echobond.entity.User;
import com.echobond.fragment.LoginPageFragment;
import com.echobond.fragment.SignUpPageFragment;
import com.echobond.fragment.StartPageFragment;
import com.echobond.intf.StartPageFragmentsSwitchAsyncTaskCallback;
import com.echobond.util.HTTPUtil;
import com.echobond.util.JSONUtil;
import com.echobond.util.SPUtil;
import com.echobond.util.SQLiteDBUtil;
import com.echobond.R;
import com.facebook.Session;

import android.content.Intent;
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
public class StartPage extends FragmentActivity implements StartPageFragmentsSwitchAsyncTaskCallback {
	
	private StartPageFragment startPageFragment;
	private SignUpPageFragment signUpPageFragment;
	private LoginPageFragment loginPageFragment;
	private String preUrl;
	private int fgIndex = 0;
	private SQLiteDBUtil dbUtil;
    private long exitTime = 0;

	
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
        //avoid dead loop of Facebook logout<->login, clear invalid Facebook session
        Session session = Session.getActiveSession();
        if (session != null) {
            if (!session.isClosed()) {
                session.closeAndClearTokenInformation();
            }
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(null == startPageFragment || null == signUpPageFragment || null == loginPageFragment){
	    	startPageFragment = new StartPageFragment();
	    	signUpPageFragment = new SignUpPageFragment();
	    	loginPageFragment = new LoginPageFragment();
	    	transaction.add(R.id.start_page_content, startPageFragment);
	    	transaction.add(R.id.start_page_content, signUpPageFragment);
	    	transaction.add(R.id.start_page_content, loginPageFragment);
	    	transaction.hide(signUpPageFragment);
	    	transaction.hide(loginPageFragment);
	    	transaction.show(startPageFragment).commit();
        }
        preUrl = HTTPUtil.getInstance().composePreURL(this);
        initDB();
        checkReturnUser();
    }
    
	@Override
	public int onFragmentSelected(int index) {
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
	public void onButtonSelected(int type, User user) {
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
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_issue), Toast.LENGTH_LONG).show();
    		}
    		else if(result.getInt("exists") == 0){
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.signin_not_exists), Toast.LENGTH_LONG).show();
			} else if(result.getInt("passMatch") == 0){
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.signin_wrong_pass), Toast.LENGTH_LONG).show();	
			} else {
				login((User) JSONUtil.fromJSONToObject(result.getJSONObject("user"),User.class));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
    }
    
    public void onSignUpResult(JSONObject result){
    	try {
    		if(null == result){
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_issue), Toast.LENGTH_LONG).show();
    		}
    		else{
				if(result.getInt("exists") == 0){
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.signup_exst_unvrfd_new_mail), Toast.LENGTH_LONG).show();
					SPUtil.put(this, "isFirstUse", true);
					login((User) JSONUtil.fromJSONToObject(result.getJSONObject("user"),User.class));
				}
				if(result.getInt("exists") == 1 && result.getInt("verified") == 1){
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.signup_exst_vrfd), Toast.LENGTH_LONG).show();
				}
				if(result.getInt("exists") == 1 && result.getInt("verified") == 0 && result.getInt("email") == 0){
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.signup_exst_unvrfd_new_mail), Toast.LENGTH_LONG).show();				
				}
				if(result.getInt("exists") == 1 && result.getInt("verified") == 0 && result.getInt("email") == 1 && result.getInt("expired") == 1){
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.signup_exst_unvrfd_new_mail), Toast.LENGTH_LONG).show();	
				}
				if(result.getInt("exists") == 1 && result.getInt("verified") == 0 && result.getInt("email") == 1 && result.getInt("expired") == 0){
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.signup_exst_unvrfd_mailed), Toast.LENGTH_LONG).show();	
				}
    		}
		} catch (JSONException e) {
			e.printStackTrace();
		}
    }
    
    public void onFBSignInResult(JSONObject result){
    	try{
			if(null == result){
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_issue), Toast.LENGTH_LONG).show();
			}
			else {
				//make sure it is a valid session instead of a cache
				Session session = Session.getActiveSession();
				if(null != session && session.isOpened()){
					login((User) JSONUtil.fromJSONToObject(result.getJSONObject("user"),User.class));
				}
			}
    	} catch (JSONException e){
    		e.printStackTrace();
    	}
    }

    public void onResetPassResult(JSONObject result){
		try{
			if(null == result){
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_issue), Toast.LENGTH_LONG).show();
			}
			else if(result.getInt("accExists") == 0){
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.resetpass_not_reg), Toast.LENGTH_LONG).show();
			} else if(result.getInt("hadReset") == 1 && result.getInt("reset") == 0 && result.getInt("expire") == 0){
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.resetpass_resetting), Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.resetpass_now), Toast.LENGTH_LONG).show();
			}
		} catch (JSONException e){
			e.printStackTrace();
		}
    }

    private void login(User user){
    	Intent intent = checkFirstLogin();
		startActivity(intent);
		finish();
    }
    
    private Intent checkFirstLogin(){
		boolean isFirstUse = (Boolean) SPUtil.get(this, "isFirstUse", true, Boolean.class);
		Class<?> target = null;
		if (isFirstUse) {
			SPUtil.put(this, "isFirstUse", false);
			target = IntroPage.class;
		} else {
			target = MainPage.class;
		}
		Intent intent = new Intent();
		intent.setClass(StartPage.this, target);
		return intent;
    }
    
    private void initDB(){
        dbUtil = SQLiteDBUtil.getInstance(this);
        dbUtil.getWritableDatabase();
    }
    
    private void checkReturnUser(){
    	Integer type = (Integer) SPUtil.get(this, "loginUser_type", User.TYPE_INVALID, Integer.class);
        String id = (String) SPUtil.get(this, "loginUser_id", null, String.class);
        User user = new User();
        if(!type.equals(User.TYPE_INVALID) && null != id){
        	if(type.equals(User.TYPE_EMAIL)){
        		String pass, email;
        		if((pass=(String) SPUtil.get(this, "loginUser_pass", null, String.class)) != null &&
        				(email=(String) SPUtil.get(this, "loginUser_pass", null, String.class)) != null){
        			user.setEmail(email);
        			user.setPassword(pass);
//        			new SignInAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, user, 
//        					preUrl + getResources().getString(R.string.url_signin), this);
        			login(user);
        		}
        	} else {
//        		String FBId = SPUtil.get(this, key, defValue, cls)
        	}
        }
    }
    
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
