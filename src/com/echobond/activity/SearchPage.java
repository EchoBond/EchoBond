package com.echobond.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.R;
import com.echobond.fragment.MoreGroupsFragment;
import com.echobond.fragment.MoreTagsFragment;
import com.echobond.fragment.SearchMainFragment;
import com.echobond.fragment.SearchPeopleFragment;
import com.echobond.fragment.SearchPeopleResultFragment;
import com.echobond.fragment.SearchThoughtsFragment;
import com.echobond.fragment.SearchThoughtsResultFragment;
import com.echobond.intf.ViewMoreSwitchCallback;
import com.echobond.util.JSONUtil;
import com.google.gson.reflect.TypeToken;

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
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
/**
 * 
 * @author aohuijun
 *
 */
public class SearchPage extends ActionBarActivity implements ViewMoreSwitchCallback {
	
	private SearchMainFragment mainFragment;
	private MoreGroupsFragment groupsThoughtsFragment;
	private MoreTagsFragment tagsThoughtsFragment;
	private MoreGroupsFragment groupsPeopleFragment;
	private MoreTagsFragment tagsPeopleFragment;
	
	private ImageView backButton;
	private EditText searchBar;
	private String searchText;
	private int searchID = 0;
	private List<Integer> idList = null;
	public static FragmentTabHost tabHost;
	private int fgType = -1;
	private int searchType = -1;

	public final static int THOUGHT_GROUP = 0;
	public final static int THOUGHT_TAG = 1;
	public final static int PEOPLE_GROUP = 2;
	public final static int PEOPLE_TAG = 3;
	
