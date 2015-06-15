package com.echobond.fragment;

import com.echobond.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
/**
 * 
 * @author aohuijun
 *
 */
public class MoreGroupsFragment extends Fragment {

	private TextView textView;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View moreGroupsView = inflater.inflate(R.layout.fragment_more_groups, container, false);
		textView = (TextView)moreGroupsView.findViewById(R.id.more_groups_text);
		Bundle bundle = this.getArguments();
		textView.setText(bundle.getString("type"));
		
		return moreGroupsView;
	}
	
}
