package com.echobond.fragment;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.R;
import com.echobond.activity.SearchPage;
import com.echobond.connector.LoadThoughtSearchAsyncTask;
import com.echobond.dao.GroupDAO;
import com.echobond.dao.TagDAO;
import com.echobond.db.CategoryDB;
import com.echobond.entity.Category;
import com.echobond.entity.Group;
import com.echobond.entity.Tag;
import com.echobond.intf.LoadSearchThoughtCallback;
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
public class SearchThoughtsFragment extends Fragment implements LoadSearchThoughtCallback {

	private final Integer CAT_NUM = 6, RANDOM_NUM = 5;	
	private TextView[] catsViews = new TextView[CAT_NUM], grpsViews = new TextView[RANDOM_NUM], tagsViews = new TextView[RANDOM_NUM];
	private ImageView moreGroupsView, moreTagsView;
	private ViewMoreSwitchCallback typeSwitchCallback;
	
	private View rootView;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if(null == rootView){
			rootView = inflater.inflate(R.layout.fragment_search_thoughts, container, false);
		} else {
			return rootView;
		}
		
		String url = HTTPUtil.getInstance().composePreURL(getActivity()) + getResources().getString(R.string.url_load_search_t);
		new LoadThoughtSearchAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url, this, RANDOM_NUM);
		int i = 0;		
		catsViews[i++] = (TextView)rootView.findViewById(R.id.search_category_feeling);
		catsViews[i++] = (TextView)rootView.findViewById(R.id.search_category_idea);
		catsViews[i++] = (TextView)rootView.findViewById(R.id.search_category_dream);
		catsViews[i++] = (TextView)rootView.findViewById(R.id.search_category_interest);
		catsViews[i++] = (TextView)rootView.findViewById(R.id.search_category_plan);
		catsViews[i] = (TextView)rootView.findViewById(R.id.search_category_others);
		i = 0;
		grpsViews[i++] = (TextView)rootView.findViewById(R.id.search_thoughts_group1);
		grpsViews[i++] = (TextView)rootView.findViewById(R.id.search_thoughts_group2);
		grpsViews[i++] = (TextView)rootView.findViewById(R.id.search_thoughts_group3);
		grpsViews[i++] = (TextView)rootView.findViewById(R.id.search_thoughts_group4);
		grpsViews[i] = (TextView)rootView.findViewById(R.id.search_thoughts_group5);
		i = 0;
		tagsViews[i++] = (TextView)rootView.findViewById(R.id.search_thoughts_tag1);
		tagsViews[i++] = (TextView)rootView.findViewById(R.id.search_thoughts_tag2);
		tagsViews[i++] = (TextView)rootView.findViewById(R.id.search_thoughts_tag3);
		tagsViews[i++] = (TextView)rootView.findViewById(R.id.search_thoughts_tag4);
		tagsViews[i] = (TextView)rootView.findViewById(R.id.search_thoughts_tag5);
		
		moreGroupsView = (ImageView)rootView.findViewById(R.id.search_thoughts_groups_more);
		moreGroupsView.setOnClickListener(new SearchPeopleTypeListener(SearchPage.THOUGHT_GROUP));
		moreTagsView = (ImageView)rootView.findViewById(R.id.search_thoughts_hashtags_more);
		moreTagsView.setOnClickListener(new SearchPeopleTypeListener(SearchPage.THOUGHT_TAG));
		
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
			throw new ClassCastException(activity.toString() + "must implement typeSwitchCallback in SearchThoughtsFragment. ");
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void onLoadSearchThoughtResult(JSONObject result) {
		ArrayList<Category> categories = null;
		ArrayList<Group> groups = null;
		ArrayList<Tag> tags = null;
		if (null != result) {
			TypeToken<ArrayList<Category>> categoryToken = new TypeToken<ArrayList<Category>>(){};
			categories = (ArrayList<Category>) JSONUtil.fromJSONToList(result, "categories", categoryToken);			
			TypeToken<ArrayList<Group>> groupToken = new TypeToken<ArrayList<Group>>(){};
			groups = (ArrayList<Group>) JSONUtil.fromJSONToList(result, "groups", groupToken);
			TypeToken<ArrayList<Tag>> tagToken = new TypeToken<ArrayList<Tag>>(){};
			tags = (ArrayList<Tag>) JSONUtil.fromJSONToList(result, "tags", tagToken);
		} else {
			categories = new ArrayList<Category>();
			groups = new ArrayList<Group>();
			tags = new ArrayList<Tag>();
			Cursor cCursor = CategoryDB.getInstance().loadAllCategories();
			Cursor gCursor = getActivity().getContentResolver().query(GroupDAO.CONTENT_URI_RANDOM, null, null, new String[]{RANDOM_NUM+""}, null);
			Cursor tCursor = getActivity().getContentResolver().query(TagDAO.CONTENT_URI_RANDOM, null, null, new String[]{RANDOM_NUM+""}, null);
			if(null != cCursor){
				while(cCursor.moveToNext()){
					Category c = new Category();
					c.setId(cCursor.getInt(cCursor.getColumnIndex("_id")));
					c.setName(cCursor.getString(cCursor.getColumnIndex("name")));
					categories.add(c);
				}
				cCursor.close();
			}
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
		for (int i = 0; i< CAT_NUM; i++) {
			Category c = categories.get(i);
			catsViews[i].setText(c.getName());
			catsViews[i].setTag(c.getId());
		}
		for (int i = 0; i < RANDOM_NUM; i++) {				
			Group g = groups.get(i);
			Tag t = tags.get(i);
			grpsViews[i].setText(g.getName());
			grpsViews[i].setTag(g.getId());
			tagsViews[i].setText(t.getName());
			tagsViews[i].setTag(t.getId());
		}
		
		for (int i = 0; i < CAT_NUM; i++) {
			catsViews[i].setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View view) {
					Integer id = (Integer) view.getTag();
					JSONObject jso = new JSONObject();
					try {
						jso.put("index", SearchPage.THOUGHT_CATEGORY);
						jso.put("id", id);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					typeSwitchCallback.onSearchSelected(jso);
				}
			});
		}
		for (int i = 0; i < RANDOM_NUM; i++) {
			grpsViews[i].setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View view) {
					Integer id = (Integer) view.getTag();
					JSONObject jso = new JSONObject();
					try {
						jso.put("index", SearchPage.THOUGHT_GROUP);
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
						jso.put("index", SearchPage.THOUGHT_TAG);
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
