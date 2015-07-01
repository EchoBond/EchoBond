package com.echobond.fragment;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.R;
import com.echobond.activity.SearchPage;
import com.echobond.connector.LoadPeopleSearchAsyncTask;
import com.echobond.dao.GroupDAO;
import com.echobond.dao.TagDAO;
import com.echobond.entity.Group;
import com.echobond.entity.Tag;
import com.echobond.intf.LoadSearchPeopleCallback;
import com.echobond.intf.ViewMoreSwitchCallback;
import com.echobond.util.HTTPUtil;
import com.echobond.util.JSONUtil;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.database.Cursor;
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
public class SearchPeopleFragment extends Fragment implements LoadSearchPeopleCallback {

	
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
		ArrayList<Group> groups = null;
		ArrayList<Tag> tags = null;
		if(null != result){
			TypeToken<ArrayList<Group>> groupToken = new TypeToken<ArrayList<Group>>(){};
			groups = (ArrayList<Group>) JSONUtil.fromJSONToList(result, "groups", groupToken);
			TypeToken<ArrayList<Tag>> tagToken = new TypeToken<ArrayList<Tag>>(){};
			tags = (ArrayList<Tag>) JSONUtil.fromJSONToList(result, "tags", tagToken);
		} else {
			groups = new ArrayList<Group>();
			tags = new ArrayList<Tag>();
			Cursor gCursor = getActivity().getContentResolver().query(GroupDAO.CONTENT_URI_RANDOM, null, null, new String[]{RANDOM_NUM+""}, null);
			Cursor tCursor = getActivity().getContentResolver().query(TagDAO.CONTENT_URI_RANDOM, null, null, new String[]{RANDOM_NUM+""}, null);
			if(null != gCursor){
				while(gCursor.moveToNext()){
					Group g = new Group();
					g.setId(gCursor.getInt(gCursor.getColumnIndex("_id")));
					g.setName(gCursor.getString(gCursor.getColumnIndex("name")));
					groups.add(g);
				}
				gCursor.close();
			}
			if(null != tCursor){
				while(tCursor.moveToNext()){
					Tag t = new Tag();
					t.setId(tCursor.getInt(tCursor.getColumnIndex("_id")));
					t.setName(tCursor.getString(tCursor.getColumnIndex("name")));
					tags.add(t);
				}
				tCursor.close();
			}
		}
		for(int i = 0; i < RANDOM_NUM; i++){
			Group g = groups.get(i);
			Tag t = tags.get(i);
			grpsViews[i].setText(g.getName());
			grpsViews[i].setTag(g.getId());
			tagsViews[i].setText(t.getName());
			tagsViews[i].setTag(t.getId());
			
			grpsViews[i].setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View view) {
					Integer id = (Integer) view.getTag();
					JSONObject jso = new JSONObject();
					try {
						jso.put("index", SearchPage.PEOPLE_GROUP);
						jso.put("id", id);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					typeSwitchCallback.onSearchSelected(jso);
				}
			});
			
			tagsViews[i].setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View view) {
					Integer id = (Integer) view.getTag();
					JSONObject jso = new JSONObject();
					try {
						jso.put("index", SearchPage.PEOPLE_TAG);
						ArrayList<Integer> idList = new ArrayList<Integer>();
						idList.add(id);
						jso.put("id", 0);
						jso.put("idList", JSONUtil.fromListToJSONArray(idList, new TypeToken<ArrayList<Integer>>(){}));
					} catch (JSONException e) {
						e.printStackTrace();
					}
					typeSwitchCallback.onSearchSelected(jso);
				}
			});
		}
	}
}
