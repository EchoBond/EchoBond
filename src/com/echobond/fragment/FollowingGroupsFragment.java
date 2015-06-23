package com.echobond.fragment;

import java.util.ArrayList;

import org.json.JSONObject;

import com.echobond.R;
import com.echobond.application.MyApp;
import com.echobond.dao.GroupDAO;
import com.echobond.entity.Group;
import com.echobond.intf.LoadGroupsCallback;
import com.echobond.util.JSONUtil;
import com.google.gson.reflect.TypeToken;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @author aohuijun
 *
 */
public class FollowingGroupsFragment extends Fragment implements OnClickListener, LoadGroupsCallback, LoaderCallbacks<Cursor> {
	
	private int[] colorBgd = new int[] {0xffffb8b8, 0xffdbc600, 0xffac97ef, 0xff8cd19d, 0xff5cacc4, 0xfff49e40};
	private GridView groups2Follow;
	private FollowingGroupsAdapter adapter;
	
	private int currentLimit;
//	private long lastLoadTime;
	
	private ArrayList<Integer> groupList;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View followingGroupsView = inflater.inflate(R.layout.fragment_following_groups, container, false);
		currentLimit = MyApp.LIMIT_INIT*10;
		groupList = new ArrayList<Integer>();
		groups2Follow = (GridView)followingGroupsView.findViewById(R.id.grid_groups);

		String[] args = new String[]{currentLimit+"", MyApp.DEFAULT_OFFSET+""};
		Cursor cursor = getActivity().getContentResolver().query(GroupDAO.CONTENT_URI, null, null, args, null);
		adapter = new FollowingGroupsAdapter(getActivity(), R.layout.item_following, cursor, 0);
		groups2Follow.setAdapter(adapter);
		groups2Follow.setOverScrollMode(View.OVER_SCROLL_NEVER);
		groups2Follow.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);

		//lastLoadTime = 0;
		
		getLoaderManager().initLoader(MyApp.LOADER_FOLLOW_GROUP, null, this);
		
		return followingGroupsView;
	}
	
	public class FollowingGroupsAdapter extends CursorAdapter {

		private LayoutInflater mInflater;
		private int layout;
		
		public FollowingGroupsAdapter(Context context, int layout, Cursor c, int flags) {
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
			
			TextView groupName = (TextView)convertView.findViewById(R.id.item_following_text);

			groupName.setText(c.getString(c.getColumnIndex("name")));
			groupName.setTag(c.getInt(c.getColumnIndex("_id")));
						
			convertView.setOnClickListener(FollowingGroupsFragment.this);

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
		TextView group = (TextView) v.findViewById(R.id.item_following_text);
		Boolean selected = false;
		if(null != v.getTag()){
			selected = (Boolean) v.getTag();
		}
		Integer id = (Integer) group.getTag();
		if (selected) {
			selection.setImageDrawable(null);
			v.setTag(false);			
			groupList.remove(id);
		} else {
			selection.setImageDrawable(getResources().getDrawable(R.drawable.button_done));
			v.setTag(true);
			groupList.add(id);
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int loader, Bundle arg1) {
		switch(loader){
		case MyApp.LOADER_FOLLOW_GROUP:
			Uri uri = GroupDAO.CONTENT_URI;
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
			
			getActivity().getContentResolver().bulkInsert(GroupDAO.CONTENT_URI, values);
			updateUI();
		} else {
			Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.network_issue), Toast.LENGTH_LONG).show();
		}
	}
	
	private void updateUI(){
		String[] args = new String[]{currentLimit+"", MyApp.DEFAULT_OFFSET+""};
		Cursor cursor = getActivity().getContentResolver().query(GroupDAO.CONTENT_URI, null, null, args, null);
		adapter.swapCursor(cursor);
		adapter.notifyDataSetChanged();
	}

	public ArrayList<Integer> getGroupList() {
		return groupList;
	}

}
