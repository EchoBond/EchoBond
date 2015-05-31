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
public class NotificationChildFragment extends Fragment {
	
	private XListView notificationList;
	private String[] testStrings = new String[50];
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View notificationChildView = inflater.inflate(R.layout.fragment_child_notification, container, false);
		for (int i = 0; i < 30; i++) {
			testStrings[i] = "Notification " + i;
		}
		notificationList = (XListView)notificationChildView.findViewById(R.id.list_notification);
		SimpleAdapter mAdapter = new SimpleAdapter(getActivity(), data(), R.layout.item_following, new String[]{"ntc"}, new int[]{R.id.text_following});
		notificationList.setAdapter(mAdapter);
		notificationList.setOnItemClickListener(new ClickListener());
		
		return notificationChildView;
	}

	private List<Map<String, Object>> data() {
		List<Map<String, Object>> ntcList = new ArrayList<Map<String,Object>>();
		for (int i = 0; i < 30; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("ntc", testStrings[i]);
			ntcList.add(map);
		}
		return ntcList;
	}
	
	public class ClickListener implements AdapterView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Toast.makeText(getActivity().getApplicationContext(), "Notification " + position, Toast.LENGTH_SHORT).show();
		}
		
	}
}
