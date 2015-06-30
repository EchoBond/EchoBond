package com.echobond.fragment;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.R;
import com.echobond.activity.SearchPage;
import com.echobond.application.MyApp;
import com.echobond.connector.LoadTagsAsyncTask;
import com.echobond.dao.TagDAO;
import com.echobond.entity.Tag;
import com.echobond.intf.LoadTagsCallback;
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
public class MoreTagsFragment extends Fragment implements OnClickListener, IXListViewListener, LoaderCallbacks<Cursor>, LoadTagsCallback {

	private XListView moreTagsList;
	private ViewMoreSwitchCallback searchCallback;
	private MoreTagsAdapter adapter;
	private TextView tagTextView;
	private ImageView buttonDone;
	
	private String type;
	private int mode;
	
	private int currentLimit;
	private long lastLoadTime;
	
	private ArrayList<Integer> tagIds;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View moreTagsView = inflater.inflate(R.layout.fragment_more_tags, container, false);
		
		currentLimit = MyApp.LIMIT_INIT;
		
		tagIds = new ArrayList<Integer>();
		
		adapter = new MoreTagsAdapter(getActivity(), R.layout.item_tag, null, 0);
		moreTagsList = (XListView)moreTagsView.findViewById(R.id.more_tags_list);
		moreTagsList.setPullRefreshEnable(false);
		moreTagsList.setPullLoadEnable(true);
		moreTagsList.setOverScrollMode(View.OVER_SCROLL_NEVER);
		moreTagsList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		moreTagsList.setAdapter(adapter);
		moreTagsList.setXListViewListener(this);
		moreTagsList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				TextView item = (TextView)v.findViewById(R.id.text_tag);
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
		
		tagTextView = (TextView)moreTagsView.findViewById(R.id.more_tags_text);
		buttonDone = (ImageView)moreTagsView.findViewById(R.id.more_tags_done);
		
		Bundle bundle = this.getArguments();
		if (bundle != null) {
			type = bundle.getString("type");
			mode = bundle.getInt("mode");
			if (mode == MyApp.VIEW_MORE_SEARCH) {
				tagTextView.setText("View More " + type);
			} else {
				tagTextView.setVisibility(View.GONE);
				buttonDone.setVisibility(View.GONE);
			}
		}
		buttonDone.setOnClickListener(this);
		getLoaderManager().initLoader(MyApp.LOADER_TAG, null, this);
		
		return moreTagsView;
	}

	/* only for visible buttonDone i.e. for fragment switch */
	/* sticky to SearchPage */
	@Override
	public void onClick(View v) {
		int index = 0;
		if (type == SearchPage.THOUGHTS_MORE_TAG) {
			index = SearchPage.THOUGHT_TAG;
		} else if (type == SearchPage.PEOPLE_MORE_TAG) {
			index = SearchPage.PEOPLE_TAG;
		}
		JSONObject data = new JSONObject();
		try {
			data.put("index", index);
			data.put("id", 0);
			data.put("idList", JSONUtil.fromListToJSONArray(tagIds, new TypeToken<ArrayList<Integer>>(){}));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		searchCallback.onSearchSelected(data);		
	}
	
	public ArrayList<Integer> getTagIds() {
		return tagIds;
	}

	public class MoreTagsAdapter extends CursorAdapter {

		private LayoutInflater inflater;
		private int layout;
		private SparseBooleanArray selectionArray = new SparseBooleanArray();
		
		public MoreTagsAdapter(Context context, int layout, Cursor c, int flags) {
			super(context, c, flags);
			this.layout = layout;
			this.inflater = LayoutInflater.from(context);
		}

		public void setSelected(int position, boolean isSelected) {
			selectionArray.put(position, isSelected);
		}
		
		@Override
		public void bindView(View convertView, Context ctx, Cursor c) {
			String name = c.getString(c.getColumnIndex("name"));
			Integer id = c.getInt(c.getColumnIndex("_id"));
			
			TextView item = (TextView)convertView.findViewById(R.id.text_tag);
			item.setText(name);
			item.setTag(id);
			
			int position = c.getPosition();
			boolean isSelected = selectionArray.get(position);
			if (isSelected) {
				item.setTextColor(Color.parseColor("#5CACC4"));
			} else if (!isSelected) {
				item.setTextColor(Color.BLACK);
			}
		}

		@Override
		public View newView(Context context, Cursor c, ViewGroup parent) {
			if (c.isNull(getCount())) {
				View view = inflater.inflate(layout, parent, false);
				return view;
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
			throw new ClassCastException(activity.toString() + "must implement searchCallback in MoreTagsFragment. ");
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
			new LoadTagsAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 
					HTTPUtil.getInstance().composePreURL(getActivity()) + getResources().getString(R.string.url_load_tags), 
					this, MyApp.DEFAULT_OFFSET, currentLimit);
		} else {
			onLoadFinished();
		}
	}
	
	private void onLoadFinished() {
		moreTagsList.stopLoadMore();
		moreTagsList.stopRefresh();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onLoadTagsResult(JSONObject result) {
		if(null != result){
			TypeToken<ArrayList<Tag>> token = new TypeToken<ArrayList<Tag>>(){};
			ArrayList<Tag> tags = null;
			tags = (ArrayList<Tag>) JSONUtil.fromJSONToList(result, "tags", token);
			ContentValues[] values = new ContentValues[tags.size()];
			int i = 0;
			for (Tag tag : tags) {
				values[i++] = tag.putValues();
			}
			getActivity().getContentResolver().bulkInsert(TagDAO.CONTENT_URI_TAG, values);
			updateUI();
			onLoadFinished();
		} else {
			onLoadFinished();
			Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.network_issue), Toast.LENGTH_LONG).show();
		}
	}
	
	private void updateUI() {
		String[] args = new String[]{currentLimit+"", MyApp.DEFAULT_OFFSET+""};
		Cursor cursor = getActivity().getContentResolver().query(TagDAO.CONTENT_URI_TAG, null, null, args, null);
		adapter.swapCursor(cursor);
		adapter.notifyDataSetChanged();
	}

	@Override
	public Loader<Cursor> onCreateLoader(int loader, Bundle arg1) {
		switch(loader){
		case MyApp.LOADER_TAG:
			Uri uri = TagDAO.CONTENT_URI_TAG;
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
	
	
}
