package com.echobond.activity;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
/**
 * 
 * @author aohuijun
 *
 */
public class IntroViewPagerAdapter extends PagerAdapter {
	
	private ArrayList<View> views;
	
	public IntroViewPagerAdapter (ArrayList<View> views) {this.views = views;}

	@Override
	public int getCount() {

		if (views != null) {
			return views.size();
		}
		return 0;
	}
	
	@Override
	public Object instantiateItem(View view, int position) {
		((ViewPager) view).addView(views.get(position), 0);
		return views.get(position);
	}

	@Override
	public boolean isViewFromObject(View view, Object arg1) {

		return (view == arg1);
	}
	
	@Override
	public void destroyItem(View view, int position, Object arg2) {
		((ViewPager) view).removeView(views.get(position));
	}
	
}
