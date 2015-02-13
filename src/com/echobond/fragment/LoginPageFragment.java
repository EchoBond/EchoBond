package com.echobond.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.R;
import com.echobond.activity.IntroPage;
import com.echobond.activity.MainPage;
import com.echobond.activity.StartPage;
import com.echobond.connector.ResetPassAsyncTask;

import android.R.interpolator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * 
 * @author aohuijun
 *
 */
public class LoginPageFragment extends Fragment {
	
	private EditText loginEmailText, loginPasswordText;
	private String loginEmailStr, loginPasswordStr, url;
	private ImageButton login, forgetPassword;
	private OnLoginSelectedListener mSelectedListener;
	
	private boolean isFirstUse;
	private Intent intent = new Intent();
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View loginPageView = inflater.inflate(R.layout.authentication_login, container, false);
		loginEmailText = (EditText)loginPageView.findViewById(R.id.loginEmailInput);
		loginPasswordText = (EditText)loginPageView.findViewById(R.id.loginPasswordInput);
		login = (ImageButton)loginPageView.findViewById(R.id.loginBtn);
		forgetPassword = (ImageButton)loginPageView.findViewById(R.id.forgetPWBtn);
		
		login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				loginEmailStr = loginEmailText.getText().toString();
				loginPasswordStr = loginPasswordText.getText().toString();
				SharedPreferences pref = getActivity().getSharedPreferences("isFirstUse", Context.MODE_PRIVATE);
				isFirstUse = pref.getBoolean("isFirstUse", true);
				/* 
				 * If it is detected that the mobile loads the app for the 1st time, 
				 * the app loads the introduction activity. Otherwise it jumps straight 
				 * to the main page. 
				*/ 
				if (isFirstUse) {
					intent.setClass(getActivity(), IntroPage.class);
				} else {
					intent.setClass(getActivity(), MainPage.class);
				}
				/*
				 * Whether the edittext of email and password input is empty. 
				 */
				if (loginEmailStr == null || loginEmailStr.equals("")) {
					Toast.makeText(getActivity(), "Oops! Seems that you forgot to enter your email address account. ;)", Toast.LENGTH_SHORT).show();
				} else if (loginPasswordStr == null || loginPasswordStr.equals("")) {
					Toast.makeText(getActivity(), "You need your password to come back your wonderland. ", Toast.LENGTH_SHORT).show();
				} else {
					mSelectedListener.OnButtonSelected(StartPage.BUTTON_TYPE_SIGNIN);
					startActivity(intent);
					getActivity().finish();
					Editor editor = pref.edit();
					editor.putBoolean("isFirstUse", false);
					editor.commit();
				}
				
			}
		});
		
		forgetPassword.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loginEmailStr = loginEmailText.getText().toString();
				if (loginEmailStr == null || loginEmailStr.equals("")) {
					Toast.makeText(getActivity(), "Oops! Seems that you forgot to enter your email address account. ;)", Toast.LENGTH_SHORT).show();
				} else {
					url = getResources().getString(R.string.url_protocol) + "://" + 
							getResources().getString(R.string.url_domain) + ":" + 
							getResources().getString(R.string.url_port) + "/" + 
							getResources().getString(R.string.url_sub) + "/" + 
							getResources().getString(R.string.url_reset_pass);
					new ResetPassAsyncTask().execute(loginEmailStr, url, LoginPageFragment.this);
				}
			}
		});
		
		return loginPageView;
		
	}
	
	public interface OnLoginSelectedListener {
		public String OnButtonSelected(int type);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mSelectedListener = (OnLoginSelectedListener) activity;
		} catch (ClassCastException e) {
			// should never happen in normal cases
			throw new ClassCastException(activity.toString() + "must implement OnButtonSelectedListener in LoginPageFragment. ");
		}
	}
}
