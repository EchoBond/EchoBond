package com.echobond.fragment;

import com.echobond.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NewContentsFragment extends Fragment {
	
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View addContentsView = inflater.inflate(R.layout.fragment_new_post_contents, container, false);		
		return addContentsView;
	}
	
}
