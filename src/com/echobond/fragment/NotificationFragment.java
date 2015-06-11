package com.echobond.fragment;

import com.echobond.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/**
 * 
 * @author aohuijun
 *
 */
public class NotificationFragment extends Fragment {
	
	private FragmentTabHost tabHost;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
//		View notificationView = inflater.inflate(R.layout.fragment_main_notification, container, false);
//		tabHost = (FragmentTabHost)notificationView.findViewById(android.R.id.tabhost);
		tabHost = new FragmentTabHost(getActivity());
		inflater.inflate(R.layout.fragment_main_notification, tabHost);
		tabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);

		Bundle b1 = new Bundle(), b2 = new Bundle();
		b1.putInt("Tab1", 1);
		b2.putInt("Tab2", 2);
		tabHost.addTab(tabHost.newTabSpec("notification").setIndicator("Notification"), NotificationChildFragment.class, b1);
		tabHost.addTab(tabHost.newTabSpec("messages").setIndicator("Messages"), MessageChildFragment.class, b2);
		
		return tabHost;
	}
}
