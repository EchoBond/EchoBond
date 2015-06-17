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
public class MoreTagsFragment extends Fragment{

	private TextView tagTextView;
	private ViewMoreSwitchCallback searchCallback;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View moreTagsView = inflater.inflate(R.layout.fragment_more_tags, container, false);
		tagTextView = (TextView)moreTagsView.findViewById(R.id.more_tags_text);
		Bundle bundle = this.getArguments();
		if (bundle != null) {
			tagTextView.setText(bundle.getString("type"));
			tagTextView.setOnClickListener(new SearchOnClickListener(bundle.getString("type")));
		}
		return moreTagsView;
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
			if (type == "THOUGHT_TAG") {
				index = SearchPage.THOUGHT_TAG;
			} else if (type == "PEOPLE_TAG") {
				index = SearchPage.PEOPLE_TAG;
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
			throw new ClassCastException(activity.toString() + "must implement searchCallback in MoreTagsFragment. ");
		}
	}
	
}
