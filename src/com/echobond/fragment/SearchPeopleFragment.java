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
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 
 * @author aohuijun
 *
 */
public class SearchPeopleFragment extends Fragment{

	private TextView grp1, grp2, grp3, grp4, grp5;
	private TextView tag1, tag2, tag3, tag4, tag5;
	private ImageView moreGroupsView, moreTagsView;
	private ViewMoreSwitchCallback typeSwitchCallback;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View searchPeopleView = inflater.inflate(R.layout.fragment_search_people, container, false);
		
		grp1 = (TextView)searchPeopleView.findViewById(R.id.search_people_group1);
		grp2 = (TextView)searchPeopleView.findViewById(R.id.search_people_group2);
		grp3 = (TextView)searchPeopleView.findViewById(R.id.search_people_group3);
		grp4 = (TextView)searchPeopleView.findViewById(R.id.search_people_group4);
		grp5 = (TextView)searchPeopleView.findViewById(R.id.search_people_group5);
		
		tag1 = (TextView)searchPeopleView.findViewById(R.id.search_people_tag1);
		tag2 = (TextView)searchPeopleView.findViewById(R.id.search_people_tag2);
		tag3 = (TextView)searchPeopleView.findViewById(R.id.search_people_tag3);
		tag4 = (TextView)searchPeopleView.findViewById(R.id.search_people_tag4);
		tag5 = (TextView)searchPeopleView.findViewById(R.id.search_people_tag5);
		
		moreGroupsView = (ImageView)searchPeopleView.findViewById(R.id.search_people_groups_more);
		moreGroupsView.setOnClickListener(new SearchPeopleTypeListener(SearchPage.PEOPLE_GROUP));
		moreTagsView = (ImageView)searchPeopleView.findViewById(R.id.search_people_hashtags_more);
		moreTagsView.setOnClickListener(new SearchPeopleTypeListener(SearchPage.PEOPLE_TAG));
		
		return searchPeopleView;
	}
	
	public class SearchPeopleTypeListener implements OnClickListener {

		private int searchType;
		
		public SearchPeopleTypeListener(int t) {
			this.searchType = t;
		}

		@Override
		public void onClick(View v) {
			typeSwitchCallback.onTypeSelected(searchType);
		}
		
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			typeSwitchCallback = (ViewMoreSwitchCallback) activity;
		} catch (ClassCastException e) {
			// should never happen in normal cases
			throw new ClassCastException(activity.toString() + "must implement typeSwitchCallback in SearchPeopleFragment. ");
		}
	}
}
