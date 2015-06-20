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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 
 * @author aohuijun
 *
 */
public class ThoughtsListPage extends ActionBarActivity implements IXListViewListener {

	private XListView thoughtsListView;
	private ThoughtListAdapter adapter;
	private TextView titleView;
	private ImageView backButton;
	
	private static final int POST = 0;
	private static final int MESSAGE = 1;
	private static final int BOOST = 2;
	private static final int COMMENT = 3;
	private static final int SHARE = 4;
	
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
		
		backButton = (ImageView)findViewById(R.id.button_left_side);
		backButton.setImageDrawable(getResources().getDrawable(R.drawable.button_back));
		backButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		titleView = (TextView)findViewById(R.id.title_name);
		titleView.setText(getIntent().getStringExtra("userName") + "Thoughts");
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

		private LayoutInflater inflater;
		private int layout;
		
		public ThoughtListAdapter(Context context, int layout, Cursor c, int flags) {
			super(context, c, flags);
			this.layout = layout;
			this.inflater = LayoutInflater.from(context);
		}

		@Override
		public void bindView(View convertView, Context ctx, Cursor c) {
			
			String id = c.getString(c.getColumnIndex("_id"));
			String title = c.getString(c.getColumnIndex("username"));
			String content = c.getString(c.getColumnIndex("content"));
			String path = c.getString(c.getColumnIndex("image"));
			String userId = c.getString(c.getColumnIndex("user_id"));
			int boost = c.getInt(c.getColumnIndex("boost"));
			int cmt = c.getInt(c.getColumnIndex("num_of_cmt"));
			int isUserBoost = c.getInt(c.getColumnIndex("isUserBoost")); 
			
			TextView titleView = (TextView)convertView.findViewById(R.id.thought_list_title);
			TextView contentView = (TextView)convertView.findViewById(R.id.thought_list_content);
			TextView boostsNum = (TextView)convertView.findViewById(R.id.thought_list_boostsnum);
			TextView commentsNum = (TextView)convertView.findViewById(R.id.thought_list_commentsnum);
			TextView thoughtIdView = (TextView)convertView.findViewById(R.id.thought_list_id);
			TextView imagePathView = (TextView)convertView.findViewById(R.id.thought_list_image);
			TextView isUserBoostView = (TextView)convertView.findViewById(R.id.thought_list_isUserBoost);
			TextView userIdView = (TextView)convertView.findViewById(R.id.thought_list_poster_id);
			
			ImageView postFigure = (ImageView)convertView.findViewById(R.id.thought_list_pic);
			ImageView messageButton = (ImageView)convertView.findViewById(R.id.thought_list_message);
			ImageView boostButton = (ImageView)convertView.findViewById(R.id.thought_list_boost);
			ImageView commentButton = (ImageView)convertView.findViewById(R.id.thought_list_comment);
			ImageView shareButton = (ImageView)convertView.findViewById(R.id.thought_list_share);
			
			titleView.setText(title);
			contentView.setText(content);
			boostsNum.setText(boost+"");
			commentsNum.setText(cmt+"");
			thoughtIdView.setText(id);
			imagePathView.setText(path);
			isUserBoostView.setText(isUserBoost+"");
			userIdView.setText(userId);
			
			postFigure.setOnClickListener(null);
			messageButton.setOnClickListener(null);
			boostButton.setOnClickListener(null);
			commentButton.setOnClickListener(null);
			shareButton.setOnClickListener(null);
			
		}

		@Override
		public View newView(Context context, Cursor c, ViewGroup parent) {
			return inflater.inflate(layout, parent, false);
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
