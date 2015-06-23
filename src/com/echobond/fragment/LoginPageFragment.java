package com.echobond.fragment;

import com.echobond.R;
import com.echobond.activity.StartPage;
import com.echobond.entity.User;
import com.echobond.intf.StartPageFragmentsSwitchAsyncTaskCallback;
import com.echobond.util.CommUtil;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
/**
 * 
 * @author aohuijun
 *
 */
public class LoginPageFragment extends Fragment {
	
	private EditText loginEmailText, loginPasswordText;
	private String loginEmailStr, loginPasswordStr;
	private ImageView login, forgetPassword;
	private StartPageFragmentsSwitchAsyncTaskCallback mSelectedListener;
		
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View loginPageView = inflater.inflate(R.layout.fragment_start_login, container, false);
		loginEmailText = (EditText)loginPageView.findViewById(R.id.email_signin_input);
		loginPasswordText = (EditText)loginPageView.findViewById(R.id.signin_password_input);
		login = (ImageView)loginPageView.findViewById(R.id.button_signin);
		forgetPassword = (ImageView)loginPageView.findViewById(R.id.button_forget_pw);
		
		login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((StartPage)getActivity()).loading(true);
				loginEmailStr = loginEmailText.getText().toString();
				loginPasswordStr = loginPasswordText.getText().toString();

				/*
				 * Whether the edittext of email and password input is empty. 
				 */
				if (loginEmailStr == null || loginEmailStr.equals("")) {
					Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.signin_empty_email), Toast.LENGTH_SHORT).show();
					((StartPage)getActivity()).loading(false);
				} else if (loginPasswordStr == null || loginPasswordStr.equals("")) {
					Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.signin_empty_pw), Toast.LENGTH_SHORT).show();
					((StartPage)getActivity()).loading(false);
				} else if(!CommUtil.isValidEmail(loginEmailStr)){
					Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.signin_invalid_email), Toast.LENGTH_SHORT).show();
					((StartPage)getActivity()).loading(false);
				} else if(!CommUtil.isValidPassword(loginPasswordStr)){
					Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.signin_invalid_password), Toast.LENGTH_SHORT).show();
					((StartPage)getActivity()).loading(false);
				} else {
					User user = new User();
					user.setEmail(loginEmailStr);
					user.setPassword(loginPasswordStr);
					mSelectedListener.onButtonSelected(StartPage.BUTTON_TYPE_SIGNIN, user);
				}
				
			}
		});
		
		forgetPassword.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((StartPage)getActivity()).loading(true);
				loginEmailStr = loginEmailText.getText().toString();
				if (loginEmailStr == null || loginEmailStr.equals("")) {
					Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.signin_empty_email), Toast.LENGTH_SHORT).show();
				} else {
					User user = new User();
					user.setEmail(loginEmailStr);
					mSelectedListener.onButtonSelected(StartPage.BUTTON_TYPE_FGTPW, user);
				}
			}
		});
		
		return loginPageView;
		
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mSelectedListener = (StartPageFragmentsSwitchAsyncTaskCallback) activity;
		} catch (ClassCastException e) {
			// should never happen in normal cases
			throw new ClassCastException(activity.toString() + "must implement OnButtonSelectedListener in LoginPageFragment. ");
		}
	}
}
