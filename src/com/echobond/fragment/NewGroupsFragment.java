package com.echobond.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.echobond.R;
import com.echobond.activity.NewPostPage;
import com.echobond.activity.ViewMorePage;
import com.echobond.application.MyApp;
import com.echobond.connector.GroupAsyncTask;
import com.echobond.entity.Group;
import com.echobond.intf.GroupCallback;
import com.echobond.intf.NewPostFragmentsSwitchAsyncTaskCallback;
import com.echobond.util.HTTPUtil;
import com.echobond.util.JSONUtil;
import com.echobond.widget.XListView;
import com.echobond.widget.XListView.IXListViewListener;
import com.google.gson.reflect.TypeToken;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @author aohuijun
 *
 */
public class NewGroupsFragment extends Fragment implements GroupCallback, IXListViewListener {
	
	private ArrayList<Group> groups;
	private XListView groupList;
	private ImageView moreGroupsView;
	private NewPostFragmentsSwitchAsyncTaskCallback callback;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View groupsView = inflater.inflate(R.layout.fragment_new_post_groups, container, false);
		moreGroupsView = (ImageView)groupsView.findViewById(R.id.thoughts_group_search);
		moreGroupsView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Toast.makeText(getActivity(), "More Groups CLICKED", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				intent.putExtra("title", NewPostPage.GROUP);
				intent.putExtra("mode", MyApp.VIEW_MORE_POST);
				intent.setClass(getActivity(), ViewMorePage.class);
				startActivity(intent);
			}
		});
		
		groupList = (XListView)groupsView.findViewById(R.id.list_group);
		groupList.setOverScrollMode(View.OVER_SCROLL_NEVER);
		groupList.setPullRefreshEnable(false);
		new GroupAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,GroupAsyncTask.GROUP_LOAD_ALL, 
				HTTPUtil.getInstance().composePreURL(getActivity())+getResources().getString(R.string.url_load_groups),
				this);

		return groupsView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			callback = (NewPostFragmentsSwitchAsyncTaskCallback) activity;
		} catch (Exception e) {
			throw new ClassCastException(activity.toString() + "must implement GroupInterface. ");
		}
	}

	
	@SuppressWarnings("unchecked")
	public void onGroupResult(JSONObject result) {
		if(null == result){
			Toast.makeText(getActivity().getApplicationContext(), "Failed loading groups", Toast.LENGTH_LONG).show();
		} else {
			TypeToken<ArrayList<Group>> token = new TypeToken<ArrayList<Group>>(){};
			groups = (ArrayList<Group>) JSONUtil.fromJSONToList(result, "groups", token);
			ArrayList<HashMap<String, Object>> listItems = new ArrayList<HashMap<String, Object>>();
			for(int i = 0; i < groups.size(); i++){
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("group", groups.get(i).getName());
				listItems.add(map);
			}
			MySimpleAdapter adapter = new MySimpleAdapter(getActivity(), listItems, R.layout.item_group, 
					new String[]{"group"}, new int[]{R.id.text_group});
			groupList.setAdapter(adapter);
			groupList.setOnItemClickListener(new GroupItemClickListener());
		}
	}
	
	public class MySimpleAdapter extends SimpleAdapter {

		private LayoutInflater mInflater;
		
		public MySimpleAdapter(Context context,
				List<? extends Map<String, ?>> data, int resource,
				String[] from, int[] to) {
			super(context, data, resource, from, to);
			this.mInflater = LayoutInflater.from(context);
		}
		
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return super.getItem(position);
		}
		
		@SuppressLint("InflateParams") 
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = new ViewHolder();
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.item_group, null);
				holder.groupName = (TextView)convertView.findViewById(R.id.text_group);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder)convertView.getTag();
			}
			holder.groupName.setText(groups.get(position).getName());
			return convertView;
		}
		
		class ViewHolder {
			TextView groupName;
		}
	}
	
	public class GroupItemClickListener implements AdapterView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			view.setSelected(true);
			String group = ((TextView)view.findViewById(R.id.text_group)).getText().toString();
			callback.selectGroup(getGroupId(group));
		}
	}
	
	private int getGroupId(String name){
		int pos = -1;
		for (Group group : groups) {
			if(group.getName().equals(name))
				pos = group.getId();
		}
		return pos;
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
				groupList.stopLoadMore();
			}
		}, 2000);
	}
}
