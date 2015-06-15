package com.echobond.fragment;

import com.echobond.R;
import com.echobond.activity.SearchPage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @author aohuijun
 *
 */
public class MoreTagsFragment extends Fragment{

	private TextView moreTextView;
	private SearchPage searchPage;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View moreTagsView = inflater.inflate(R.layout.fragment_more_tags, container, false);
		moreTextView = (TextView)moreTagsView.findViewById(R.id.more_tags_text);
		Bundle bundle = this.getArguments();
		moreTextView.setText(bundle.getString("type"));
		moreTextView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent upIntent = NavUtils.getParentActivityIntent(getActivity());
				upIntent.putExtra("string", moreTextView.getText().toString());
				if (NavUtils.shouldUpRecreateTask(getActivity(), upIntent)) {
					TaskStackBuilder.create(getActivity()).addNextIntentWithParentStack(upIntent).startActivities();
				}else {
					upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					NavUtils.navigateUpTo(getActivity(), upIntent);
				}
				changeSearchUI();
				searchPage = SearchPage.instance; 
				searchPage.setResultString(moreTextView.getText().toString());
				Toast.makeText(getActivity(), "BACK", Toast.LENGTH_SHORT).show();
				getActivity().finish();
				
			}
		});
		
		return moreTagsView;
	}
	
	private void changeSearchUI() {
//		searchPage = SearchPage.instance;
		FragmentTabHost tabHost = ((SearchPage)getActivity().getParent()).getTabHost();
		if (moreTextView.getText().toString() == "Hashtags for Thoughts") {
//			tabHost.clearAllTabs();
//			tabHost.addTab(tabHost.newTabSpec("thoughts").setIndicator("Thoughts"), SearchThoughtsResultFragment.class, null);
//			tabHost.addTab(tabHost.newTabSpec("people").setIndicator("People"), SearchPeopleFragment.class, null);
//			tabHost.setCurrentTab(0);
		} else if (moreTextView.getText().toString() == "Hashtags for People") {
//			tabHost.clearAllTabs();
//			tabHost.addTab(tabHost.newTabSpec("thoughts").setIndicator("Thoughts"), SearchThoughtsFragment.class, null);
//			tabHost.addTab(tabHost.newTabSpec("people").setIndicator("People"), SearchPeopleResultFragment.class, null);
//			tabHost.setCurrentTab(1);
		}
		
	}
}
