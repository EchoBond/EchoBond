package com.echobond.activity;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.R;
import com.echobond.fragment.MoreGroupsFragment;
import com.echobond.fragment.MoreTagsFragment;
import com.echobond.fragment.SearchMainFragment;
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
	private ArrayList<Integer> idList = null;
	public static FragmentTabHost tabHost;
	private int fgType = -1;
	private int searchType = -1;

	public final static int THOUGHT_GROUP = 0;
	public final static int THOUGHT_TAG = 1;
	public final static int PEOPLE_GROUP = 2;
	public final static int PEOPLE_TAG = 3;
	public final static int THOUGHT_CATEGORY = 4;
	
	public final static String THOUGHTS_CATEGORY = "This Category's Thoughts";
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
	public void onTypeSelected(int type) {
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
			bundle.putString("type", THOUGHTS_MORE_GROUP);
			groupsThoughtsFragment.setArguments(bundle);
			transaction.show(groupsThoughtsFragment).hide(tagsThoughtsFragment).hide(groupsPeopleFragment).hide(tagsPeopleFragment).hide(mainFragment).commit();
			break;
		case THOUGHT_TAG:
			bundle.putString("type", THOUGHTS_MORE_TAG);
			tagsThoughtsFragment.setArguments(bundle);
			transaction.show(tagsThoughtsFragment).hide(groupsThoughtsFragment).hide(groupsPeopleFragment).hide(tagsPeopleFragment).hide(mainFragment).commit();
			break;
		case PEOPLE_GROUP:
			bundle.putString("type", PEOPLE_MORE_GROUP);
			groupsPeopleFragment.setArguments(bundle);
			transaction.show(groupsPeopleFragment).hide(groupsThoughtsFragment).hide(tagsThoughtsFragment).hide(tagsPeopleFragment).hide(mainFragment).commit();
			break;
		case PEOPLE_TAG:
			bundle.putString("type", PEOPLE_MORE_TAG);
			tagsPeopleFragment.setArguments(bundle);
			transaction.show(tagsPeopleFragment).hide(groupsThoughtsFragment).hide(tagsThoughtsFragment).hide(groupsPeopleFragment).hide(mainFragment).commit();
			break;
			
		default:
			break;
		}
	}
	
	//	Set the content of tabs for showing search results. 
	@SuppressWarnings("unchecked")
	@Override
	public void onSearchSelected(JSONObject data) {
		try {
			searchType = 0;
			searchID = 0;
			idList = null;
			searchType = data.getInt("index");
			searchID = data.getInt("id");
			if(null != data.getJSONObject("idList")){
				TypeToken<ArrayList<Integer>> token = new TypeToken<ArrayList<Integer>>(){};
				idList = (ArrayList<Integer>) JSONUtil.fromJSONToList(data, "idList", token);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		Intent intent = new Intent();
		intent.putExtra("type", searchType);
		intent.putExtra("id", searchID);
		intent.putIntegerArrayListExtra("idList", idList);
		intent.setClass(this, SearchResultPage.class);
		startActivity(intent);
		
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		if (fgType != -1) {
			transaction.hide(groupsThoughtsFragment).hide(tagsThoughtsFragment).hide(groupsPeopleFragment).hide(tagsPeopleFragment).show(mainFragment).commit();
			if (groupsThoughtsFragment != null || groupsPeopleFragment != null || tagsThoughtsFragment != null || tagsPeopleFragment != null) {
				groupsThoughtsFragment = null;
				tagsThoughtsFragment = null;
				groupsPeopleFragment = null;
				tagsPeopleFragment = null;
			}
			fgType = -1;
		}
	}

	//	Detect the Fragment Situation While Pressing the BACK Button. 
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

}
