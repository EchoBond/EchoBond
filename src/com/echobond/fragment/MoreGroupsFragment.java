package com.echobond.fragment;

import com.echobond.R;
import com.echobond.activity.SearchPage;
import com.echobond.intf.ViewMoreSwitchCallback;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @author aohuijun
 *
 */
public class MoreGroupsFragment extends Fragment {

	private TextView groupTextView;
	private ViewMoreSwitchCallback searchCallback;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View moreGroupsView = inflater.inflate(R.layout.fragment_more_groups, container, false);
		groupTextView = (TextView)moreGroupsView.findViewById(R.id.more_groups_text);
		Bundle bundle = this.getArguments();
		if (bundle != null) {
			groupTextView.setText("View More " + bundle.getString("type"));
			groupTextView.setOnClickListener(new SearchOnClickListener(bundle.getString("type")));
		}
		return moreGroupsView;
	}
	
	public class SearchOnClickListener implements OnClickListener {

		private String type;
		private int index;
		
		public SearchOnClickListener(String typeString) {
			this.type = typeString;
		}

		@Override
		public void onClick(View v) {
			Toast.makeText(getActivity(), "BACK", Toast.LENGTH_SHORT).show();
			if (type == SearchPage.THOUGHTS_MORE_GROUP) {
				index = SearchPage.THOUGHT_GROUP;
			} else if (type == SearchPage.PEOPLE_MORE_GROUP) {
				index = SearchPage.PEOPLE_GROUP;
			}
			searchCallback.onSearchSelected(index);
		}
		
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			searchCallback = (ViewMoreSwitchCallback) activity;
		} catch (Exception e) {
			throw new ClassCastException(activity.toString() + "must implement searchCallback in MoreGroupsFragment. ");
		}
	}
	
}
