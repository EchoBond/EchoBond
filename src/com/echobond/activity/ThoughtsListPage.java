package com.echobond.activity;

import com.echobond.R;
import com.echobond.widget.XListView;
import com.echobond.widget.XListView.IXListViewListener;

import android.app.ActionBar;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
/**
 * 
 * @author aohuijun
 *
 */
public class ThoughtsListPage extends ActionBarActivity implements IXListViewListener {

	private XListView thoughtsListView;
	private ThoughtListAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_thoughts_list_page);
		initActionBar();
		initThoughtsList();
	}

	private void initActionBar() {
		Toolbar thoughtsListToolbar = (Toolbar)findViewById(R.id.toolbar_thoughts_list);
		setSupportActionBar(thoughtsListToolbar);
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.title_bar_setting);
	}

	private void initThoughtsList() {
		adapter = new ThoughtListAdapter(this, R.layout.item_thought, null, 0);
		thoughtsListView = (XListView)findViewById(R.id.thoughts_list);
		thoughtsListView.setAdapter(adapter);
		thoughtsListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
		thoughtsListView.setPullRefreshEnable(false);
		thoughtsListView.setXListViewListener(this);
	}
	
	public class ThoughtListAdapter extends CursorAdapter {

		public ThoughtListAdapter(Context context, int layout, Cursor c, int flags) {
			super(context, c, flags);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void bindView(View convertView, Context ctx, Cursor c) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public View newView(Context context, Cursor c, ViewGroup parent) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

	@Override
	public void onRefresh() {
		onRefresh();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		
	}
}
