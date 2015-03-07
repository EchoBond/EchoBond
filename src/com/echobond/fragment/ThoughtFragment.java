package com.echobond.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.echobond.R;
import com.google.android.gms.plus.model.people.Person.Image;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

/**
 * 
 * @author aohuijun
 *
 */
public class ThoughtFragment extends ListFragment implements AdapterView.OnItemClickListener{
	
	private int[] testImages = new int[] {
		R.drawable.temp_1, R.drawable.temp_2, R.drawable.temp_3, R.drawable.temp_4, R.drawable.temp_5, R.drawable.temp_6
	};
	
	private String[] testTitles = {
		"Happy girl:)", "Global citizen", "Sports fanatic", "Japan lover", "Anonymous", "Traveller"
	};
	
	private String[] testContents = {
		"其實係咪讀港大神科既男生都甘悶?最近有個男仔追我但硬係覺得個男仔有d奇怪⋯⋯", 
		"I love Asian culture! Wanna meet some locals here! ", 
		"Anyone wanna hike this weekend? HKU to the Peak! Hit me up:)", 
		"Learning Japanese now. Want to meet some Japanese friends! Anyone who are from Japan or love Japan are welcome! ", 
		"Every Christmas I feel super lonely. Being fat makes me a loner forever.", 
		"I’m Sri Lankan. A guy covered his nose when he saw me! This has happened so many times in HK - an int’l city!!"	
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View thoughtView = inflater.inflate(R.layout.main_thoughts, container, false);
		return thoughtView;
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		
		final String[] from = new String[] {"pic", "title", "content"};
		final int[] to = new int[] {R.id.thought_list_pic, R.id.thought_list_title, R.id.thought_list_content};
		super.onActivityCreated(savedInstanceState);
		SimpleAdapter adapter = new SimpleAdapter(this.getActivity(), getSimpleData(), R.layout.item_thoughts_list, from, to);
		this.setListAdapter(adapter);
		getListView().setOnItemClickListener(this);
	}

	private List<Map<String, Object>> getSimpleData() {

		List<Map<String, Object>> thoughtList = new ArrayList<Map<String,Object>>();
		for (int i = 0; i < testTitles.length; i++) {
			Map<String, Object> thoughtMap = new HashMap<String, Object>();
			thoughtMap.put("pic", testImages[i]);
			thoughtMap.put("title", testTitles[i]);
			thoughtMap.put("content", testContents[i]);
			thoughtList.add(thoughtMap);
		}
		return thoughtList;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Toast.makeText(getActivity(), "You've clicked me & you are a Somebody-Brilliant. ", Toast.LENGTH_SHORT).show();
	}
}
