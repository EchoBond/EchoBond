package com.echobond.fragment;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * 
 * @author aohuijun
 *
 */
public class MainFragmentPagerAdapter extends FragmentPagerAdapter {

	private ArrayList<Fragment> mainFragmentsList;	

	public MainFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> mainFragmentsList) {
		super(fm);
		this.mainFragmentsList = mainFragmentsList;
	}
	
	@Override
	public Fragment getItem(int index) {
		return mainFragmentsList.get(index);
	}

	@Override
	public int getCount() {
		return mainFragmentsList.size();
	}

}
