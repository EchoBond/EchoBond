package com.echobond.activity;

import java.util.ArrayList;

import org.json.JSONObject;

import com.echobond.R;
import com.echobond.application.MyApp;
import com.echobond.connector.FollowGroupsAsyncTask;
import com.echobond.connector.FollowTagsAsyncTask;
import com.echobond.entity.User;
import com.echobond.fragment.FollowingGroupsFragment;
import com.echobond.fragment.FollowingHashtagsFragment;
import com.echobond.intf.FollowGroupsCallback;
import com.echobond.intf.FollowTagsCallback;
import com.echobond.util.HTTPUtil;
import com.echobond.util.SPUtil;

import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
/**
 * 
 * @author aohuijun
 *
 */
public class FollowingPage extends ActionBarActivity implements FollowGroupsCallback, FollowTagsCallback{

	private FollowingGroupsFragment groupsFragment;
	private FollowingHashtagsFragment hashtagsFragment;
	private ImageView doneButton;
	private ImageView backButton;
	private TextView followingTitle;
	private ProgressBar spinner;
	private int fgIndex = 0;
	private boolean followGroups = false;
	private boolean followTags = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_following_page);
		initActionBar();
		initViews();
	}

	private void initActionBar() {
		Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_following);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.title_bar_setting);
		
		spinner = (ProgressBar) findViewById(R.id.following_spinner);
		
		followingTitle = (TextView)findViewById(R.id.title_name);
		followingTitle.setText(R.string.following_groups);
		
		backButton = (ImageView)findViewById(R.id.button_left_side);
		backButton.setOnClickListener(new backOnClickListener());
		backButton.setImageDrawable(getResources().getDrawable(R.drawable.button_back));
		backButton.setVisibility(View.GONE);
		
		doneButton = (ImageView)findViewById(R.id.button_right_side);
		doneButton.setImageDrawable(getResources().getDrawable(R.drawable.button_done));
		doneButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switch (fgIndex) {
				case 0:
					fgIndex += 1;
					backButton.setVisibility(View.VISIBLE);
					followingTitle.setText(R.string.following_hashtags);
					getSupportFragmentManager().beginTransaction().hide(groupsFragment).show(hashtagsFragment).commit();
					break;
				case 1:
					spinner.setVisibility(View.VISIBLE);
					spinner.bringToFront();
					ArrayList<Integer> groups = groupsFragment.getGroupList();
					ArrayList<Integer> tags = hashtagsFragment.getTagList();
					if(null != groups && !groups.isEmpty()){
						followGroups = true;
					}
					if(null != tags && !tags.isEmpty()){
						followTags = true;
					}
					String gUrl = HTTPUtil.getInstance().composePreURL(FollowingPage.this) 
							+ getResources().getString(R.string.url_follow_group);
					String tUrl = HTTPUtil.getInstance().composePreURL(FollowingPage.this)
							+ getResources().getString(R.string.url_follow_tag);
					User user = new User();
					user.setId((String) SPUtil.get(FollowingPage.this, MyApp.PREF_TYPE_LOGIN, MyApp.LOGIN_ID, "", String.class));
					if(!followGroups && !followTags){
						Intent intent = new Intent();
						intent.setClass(FollowingPage.this, MainPage.class);
						startActivity(intent);
						finish();
					} else if(followGroups && !followTags){
						new FollowGroupsAsyncTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, gUrl,FollowingPage.this,user,groups);
					} else if(!followGroups && followTags){
						new FollowTagsAsyncTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, tUrl,FollowingPage.this,user,tags);
					} else {
						new FollowGroupsAsyncTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, gUrl,FollowingPage.this,user,groups);
						new FollowTagsAsyncTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, tUrl,FollowingPage.this,user,tags);						
					}
				default:
					break;
				}
			}
		});
		
	}
	
	public class backOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			fgIndex -= 1;
			backButton.setVisibility(View.GONE);
			followingTitle.setText(R.string.following_groups);
			getSupportFragmentManager().beginTransaction().hide(hashtagsFragment).show(groupsFragment).commit();
		}
		
	}
	private void initViews() {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		if (null == groupsFragment || null == hashtagsFragment) {
			groupsFragment = new FollowingGroupsFragment();
			hashtagsFragment = new FollowingHashtagsFragment();
			transaction.add(R.id.following_content, groupsFragment);
			transaction.add(R.id.following_content, hashtagsFragment);
			transaction.hide(hashtagsFragment);
			transaction.show(groupsFragment).commit();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			Intent intent = new Intent();
			intent.setClass(this, MainPage.class);
			startActivity(intent);
			finish();
		}
		return true;
	}

	@Override
	public void onFollowGroupsFinished(JSONObject result) {
		if(followTags) {
			return;
		} else if(null != result){
			Intent intent = new Intent();
			intent.setClass(FollowingPage.this, MainPage.class);
			spinner.setVisibility(View.INVISIBLE);
			startActivity(intent);
			finish();
		}
	}

	@Override
	public void onFollowTagsFinished(JSONObject result) {
		if(null != result){
			Intent intent = new Intent();
			intent.setClass(FollowingPage.this, MainPage.class);
			spinner.setVisibility(View.INVISIBLE);
			startActivity(intent);
			finish();			
		}
	}

}
