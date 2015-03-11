package com.echobond.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.echobond.R;

import android.os.Bundle;
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
	
	private String[] testGroups = {"HKU", "CityU", "CUHK", "HKBU", "UST", "PolyU", "LingU", "SYU", "IVE", 
			"HKDI", "HKCC", "Vintage", "名校", "HKMC", "HSBC", "Swire", "RCLee", "Starr"};
	private GridView groups2Follow;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View followingGroupsView = inflater.inflate(R.layout.fragment_following_groups, container, false);
		groups2Follow = (GridView)followingGroupsView.findViewById(R.id.grid_groups);
		SimpleAdapter adapter = new SimpleAdapter(this.getActivity(), data(), 
				R.layout.item_following, new String[]{"group"}, new int[]{R.id.text_following});
		groups2Follow.setAdapter(adapter);
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
	
	public class groupSelectedListener implements AdapterView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Toast.makeText(getActivity().getApplicationContext(), "Clicked. ", Toast.LENGTH_SHORT).show();
		}
	}
}
