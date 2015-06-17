package com.echobond.fragment;

import com.echobond.R;
import com.echobond.activity.SearchPage;

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

	private TextView groupTextView;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View moreGroupsView = inflater.inflate(R.layout.fragment_more_groups, container, false);
		groupTextView = (TextView)moreGroupsView.findViewById(R.id.more_groups_text);
		
		return moreGroupsView;
	}

	public void setGroupTextView(int text) {
		switch (text) {
		case SearchPage.THOUGHT_GROUP:
			this.groupTextView.setText("THOUGHT_GROUP");
			break;
		case SearchPage.PEOPLE_GROUP:
			this.groupTextView.setText("PEOPLE_GROUP");
			break;
		default:
			break;
		}
	}
	
}
