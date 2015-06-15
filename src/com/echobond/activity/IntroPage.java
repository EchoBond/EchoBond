package com.echobond.activity;

import java.util.ArrayList;

import com.echobond.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
/**
 * 
 * @author aohuijun
 *
 */
public class IntroPage extends Activity implements OnClickListener, OnPageChangeListener{
	
	private ViewPager viewPager;
	private IntroViewPagerAdapter vpAdapter;
	private ArrayList<View> views;
	private ImageView[] characterView, tagView, points;
	private AlphaAnimation[] alphaAnimation;
	private View view1, view2, view3;
	private int currentIndex;
	private boolean isSlided = false;
	Intent intent = new Intent();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro_page);
		
		initView();
		initData();
		setAnim();
		
		ImageView enterButton = (ImageView)view3.findViewById(R.id.button_enter);
		enterButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				intent.setClass(IntroPage.this, FollowingPage.class);
				startActivity(intent);
				finish();
			}
		});
		
	}

	@SuppressLint("InflateParams") 
	private void initView() {

		views = new ArrayList<View>();
		viewPager = (ViewPager)findViewById(R.id.introViewPager);
		vpAdapter = new IntroViewPagerAdapter(views);
		
		LayoutInflater mLayoutInflater = LayoutInflater.from(this);
		view1 = mLayoutInflater.inflate(R.layout.intro_view01, null);
		view2 = mLayoutInflater.inflate(R.layout.intro_view02, null);
		view3 = mLayoutInflater.inflate(R.layout.intro_view03, null);
		
		characterView = new ImageView[3];
		tagView = new ImageView[11];
		
		for (int i = 0; i < 3; i++) { characterView[i] = (ImageView)view1.findViewById(R.id.character1+i); }
		for (int i = 0; i < 11; i++) {
			tagView[i] = (ImageView)view2.findViewById(R.id.tag1+i);
			tagView[i].setVisibility(View.INVISIBLE);
		}

	}

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
	
	private void initData() {
		views.add(view1);
		views.add(view2);
		views.add(view3);
		viewPager.setAdapter(vpAdapter);
		viewPager.setOnPageChangeListener(this);
		initPoint();
	}

	private void initPoint() {
		LinearLayout linearLayout = (LinearLayout)findViewById(R.id.switching_points);
		points = new ImageView[3];
		for (int i = 0; i < 3; i++) {
			points[i] = (ImageView)linearLayout.getChildAt(i);
			points[i].setOnClickListener(this);
			points[i].setEnabled(true);
			points[i].setTag(i);
		}
		currentIndex = 0;
		points[currentIndex].setEnabled(false);
	}

	private void setAnim() {
		alphaAnimation = new AlphaAnimation[3];
		for (int i = 0; i < 3; i++) {
			alphaAnimation[i] = new AlphaAnimation(0, 1);
			alphaAnimation[i].setDuration(1000);
			alphaAnimation[i].setRepeatCount(0);
			alphaAnimation[i].setFillAfter(true);
			alphaAnimation[i].setStartOffset(i*1000);
		}
		for (int i = 0; i < 3; i++) { characterView[i].startAnimation(alphaAnimation[i]); }
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onPageSelected(int position) {

		setCurrentPoint(position);
		if (position == 1 && isSlided == false) {
			tagView[0].startAnimation(alphaAnimation[1]);
			tagView[1].startAnimation(alphaAnimation[0]);
			tagView[2].startAnimation(alphaAnimation[2]);
			tagView[3].startAnimation(alphaAnimation[0]);
			tagView[4].startAnimation(alphaAnimation[2]);
			tagView[5].startAnimation(alphaAnimation[1]);
			tagView[6].startAnimation(alphaAnimation[1]);
			tagView[7].startAnimation(alphaAnimation[2]);
			tagView[8].startAnimation(alphaAnimation[0]);
			tagView[9].startAnimation(alphaAnimation[2]);
			tagView[10].startAnimation(alphaAnimation[1]);
			isSlided = true;
		}
	}


	@Override
	public void onClick(View v) {

		int position = (Integer)v.getTag();
		setCurrentView(position);
		setCurrentPoint(position);
	}

	private void setCurrentView(int position) {

		if (position < 0 || position >= 3) {
			return;
		}
		viewPager.setCurrentItem(position);
	}

	private void setCurrentPoint(int position) {

		if (position < 0 || position >= 3 || currentIndex == position) {
			return;
		}
		points[position].setEnabled(false);
		points[currentIndex].setEnabled(true);
		currentIndex = position;
	}
	
}
