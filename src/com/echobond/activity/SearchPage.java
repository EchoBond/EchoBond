package com.echobond.activity;

import com.echobond.R;
import com.echobond.fragment.SearchPeopleFragment;
import com.echobond.fragment.SearchThoughtsFragment;
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
	private FragmentTabHost tabHost;
	private int fgtype = -1;

	public final static int THOUGHT_GROUP = 0;
	public final static int THOUGHT_TAG = 1;
	public final static int PEOPLE_GROUP = 2;
	public final static int PEOPLE_TAG = 3;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_page);
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
	}

	@Override
	public int onTypeSelected(int type) {
		this.fgtype = type;
		switch (fgtype) {
		case THOUGHT_GROUP:
			Toast.makeText(getApplicationContext(), "thoughts' more groups", Toast.LENGTH_SHORT).show();
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
		return fgtype;
	}
	
}
