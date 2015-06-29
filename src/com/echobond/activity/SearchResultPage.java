package com.echobond.activity;

import java.util.ArrayList;

import com.echobond.R;
import com.echobond.fragment.SearchPeopleResultFragment;
import com.echobond.fragment.SearchThoughtsResultFragment;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 
 * @author aohuijun
 *
 */
public class SearchResultPage extends ActionBarActivity {

	private ImageView backButton;
	private TextView titleView;
	private SearchThoughtsResultFragment thoughtsGroupResultFragment;
	private SearchThoughtsResultFragment thoughtsTagResultFragment;
	private SearchThoughtsResultFragment thoughtsCategoryResultFragment;
	private SearchThoughtsResultFragment thoughtsKeywordResultFragment;
	private SearchPeopleResultFragment peopleGroupResultFragment;
	private SearchPeopleResultFragment peopleTagResultFragment;
	private SearchPeopleResultFragment peopleKeywordResultFragment;	
	
	private int searchType = -1;
	private int searchID = 0;
	private String searchText = "";
	private ArrayList<Integer> searchIDList = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_result_page);
		fetchIntent();
		initToolBar();
		initContent();
	}
	
	private void fetchIntent() {
		searchType = getIntent().getIntExtra("type", -1);
		searchID = getIntent().getIntExtra("id", 0);
		searchText = getIntent().getStringExtra("keyword");
		searchIDList = getIntent().getIntegerArrayListExtra("idList");
	}
	
	private void initToolBar() {
		Toolbar searchResultToolbar = (Toolbar)findViewById(R.id.toolbar_search_result);
		setSupportActionBar(searchResultToolbar);
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.title_bar_setting);
		
		titleView = (TextView)findViewById(R.id.title_name);
		titleView.setTextSize(18);
		String nameString = "";
		switch (searchType) {
		case SearchPage.THOUGHT_GROUP:
			nameString = SearchPage.THOUGHTS_MORE_GROUP;
			break;
		case SearchPage.THOUGHT_TAG:
			nameString = SearchPage.THOUGHTS_MORE_TAG;
			break;
		case SearchPage.PEOPLE_GROUP:
			nameString = SearchPage.PEOPLE_MORE_GROUP;
			break;
		case SearchPage.PEOPLE_TAG:
			nameString = SearchPage.PEOPLE_MORE_TAG;
			break;
		case SearchPage.THOUGHT_CATEGORY:
			nameString = SearchPage.THOUGHTS_CATEGORY;
			break;
		default:
			break;
		}
		titleView.setText("Search Results for " + nameString);
		
		backButton = (ImageView)findViewById(R.id.button_left_side);
		backButton.setImageDrawable(getResources().getDrawable(R.drawable.button_back));
		backButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}
	
	private void initContent() {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		if (thoughtsGroupResultFragment == null || thoughtsTagResultFragment == null || thoughtsCategoryResultFragment == null 
				|| peopleGroupResultFragment == null || peopleTagResultFragment == null 
				|| thoughtsKeywordResultFragment == null || peopleKeywordResultFragment == null) {
			thoughtsGroupResultFragment = new SearchThoughtsResultFragment();
			thoughtsTagResultFragment = new SearchThoughtsResultFragment();
			thoughtsCategoryResultFragment = new SearchThoughtsResultFragment();
			peopleGroupResultFragment = new SearchPeopleResultFragment();
			peopleTagResultFragment = new SearchPeopleResultFragment();
			thoughtsKeywordResultFragment = new SearchThoughtsResultFragment();
			peopleKeywordResultFragment = new SearchPeopleResultFragment();
		}
		
		Bundle bundle = new Bundle();
		bundle.putInt("type", searchType);
		bundle.putInt("id", searchID);
		bundle.putString("keyword", searchText);
		bundle.putIntegerArrayList("idList", searchIDList);
		switch (searchType) {
		case SearchPage.THOUGHT_GROUP:
			thoughtsGroupResultFragment.setArguments(bundle);
			transaction.add(R.id.search_result_content, thoughtsGroupResultFragment).show(thoughtsGroupResultFragment).commit();
			break;
		case SearchPage.THOUGHT_TAG:
			thoughtsTagResultFragment.setArguments(bundle);
			transaction.add(R.id.search_result_content, thoughtsTagResultFragment).show(thoughtsTagResultFragment).commit();
			break;
		case SearchPage.PEOPLE_GROUP:
			peopleGroupResultFragment.setArguments(bundle);
			transaction.add(R.id.search_result_content, peopleGroupResultFragment).show(peopleGroupResultFragment).commit();
			break;
		case SearchPage.PEOPLE_TAG:
			peopleTagResultFragment.setArguments(bundle);
			transaction.add(R.id.search_result_content, peopleTagResultFragment).show(peopleTagResultFragment).commit();
			break;
		case SearchPage.THOUGHT_CATEGORY:
			thoughtsCategoryResultFragment.setArguments(bundle);
			transaction.add(R.id.search_result_content, thoughtsCategoryResultFragment).show(thoughtsCategoryResultFragment).commit();
			break;
		case SearchPage.THOUGHT_KEYWORD:
			thoughtsKeywordResultFragment.setArguments(bundle);
			transaction.add(R.id.search_result_content, thoughtsKeywordResultFragment).show(thoughtsKeywordResultFragment).commit();
			break;
		case SearchPage.PEOPLE_KEYWORD:
			peopleKeywordResultFragment.setArguments(bundle);
			transaction.add(R.id.search_result_content, peopleKeywordResultFragment).show(peopleKeywordResultFragment).commit();
			break;
		default:
			break;
		}
	}
	
}
