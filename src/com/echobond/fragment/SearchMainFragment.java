package com.echobond.fragment;

import com.echobond.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/**
 * 
 * @author aohuijun
 *
 */
public class SearchMainFragment extends Fragment {

	private FragmentTabHost tabHost;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View searchMainView = inflater.inflate(R.layout.fragment_search_main, container, false);
		tabHost = (FragmentTabHost)searchMainView.findViewById(android.R.id.tabhost);
		tabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);
		tabHost.addTab(tabHost.newTabSpec("thoughts").setIndicator("Thoughts"), SearchThoughtsFragment.class, null);
		tabHost.addTab(tabHost.newTabSpec("people").setIndicator("People"), SearchPeopleFragment.class, null);
		tabHost.setCurrentTab(0);
		
		return searchMainView;
	}

	public FragmentTabHost getTabHost() {
		return tabHost;
	}
	
}
