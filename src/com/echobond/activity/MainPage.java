package com.echobond.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.echobond.fragment.LikeMindedFragment;
import com.echobond.fragment.MainFragmentPagerAdapter;
import com.echobond.fragment.ProfileFragment;
import com.echobond.fragment.ThoughtFragment;
import com.echobond.R;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
/**
 * 
 * @author aohuijun
 *
 */
public class MainPage extends ActionBarActivity {

	public static final int SETTING_EDIT_PROFILE = 0;
	public static final int SETTING_APP_SETTING = 1;
	public static final int SETTING_FOLLOWING = 2;
	public static final int SETTING_THIS_APP_SUCKS = 3;
	public static final int SETTING_TERMS_OF_SERVICES = 4;
	public static final int SETTING_CONTACT_US = 5;
	
	private ThoughtFragment homeFragment, hitFragment, newTrendFragment;
	private LikeMindedFragment likeMindedFragment;
	private ProfileFragment profileFragment;
	private ArrayList<Fragment> mainFragmentsList;
	private MainFragmentPagerAdapter mAdapter;
	private ViewPager mTabPager;
	private ImageView homeButton, hitButton, newTrendButton, likeMindedPplButton, profileButton, addButton, 
						messageButton, settingButton, tabSelector;
	private EditText searchBar;
	private int currentIndex = 0;
	private int[] offset;
	public FragmentManager fManager = getSupportFragmentManager();
	
	private ActionBarDrawerToggle drawerToggle;
	private DrawerLayout mainDrawerLayout;
	private ListView drawerList;
	private int[] settingIcons = new int[]{
			R.drawable.edit_profile, R.drawable.app_setting, R.drawable.following_blue, 
			R.drawable.sucks_comment, R.drawable.terms_of_service, R.drawable.contact};
	private String[] settingTitles;
	private SimpleAdapter settingPageAdapter;
	private boolean isOpened = false;
	private long exitTime = 0;

	Intent intent = new Intent();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_page);
		initActionBar();
		initViews();
		initTabPager();
		initSettingPage();
		
	}
	
	private List<Map<String, Object>> getSettingData() {
		List<Map<String, Object>> settingList = new ArrayList<Map<String,Object>>();
		for (int i = 0; i < settingIcons.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("pic", settingIcons[i]);
			map.put("title", settingTitles[i]);
			settingList.add(map);
		}
		return settingList;
	}

	private void initActionBar() {
		Toolbar mainToolbar = (Toolbar)findViewById(R.id.main_toolbar);
		setSupportActionBar(mainToolbar);
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.title_bar_main);
		
		searchBar = (EditText)findViewById(R.id.searchBarView);
		messageButton = (ImageView)findViewById(R.id.messageBtn);
		settingButton = (ImageView)findViewById(R.id.settingBtn);
		searchBar.setOnClickListener(new BarItemOnClickListener(0));
		settingButton.setOnClickListener(new BarItemOnClickListener(2));
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

		homeButton = (ImageView)findViewById(R.id.homeBtn);
		hitButton = (ImageView)findViewById(R.id.hitBtn);
		newTrendButton = (ImageView)findViewById(R.id.newTrendBtn);
		likeMindedPplButton = (ImageView)findViewById(R.id.likeMindedPplBtn);
		profileButton = (ImageView)findViewById(R.id.profileBtn);
		addButton = (ImageView)findViewById(R.id.addBtn);
		
		homeButton.setOnClickListener(new FragmentChangeOnClickListener(0));
		hitButton.setOnClickListener(new FragmentChangeOnClickListener(1));
		newTrendButton.setOnClickListener(new FragmentChangeOnClickListener(2));
		likeMindedPplButton.setOnClickListener(new FragmentChangeOnClickListener(3));
		profileButton.setOnClickListener(new FragmentChangeOnClickListener(4));
		addButton.setOnClickListener(new FragmentChangeOnClickListener(5));
		
	}
	
	private void initSettingPage() {
		
		final String[] from = new String[] {"pic", "title"};
		final int[] to = new int[] {R.id.settingItemIcon, R.id.settingItemText};
		settingTitles = getResources().getStringArray(R.array.setting_list_array);
		settingPageAdapter = new SimpleAdapter(this, getSettingData(), R.layout.item_setting, from, to);
    	mainDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
    	drawerList = (ListView)findViewById(R.id.setting_drawer);
    	drawerList.setAdapter(settingPageAdapter);
    	drawerToggle = new ActionBarDrawerToggle(this, mainDrawerLayout, null, 0, 0) {
    		
    		@Override
    		public void onDrawerOpened(View drawerView) {
    			isOpened = true;
    			super.onDrawerOpened(drawerView);
    		}
    		
    		@Override
    		public void onDrawerClosed(View drawerView) {
    			isOpened = false;
    			super.onDrawerClosed(drawerView);
    		}
    	};
    	mainDrawerLayout.setDrawerListener(drawerToggle);
    	drawerList.setOnItemClickListener(new SettingItemClickListener());
	}

	public class BarItemOnClickListener implements OnClickListener {
		
		private int barItemIndex = 0;
		public BarItemOnClickListener(int i) { barItemIndex = i; }
		@Override
		public void onClick(View v) {
			switch (barItemIndex) {
			case 0:
				intent.setClass(MainPage.this, SearchPage.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				break;
			case 1:
				
				break;
			case 2:
				try {
					mainDrawerLayout.openDrawer(drawerList);
					isOpened = true;
				} catch (ClassCastException e) {
					throw new ClassCastException(this.toString() + "must implement SettingButtonClickListener. ");
				}
				break;
				
			default:
				break;
			}
		}
		
	}
	
	public class SettingItemClickListener implements AdapterView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			switch (position) {
			case SETTING_EDIT_PROFILE:
				Toast.makeText(getApplicationContext(), settingTitles[0], Toast.LENGTH_SHORT).show();
				break;
			case SETTING_APP_SETTING:
				intent.setClass(MainPage.this, AppSettingPage.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				break;
			case SETTING_FOLLOWING:
				Toast.makeText(getApplicationContext(), settingTitles[2], Toast.LENGTH_SHORT).show();
				break;
			case SETTING_THIS_APP_SUCKS:
				Toast.makeText(getApplicationContext(), settingTitles[3], Toast.LENGTH_SHORT).show();
				break;
			case SETTING_TERMS_OF_SERVICES:
				Toast.makeText(getApplicationContext(), settingTitles[4], Toast.LENGTH_SHORT).show();
				break;
			case SETTING_CONTACT_US:
				Toast.makeText(getApplicationContext(), settingTitles[5], Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
		
	}
	
	public class FragmentChangeOnClickListener implements OnClickListener {
		
		private int fragmentIndex = 0;
		public FragmentChangeOnClickListener(int i) { fragmentIndex = i; }
		public void onClick(View v){
			mTabPager.setCurrentItem(fragmentIndex);
		}
	};
	
	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if (isOpened == true) {
				mainDrawerLayout.closeDrawer(drawerList);
				isOpened = false;
			}else if (isOpened == false) {
				if ((System.currentTimeMillis() - exitTime) > 2000) {
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.hint_quit), Toast.LENGTH_SHORT).show();
					exitTime = System.currentTimeMillis();
				} else {
					finish();
					System.exit(0);
				}
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
