package com.echobond.fragment;

import com.echobond.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 
 * @author aohuijun
 *
 */
public class ProfileFragment extends Fragment {
	
	private ImageView profileFigureView;
	private TextView profileTitle, profileBio;
	private FragmentTabHost tabHost;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View profileView = inflater.inflate(R.layout.fragment_main_profile, container, false);
		profileFigureView = (ImageView)profileView.findViewById(R.id.profile_pic);
		profileTitle = (TextView)profileView.findViewById(R.id.profile_title);
		profileBio = (TextView)profileView.findViewById(R.id.profile_content);
		
		profileFigureView.setImageResource(R.drawable.default_avatar);
		profileTitle.setText(getResources().getString(R.string.app_name));
		profileBio.setText(getResources().getString(R.string.hello_world));
		
		tabHost = (FragmentTabHost)profileView.findViewById(R.id.profile_tabhost);
		tabHost.setup(getActivity(), getChildFragmentManager(), R.id.profile_frame);

		Bundle b1 = new Bundle(), b2 = new Bundle();
		b1.putInt("Tab1", 1);
		b2.putInt("Tab2", 2);
		tabHost.addTab(tabHost.newTabSpec("about").setIndicator("About"), ProfileAboutFragment.class, b1);
		tabHost.addTab(tabHost.newTabSpec("thoughts").setIndicator("Thoughts"), ProfileThoughtsFragment.class, b2);
		
		return profileView;
		
	}
}
