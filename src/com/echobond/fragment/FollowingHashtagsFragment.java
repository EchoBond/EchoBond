package com.echobond.fragment;

import java.util.ArrayList;

import org.json.JSONObject;

import com.echobond.R;
import com.echobond.application.MyApp;
import com.echobond.connector.LoadTagsAsyncTask;
import com.echobond.dao.TagDAO;
import com.echobond.entity.Tag;
import com.echobond.intf.LoadTagsCallback;
import com.echobond.util.HTTPUtil;
import com.echobond.util.JSONUtil;
import com.google.gson.reflect.TypeToken;

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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @author aohuijun
 *
 */
public class FollowingHashtagsFragment extends Fragment implements OnClickListener, LoadTagsCallback, LoaderCallbacks<Cursor>{
	
	private int[] colorBgd = new int[] {0xffffb8b8, 0xffdbc600, 0xffac97ef, 0xff8cd19d, 0xff5cacc4, 0xfff49e40};
	private GridView hashtags2Follow;
	private FollowingTagsAdapter adapter;
	
	private int currentLimit;
	
	private ArrayList<Integer> tagList;
	
//	private long lastLoadTime;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View followingHashtagsView = inflater.inflate(R.layout.fragment_following_hashtags, container, false);
		hashtags2Follow = (GridView)followingHashtagsView.findViewById(R.id.grid_hashtags);
		currentLimit = MyApp.LIMIT_INIT;
		
		tagList = new ArrayList<Integer>();
		
		String[] args = new String[]{currentLimit+"", MyApp.DEFAULT_OFFSET+""};
		Cursor cursor = getActivity().getContentResolver().query(TagDAO.CONTENT_URI_TAG, null, null, args, null);
		adapter = new FollowingTagsAdapter(getActivity(), R.layout.item_following, cursor, 0);
		hashtags2Follow.setAdapter(adapter);
		hashtags2Follow.setOverScrollMode(View.OVER_SCROLL_NEVER);
		hashtags2Follow.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
		
		getLoaderManager().initLoader(MyApp.LOADER_FOLLOW_TAG, null, this);
		
		String url = HTTPUtil.getInstance().composePreURL(getActivity()) + getResources().getString(R.string.url_load_tags);
		new LoadTagsAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url, this, MyApp.DEFAULT_OFFSET, currentLimit);
		
		return followingHashtagsView;
	}
	
	public class FollowingTagsAdapter extends CursorAdapter {

		private LayoutInflater mInflater;
		private int layout;
		
		public FollowingTagsAdapter(Context context, int layout, Cursor c, int flags) {
			super(context, c, flags);
			this.layout = layout;
			this.mInflater = LayoutInflater.from(context);
		}
		
		@Override
		public int getCount() {
			if(null == getCursor()){
				return 0;
			}
			return super.getCount();
		}
		
		@Override
		public void bindView(View convertView, Context ctx, Cursor c) {
			int position = c.getPosition();
			int colorPos = position % (3 * colorBgd.length);
			convertView.setBackgroundColor(colorBgd[colorPos/3]);
			
			TextView tagName = (TextView)convertView.findViewById(R.id.item_following_text);

			tagName.setText(c.getString(c.getColumnIndex("name")));
			tagName.setTag(c.getInt(c.getColumnIndex("_id")));			
			
			convertView.setOnClickListener(FollowingHashtagsFragment.this);

		}
		
		@Override
		public View newView(Context context, Cursor c, ViewGroup parent) {
			if (c.isNull(getCount())) {
				return mInflater.inflate(layout, parent, false);
			} else {
				return null;
			}
		}
		
	}

	@Override
	public void onClick(View v) {
		ImageView selection = (ImageView)v.findViewById(R.id.item_following_selected);
		TextView tag = (TextView) v.findViewById(R.id.item_following_text);
		Boolean selected = false;
		if(null != v.getTag()){
			selected = (Boolean) v.getTag();
		}
		Integer id = (Integer) tag.getTag();
		if (selected) {
			selection.setImageDrawable(null);
			v.setTag(false);			
			tagList.remove(id);
		} else {
			selection.setImageDrawable(getResources().getDrawable(R.drawable.button_done));
			v.setTag(true);
			tagList.add(id);
		}
	}
	@Override
	public Loader<Cursor> onCreateLoader(int loader, Bundle arg1) {
		switch(loader){
		case MyApp.LOADER_FOLLOW_TAG:
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
		} else {
			Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.hint_network_issue), Toast.LENGTH_LONG).show();
		}
	}
	
	private void updateUI(){
		String[] args = new String[]{currentLimit+"", MyApp.DEFAULT_OFFSET+""};
		Cursor cursor = getActivity().getContentResolver().query(TagDAO.CONTENT_URI_TAG, null, null, args, null);
		adapter.swapCursor(cursor);
		adapter.notifyDataSetChanged();
	}
	public ArrayList<Integer> getTagList() {
		return tagList;
	}

}
