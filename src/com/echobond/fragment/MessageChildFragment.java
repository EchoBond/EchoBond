package com.echobond.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.echobond.R;
import com.echobond.widget.XListView;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
public class MessageChildFragment extends Fragment {
	
	private XListView messagesList;
	private String[] testStrings = new String[50];
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View messageChildView = inflater.inflate(R.layout.fragment_child_message, container, false);
		for (int i = 0; i < 30; i++) {
			testStrings[i] = "message " + i;
		}
		messagesList = (XListView)messageChildView.findViewById(R.id.list_messages);
		SimpleAdapter mAdapter = new SimpleAdapter(getActivity(), data(), R.layout.item_following, new String[]{"msg"}, new int[]{R.id.text_following});
		messagesList.setAdapter(mAdapter);
		messagesList.setOnItemClickListener(new ClickListener());
		
		return messageChildView;
	}
	private List<Map<String, Object>> data() {
		List<Map<String, Object>> ntcList = new ArrayList<Map<String,Object>>();
		for (int i = 0; i < 30; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("msg", testStrings[i]);
			ntcList.add(map);
		}
		return ntcList;
	}
	
	public class ClickListener implements AdapterView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Toast.makeText(getActivity().getApplicationContext(), "Message " + position, Toast.LENGTH_SHORT).show();
		}
	}
}