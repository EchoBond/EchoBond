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
public class SearchThoughtsFragment extends Fragment {

	private TextView cat1, cat2, cat3, cat4, cat5, cat6;
	private TextView grp1, grp2, grp3, grp4, grp5;
	private TextView tag1, tag2, tag3, tag4, tag5;
	private ImageView moreGroupsView, moreTagsView;
	private ViewMoreSwitchCallback typeSwitchCallback;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View searchThoughtsView = inflater.inflate(R.layout.fragment_search_thoughts, container, false);
		
		cat1 = (TextView)searchThoughtsView.findViewById(R.id.search_category_feeling);
		cat2 = (TextView)searchThoughtsView.findViewById(R.id.search_category_idea);
		cat3 = (TextView)searchThoughtsView.findViewById(R.id.search_category_dream);
		cat4 = (TextView)searchThoughtsView.findViewById(R.id.search_category_interest);
		cat5 = (TextView)searchThoughtsView.findViewById(R.id.search_category_plan);
		cat6 = (TextView)searchThoughtsView.findViewById(R.id.search_category_others);
		
		grp1 = (TextView)searchThoughtsView.findViewById(R.id.search_thoughts_group1);
		grp2 = (TextView)searchThoughtsView.findViewById(R.id.search_thoughts_group2);
		grp3 = (TextView)searchThoughtsView.findViewById(R.id.search_thoughts_group3);
		grp4 = (TextView)searchThoughtsView.findViewById(R.id.search_thoughts_group4);
		grp5 = (TextView)searchThoughtsView.findViewById(R.id.search_thoughts_group5);
		
		tag1 = (TextView)searchThoughtsView.findViewById(R.id.search_thoughts_tag1);
		tag2 = (TextView)searchThoughtsView.findViewById(R.id.search_thoughts_tag2);
		tag3 = (TextView)searchThoughtsView.findViewById(R.id.search_thoughts_tag3);
		tag4 = (TextView)searchThoughtsView.findViewById(R.id.search_thoughts_tag4);
		tag5 = (TextView)searchThoughtsView.findViewById(R.id.search_thoughts_tag5);
		
		moreGroupsView = (ImageView)searchThoughtsView.findViewById(R.id.search_thoughts_groups_more);
		moreGroupsView.setOnClickListener(new SearchPeopleTypeListener(SearchPage.THOUGHT_GROUP));
		moreTagsView = (ImageView)searchThoughtsView.findViewById(R.id.search_thoughts_hashtags_more);
		moreTagsView.setOnClickListener(new SearchPeopleTypeListener(SearchPage.THOUGHT_TAG));
		
		return searchThoughtsView;
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
			throw new ClassCastException(activity.toString() + "must implement typeSwitchCallback in SearchThoughtsFragment. ");
		}
	}
}
