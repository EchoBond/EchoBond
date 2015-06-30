package com.echobond.fragment;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.R;
import com.echobond.activity.NewPostPage;
import com.echobond.activity.ViewMorePage;
import com.echobond.application.MyApp;
import com.echobond.connector.UsersAsyncTask;
import com.echobond.dao.GroupDAO;
import com.echobond.dao.TagDAO;
import com.echobond.dao.UserDAO;
import com.echobond.db.CountryDB;
import com.echobond.db.LanguageDB;
import com.echobond.entity.User;
import com.echobond.intf.EditProfileSwitchCallback;
import com.echobond.intf.UserAsyncTaskCallback;
import com.echobond.util.CommUtil;
import com.echobond.util.HTTPUtil;
import com.echobond.util.JSONUtil;
import com.echobond.util.SPUtil;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
/**
 * 
 * @author aohuijun
 *
 */
public class EditProfileFragment extends Fragment implements UserAsyncTaskCallback{

	private EditProfileSwitchCallback switchCallback;
	private TextView setIconView, moreTagsView, moreGroupsView, moreLikedTagsView;
	private EditText userName, userGender, userBio, userAge, //userOrigin, 
					userDNA, userTrophy, userTodo, userPhilo, userDesc, userInterest, userSec, //userLang, 
					userTags, userGroups, userLikedTags;
	private Spinner originSpinner, langSpinner;
	private SimpleCursorAdapter originAdapter, langAdapter;
	private Cursor originCursor, langCursor;
	
	public static final int MORE_SELF_TAGS = 1;
	public static final int MORE_LIKE_TAGS = 2;
	public static final int MORE_FOLLOW_GROUPS = 3;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View editProfileView = inflater.inflate(R.layout.fragment_edit_profile, container, false);
		
		originSpinner = (Spinner) editProfileView.findViewById(R.id.edit_profile_earth_spinner);
		originCursor = CountryDB.getInstance().loadAllCountries();
		originAdapter = new SimpleCursorAdapter(getActivity(), R.layout.item_country, originCursor, 
				new String[]{"_id","name","region"}, 
				new int[]{R.id.text_country_id, R.id.text_country_name, R.id.text_country_region}, 0);
		originSpinner.setAdapter(originAdapter);
		
		langSpinner = (Spinner) editProfileView.findViewById(R.id.edit_profile_lang_spinner);
		langCursor = LanguageDB.getInstance().loadAllLanguages();
		langAdapter = new SimpleCursorAdapter(getActivity(), R.layout.item_language, langCursor, 
				new String[]{"_id","name","region"}, 
				new int[]{R.id.text_language_id, R.id.text_language_name, R.id.text_language_region}, 0);
		langSpinner.setAdapter(langAdapter);
		
		userName = (EditText)editProfileView.findViewById(R.id.edit_profile_title_text);
		userGender = (EditText)editProfileView.findViewById(R.id.edit_profile_gender_text);
		userBio = (EditText)editProfileView.findViewById(R.id.edit_profile_bio_text);
		userAge = (EditText)editProfileView.findViewById(R.id.edit_profile_age_text);
//		userOrigin = (EditText)editProfileView.findViewById(R.id.edit_profile_earth_text);
		
		userDNA = (EditText)editProfileView.findViewById(R.id.edit_profile_dna_text);
		userTrophy = (EditText)editProfileView.findViewById(R.id.edit_profile_trophy_text);
		userTodo = (EditText)editProfileView.findViewById(R.id.edit_profile_todo_text);
		userPhilo = (EditText)editProfileView.findViewById(R.id.edit_profile_philo_text);
		userDesc = (EditText)editProfileView.findViewById(R.id.edit_profile_desc_text);
		userInterest = (EditText)editProfileView.findViewById(R.id.edit_profile_heart_text);
		userSec = (EditText)editProfileView.findViewById(R.id.edit_profile_sec_text);
//		userLang = (EditText)editProfileView.findViewById(R.id.edit_profile_lang_text);
		
		userTags = (EditText)editProfileView.findViewById(R.id.edit_profile_tag_text);
		userLikedTags = (EditText) editProfileView.findViewById(R.id.edit_profile_like_tag_text);
		userGroups = (EditText)editProfileView.findViewById(R.id.edit_profile_group_text);
		
