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
public class SignUpPageFragment extends Fragment {
	
	private EditText signUpEmailText, signUpPasswordText;
	private String signUpEmailStr, signUpPasswordStr;
	private ImageButton signUp;
	private OnSignUpSelectedListener mSelectedListener;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View signUpPageView = inflater.inflate(R.layout.fragment_start_signup, container, false);
		signUpEmailText = (EditText)signUpPageView.findViewById(R.id.email_signup_input);
		signUpPasswordText = (EditText)signUpPageView.findViewById(R.id.signup_password_input);
		signUp = (ImageButton)signUpPageView.findViewById(R.id.button_signup);

		signUp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				signUpEmailStr = signUpEmailText.getText().toString();
				signUpPasswordStr = signUpPasswordText.getText().toString();
				
				if (signUpEmailStr == null || signUpEmailStr.equals("")) {
					Toast.makeText(getActivity(), "Oops! You need an email address to join us. :)", Toast.LENGTH_SHORT).show();
				} else if (signUpPasswordStr == null || signUpPasswordStr.equals("")) {
					Toast.makeText(getActivity(), "You need a password to protect your account. ", Toast.LENGTH_SHORT).show();
				} else {
					// TODO REGULAR-EXPRESSION CHECK
					User user = new User();
					user.setEmail(signUpEmailStr);
					user.setPassword(signUpPasswordStr);
					mSelectedListener.OnButtonSelected(StartPage.BUTTON_TYPE_SIGNUP, user);
				}
			}
		});
		
		return signUpPageView;
	}
	
	public interface OnSignUpSelectedListener {
		public void OnButtonSelected(int type, User user);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mSelectedListener = (OnSignUpSelectedListener) activity;
		} catch (ClassCastException e) {
			// should never happen in normal cases
			throw new ClassCastException(activity.toString() + "must implement OnButtonSelectedListener in SignUpPageFragment. ");
		}
	}
}
