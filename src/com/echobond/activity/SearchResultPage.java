package com.echobond.activity;

import java.util.ArrayList;

import com.echobond.R;
import com.echobond.fragment.SearchPeopleResultFragment;
import com.echobond.fragment.SearchThoughtsResultFragment;

import android.app.ActionBar;
import android.os.Bundle;
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
	private SearchPeopleResultFragment peopleGroupResultFragment;
	private SearchPeopleResultFragment peopleTagResultFragment;
	
	private int searchType = -1;
	private int searchID = 0;
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
		searchIDList = getIntent().getIntegerArrayListExtra("idList");
	}
	
	private void initToolBar() {
		Toolbar searchResultToolbar = (Toolbar)findViewById(R.id.toolbar_search_result);
		setSupportActionBar(searchResultToolbar);
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.title_bar_setting);
		
		titleView = (TextView)findViewById(R.id.title_name);
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
		if (thoughtsGroupResultFragment == null || thoughtsTagResultFragment == null || peopleGroupResultFragment == null || peopleTagResultFragment == null) {
			thoughtsGroupResultFragment = new SearchThoughtsResultFragment();
			thoughtsTagResultFragment = new SearchThoughtsResultFragment();
			peopleGroupResultFragment = new SearchPeopleResultFragment();
			peopleTagResultFragment = new SearchPeopleResultFragment();
		}
		switch (searchType) {
		case SearchPage.THOUGHT_GROUP:
			
			break;
		case SearchPage.THOUGHT_TAG:
			
			break;
		case SearchPage.PEOPLE_GROUP:
			
			break;
		case SearchPage.PEOPLE_TAG:
			
			break;
		case SearchPage.THOUGHT_CATEGORY:
			
			break;
		default:
			break;
		}
	}
	
}
