package com.echobond.activity;

import com.echobond.R;
import com.echobond.fragment.MoreGroupsFragment;
import com.echobond.fragment.MoreTagsFragment;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
/**
 * 
 * @author aohuijun
 *
 */
public class ViewMorePage extends ActionBarActivity {

	private TextView titleView;
	private String type;
	private MoreGroupsFragment groupsTFragment;
	private MoreTagsFragment tagsTFragment;
	private MoreGroupsFragment groupsPFragment;
	private MoreTagsFragment tagsPFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_more_page);
		initActionBar();
		initPage();
		
	}

	private void initActionBar() {
		Toolbar moreToolbar = (Toolbar)findViewById(R.id.toolbar_more_page);
		setSupportActionBar(moreToolbar);
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.title_bar_setting);

	}
	
	private void initPage() {
		Intent intent = getIntent();
		Bundle bundle = new Bundle();
		int typeIndex = intent.getIntExtra("type", -1);
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		if (groupsTFragment == null || tagsTFragment == null || groupsPFragment == null || tagsPFragment == null) {
			groupsTFragment = new MoreGroupsFragment();
			tagsTFragment = new MoreTagsFragment();
			groupsPFragment = new MoreGroupsFragment();
			tagsPFragment = new MoreTagsFragment();
		}
		switch (typeIndex) {
		case SearchPage.THOUGHT_GROUP:
			type = "Groups for Thoughts";
			bundle.putString("type", type);
			groupsTFragment.setArguments(bundle);
			transaction.add(R.id.more_content, groupsTFragment).show(groupsTFragment).commit();
			break;
		case SearchPage.THOUGHT_TAG:
			type = "Hashtags for Thoughts";
			bundle.putString("type", type);
			tagsTFragment.setArguments(bundle);
			transaction.add(R.id.more_content, tagsTFragment).show(tagsTFragment).commit();
			break;
		case SearchPage.PEOPLE_GROUP:
			type = "Groups for People";
			bundle.putString("type", type);
			groupsPFragment.setArguments(bundle);
			transaction.add(R.id.more_content, groupsPFragment).show(groupsPFragment).commit();
			break;
		case SearchPage.PEOPLE_TAG:
			type = "Hashtags for People";
			bundle.putString("type", type);
			tagsPFragment.setArguments(bundle);
			transaction.add(R.id.more_content, tagsPFragment).show(tagsPFragment).commit();
			break;
		default:
			break;
		}
		
		titleView = (TextView)findViewById(R.id.title_name);
		titleView.setText("More " + type);

	}
	
}
