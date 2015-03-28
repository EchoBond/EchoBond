package com.echobond.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.echobond.R;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
/**
 * 
 * @author aohuijun
 *
 */
public class FollowingGroupsFragment extends Fragment {
	
	private int[] colorBgd = new int[] {0xffffb8b8, 0xffdbc600, 0xffac97ef, 0xff8cd19d, 0xff5cacc4, 0xfff49e40};
	private String[] testGroups = {"HKU", "CityU", "CUHK", "HKBU", "UST", "PolyU", "LingU", "SYU", "IVE", 
			"HKDI", "HKCC", "Vintage", "名校", "HKMC", "HSBC", "Swire", "RCLee", "Starr", "laoliu", 
			"HSBC", "Swire", "RCLee", "Starr", "laoliu", 
			"HSBC", "Swire", "RCLee", "Starr", "laoliu", 
			"HKU", "CityU", "CUHK", "HKBU", "UST", "PolyU", "LingU", "SYU", "IVE",
			"HKU", "CityU", "CUHK", "HKBU", "UST", "PolyU", "LingU", "SYU", "IVE",
			"HKU", "CityU", "CUHK", "HKBU", "UST", "PolyU", "LingU", "SYU", "IVE"};
	private GridView groups2Follow;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View followingGroupsView = inflater.inflate(R.layout.fragment_following_groups, container, false);
		groups2Follow = (GridView)followingGroupsView.findViewById(R.id.grid_groups);
		mySimpleAdapter adapter = new mySimpleAdapter(this.getActivity(), data(), 
				R.layout.item_following, new String[]{"group"}, new int[]{R.id.text_following});
		groups2Follow.setAdapter(adapter);
		groups2Follow.setOverScrollMode(View.OVER_SCROLL_NEVER);
		groups2Follow.setOnItemClickListener(new groupSelectedListener());

		return followingGroupsView;
	}

	private List<Map<String, Object>> data() {
		List<Map<String, Object>> groupsList = new ArrayList<Map<String,Object>>();
		for (int i = 0; i < testGroups.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("group", testGroups[i]);
			groupsList.add(map);
		}
		return groupsList;
	}
	
	public class mySimpleAdapter extends SimpleAdapter {

		public mySimpleAdapter(Context context,
				List<? extends Map<String, ?>> data, int resource,
				String[] from, int[] to) {
			super(context, data, resource, from, to);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = super.getView(position, convertView, parent);
			int colorPos = position % (3 * colorBgd.length);
			view.setBackgroundColor(colorBgd[colorPos/3]);
			return view;
		}
	}
	
	public class groupSelectedListener implements AdapterView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			view.setSelected(true);
			Toast.makeText(getActivity().getApplicationContext(), "You've clicked position " + position, Toast.LENGTH_SHORT).show();
		}
	}

}
