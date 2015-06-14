package com.echobond.fragment;

import com.echobond.R;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/**
 * 
 * @author aohuijun
 *
 */
public class SearchPeopleResultFragment extends ListFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View searchPeopleResultView = inflater.inflate(R.layout.fragment_search_result_people, container, false);
		
		return searchPeopleResultView;
	}
	
}
