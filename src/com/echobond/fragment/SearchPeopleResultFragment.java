package com.echobond.fragment;

import com.echobond.R;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
/**
 * 
 * @author aohuijun
 *
 */
public class SearchPeopleResultFragment extends ListFragment {

	private TextView textView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View searchPeopleResultView = inflater.inflate(R.layout.fragment_search_result_people, container, false);
		textView = (TextView)searchPeopleResultView.findViewById(R.id.search_result_people);
		
		return searchPeopleResultView;
	}
	
}
