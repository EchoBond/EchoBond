package com.echobond.activity;

import com.echobond.R;
import com.echobond.fragment.SearchPeopleFragment;
import com.echobond.fragment.SearchPeopleResultFragment;
import com.echobond.fragment.SearchThoughtsFragment;
import com.echobond.fragment.SearchThoughtsResultFragment;
import com.echobond.intf.ViewMoreSwitchCallback;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
/**
 * 
 * @author aohuijun
 *
 */
public class SearchPage extends ActionBarActivity implements ViewMoreSwitchCallback {
	
	private ImageView backButton;
	private EditText searchBar;
	private String searchText;
	public static FragmentTabHost tabHost;
	public static SearchPage instance = null;
	private int fgType = -1;
	private String resultString = "";

	public final static int THOUGHT_GROUP = 0;
	public final static int THOUGHT_TAG = 1;
	public final static int PEOPLE_GROUP = 2;
	public final static int PEOPLE_TAG = 3;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_page);
		instance = this;
		initActionBar();
		initTabs();
	}

	private void initActionBar() {
		Toolbar searchToolbar = (Toolbar)findViewById(R.id.toolbar_search);
		setSupportActionBar(searchToolbar);
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.title_bar_search);
		
		searchBar = (EditText)findViewById(R.id.searchBar);
		searchText = searchBar.getText().toString();
		backButton = (ImageView)findViewById(R.id.search_button_back);
		backButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent upIntent = NavUtils.getParentActivityIntent(SearchPage.this);
				if (NavUtils.shouldUpRecreateTask(SearchPage.this, upIntent)) {
					TaskStackBuilder.create(SearchPage.this).addNextIntentWithParentStack(upIntent).startActivities();
				}else {
					upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					NavUtils.navigateUpTo(SearchPage.this, upIntent);
				}
			}
		});
		
	}
	
	private void initTabs() {
		tabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
		tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
		tabHost.addTab(tabHost.newTabSpec("thoughts").setIndicator("Thoughts"), SearchThoughtsFragment.class, null);
		tabHost.addTab(tabHost.newTabSpec("people").setIndicator("People"), SearchPeopleFragment.class, null);
		tabHost.setCurrentTab(0);
		
//		Intent intent = getIntent();
//		String loadPage = intent.getStringExtra("string");
//		if (loadPage == null || loadPage.equals("")) {
//			
//		} else if (loadPage == "Groups for Thoughts") {
//			tabHost.clearAllTabs();
//			tabHost.addTab(tabHost.newTabSpec("thoughts").setIndicator("Thoughts"), SearchThoughtsResultFragment.class, null);
//			tabHost.addTab(tabHost.newTabSpec("people").setIndicator("People"), SearchPeopleFragment.class, null);
//			tabHost.setCurrentTab(0);
//		} else if (loadPage == "Hashtags for Thoughts") {
//			tabHost.clearAllTabs();
//			tabHost.addTab(tabHost.newTabSpec("thoughts").setIndicator("Thoughts"), SearchThoughtsResultFragment.class, null);
//			tabHost.addTab(tabHost.newTabSpec("people").setIndicator("People"), SearchPeopleFragment.class, null);
//			tabHost.setCurrentTab(0);
//		} else if (loadPage == "Groups for People") {
//			tabHost.clearAllTabs();
//			tabHost.addTab(tabHost.newTabSpec("thoughts").setIndicator("Thoughts"), SearchThoughtsFragment.class, null);
//			tabHost.addTab(tabHost.newTabSpec("people").setIndicator("People"), SearchPeopleResultFragment.class, null);
//			tabHost.setCurrentTab(1);
//		} else if (loadPage == "Hashtags for People") {
//			tabHost.clearAllTabs();
//			tabHost.addTab(tabHost.newTabSpec("thoughts").setIndicator("Thoughts"), SearchThoughtsFragment.class, null);
//			tabHost.addTab(tabHost.newTabSpec("people").setIndicator("People"), SearchPeopleResultFragment.class, null);
//			tabHost.setCurrentTab(1);
//		}
		
	}

	@Override
	public int onTypeSelected(int type) {
		this.fgType = type;
		Intent intent = new Intent();
		intent.setClass(this, ViewMorePage.class);
		
		switch (fgType) {
		case THOUGHT_GROUP:
			Toast.makeText(getApplicationContext(), "thoughts' more groups", Toast.LENGTH_SHORT).show();
			setContentView(R.layout.fragment_more_groups);
			break;
		case THOUGHT_TAG:
			Toast.makeText(getApplicationContext(), "thoughts' more tags", Toast.LENGTH_SHORT).show();
			break;
		case PEOPLE_GROUP:
			Toast.makeText(getApplicationContext(), "people's more groups", Toast.LENGTH_SHORT).show();
			break;
		case PEOPLE_TAG:
			Toast.makeText(getApplicationContext(), "people's more tags", Toast.LENGTH_SHORT).show();
			break;
			
		default:
			break;
		}
		intent.putExtra("type", fgType);
		startActivity(intent);
		return fgType;
	}

	public FragmentTabHost getTabHost() {
		return tabHost;
	}

	public void setResultString(String resultString) {
		this.resultString = resultString;
		searchBar.setText(resultString);
	}
	
}
