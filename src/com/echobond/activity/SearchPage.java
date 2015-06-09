package com.echobond.activity;

import java.util.ArrayList;

import com.echobond.R;
import com.echobond.connector.CategoryAsyncTask;
import com.echobond.entity.Category;
import com.echobond.util.HTTPUtil;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
/**
 * 
 * @author aohuijun
 *
 */
public class SearchPage extends ActionBarActivity {
	
	private ImageView backButton;
	private EditText searchBar;
	private ArrayList<Drawable> cgList;
	private ArrayList<Category> categories;
	private ListView categoryListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_page);
		initActionBar();
		initCategoriesList();
		initGroupsList();
		initHashtagsList();
	}

	private void initActionBar() {
		Toolbar searchToolbar = (Toolbar)findViewById(R.id.toolbar_search);
		setSupportActionBar(searchToolbar);
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.title_bar_search);
		
		searchBar = (EditText)findViewById(R.id.searchBar);
		backButton = (ImageView)findViewById(R.id.searchBackBtn);
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
	
	private void initCategoriesList() {
		cgList = new ArrayList<Drawable>();
		cgList.add(getResources().getDrawable(R.drawable.corners_bg_blue));
		cgList.add(getResources().getDrawable(R.drawable.corners_bg_red));
		cgList.add(getResources().getDrawable(R.drawable.corners_bg_orange));
		cgList.add(getResources().getDrawable(R.drawable.corners_bg_green));
		cgList.add(getResources().getDrawable(R.drawable.corners_bg_yellow));
		cgList.add(getResources().getDrawable(R.drawable.corners_bg_cyan));
		
		categoryListView = (ListView)findViewById(R.id.search_page_category);
		categoryListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
//		new CategoryAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, CategoryAsyncTask.CATEGORY_LOAD, 
//				HTTPUtil.getInstance().composePreURL(this) + getResources().getString(R.string.url_load_categories), this);
	}

	private void initGroupsList() {
		// TODO Auto-generated method stub
		
	}

	private void initHashtagsList() {
		// TODO Auto-generated method stub
		
	}
	
}
