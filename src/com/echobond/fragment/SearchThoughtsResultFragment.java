package com.echobond.fragment;

import com.echobond.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
/**
 * 
 * @author aohuijun
 *
 */
public class SearchThoughtsResultFragment extends Fragment {
	
	private ListView searchThoughtsResultList;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View searchThoughtsResultView = inflater.inflate(R.layout.fragment_search_result_thoughts, container, false);
		searchThoughtsResultList = (ListView)searchThoughtsResultView.findViewById(R.id.search_result_list_thoughts);
		searchThoughtsResultList.setOverScrollMode(View.OVER_SCROLL_NEVER);
		
		return searchThoughtsResultView;
	}
	
}
