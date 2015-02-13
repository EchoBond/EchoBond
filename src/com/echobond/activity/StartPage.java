package com.echobond.activity;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.fragment.LoginPageFragment.OnLoginSelectedListener;
import com.echobond.fragment.SignUpPageFragment.OnSignUpSelectedListener;
import com.echobond.fragment.StartPageFragment;
import com.echobond.fragment.StartPageFragment.OnLoginClickListener;
import com.echobond.R;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.Toast;

/**
 * 
 * @author aohuijun
 *
 */
public class StartPage extends FragmentActivity implements OnLoginClickListener, OnSignUpSelectedListener, OnLoginSelectedListener {
	
	private StartPageFragment startPageFragment;
	private FragmentTransaction transaction;
	private int fgIndex = 0;
	private String preUrl, requestUrl;
	public static final int BUTTON_TYPE_SIGNUP = 0;
	public static final int BUTTON_TYPE_SIGNIN = 1;
	public static final int BUTTON_TYPE_FGTPW = 2;
	public static final int BUTTON_TYPE_FBSIGNIN = 3;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);
        
        startPageFragment = new StartPageFragment();
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.startContent, startPageFragment).commit();
        preUrl = this.getResources().getString(R.string.url_protocol) + "://" + 
    			getResources().getString(R.string.url_domain) + ":" + 
    			getResources().getString(R.string.url_port) + "/" + 
    			getResources().getString(R.string.url_sub) + "/";
    }
    
	@Override
	public int OnFragmentSelected(int index) {
		this.fgIndex = index;
		return fgIndex;
	}
    
	@Override
	public String OnButtonSelected(int type) {
        switch (type) {
		case BUTTON_TYPE_SIGNUP:
			requestUrl = preUrl + getResources().getString(R.string.url_signup);
			break;
		case BUTTON_TYPE_SIGNIN:
			requestUrl = preUrl + getResources().getString(R.string.url_signin);
			break;
		case BUTTON_TYPE_FGTPW:
			requestUrl = preUrl + getResources().getString(R.string.url_reset_pass);
			break;
		case BUTTON_TYPE_FBSIGNIN:
			requestUrl = preUrl + getResources().getString(R.string.url_fb_signin);
			break;
		default:
			break;
		}
        Toast.makeText(this, requestUrl+"", Toast.LENGTH_LONG).show();
		return requestUrl;
	}
	
	public void setAsyncTask() {
		
	}
	
    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
		// Click twice to leave the app. 
    	if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
    		if (fgIndex != 0) {
				transaction = getSupportFragmentManager().beginTransaction().replace(R.id.startContent, startPageFragment);
				transaction.addToBackStack(null);
				transaction.commit();
				fgIndex = 0;
			}else if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "Click once more to quit.", Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
    
    public void onSignInResult(JSONObject result){
    	
    }
    
    public void onSignUpResult(JSONObject result){
		try{
			if(result.getInt("accExists") == 0){
				new AlertDialog.Builder(this).setMessage(getResources().getString(R.string.resetpass_not_reg)).show();
			} else if(result.getInt("hadReset") == 1 && result.getInt("reset") == 0 && result.getInt("expire") == 0){
				new AlertDialog.Builder(this).setMessage(getResources().getString(R.string.resetpass_resetting)).show();
			} else {
				new AlertDialog.Builder(this).setMessage(getResources().getString(R.string.resetpass_now)).show();
			}
		} catch (JSONException e){
			e.printStackTrace();
		}
    }
    
    public void onFBSignInResult(JSONObject result){
    	if(null != result){
    		try{
    			int isNew = result.getInt("new");
    		} catch (JSONException e){
    			e.printStackTrace();
    		}
    	}
    }

    public void onResetPassResult(JSONObject result){
    	
    }
}
