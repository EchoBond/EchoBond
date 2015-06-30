package com.echobond.fragment;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.R;
import com.echobond.activity.SearchPage;
import com.echobond.application.MyApp;
import com.echobond.connector.LoadGroupsAsyncTask;
import com.echobond.dao.GroupDAO;
import com.echobond.entity.Group;
import com.echobond.intf.LoadGroupsCallback;
import com.echobond.intf.ViewMoreSwitchCallback;
import com.echobond.util.HTTPUtil;
import com.echobond.util.JSONUtil;
import com.echobond.widget.XListView;
import com.echobond.widget.XListView.IXListViewListener;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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
public class MoreGroupsFragment extends Fragment implements IXListViewListener, LoaderCallbacks<Cursor>, LoadGroupsCallback{

	private XListView moreGroupsList;
	private ViewMoreSwitchCallback searchCallback;
	private MoreGroupsCursorAdapter adapter;
	private String type;
	private int mode;
	private TextView groupTextView;
	
	private int currentLimit;
	private long lastLoadTime;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View moreGroupsView = inflater.inflate(R.layout.fragment_more_groups, container, false);
		
		currentLimit = MyApp.LIMIT_INIT;
		
		adapter = new MoreGroupsCursorAdapter(getActivity(), R.layout.item_group, null, 0);
		moreGroupsList = (XListView)moreGroupsView.findViewById(R.id.more_groups_list);
		moreGroupsList.setPullRefreshEnable(false);
		moreGroupsList.setPullLoadEnable(true);
		moreGroupsList.setOverScrollMode(View.OVER_SCROLL_NEVER);
		moreGroupsList.setAdapter(adapter);
		moreGroupsList.setXListViewListener(this);
		
		groupTextView = (TextView)moreGroupsView.findViewById(R.id.more_groups_text);
		Bundle bundle = this.getArguments();
		if (bundle != null) {
			type = bundle.getString("type");
			mode = bundle.getInt("mode");
			if (mode == SearchPage.IN_SEARCH) {
				groupTextView.setText("View More " + type);
			} else {
				groupTextView.setVisibility(View.GONE);
			}
		}
		getLoaderManager().initLoader(MyApp.LOADER_GROUP, null, this);
		return moreGroupsView;
	}
	
	public class SearchOnClickListener implements OnClickListener {

		private String type;
		private int index;
		private int id;
		
		public SearchOnClickListener(int id, String typeString) {
			this.id = id;
			this.type = typeString;
		}

		@Override
		public void onClick(View v) {
			if (type == SearchPage.THOUGHTS_MORE_GROUP) {
				index = SearchPage.THOUGHT_GROUP;
			} else if (type == SearchPage.PEOPLE_MORE_GROUP) {
				index = SearchPage.PEOPLE_GROUP;
			}
			JSONObject jso = new JSONObject();
			try {
				jso.put("index", index);
				jso.put("id", id);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			searchCallback.onSearchSelected(jso);
		}
		
	}
	
	public class MoreGroupsCursorAdapter extends CursorAdapter {

		private LayoutInflater inflater;
		private int layout;
		
		public MoreGroupsCursorAdapter(Context context, int layout, Cursor c, int flags) {
			super(context, c, flags);
			this.layout = layout;
			this.inflater = LayoutInflater.from(context);
		}

		@Override
		public void bindView(View convertView, Context ctx, Cursor c) {
			String name = c.getString(c.getColumnIndexOrThrow("name"));
			Integer id = c.getInt(c.getColumnIndexOrThrow("_id"));
			
			TextView item = (TextView) convertView.findViewById(R.id.text_group);
			
			item.setText(name);
			item.setTag(id);
			
			item.setOnClickListener(new SearchOnClickListener(id, type));
		}

		@Override
		public View newView(Context context, Cursor c, ViewGroup parent) {
			if (c.isNull(getCount())) {
				return inflater.inflate(layout, parent, false);
			} else {
				return null;
			}
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
		onLoadMore();
	}

	@Override
	public void onLoadMore() {
		if(System.currentTimeMillis() - lastLoadTime > MyApp.LOAD_INTERVAL){
			lastLoadTime = System.currentTimeMillis();
			currentLimit += MyApp.LIMIT_INCREMENT;
			new LoadGroupsAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 
					HTTPUtil.getInstance().composePreURL(getActivity()) + getResources().getString(R.string.url_load_groups), 
					this, MyApp.DEFAULT_OFFSET, currentLimit);
		} else {
			onLoadFinished();
		}
	}
	
	private void onLoadFinished(){
		moreGroupsList.stopLoadMore();
		moreGroupsList.stopRefresh();
	}

	@Override
	public Loader<Cursor> onCreateLoader(int loader, Bundle arg1) {
		switch(loader){
		case MyApp.LOADER_GROUP:
			Uri uri = GroupDAO.CONTENT_URI_GROUP;
			String[] args = new String[]{currentLimit+"", MyApp.DEFAULT_OFFSET+""};
			return new CursorLoader(getActivity(), uri, null, null, args, null);
		}
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor c) {
		adapter.swapCursor(c);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		adapter.swapCursor(null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onLoadGroupsResult(JSONObject result) {
		if(null != result){
			TypeToken<ArrayList<Group>> token = new TypeToken<ArrayList<Group>>(){};
			ArrayList<Group> groups = null;
			groups = (ArrayList<Group>) JSONUtil.fromJSONToList(result, "groups", token);
			ContentValues[] values = new ContentValues[groups.size()];
			int i = 0;
			for (Group group : groups) {
				values[i++] = group.putValues();
			}
			
			getActivity().getContentResolver().bulkInsert(GroupDAO.CONTENT_URI_GROUP, values);
			updateUI();
			onLoadFinished();
		} else {
			onLoadFinished();
			Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.network_issue), Toast.LENGTH_LONG).show();
		}
	}
	
	private void updateUI(){
		String[] args = new String[]{currentLimit+"", MyApp.DEFAULT_OFFSET+""};
		Cursor cursor = getActivity().getContentResolver().query(GroupDAO.CONTENT_URI_GROUP, null, null, args, null);
		adapter.swapCursor(cursor);
		adapter.notifyDataSetChanged();
	}
	
}
