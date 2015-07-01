package com.echobond.fragment;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.R;
import com.echobond.activity.SearchPage;
import com.echobond.application.MyApp;
import com.echobond.connector.UsersAsyncTask;
import com.echobond.dao.GroupDAO;
import com.echobond.dao.TagDAO;
import com.echobond.dao.UserDAO;
import com.echobond.entity.Group;
import com.echobond.entity.Tag;
import com.echobond.entity.User;
import com.echobond.intf.UserAsyncTaskCallback;
import com.echobond.util.HTTPUtil;
import com.echobond.util.JSONUtil;
import com.echobond.util.SPUtil;
import com.echobond.widget.XListView;
import com.echobond.widget.XListView.IXListViewListener;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 
 * @author aohuijun
 *
 */
public class SearchPeopleResultFragment extends Fragment implements IXListViewListener, UserAsyncTaskCallback {
	
	private XListView searchPeopleResultList;
	private PeopleResultAdapter adapter;
	private int searchType;
	private int searchId;
	private String searchText;
	private Integer currentLimit;
	private long lastLoadTime;
	private ArrayList<Integer> searchIDList;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View searchPeopleResultView = inflater.inflate(R.layout.fragment_search_result_people, container, false);
		adapter = new PeopleResultAdapter(getActivity(), R.layout.item_people, null, 0);
		searchPeopleResultList = (XListView)searchPeopleResultView.findViewById(R.id.search_result_list_people);
		searchPeopleResultList.setXListViewListener(this);
		searchPeopleResultList.setOverScrollMode(View.OVER_SCROLL_NEVER);
		searchPeopleResultList.setPullRefreshEnable(true);
		searchPeopleResultList.setPullLoadEnable(true);
		searchPeopleResultList.setAdapter(adapter);
		searchPeopleResultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				
			}
		});
		
		Bundle bundle = getArguments();
		searchType = bundle.getInt("type");
		searchId = bundle.getInt("id");
		searchIDList = bundle.getIntegerArrayList("idList");
		searchText = bundle.getString("keyword");
		
		currentLimit = MyApp.LIMIT_INIT;
		
		doSearch();
		return searchPeopleResultView;
	}

	public void doSearch(){
		JSONObject condition = new JSONObject();
		try{
			switch (searchType) {
			case SearchPage.PEOPLE_GROUP:
				condition.put("key", "g");
				condition.put("id", searchId);
				break;
			case SearchPage.PEOPLE_TAG:
				condition.put("key", "t");
				JSONArray idArray = JSONUtil.fromListToJSONArray(searchIDList, new TypeToken<ArrayList<Integer>>(){});
				condition.put("idList", idArray);
				break;
			case SearchPage.PEOPLE_KEYWORD:
				condition.put("key", "k");
				condition.put("keyword", searchText);
				break;
			default:
				break;
			}
		} catch (JSONException e){
			e.printStackTrace();
		}
		String url = HTTPUtil.getInstance().composePreURL(getActivity()) + getResources().getString(R.string.url_load_users);
		User user = new User();
		user.setId((String) SPUtil.get(getActivity(), MyApp.PREF_TYPE_LOGIN, MyApp.LOGIN_ID, "", String.class));
		new UsersAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url, this, UsersAsyncTask.USER_LOAD_BY_CONDITIONS,
				 user, MyApp.DEFAULT_OFFSET, currentLimit, condition);		
	}	
	
	public class PeopleResultAdapter extends CursorAdapter {

		private LayoutInflater inflater;
		private int layout;
		
		public PeopleResultAdapter(Context context, int layout, Cursor c, int flags) {
			super(context, c, flags);
			this.layout = layout;
			this.inflater = LayoutInflater.from(context);
		}

		@Override
		public void bindView(View convertView, Context ctx, Cursor c) {
			String id = c.getString(c.getColumnIndex("_id"));
			String userName = c.getString(c.getColumnIndex("username"));
			String bio = c.getString(c.getColumnIndex("bio"));
			String gender = c.getString(c.getColumnIndex("gender"));
			
			ImageView peoplePic = (ImageView)convertView.findViewById(R.id.item_people_pic);
			TextView peopleTitle = (TextView)convertView.findViewById(R.id.item_people_title);
			TextView peopleBio = (TextView)convertView.findViewById(R.id.item_people_content);
			TextView peopleGender = (TextView)convertView.findViewById(R.id.item_people_gender);
			
			peopleTitle.setText(userName);
			peopleBio.setText(bio);
			peopleGender.setText(gender);
			
			String imageUrl = HTTPUtil.getInstance().composePreURL(getActivity()) 
					+ getResources().getString(R.string.url_down_img) + "?path=" + id;
			ImageLoader.getInstance().displayImage(imageUrl, peoplePic);
		}

		@Override
		public View newView(Context context, Cursor c, ViewGroup parent) {
			return inflater.inflate(layout, parent, false);
		}
		
	}
	
	@Override
	public void onRefresh() {
		if(System.currentTimeMillis() - lastLoadTime > MyApp.LOAD_INTERVAL){
			lastLoadTime = System.currentTimeMillis();
			doSearch();
		} else {
			onLoadFinished();
		}
	}
	
	@Override
	public void onLoadMore() {
		if(System.currentTimeMillis() - lastLoadTime > 	MyApp.LOAD_INTERVAL){
			lastLoadTime = System.currentTimeMillis();
			currentLimit += MyApp.LIMIT_INCREMENT;
			doSearch();
		} else {
			onLoadFinished();
		}
	}
	
	public void onLoadFinished() {
		searchPeopleResultList.stopRefresh();
		searchPeopleResultList.stopLoadMore();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onLoadUsersResult(JSONObject result) {
		if(null != result){
			TypeToken<ArrayList<User>> token = new TypeToken<ArrayList<User>>(){};
			ArrayList<User> users = null;
			JSONArray metaArray = null;
			TypeToken<ArrayList<Tag>> tToken = new TypeToken<ArrayList<Tag>>(){};
			TypeToken<ArrayList<Group>> gToken = new TypeToken<ArrayList<Group>>(){};
			try {
				metaArray = result.getJSONArray("userMeta");
				users = (ArrayList<User>) JSONUtil.fromJSONArrayToList(result.getJSONArray("users"), token);
				ContentValues[] userValues = new ContentValues[users.size()];
				int i = 0;
				for(User u: users){
					/* meta */
					JSONObject meta = (JSONObject) metaArray.get(i);
					String userId = u.getId();
					if(null != meta && meta.length() > 0){
						JSONArray selfTags = meta.getJSONArray("selfTags");
						JSONArray likedTags = meta.getJSONArray("likedTags");
						JSONArray followedGroups = meta.getJSONArray("followedGroups");
						/* self tags */
						if(selfTags.length() > 0){
							ContentValues[] selfTagValues = new ContentValues[selfTags.length()];
							ContentValues[] selfTagsObj = new ContentValues[selfTags.length()];
							ArrayList<Tag> selfTagsList = (ArrayList<Tag>) JSONUtil.fromJSONArrayToList(selfTags, tToken);
							int x = 0;
							for (Tag tag : selfTagsList) {
								ContentValues tagValues = new ContentValues();
								ContentValues tagObj = new ContentValues();
								tagValues.put("tag_id", tag.getId());
								tagValues.put("user_id", userId);
								tagObj = tag.putValues();
								selfTagValues[x] = tagValues;
								selfTagsObj[x++] = tagObj;
							}
							getActivity().getContentResolver().bulkInsert(TagDAO.CONTENT_URI_SELF, selfTagValues);
							getActivity().getContentResolver().bulkInsert(TagDAO.CONTENT_URI_TAG, selfTagsObj);
						}
						/* liked tags */
						if(likedTags.length() > 0){
							ContentValues[] likedTagValues = new ContentValues[likedTags.length()];
							ContentValues[] likedTagsObj = new ContentValues[likedTags.length()];
							ArrayList<Tag> likedTagsList = (ArrayList<Tag>) JSONUtil.fromJSONArrayToList(likedTags, tToken);
							int x = 0;
							for (Tag tag : likedTagsList) {
								ContentValues tagValues = new ContentValues();
								ContentValues tagObj = new ContentValues();
								tagValues.put("tag_id", tag.getId());
								tagValues.put("user_id", userId);
								tagObj = tag.putValues();
								likedTagValues[x] = tagValues;
								likedTagsObj[x++] = tagObj;
							}
							getActivity().getContentResolver().bulkInsert(TagDAO.CONTENT_URI_LIKE, likedTagValues);
							getActivity().getContentResolver().bulkInsert(TagDAO.CONTENT_URI_TAG, likedTagsObj);
						}
						/* followed groups */
						if(followedGroups.length() > 0){
							ContentValues[] followedGroupValues = new ContentValues[followedGroups.length()];
							ContentValues[] followedGroupsObj = new ContentValues[followedGroups.length()];
							ArrayList<Group> followedGroupsList = (ArrayList<Group>) JSONUtil.fromJSONArrayToList(followedGroups, gToken);
							int x = 0;
							for (Group g : followedGroupsList) {
								ContentValues groupValues = new ContentValues();
								ContentValues groupObj = new ContentValues();
								groupValues.put("group_id", g.getId());
								groupValues.put("user_id", userId);
								groupObj = g.putValues();
								followedGroupValues[x] = groupValues;
								followedGroupsObj[x++] = groupObj;
							}
							getActivity().getContentResolver().bulkInsert(GroupDAO.CONTENT_URI_FOLLOW, followedGroupValues);
							getActivity().getContentResolver().bulkInsert(GroupDAO.CONTENT_URI_GROUP, followedGroupsObj);
						}
					}
					/* user */
					userValues[i++] = u.putValues();
				}
				getActivity().getContentResolver().bulkInsert(UserDAO.CONTENT_URI_USER, userValues);				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		String args[] = null;
		Cursor c = null;
		switch (searchType) {
		case SearchPage.PEOPLE_GROUP:
			args = new String[]{searchId+"", currentLimit+"", MyApp.DEFAULT_OFFSET+""};
			c = getActivity().getContentResolver().query(UserDAO.CONTENT_URI_GRP, null, null, args, null);
			break;
		case SearchPage.PEOPLE_TAG:
			args = new String[12];
			for(int i = 0; i<10; i++){
				Integer id = 0;
				if(searchIDList.size() > i)
					id = searchIDList.get(i);
				args[i] = id+"";
			}
			args[10] = currentLimit+"";
			args[11] = MyApp.DEFAULT_OFFSET+"";
			c = getActivity().getContentResolver().query(UserDAO.CONTENT_URI_SELF_TAG, null, null, args, null);
			break;
		case SearchPage.PEOPLE_KEYWORD:
			args = new String[]{searchText, currentLimit+"", MyApp.DEFAULT_OFFSET+""};
			c = getActivity().getContentResolver().query(UserDAO.CONTENT_URI_KEYWORD, null,null,args,null);
			break;
		default:
			break;
		}
		adapter.swapCursor(c);
		adapter.notifyDataSetChanged();
		onLoadFinished();
	}

	@Override
	public void onUpdateUserResult(JSONObject result) {}
	
}
