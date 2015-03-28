package com.echobond.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.echobond.R;
import com.echobond.connector.GroupAsyncTask;
import com.echobond.entity.Group;
import com.echobond.intf.GroupCallback;
import com.echobond.intf.NewPostFragmentsSwitchAsyncTaskCallback;
import com.echobond.util.HTTPUtil;
import com.echobond.util.JSONUtil;
import com.google.gson.reflect.TypeToken;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class NewGroupsFragment extends Fragment implements GroupCallback{
	
	private ArrayList<Group> groups;
	private ListView groupList;
	private NewPostFragmentsSwitchAsyncTaskCallback callback;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View groupsView = inflater.inflate(R.layout.fragment_new_post_groups, container, false);
		groupList = (ListView) groupsView.findViewById(R.id.list_group);
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
			MySimpleAdapter adapter2 = new MySimpleAdapter(getActivity(), listItems, R.layout.item_group, new String[]{"group"}, new int[]{R.id.text_group});
			SimpleAdapter adapter = new SimpleAdapter(getActivity(), listItems, R.layout.item_group, new String[]{"group"}, new int[]{R.id.text_group});
			groupList.setAdapter(adapter2);
			groupList.setOverScrollMode(View.OVER_SCROLL_NEVER);
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
		
		@SuppressLint("InflateParams") @Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = new ViewHolder();
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.item_group, null);
				holder.groupName = (TextView)convertView.findViewById(R.id.text_group);
				convertView.setTag(holder);
			}else {
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
			callback.getGroup(getGroupId(group));
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
}
