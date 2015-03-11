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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
/**
 * 
 * @author aohuijun
 *
 */
public class FollowingHashtagsFragment extends Fragment {
	
	private String[] testHashTags = {"Inspiring", "ChitChat", "Hip", "France", "India", "Hong Kong", "Honkey", 
			"Football", "Dragon Boat", "Nerd", "Punk", "Vintage", "Politics", "Equality", "LGBT", "Fun", "Philo", "Random"};
	private GridView hashtags2Follow;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View followingHashtagsView = inflater.inflate(R.layout.fragment_following_hashtags, container, false);
		hashtags2Follow = (GridView)followingHashtagsView.findViewById(R.id.grid_hashtags);
		SimpleAdapter adapter = new SimpleAdapter(getActivity(), data(), 
				R.layout.item_following, new String[]{"hashtag"}, new int[]{R.id.text_following});
		hashtags2Follow.setAdapter(adapter);
		hashtags2Follow.setOnItemClickListener(new hashtagSelectedListener());
		return followingHashtagsView;
	}
	private List<Map<String, Object>> data() {
		List<Map<String, Object>> hashtagsList = new ArrayList<Map<String,Object>>();
		for (int i = 0; i < testHashTags.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("hashtag", testHashTags[i]);
			hashtagsList.add(map);
		}
		return hashtagsList;
	}
	
	public class hashtagSelectedListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Toast.makeText(getActivity().getApplicationContext(), "Clicked. ", Toast.LENGTH_SHORT).show();
		}
		
	}
}
