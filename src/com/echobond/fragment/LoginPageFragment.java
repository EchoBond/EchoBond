package com.echobond.fragment;

import com.echobond.R;
import com.echobond.activity.StartPage;
import com.echobond.entity.User;

import android.app.Activity;
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
	private String loginEmailStr, loginPasswordStr;
	private ImageButton login, forgetPassword;
	private OnLoginSelectedListener mSelectedListener;
		
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

				/*
				 * Whether the edittext of email and password input is empty. 
				 */
				if (loginEmailStr == null || loginEmailStr.equals("")) {
					Toast.makeText(getActivity(), "Oops! Seems that you forgot to enter your email address account. ;)", Toast.LENGTH_SHORT).show();
				} else if (loginPasswordStr == null || loginPasswordStr.equals("")) {
					Toast.makeText(getActivity(), "You need your password to come back your wonderland. ", Toast.LENGTH_SHORT).show();
				} else {
					// TODO REGULAR EXPRESSION CHECK
					User user = new User();
					user.setEmail(loginEmailStr);
					user.setPassword(loginPasswordStr);
					mSelectedListener.OnButtonSelected(StartPage.BUTTON_TYPE_SIGNIN, user);
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
					// TODO REGULAR EXPRESSION CHECK
					User user = new User();
					user.setEmail(loginEmailStr);
					mSelectedListener.OnButtonSelected(StartPage.BUTTON_TYPE_FGTPW, user);
				}
			}
		});
		
		return loginPageView;
		
	}
	
	public interface OnLoginSelectedListener {
		public void OnButtonSelected(int type, User user);
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
