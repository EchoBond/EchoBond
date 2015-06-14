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
public class SearchThoughtsResultFragment extends ListFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View searchThoughtsResultView = inflater.inflate(R.layout.fragment_search_result_thoughts, container, false);
		
		return searchThoughtsResultView;
	}
	
}
