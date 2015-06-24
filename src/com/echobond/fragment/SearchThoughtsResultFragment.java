package com.echobond.fragment;

import com.echobond.R;
import com.echobond.widget.XListView;
import com.echobond.widget.XListView.IXListViewListener;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
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
public class SearchThoughtsResultFragment extends Fragment implements IXListViewListener {
	
	private XListView searchThoughtsResultList;
	private ThoughtsResultAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View searchThoughtsResultView = inflater.inflate(R.layout.fragment_search_result_thoughts, container, false);
		adapter = new ThoughtsResultAdapter(getParentFragment().getActivity(), R.layout.item_thought, null, 0);
		searchThoughtsResultList = (XListView)searchThoughtsResultView.findViewById(R.id.search_result_list_thoughts);
		searchThoughtsResultList.setOverScrollMode(View.OVER_SCROLL_NEVER);
		searchThoughtsResultList.setPullRefreshEnable(false);
		searchThoughtsResultList.setAdapter(adapter);
		
		Bundle bundle = this.getArguments();
		bundle.getInt("type");
		return searchThoughtsResultView;
	}
	
	
	public class ThoughtsResultAdapter extends CursorAdapter {

		private LayoutInflater inflater;
		private int layout;
		
		public ThoughtsResultAdapter(Context context, int layout, Cursor c, int flags) {
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
			TextView imagePathView = (TextView) convertView.findViewById(R.id.thought_list_image);
			TextView isUserBoostView = (TextView) convertView.findViewById(R.id.thought_list_isUserBoost);
			TextView userIdView = (TextView) convertView.findViewById(R.id.thought_list_poster_id);
			
			titleView.setText(title);
			contentView.setText(content);
			boostsNum.setText(boost+"");
			commentsNum.setText(cmt+"");
			thoughtIdView.setText(id);
			imagePathView.setText(path);
			isUserBoostView.setText(isUserBoost+"");
			userIdView.setText(userId);
			
			ImageView postFigure = (ImageView)convertView.findViewById(R.id.thought_list_pic);
			ImageView messageButton = (ImageView)convertView.findViewById(R.id.thought_list_message);
			ImageView boostButton = (ImageView)convertView.findViewById(R.id.thought_list_boost);
			ImageView commentButton = (ImageView)convertView.findViewById(R.id.thought_list_comment);
			ImageView shareButton = (ImageView)convertView.findViewById(R.id.thought_list_share);
			
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
