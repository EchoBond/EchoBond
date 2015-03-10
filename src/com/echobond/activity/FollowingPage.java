package com.echobond.activity;

import com.echobond.R;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

public class FollowingPage extends ActionBarActivity {

	private ImageView doneButton;
	private TextView followingTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_following_page);
		initActionBar();
		
	}

	private void initActionBar() {
		Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_following);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.title_bar_setting);
		
		followingTitle = (TextView)findViewById(R.id.title_name);
		followingTitle.setText(R.string.following_groups);
		doneButton = (ImageView)findViewById(R.id.button_right_side);
		doneButton.getResources().getDrawable(R.drawable.done_button);
		
	}
}