	public final static String THOUGHTS_MORE_GROUP = "Thoughts in More Groups";
	public final static String THOUGHTS_MORE_TAG = "Thoughts of More Tags";
	public final static String PEOPLE_MORE_GROUP = "People in More Groups";
	public final static String PEOPLE_MORE_TAG = "People of more Tags";

		
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
		searchBar.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				Toast.makeText(getApplicationContext(), "search", Toast.LENGTH_SHORT).show();
				return false;
			}
		});
		
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
	
	//	Blocks for Loading More Groups/Hashtags. 
	@Override
	public int onTypeSelected(int type) {
		this.fgType = type;
		Bundle bundle = new Bundle();
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
			bundle.putString("type", THOUGHTS_MORE_GROUP);
			groupsThoughtsFragment.setArguments(bundle);
			transaction.show(groupsThoughtsFragment).hide(tagsThoughtsFragment).hide(groupsPeopleFragment).hide(tagsPeopleFragment).hide(mainFragment).commit();
			break;
		case THOUGHT_TAG:
			Toast.makeText(getApplicationContext(), "thoughts' more tags", Toast.LENGTH_SHORT).show();
			bundle.putString("type", THOUGHTS_MORE_TAG);
			tagsThoughtsFragment.setArguments(bundle);
			transaction.show(tagsThoughtsFragment).hide(groupsThoughtsFragment).hide(groupsPeopleFragment).hide(tagsPeopleFragment).hide(mainFragment).commit();
			break;
		case PEOPLE_GROUP:
			Toast.makeText(getApplicationContext(), "people's more groups", Toast.LENGTH_SHORT).show();
			bundle.putString("type", PEOPLE_MORE_GROUP);
			groupsPeopleFragment.setArguments(bundle);
			transaction.show(groupsPeopleFragment).hide(groupsThoughtsFragment).hide(tagsThoughtsFragment).hide(tagsPeopleFragment).hide(mainFragment).commit();
			break;
		case PEOPLE_TAG:
			Toast.makeText(getApplicationContext(), "people's more tags", Toast.LENGTH_SHORT).show();
			bundle.putString("type", PEOPLE_MORE_TAG);
			tagsPeopleFragment.setArguments(bundle);
			transaction.show(tagsPeopleFragment).hide(groupsThoughtsFragment).hide(tagsThoughtsFragment).hide(groupsPeopleFragment).hide(mainFragment).commit();
			break;
			
		default:
			break;
		}
		return fgType;
	}
	
	//	Set the content of tabs for showing search results. 
	@SuppressWarnings("unchecked")
	@Override
	public void onSearchSelected(JSONObject data) {
		try {
			this.searchType = data.getInt("index");
			searchID = data.getInt("id");
			if(null != data.getJSONObject("idList")){
				TypeToken<ArrayList<Integer>> token = new TypeToken<ArrayList<Integer>>(){};
				idList = JSONUtil.fromJSONToList(data, "idList", token);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		FragmentTabHost tabHost = mainFragment.getTabHost();
		tabHost.clearAllTabs();

		switch (searchType) {
		case THOUGHT_GROUP:
			tabHost.addTab(tabHost.newTabSpec("thoughts_group_result").setIndicator("Thoughts"), SearchThoughtsResultFragment.class, null);
			tabHost.addTab(tabHost.newTabSpec("people").setIndicator("People"), SearchPeopleFragment.class, null);
			tabHost.setCurrentTab(0);
			break;
		case THOUGHT_TAG:
			tabHost.addTab(tabHost.newTabSpec("thoughts_tag_result").setIndicator("Thoughts"), SearchThoughtsResultFragment.class, null);
			tabHost.addTab(tabHost.newTabSpec("people").setIndicator("People"), SearchPeopleFragment.class, null);
			tabHost.setCurrentTab(0);
			break;
		case PEOPLE_GROUP:
			tabHost.addTab(tabHost.newTabSpec("thoughts").setIndicator("Thoughts"), SearchThoughtsFragment.class, null);
			tabHost.addTab(tabHost.newTabSpec("people_group_result").setIndicator("People"), SearchPeopleResultFragment.class, null);
			tabHost.setCurrentTab(1);
			break;
		case PEOPLE_TAG:
			tabHost.addTab(tabHost.newTabSpec("thoughts").setIndicator("Thoughts"), SearchThoughtsFragment.class, null);
			tabHost.addTab(tabHost.newTabSpec("people_tag_result").setIndicator("People"), SearchPeopleResultFragment.class, null);
			tabHost.setCurrentTab(1);
			break;
		default:
			break;
		}
		transaction.hide(groupsThoughtsFragment).hide(tagsThoughtsFragment).hide(groupsPeopleFragment).hide(tagsPeopleFragment).show(mainFragment).commit();
		if (groupsThoughtsFragment != null || groupsPeopleFragment != null || tagsThoughtsFragment != null || tagsPeopleFragment != null) {
			groupsThoughtsFragment = null;
			tagsThoughtsFragment = null;
			groupsPeopleFragment = null;
			tagsPeopleFragment = null;
		}
	}

	//	Detect the Fragment Situation While Pressing the BACK Button. 
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if (groupsThoughtsFragment == null || groupsPeopleFragment == null || tagsThoughtsFragment == null || tagsPeopleFragment == null || fgType == -1) {
				if (searchType == -1) {
					Intent upIntent = NavUtils.getParentActivityIntent(SearchPage.this);
					if (NavUtils.shouldUpRecreateTask(SearchPage.this, upIntent)) {
						TaskStackBuilder.create(SearchPage.this).addNextIntentWithParentStack(upIntent).startActivities();
					}else {
						upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						NavUtils.navigateUpTo(SearchPage.this, upIntent);
					}
					return true;
				} else {
					FragmentTabHost tabHost = mainFragment.getTabHost();
					tabHost.clearAllTabs();
					tabHost.addTab(tabHost.newTabSpec("thoughts").setIndicator("Thoughts"), SearchThoughtsFragment.class, null);
					tabHost.addTab(tabHost.newTabSpec("people").setIndicator("People"), SearchPeopleFragment.class, null);
					tabHost.setCurrentTab(0);
					searchType = -1;
				}
			} else {
				FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
				transaction.hide(groupsThoughtsFragment);
				transaction.hide(tagsThoughtsFragment);
				transaction.hide(groupsPeopleFragment);
				transaction.hide(tagsPeopleFragment);
				transaction.show(mainFragment).commit();
				if (groupsThoughtsFragment != null || groupsPeopleFragment != null || tagsThoughtsFragment != null || tagsPeopleFragment != null) {
					groupsThoughtsFragment = null;
					tagsThoughtsFragment = null;
					groupsPeopleFragment = null;
					tagsPeopleFragment = null;
				}
				fgType = -1;
			}
		}
		return true;
	}

	public int getSearchID() {
		return searchID;
	}

	public void setSearchID(int searchID) {
		this.searchID = searchID;
	}

	public List<Integer> getIdList() {
		return idList;
	}

	public void setIdList(List<Integer> idList) {
		this.idList = idList;
	}

}
