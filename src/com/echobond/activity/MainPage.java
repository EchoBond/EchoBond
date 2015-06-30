package com.echobond.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.echobond.application.MyApp;
import com.echobond.connector.DataFetchIntentService;
import com.echobond.dao.ChatDAO;
import com.echobond.fragment.MainFragmentPagerAdapter;
import com.echobond.fragment.HomeThoughtFragment;
import com.echobond.fragment.HotThoughtFragment;
import com.echobond.fragment.NotificationFragment;
import com.echobond.fragment.ProfileFragment;
import com.echobond.intf.GCMCallback;
import com.echobond.util.GCMUtil;
import com.echobond.util.SPUtil;
import com.echobond.R;

import android.app.ActionBar;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
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
public class MainPage extends ActionBarActivity implements GCMCallback {

	public static final int SETTING_EDIT_PROFILE = 0;
	public static final int SETTING_APP_SETTING = 1;
	public static final int SETTING_FOLLOWING = 2;
	public static final int SETTING_INVITE_FRIENDS = 3;
	public static final int SETTING_THIS_APP_SUCKS = 4;
	public static final int SETTING_TERMS_OF_SERVICES = 5;
	public static final int SETTING_CONTACT_US = 6;
	
	public static final int TAB_NUMBER = 4;
	
	private HomeThoughtFragment homeFragment;
	private HotThoughtFragment hitFragment;
	private NotificationFragment notificationFragment;
	private ProfileFragment profileFragment;
	
	private ArrayList<Fragment> mainFragmentsList;
	private MainFragmentPagerAdapter mAdapter;
	private ViewPager mTabPager;
	private ImageView homeButton, hitButton, notificationButton, profileButton,  
						newPostButton, settingButton, notificationIndicator;
	private EditText searchBar;
	private int currentIndex = 0;
	private int[] offset;
	public FragmentManager fManager = getSupportFragmentManager();
	private long exitTime = 0;
	
