package com.echobond.fragment;

import com.echobond.R;
import com.echobond.activity.MainPage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/**
 * 
 * @author aohuijun
 *
 */
public class ServiceFragment extends Fragment {

	private View finalView;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		switch (getArguments().getInt("page")) {
		case MainPage.SETTING_FOLLOWING:
			finalView = inflater.inflate(R.layout.fragment_setting_following, container, false);
			break;
		case MainPage.SETTING_TERMS_OF_SERVICES:
			finalView = inflater.inflate(R.layout.fragment_setting_service, container, false);
			break;
		default:
			break;
		}
		return finalView;
	}
	
}
