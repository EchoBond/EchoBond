package com.echobond.activity;

import com.echobond.R;
import com.echobond.fragment.MoreGroupsFragment;
import com.echobond.fragment.MoreTagsFragment;
import com.echobond.fragment.SearchMainFragment;
import com.echobond.fragment.SearchPeopleResultFragment;
import com.echobond.fragment.SearchThoughtsResultFragment;
import com.echobond.intf.ViewMoreSwitchCallback;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
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
	
	private SearchMainFragment mainFragment;
	private SearchThoughtsResultFragment searchThoughtsResultFragment;
	private SearchPeopleResultFragment searchPeopleResultFragment;
	private MoreGroupsFragment groupsThoughtsFragment;
	private MoreTagsFragment tagsThoughtsFragment;
	private MoreGroupsFragment groupsPeopleFragment;
	private MoreTagsFragment tagsPeopleFragment;
	
	private ImageView backButton;
	private EditText searchBar;
	private String searchText;
	public static FragmentTabHost tabHost;
	private int fgType = -1;

	public final static int THOUGHT_GROUP = 0;
	public final static int THOUGHT_TAG = 1;
	public final static int PEOPLE_GROUP = 2;
	public final static int PEOPLE_TAG = 3;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_page);
		initActionBar();
		initView();
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

	private void initView() {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		if (mainFragment == null) {
			mainFragment = new SearchMainFragment();
			transaction.add(R.id.search_page_container, mainFragment);
			transaction.show(mainFragment).commit();
		}
	}
	
	@Override
	public int onTypeSelected(int type) {
		this.fgType = type;
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		if (groupsThoughtsFragment == null || groupsPeopleFragment == null || tagsThoughtsFragment == null || tagsPeopleFragment == null) {
			groupsThoughtsFragment = new MoreGroupsFragment();
			groupsPeopleFragment = new MoreGroupsFragment();
			tagsThoughtsFragment = new MoreTagsFragment();
			tagsPeopleFragment = new MoreTagsFragment();
			transaction.add(R.id.search_page_container, groupsThoughtsFragment);
			transaction.add(R.id.search_page_container, tagsThoughtsFragment);
			transaction.add(R.id.search_page_container, groupsPeopleFragment);
			transaction.add(R.id.search_page_container, tagsPeopleFragment);
		}
		switch (fgType) {
		case THOUGHT_GROUP:
			Toast.makeText(getApplicationContext(), "thoughts' more groups", Toast.LENGTH_SHORT).show();
			switchActionBar();
			transaction.show(groupsThoughtsFragment).hide(tagsThoughtsFragment).hide(groupsPeopleFragment).hide(tagsPeopleFragment).hide(mainFragment).commit();
			break;
		case THOUGHT_TAG:
			Toast.makeText(getApplicationContext(), "thoughts' more tags", Toast.LENGTH_SHORT).show();
			transaction.show(tagsThoughtsFragment).hide(groupsThoughtsFragment).hide(groupsPeopleFragment).hide(tagsPeopleFragment).hide(mainFragment).commit();
			break;
		case PEOPLE_GROUP:
			Toast.makeText(getApplicationContext(), "people's more groups", Toast.LENGTH_SHORT).show();
			transaction.show(groupsPeopleFragment).hide(groupsThoughtsFragment).hide(tagsThoughtsFragment).hide(tagsPeopleFragment).hide(mainFragment).commit();
			break;
		case PEOPLE_TAG:
			Toast.makeText(getApplicationContext(), "people's more tags", Toast.LENGTH_SHORT).show();
			transaction.show(tagsPeopleFragment).hide(groupsThoughtsFragment).hide(tagsThoughtsFragment).hide(groupsPeopleFragment).hide(mainFragment).commit();
			break;
			
		default:
			break;
		}
		return fgType;
	}
	
	private void switchActionBar() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if (fgType == -1) {
				Intent upIntent = NavUtils.getParentActivityIntent(SearchPage.this);
				if (NavUtils.shouldUpRecreateTask(SearchPage.this, upIntent)) {
					TaskStackBuilder.create(SearchPage.this).addNextIntentWithParentStack(upIntent).startActivities();
				}else {
					upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					NavUtils.navigateUpTo(SearchPage.this, upIntent);
				}
				return true;
			} else {
				FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
				transaction.hide(groupsThoughtsFragment);
				transaction.hide(tagsThoughtsFragment);
				transaction.hide(groupsPeopleFragment);
				transaction.hide(tagsPeopleFragment);
				transaction.show(mainFragment).commit();
				fgType = -1;
			}
		}
		return true;
	}
}