	/**
	 * Variables for Drawer Setting
	 */
	private ActionBarDrawerToggle drawerToggle;
	private DrawerLayout mainDrawerLayout;
	private ListView drawerList;
	private int[] settingIcons = new int[]{
			R.drawable.setting_edit_profile, R.drawable.setting_app_setting, R.drawable.setting_following, 
			R.drawable.setting_invite_friends, R.drawable.setting_sucks_comment, R.drawable.setting_terms_of_service, 
			R.drawable.setting_contact};
	private String[] settingTitles;
	private SimpleAdapter settingPageAdapter;
	private boolean isOpened = false;
	
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getExtras().getBoolean("new")){
				notificationIndicator.setImageDrawable(getResources().getDrawable(R.drawable.button_notification));
			} else {
				notificationIndicator.setImageDrawable(null);
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_page);
		
		Intent intent = new Intent();
		intent.setClass(this, DataFetchIntentService.class);
		startService(intent);
		initActionBar();
		initViews();
		initTabPager();
		initSettingPage();
		
		GCMUtil.getInstance().registerDevice(this);
		LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("newNotification"));
		
		String[] selectionArgs = new String[]{
				(String) SPUtil.get(this, MyApp.PREF_TYPE_LOGIN, MyApp.LOGIN_ID, "", String.class),""};
		Cursor c = getContentResolver().query(ChatDAO.CONTENT_URI, null, null, selectionArgs, null);
		if(null != c){
			if(c.moveToFirst()){
				int count = c.getInt(c.getColumnIndex("count"));
				if(count > 0){
					notificationIndicator.setImageDrawable(getResources().getDrawable(R.drawable.button_notification));
				}
			}
			c.close();
		}
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Intent thisIntent = getIntent();
		if(null != thisIntent && thisIntent.getExtras()!= null && thisIntent.getExtras().getBoolean("fromDataFetch")){
			notificationFragment.changeTab(1);
			mTabPager.setCurrentItem(2);
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
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
		Toolbar mainToolbar = (Toolbar)findViewById(R.id.toolbar_main);
		setSupportActionBar(mainToolbar);
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.title_bar_main);
		
		searchBar = (EditText)findViewById(R.id.search_bar_view);
		newPostButton = (ImageView)findViewById(R.id.button_new_post);
		settingButton = (ImageView)findViewById(R.id.button_setting);
		searchBar.setOnClickListener(new BarItemOnClickListener(0));
		newPostButton.setOnClickListener(new BarItemOnClickListener(1));
		settingButton.setOnClickListener(new BarItemOnClickListener(2));
	}

	private void initViews() {
		
		homeFragment = new HomeThoughtFragment();
		hitFragment = new HotThoughtFragment();
		notificationFragment = new NotificationFragment();
		profileFragment = new ProfileFragment();
		
		mainFragmentsList = new ArrayList<Fragment>();
		mainFragmentsList.add(homeFragment);
		mainFragmentsList.add(hitFragment);
		mainFragmentsList.add(notificationFragment);
		mainFragmentsList.add(profileFragment);
		
		Display currentDisplay = getWindowManager().getDefaultDisplay();
		Point outSize = new Point();
		currentDisplay.getSize(outSize);		// Better NOT use getWidth();
		int displayWidth = outSize.x;
		offset = new int[TAB_NUMBER];
		for (int i = 0; i < TAB_NUMBER; i++) {
			offset[i] = displayWidth * (i+1) / TAB_NUMBER;
		}
	}

	private void initTabPager() {
		
		mAdapter = new MainFragmentPagerAdapter(fManager, mainFragmentsList);
		mTabPager = (ViewPager)findViewById(R.id.tabpager);
		mTabPager.setOnPageChangeListener(new MyOnPageChangeListener());
		mTabPager.setAdapter(mAdapter);
		mTabPager.setOffscreenPageLimit(TAB_NUMBER);

		homeButton = (ImageView)findViewById(R.id.home_button);
		hitButton = (ImageView)findViewById(R.id.hit_button);
		notificationButton = (ImageView)findViewById(R.id.notification_button);
		notificationIndicator = (ImageView) findViewById(R.id.notification_indicator);
		profileButton = (ImageView)findViewById(R.id.profile_button);
		
		homeButton.setImageDrawable(getResources().getDrawable(R.drawable.main_home_button_selected));
		
		homeButton.setOnClickListener(new FragmentChangeOnClickListener(0));
		hitButton.setOnClickListener(new FragmentChangeOnClickListener(1));
		notificationButton.setOnClickListener(new FragmentChangeOnClickListener(2));
		profileButton.setOnClickListener(new FragmentChangeOnClickListener(3));
		
	}
	
	private void initSettingPage() {
		
		final String[] from = new String[] {"pic", "title"};
		final int[] to = new int[] {R.id.icon_setting_item, R.id.text_setting_item};
		settingTitles = getResources().getStringArray(R.array.setting_list_array);
		settingPageAdapter = new SimpleAdapter(this, getSettingData(), R.layout.item_setting, from, to);
    	mainDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
    	drawerList = (ListView)findViewById(R.id.setting_drawer);
    	drawerList.setAdapter(settingPageAdapter);
    	drawerList.setOverScrollMode(View.OVER_SCROLL_NEVER);
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
			Intent intent = new Intent();
			switch (barItemIndex) {
			case 0:
				intent.setClass(MainPage.this, SearchPage.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				break;
			case 1:
				intent.putExtra("mode", MyApp.VIEW_MORE_POST);
				intent.setClass(MainPage.this, NewPostPage.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
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
	
	/**
	 * Navigations to Different Activities in Drawer Layout 
	 * @author aohuijun
	 *
	 */
	public class SettingItemClickListener implements AdapterView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) { 
			Intent intent = new Intent();
			switch (position) {
			case SETTING_EDIT_PROFILE:
				intent.setClass(MainPage.this, EditProfilePage.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				break;
			case SETTING_APP_SETTING:
				intent.setClass(MainPage.this, AppSettingPage.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				break;
			case SETTING_FOLLOWING:
				intent.putExtra("page", SETTING_FOLLOWING);
				intent.setClass(MainPage.this, ServicePage.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				break;
			case SETTING_INVITE_FRIENDS:
				intent.putExtra("page", SETTING_INVITE_FRIENDS);
				intent.setClass(MainPage.this, ServicePage.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				break;
			case SETTING_THIS_APP_SUCKS:
				intent.putExtra("page", SETTING_THIS_APP_SUCKS);
				intent.setClass(MainPage.this, ServicePage.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				break;
			case SETTING_TERMS_OF_SERVICES:
				intent.putExtra("page", SETTING_TERMS_OF_SERVICES);
				intent.setClass(MainPage.this, ServicePage.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				break;
			case SETTING_CONTACT_US:
				intent.putExtra("page", SETTING_CONTACT_US);
				intent.setClass(MainPage.this, ServicePage.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
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
		public void onPageScrolled(int position, float arg1, int arg2) {
			
		}

		@Override
		public void onPageSelected(int position) {
			switch (position) {
			case 0:
				homeButton.setImageDrawable(getResources().getDrawable(R.drawable.main_home_button_selected));
				if (currentIndex == 1) {
					hitButton.setImageDrawable(getResources().getDrawable(R.drawable.main_hit_button));
				} else if (currentIndex == 2) {
					notificationButton.setImageDrawable(getResources().getDrawable(R.drawable.main_notification_button));
				} else if (currentIndex == 3) {
					profileButton.setImageDrawable(getResources().getDrawable(R.drawable.main_profile_button));
				}
				break;
			case 1:
				hitButton.setImageDrawable(getResources().getDrawable(R.drawable.main_hit_button_selected));
				if (currentIndex == 0) {
					homeButton.setImageDrawable(getResources().getDrawable(R.drawable.main_home_button));
				} else if (currentIndex == 2) {
					notificationButton.setImageDrawable(getResources().getDrawable(R.drawable.main_notification_button));
				} else if (currentIndex == 3) {
					profileButton.setImageDrawable(getResources().getDrawable(R.drawable.main_profile_button));
				}
				break;
			case 2:
				notificationButton.setImageDrawable(getResources().getDrawable(R.drawable.main_notification_button_selected));
				if (currentIndex == 0) {
					homeButton.setImageDrawable(getResources().getDrawable(R.drawable.main_home_button));
				} else if (currentIndex == 1) {
					hitButton.setImageDrawable(getResources().getDrawable(R.drawable.main_hit_button));
				} else if (currentIndex == 3) {
					profileButton.setImageDrawable(getResources().getDrawable(R.drawable.main_profile_button));
				}
				break;
			case 3:
				profileButton.setImageDrawable(getResources().getDrawable(R.drawable.main_profile_button_selected));
				if (currentIndex == 0) {
					homeButton.setImageDrawable(getResources().getDrawable(R.drawable.main_home_button));
				} else if (currentIndex == 1) {
					hitButton.setImageDrawable(getResources().getDrawable(R.drawable.main_hit_button));
				} else if (currentIndex == 2) {
					notificationButton.setImageDrawable(getResources().getDrawable(R.drawable.main_notification_button));
				}
				break;

			default:
				break;
			}
			currentIndex = position;
		}
	}

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if (isOpened == true) {
				mainDrawerLayout.closeDrawer(drawerList);
				isOpened = false;
			} else if (isOpened == false) {
				if ((System.currentTimeMillis() - exitTime) > 2000) {
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.hint_quit), Toast.LENGTH_SHORT).show();
					exitTime = System.currentTimeMillis();
				} else {
					NotificationManager nm = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
					nm.cancelAll();			
					finish();		
//					System.exit(0);
				}
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
    
    @Override
    public void onRegFinished(JSONObject result) {
    	// TODO Auto-generated method stub
    	
    }
}
