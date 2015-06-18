package com.echobond.fragment;

import com.echobond.R;
import com.echobond.activity.SearchPage;
import com.echobond.intf.ViewMoreSwitchCallback;
import com.echobond.widget.XListView;
import com.echobond.widget.XListView.IXListViewListener;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @author aohuijun
 *
 */
public class MoreGroupsFragment extends Fragment implements IXListViewListener {

	private TextView groupTextView;
	private XListView moreGroupsList;
	private ViewMoreSwitchCallback searchCallback;
	private MoreGroupsCursorAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View moreGroupsView = inflater.inflate(R.layout.fragment_more_groups, container, false);
		
		adapter = new MoreGroupsCursorAdapter(getActivity(), null, 0);
		moreGroupsList = (XListView)moreGroupsView.findViewById(R.id.more_groups_list);
		moreGroupsList.setPullRefreshEnable(false);
		moreGroupsList.setOverScrollMode(View.OVER_SCROLL_NEVER);
		moreGroupsList.setAdapter(adapter);
		moreGroupsList.setXListViewListener(this);
		
		groupTextView = (TextView)moreGroupsView.findViewById(R.id.more_groups_text);
		Bundle bundle = this.getArguments();
		if (bundle != null) {
			groupTextView.setText("View More " + bundle.getString("type"));
			groupTextView.setOnClickListener(new SearchOnClickListener(bundle.getString("type")));
		}
		return moreGroupsView;
	}
	
	public class SearchOnClickListener implements OnClickListener {

		private String type;
		private int index;
		
		public SearchOnClickListener(String typeString) {
			this.type = typeString;
		}

		@Override
		public void onClick(View v) {
			Toast.makeText(getActivity(), "BACK", Toast.LENGTH_SHORT).show();
			if (type == SearchPage.THOUGHTS_MORE_GROUP) {
				index = SearchPage.THOUGHT_GROUP;
			} else if (type == SearchPage.PEOPLE_MORE_GROUP) {
				index = SearchPage.PEOPLE_GROUP;
			}
			searchCallback.onSearchSelected(index);
		}
		
	}
	
	public class MoreGroupsCursorAdapter extends CursorAdapter {

		public MoreGroupsCursorAdapter(Context context, Cursor c, int flags) {
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
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			searchCallback = (ViewMoreSwitchCallback) activity;
		} catch (Exception e) {
			throw new ClassCastException(activity.toString() + "must implement searchCallback in MoreGroupsFragment. ");
		}
	}

	@Override
	public void onRefresh() {
		onRefresh();
	}

	@Override
	public void onLoadMore() {
		//	TEMPORARY USE
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				moreGroupsList.stopLoadMore();
			}
		}, 2000);
	}
	
}
