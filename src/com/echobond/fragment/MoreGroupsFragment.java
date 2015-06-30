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
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @author aohuijun
 *
 */
public class MoreGroupsFragment extends Fragment implements OnClickListener, IXListViewListener, LoaderCallbacks<Cursor>, LoadGroupsCallback {

	private XListView moreGroupsList;
	private ViewMoreSwitchCallback searchCallback;
	private MoreGroupsCursorAdapter adapter;
	private String type;
	private int mode;
	private TextView groupTextView;
	private ImageView groupView;
	

	
	private int currentLimit;
	private long lastLoadTime;
	
	private ArrayList<Integer> tagIds;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View moreGroupsView = inflater.inflate(R.layout.fragment_more_groups, container, false);
		
		currentLimit = MyApp.LIMIT_INIT;
		
		tagIds = new ArrayList<Integer>();
		
		adapter = new MoreGroupsCursorAdapter(getActivity(), R.layout.item_group, null, 0);
		moreGroupsList = (XListView)moreGroupsView.findViewById(R.id.more_groups_list);
		moreGroupsList.setPullRefreshEnable(false);
		moreGroupsList.setPullLoadEnable(true);
		moreGroupsList.setOverScrollMode(View.OVER_SCROLL_NEVER);
		moreGroupsList.setAdapter(adapter);
		moreGroupsList.setXListViewListener(this);
		
		groupView = (ImageView)moreGroupsView.findViewById(R.id.more_groups_view);
		groupTextView = (TextView)moreGroupsView.findViewById(R.id.more_groups_text);
		Bundle bundle = this.getArguments();
		if (bundle != null) {
			type = bundle.getString("type");
			mode = bundle.getInt("mode");
			switch(mode){
			case MyApp.VIEW_MORE_SEARCH:
				groupView.setVisibility(View.GONE);
				groupTextView.setText("View More " + type);
				break;
			case MyApp.VIEW_MORE_PROFILE:
				groupView.setVisibility(View.GONE);
				groupTextView.setVisibility(View.GONE);
				moreGroupsList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
				moreGroupsList.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View v, int position,
							long id) {
						TextView item = (TextView)v.findViewById(R.id.text_group);
						if (item.isSelected()) {
							item.setSelected(false);
							adapter.setSelected(position-1, false);
							tagIds.remove(item.getTag());
						} else if (!item.isSelected()) {
							item.setSelected(true);
							adapter.setSelected(position-1, true);
							tagIds.add((Integer) item.getTag());
						}
						adapter.notifyDataSetChanged();
					}
				});
				break;
			case MyApp.VIEW_MORE_POST:
				groupView.setImageDrawable(getResources().getDrawable(R.drawable.thoughts_group_logoview));
				groupTextView.setTextColor(Color.parseColor("#8980DA"));
				groupTextView.setText(getString(R.string.title_new_post_grouptitle));
				break;
			default:
				break;
			}
		}
		getLoaderManager().initLoader(MyApp.LOADER_GROUP, null, this);
		return moreGroupsView;
	}
	

	/* only for fragment switch */
	/* sticky to SearchPage */
	@Override
	public void onClick(View v) {
		if(mode != MyApp.VIEW_MORE_PROFILE){
			int index = 0;
			int id = (Integer) v.getTag();
			if (type == SearchPage.THOUGHTS_MORE_GROUP) {
				index = SearchPage.THOUGHT_GROUP;
			} else if (type == SearchPage.PEOPLE_MORE_GROUP) {
				index = SearchPage.PEOPLE_GROUP;
			}
			JSONObject data = new JSONObject();
			try {
				data.put("index", index);
				data.put("id", id);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			searchCallback.onSearchSelected(data);
		}
	}
		
	public ArrayList<Integer> getTagIds() {
		return tagIds;
	}

	public class MoreGroupsCursorAdapter extends CursorAdapter {

		private LayoutInflater inflater;
		private int layout;
		private SparseBooleanArray selectionArray = new SparseBooleanArray();		
		
		public MoreGroupsCursorAdapter(Context context, int layout, Cursor c, int flags) {
			super(context, c, flags);
			this.layout = layout;
			this.inflater = LayoutInflater.from(context);
		}

		public void setSelected(int position, boolean isSelected) {
			selectionArray.put(position, isSelected);
		}
		
		@Override
		public void bindView(View convertView, Context ctx, Cursor c) {
			String name = c.getString(c.getColumnIndexOrThrow("name"));
			Integer id = c.getInt(c.getColumnIndexOrThrow("_id"));
			
			TextView item = (TextView) convertView.findViewById(R.id.text_group);
			
			item.setText(name);
			item.setTag(id);
			
			int position = c.getPosition();
			boolean isSelected = selectionArray.get(position);
			if (isSelected) {
				item.setTextColor(Color.parseColor("#5CACC4"));
			} else if (!isSelected) {
				item.setTextColor(Color.BLACK);
			}
			
			if(mode != MyApp.VIEW_MORE_PROFILE){
				item.setOnClickListener(MoreGroupsFragment.this);
			}
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