		setIconView = (TextView)editProfileView.findViewById(R.id.edit_profile_icon);
		moreTagsView = (TextView)editProfileView.findViewById(R.id.edit_profile_tag_more);
		moreGroupsView = (TextView)editProfileView.findViewById(R.id.edit_profile_group_more);
		moreLikedTagsView = (TextView) editProfileView.findViewById(R.id.edit_profile_like_tag_more);
		
		String url = HTTPUtil.getInstance().composePreURL(getActivity()) + getResources().getString(R.string.url_load_users);
		User user = new User();
		user.setId((String) SPUtil.get(getActivity(), MyApp.PREF_TYPE_LOGIN, MyApp.LOGIN_ID, "", String.class));
		new UsersAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url, this, 
				UsersAsyncTask.USER_LOAD_BY_ID, user);
		
		setIconView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switchCallback.setPoster(true);
			}
		});
		moreTagsView.setOnClickListener(new ViewMoreClickListener(NewPostPage.TAG, MORE_SELF_TAGS));
		moreGroupsView.setOnClickListener(new ViewMoreClickListener(NewPostPage.GROUP,MORE_LIKE_TAGS));
		moreLikedTagsView.setOnClickListener(new ViewMoreClickListener(NewPostPage.TAG, MORE_FOLLOW_GROUPS));
		
		return editProfileView;
	}
	
	public class ViewMoreClickListener implements OnClickListener {

		private String typeString;
		private int type;
		
		public ViewMoreClickListener(String tag, int type) {
			this.typeString = tag;
			this.type = type;
		}

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.putExtra("title", typeString);
			intent.putExtra("mode", MyApp.VIEW_MORE_PROFILE);
			intent.setClass(getActivity(), ViewMorePage.class);
			startActivityForResult(intent, type);
		}
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Bundle bundle = data.getExtras();
		ArrayList<Integer> idList = bundle.getIntegerArrayList("idList");
		switch(requestCode){
		case MORE_SELF_TAGS:
			break;
		case MORE_LIKE_TAGS:
			break;
		case MORE_FOLLOW_GROUPS:
			break;
		}
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			switchCallback = (EditProfileSwitchCallback) activity;
		} catch (Exception e) {
			throw new ClassCastException(activity.toString() + "must implement switchCallback in EditProfileFragment. ");
		}
	}
	
	@Override
	public void onLoadUsersResult(JSONObject result) {
		if(null != result){
			try {
				JSONObject userJSON = result.getJSONObject("user");
				JSONObject metaJSON = result.getJSONObject("userMeta");
				User user = (User) JSONUtil.fromJSONToObject(userJSON, User.class);
				ContentValues values = user.putValues();
				getActivity().getContentResolver().insert(UserDAO.CONTENT_URI_USER, values);
				for(int i = 0; i < metaJSON.getJSONArray("selfTags").length(); i++){
					values = new ContentValues();
					int id = metaJSON.getJSONArray("selfTags").getJSONObject(i).getInt("id");
					values.put("tag_id", id);
					values.put("user_id", user.getId());
					getActivity().getContentResolver().insert(TagDAO.CONTENT_URI_SELF, values);
				}
				for(int i = 0; i < metaJSON.getJSONArray("likedTags").length(); i++){
					values = new ContentValues();
					int id = metaJSON.getJSONArray("likedTags").getJSONObject(i).getInt("id");
					values.put("tag_id", id);
					values.put("user_id", user.getId());
					getActivity().getContentResolver().insert(TagDAO.CONTENT_URI_LIKE, values);
				}
				for(int i = 0; i < metaJSON.getJSONArray("followedGroups").length(); i++){
					values = new ContentValues();
					int id = metaJSON.getJSONArray("followedGroups").getJSONObject(i).getInt("id");
					values.put("group_id", id);
					values.put("user_id", user.getId());
					getActivity().getContentResolver().insert(GroupDAO.CONTENT_URI_FOLLOW, values);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		String id = (String) SPUtil.get(getActivity(), MyApp.PREF_TYPE_LOGIN, MyApp.LOGIN_ID, "", String.class);
		Cursor cursor = getActivity().getContentResolver().query(UserDAO.CONTENT_URI_USER, null, null, new String[]{id}, null);
		if(null != cursor){
			if(cursor.moveToFirst()){
				Cursor gCursor = getActivity().getContentResolver().query(GroupDAO.CONTENT_URI_FOLLOW, null, null, new String[]{id}, null);
				Cursor tCursor = getActivity().getContentResolver().query(TagDAO.CONTENT_URI_SELF, null, null, new String[]{id}, null);
				Cursor lCursor = getActivity().getContentResolver().query(TagDAO.CONTENT_URI_LIKE, null, null, new String[]{id}, null);
				String groups = "", tags = "", likedTags = "";
				if(null != gCursor){
					while(gCursor.moveToNext()){
						groups += gCursor.getString(gCursor.getColumnIndex("name"));
						if(!gCursor.isLast())
							groups+=",";
					}
					gCursor.close();
				}
				if(null != tCursor){
					while(tCursor.moveToNext()){
						tags += tCursor.getString(tCursor.getColumnIndex("name"));
						if(!tCursor.isLast())
							tags+=",";
					}
					tCursor.close();
				}
				if(null != lCursor){
					while(lCursor.moveToNext()){
						likedTags += lCursor.getString(lCursor.getColumnIndex("name"));
						if(!lCursor.isLast())
							likedTags+=",";
					}
					lCursor.close();
				}
				
				userName.setText(cursor.getString(cursor.getColumnIndex("username")));
				userGender.setText(cursor.getString(cursor.getColumnIndex("gender")));
				userBio.setText(cursor.getString(cursor.getColumnIndex("bio")));
				userAge.setText(cursor.getInt(cursor.getColumnIndex("age"))+"");
				//userOrigin.setText(cursor.getInt(cursor.getColumnIndex("home_id"))+"");
				userDNA.setText(cursor.getString(cursor.getColumnIndex("sth_interesting")));
				userTrophy.setText(cursor.getString(cursor.getColumnIndex("amz_exp")));
				userTodo.setText(cursor.getString(cursor.getColumnIndex("to_do")));
				userPhilo.setText(cursor.getString(cursor.getColumnIndex("philosophy")));
				userDesc.setText(cursor.getString(cursor.getColumnIndex("friends_desc")));
				userInterest.setText(cursor.getString(cursor.getColumnIndex("interest")));
				userSec.setText(cursor.getString(cursor.getColumnIndex("little_secret")));
				//userLang.setText(cursor.getString(cursor.getColumnIndex("locale")));
				userTags.setText(tags);
				userLikedTags.setText(likedTags);
				userGroups.setText(groups);
				
				int homeID = cursor.getInt(cursor.getColumnIndex("home_id"));
				if(homeID == 0){
					originSpinner.setSelection(0);
				} else originSpinner.setSelection(homeID-1);
				int langID = cursor.getInt(cursor.getColumnIndex("lang_id"));
				if(langID == 0){
					langSpinner.setSelection(0);
				} else langSpinner.setSelection(langID-1);
			}
			cursor.close();
		}
	}

	@Override
	public void onUpdateUserResult(JSONObject result) {
		
	}
	
	public User getUser(){
		User user = new User();
		user.setId((String) SPUtil.get(getActivity(), MyApp.PREF_TYPE_LOGIN, MyApp.LOGIN_ID, "", String.class));
		user.setUserName(userName.getText().toString());
		user.setTimeZone(0);
		user.setGender(userGender.getText().toString());
		String ageTxt = userAge.getText().toString();
		int age = 0;
		try{
			age = Integer.parseInt(ageTxt);
		} catch (NumberFormatException e){
			age = 0;
		}
		user.setAge(age);
		user.setBirthday("");
		user.setCountryId(0);
		int homeId = originSpinner.getSelectedItemPosition() + 1;
		user.setHomeId(homeId);
		user.setBio(userBio.getText().toString());
		user.setSthInteresting(userDNA.getText().toString());
		user.setAmzExp(userTrophy.getText().toString());
		user.setToDo(userTodo.getText().toString());
		user.setPhilosophy(userPhilo.getText().toString());
		user.setFriendsDesc(userDesc.getText().toString());
		user.setInterest(userInterest.getText().toString());
		user.setLittleSecret(userSec.getText().toString());
		int langId = langSpinner.getSelectedItemPosition() + 1;
		user.setLangId(langId);
		user.setLocale("");
		return user;
	}
	
	public String[] getSelfTags(){
		return CommUtil.parseString(userTags.getText().toString(), ",");
	}
	
	public String[] getLikedTags(){
		return CommUtil.parseString(userLikedTags.getText().toString(), ",");
	}

	public String[] getFollowedGroups(){
		return CommUtil.parseString(userGroups.getText().toString(), ",");
	}
	

}
