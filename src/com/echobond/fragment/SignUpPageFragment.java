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
public class SignUpPageFragment extends Fragment {
	
	private EditText signUpEmailText, signUpPasswordText;
	private String signUpEmailStr, signUpPasswordStr;
	private ImageView signUp;
	private StartPageFragmentsSwitchAsyncTaskCallback mSelectedListener;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View signUpPageView = inflater.inflate(R.layout.fragment_start_signup, container, false);
		signUpEmailText = (EditText)signUpPageView.findViewById(R.id.email_signup_input);
		signUpPasswordText = (EditText)signUpPageView.findViewById(R.id.signup_password_input);
		signUp = (ImageView)signUpPageView.findViewById(R.id.button_signup);

		signUp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((StartPage)getActivity()).loading(true);
				signUpEmailStr = signUpEmailText.getText().toString();
				signUpPasswordStr = signUpPasswordText.getText().toString();
				
				if (signUpEmailStr == null || signUpEmailStr.equals("")) {
					Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.signup_empty_email), Toast.LENGTH_SHORT).show();
					((StartPage)getActivity()).loading(false);
				} else if (signUpPasswordStr == null || signUpPasswordStr.equals("")) {
					Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.signup_empty_pw), Toast.LENGTH_SHORT).show();
					((StartPage)getActivity()).loading(false);
				} else if(!CommUtil.isValidEmail(signUpEmailStr)){
					Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.signup_invalid_email), Toast.LENGTH_SHORT).show();
					((StartPage)getActivity()).loading(false);
				} else if(!CommUtil.isValidPassword(signUpPasswordStr)){
					Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.signup_invalid_password), Toast.LENGTH_SHORT).show();
					((StartPage)getActivity()).loading(false);
				} else {
					User user = new User();
					user.setEmail(signUpEmailStr);
					user.setPassword(signUpPasswordStr);
					mSelectedListener.onButtonSelected(StartPage.BUTTON_TYPE_SIGNUP, user);
				}
			}
		});
		
		return signUpPageView;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mSelectedListener = (StartPageFragmentsSwitchAsyncTaskCallback) activity;
		} catch (ClassCastException e) {
			// should never happen in normal cases
			throw new ClassCastException(activity.toString() + "must implement OnButtonSelectedListener in SignUpPageFragment. ");
		}
	}
}
