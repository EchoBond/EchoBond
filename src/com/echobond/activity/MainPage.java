package com.echobond.activity;

import java.util.ArrayList;

import com.echobond.fragment.LikeMindedFragment;
import com.echobond.fragment.MainFragmentPagerAdapter;
import com.echobond.fragment.ProfileFragment;
import com.echobond.fragment.ThoughtFragment;
import com.echobond.R;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * 
 * @author aohuijun
 *
 */
public class MainPage extends FragmentActivity {

	private ThoughtFragment homeFragment, hitFragment, newTrendFragment;
	private LikeMindedFragment likeMindedFragment;
	private ProfileFragment profileFragment;
	private ArrayList<Fragment> mainFragmentsList;
	private MainFragmentPagerAdapter mAdapter;
	private ViewPager mTabPager;
	private ImageView home, hit, newTrend, likeMindedPpl, profile, add, tabSelector;
	private int currentIndex = 0;
	private int[] offset;
	public FragmentManager fManager;
	Intent intent = new Intent();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		ColorDrawable colorDrawable = new ColorDrawable();
		colorDrawable.setColor(Color.WHITE);
		colorDrawable.setAlpha(0);
		getActionBar().setBackgroundDrawable(colorDrawable);
		getActionBar().setHomeButtonEnabled(true);
		
//		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main_page);
		
		fManager = getSupportFragmentManager();
		initViews();
		initTabPager();
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.mainpage_actionbar_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			intent.setClass(MainPage.this, SearchPage.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private void initViews() {
		
		mainFragmentsList = new ArrayList<Fragment>();
		homeFragment = new ThoughtFragment();
		hitFragment = new ThoughtFragment();
		newTrendFragment = new ThoughtFragment();
		likeMindedFragment = new LikeMindedFragment();
		profileFragment = new ProfileFragment();
		ThoughtFragment f5 = new ThoughtFragment();
		mainFragmentsList.add(homeFragment);
		mainFragmentsList.add(hitFragment);
		mainFragmentsList.add(newTrendFragment);
		mainFragmentsList.add(likeMindedFragment);
		mainFragmentsList.add(profileFragment);
		mainFragmentsList.add(f5);
		
		Display currentDisplay = getWindowManager().getDefaultDisplay();
		Point outSize = new Point();
		currentDisplay.getSize(outSize);		// Better NOT use getWidth();
		int displayWidth = outSize.x;
		offset = new int[6];
		for (int i = 0; i < 6; i++) {
			offset[i] = displayWidth * (i+1) / 6;
		}
	}

	private void initTabPager() {
		
		mAdapter = new MainFragmentPagerAdapter(fManager, mainFragmentsList);
		mTabPager = (ViewPager)findViewById(R.id.tabpager);
		mTabPager.setOnPageChangeListener(new MyOnPageChangeListener());
		mTabPager.setAdapter(mAdapter);
		
		tabSelector = (ImageView)findViewById(R.id.tabBtn);

		home = (ImageView)findViewById(R.id.homeBtn);
		hit = (ImageView)findViewById(R.id.hitBtn);
		newTrend = (ImageView)findViewById(R.id.newTrendBtn);
		likeMindedPpl = (ImageView)findViewById(R.id.likeMindedPplBtn);
		profile = (ImageView)findViewById(R.id.profileBtn);
		add = (ImageView)findViewById(R.id.addBtn);
		
		home.setOnClickListener(new MyOnClickListener(0));
		hit.setOnClickListener(new MyOnClickListener(1));
		newTrend.setOnClickListener(new MyOnClickListener(2));
		likeMindedPpl.setOnClickListener(new MyOnClickListener(3));
		profile.setOnClickListener(new MyOnClickListener(4));
		add.setOnClickListener(new MyOnClickListener(5));
		
	}

	
	public class MyOnClickListener implements OnClickListener {
		
		private int index = 0;
		public MyOnClickListener(int i) {	index = i;	}
		public void onClick(View v){
			mTabPager.setCurrentItem(index);
		}
	};
	
	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

			if (arg0 == 2) {
				int i = mTabPager.getCurrentItem();
				switch (i) {
				case 0: mTabPager.setCurrentItem(0); break;
				case 1: mTabPager.setCurrentItem(1); break;
				case 2: mTabPager.setCurrentItem(2); break;
				case 3: mTabPager.setCurrentItem(3); break;
				case 4: mTabPager.setCurrentItem(4); break;
				case 5: mTabPager.setCurrentItem(5); break;
				default: break;
				}
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageSelected(int arg0) {
			
			Animation animation = new TranslateAnimation(offset[0]*currentIndex, offset[0]*arg0, 0, 0);
			currentIndex = arg0;
			animation.setFillAfter(true);
			animation.setDuration(300);
			tabSelector.startAnimation(animation);
		}
	}

	private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "Click once more to quit.", Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
