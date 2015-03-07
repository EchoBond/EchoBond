package com.echobond.fragment;

import com.echobond.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 
 * @author aohuijun
 *
 */
public class LikeMindedFragment extends Fragment{
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View likeMindedView = inflater.inflate(R.layout.fragment_main_like_minded, container, false);
		return likeMindedView;
		
	}
}
