package com.echobond.activity;

import com.echobond.R;
import com.echobond.fragment.FollowingGroupsFragment;
import com.echobond.fragment.FollowingHashtagsFragment;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 
 * @author aohuijun
 *
 */
public class FollowingPage extends ActionBarActivity {

	private FollowingGroupsFragment groupsFragment;
	private FollowingHashtagsFragment hashtagsFragment;
	private ImageView doneButton;
	private ImageView backButton;
	private TextView followingTitle;
	private Intent intent = new Intent();
	private int fgIndex = 0;
	
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
		
		followingTitle = (TextView)findViewById(R.id.title_name);
		followingTitle.setText(R.string.following_groups);
		
		backButton = (ImageView)findViewById(R.id.button_left_side);
		backButton.setOnClickListener(new backOnClickListener());
		backButton.setImageDrawable(getResources().getDrawable(R.drawable.back_button));
		backButton.setVisibility(View.GONE);
		
		doneButton = (ImageView)findViewById(R.id.button_right_side);
		doneButton.setImageDrawable(getResources().getDrawable(R.drawable.done_button));
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
					intent.setClass(FollowingPage.this, MainPage.class);
					startActivity(intent);
					finish();
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
}
