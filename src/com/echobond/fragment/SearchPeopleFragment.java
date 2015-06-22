package com.echobond.fragment;

import java.util.ArrayList;

import org.json.JSONObject;

import com.echobond.R;
import com.echobond.activity.SearchPage;
import com.echobond.connector.LoadPeopleSearchAsyncTask;
import com.echobond.entity.Group;
import com.echobond.entity.Tag;
import com.echobond.intf.LoadSearchPeopleCallback;
import com.echobond.intf.ViewMoreSwitchCallback;
import com.echobond.util.HTTPUtil;
import com.echobond.util.JSONUtil;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.os.AsyncTask;
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
public class SearchPeopleFragment extends Fragment implements LoadSearchPeopleCallback{

	
	private final Integer RANDOM_NUM = 5;
	private TextView[] grpsViews = new TextView[RANDOM_NUM];
	private TextView[] tagsViews = new TextView[RANDOM_NUM];
	private ImageView moreGroupsView, moreTagsView;
	private ViewMoreSwitchCallback typeSwitchCallback;
	
	private View rootView;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if(null == rootView){
			rootView = inflater.inflate(R.layout.fragment_search_people, container, false);
		} else {
			return rootView;
		}
		String url = HTTPUtil.getInstance().composePreURL(getActivity()) + getResources().getString(R.string.url_load_search_ppl);
		new LoadPeopleSearchAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url, this, RANDOM_NUM);
		int i = 0;
		grpsViews[i++] = (TextView)rootView.findViewById(R.id.search_people_group1);
		grpsViews[i++] = (TextView)rootView.findViewById(R.id.search_people_group2);
		grpsViews[i++] = (TextView)rootView.findViewById(R.id.search_people_group3);
		grpsViews[i++] = (TextView)rootView.findViewById(R.id.search_people_group4);
		grpsViews[i] = (TextView)rootView.findViewById(R.id.search_people_group5);
		i = 0;
		tagsViews[i++] = (TextView)rootView.findViewById(R.id.search_people_tag1);
		tagsViews[i++] = (TextView)rootView.findViewById(R.id.search_people_tag2);
		tagsViews[i++] = (TextView)rootView.findViewById(R.id.search_people_tag3);
		tagsViews[i++] = (TextView)rootView.findViewById(R.id.search_people_tag4);
		tagsViews[i] = (TextView)rootView.findViewById(R.id.search_people_tag5);
		
		moreGroupsView = (ImageView)rootView.findViewById(R.id.search_people_groups_more);
		moreGroupsView.setOnClickListener(new SearchPeopleTypeListener(SearchPage.PEOPLE_GROUP));
		moreTagsView = (ImageView)rootView.findViewById(R.id.search_people_hashtags_more);
		moreTagsView.setOnClickListener(new SearchPeopleTypeListener(SearchPage.PEOPLE_TAG));
		
		return rootView;
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

	@Override
	@SuppressWarnings("unchecked")
	public void onLoadSearchPeopleResult(JSONObject result) {
		if(null != result){
			TypeToken<ArrayList<Group>> groupToken = new TypeToken<ArrayList<Group>>(){};
			ArrayList<Group> groups = (ArrayList<Group>) JSONUtil.fromJSONToList(result, "groups", groupToken);
			TypeToken<ArrayList<Tag>> tagToken = new TypeToken<ArrayList<Tag>>(){};
			ArrayList<Tag> tags = (ArrayList<Tag>) JSONUtil.fromJSONToList(result, "tags", tagToken);
			for(int i = 0; i < RANDOM_NUM; i++){
				Group g = groups.get(i);
				Tag t = tags.get(i);
				grpsViews[i].setText(g.getName());
				grpsViews[i].setTag(g.getId());
				tagsViews[i].setText(t.getName());
				tagsViews[i].setTag(t.getId());
			}
		}
	}
}
