package com.echobond.fragment;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.R;
import com.echobond.activity.AvatarPage;
import com.echobond.activity.ChatPage;
import com.echobond.activity.SearchPage;
import com.echobond.activity.SearchResultPage;
import com.echobond.activity.ThoughtsListPage;
import com.echobond.application.MyApp;
import com.echobond.connector.UsersAsyncTask;
import com.echobond.dao.GroupDAO;
import com.echobond.dao.TagDAO;
import com.echobond.dao.UserDAO;
import com.echobond.entity.User;
import com.echobond.intf.UserAsyncTaskCallback;
import com.echobond.util.HTTPUtil;
import com.echobond.util.JSONUtil;
import com.echobond.util.SPUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @author aohuijun
 *
 */
public class ProfileFragment extends Fragment implements UserAsyncTaskCallback{
	
	private ImageView profileFigureView, profileChat;
	private TextView profileTitle, profileBio, profileGender;
	private TextView profileDNA, profileTrophy, profileTodo, profilePhilo, profileEarth, 
					profileDesc, profileHeart, profileSec, profileLang, profileTag, profileGroup; 
	private TextView viewThoughtsButton;
	private String userId, userName, localId;
	
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			//clear cache
			String avatarUrl = HTTPUtil.getInstance().composePreURL(getActivity()) +
					getResources().getString(R.string.url_down_img) + "?path=" + userId;
			MemoryCacheUtils.removeFromCache(avatarUrl, ImageLoader.getInstance().getMemoryCache());
			DiskCacheUtils.removeFromCache(avatarUrl, ImageLoader.getInstance().getDiskCache());
			updateUserProfile();
		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View profileView = inflater.inflate(R.layout.fragment_main_profile, container, false);
		
		localId = (String) SPUtil.get(getActivity(), MyApp.PREF_TYPE_LOGIN, MyApp.LOGIN_ID, "", String.class);
		
		profileFigureView = (ImageView)profileView.findViewById(R.id.profile_pic);
		profileFigureView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("userId", userId);
				intent.setClass(getActivity(), AvatarPage.class);
				startActivity(intent);
			}
		});
		profileChat = (ImageView) profileView.findViewById(R.id.profile_chat);
		profileTitle = (TextView)profileView.findViewById(R.id.profile_title);
		profileBio = (TextView)profileView.findViewById(R.id.profile_content);
		profileGender = (TextView)profileView.findViewById(R.id.profile_gender);
		
		profileDNA = (TextView)profileView.findViewById(R.id.profile_about_dna_text);
		profileTrophy = (TextView)profileView.findViewById(R.id.profile_about_trophy_text);
		profileTodo = (TextView)profileView.findViewById(R.id.profile_about_todo_text);
		profilePhilo = (TextView)profileView.findViewById(R.id.profile_about_philo_text);
		profileEarth = (TextView)profileView.findViewById(R.id.profile_about_earth_text);
		profileDesc = (TextView)profileView.findViewById(R.id.profile_about_desc_text);
		profileHeart = (TextView)profileView.findViewById(R.id.profile_about_heart_text);
		profileSec = (TextView)profileView.findViewById(R.id.profile_about_sec_text);
		profileLang = (TextView)profileView.findViewById(R.id.profile_about_lang_text);
		profileTag = (TextView)profileView.findViewById(R.id.profile_about_tag_text);
		profileGroup = (TextView) profileView.findViewById(R.id.profile_about_group_text);
		
		viewThoughtsButton = (TextView)profileView.findViewById(R.id.profile_view_thoughts);
		
		profileChat.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if("".equals(SPUtil.get(ProfileFragment.this.getActivity(), MyApp.PREF_TYPE_LOGIN, MyApp.LOGIN_USERNAME, "", String.class))){
					Toast.makeText(ProfileFragment.this.getActivity(), getResources().getString(R.string.hint_empty_username), Toast.LENGTH_SHORT).show();
					return;
				}
				Intent chatIntent = new Intent();
				chatIntent.setClass(ProfileFragment.this.getActivity(), ChatPage.class);
				chatIntent.putExtra("guestId", userId);
				chatIntent.putExtra("userName", userName);
				chatIntent.putExtra("notFromMain", true);
				startActivity(chatIntent);
			}
		});
		
		viewThoughtsButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				if(localId.equals(userId)){
					intent.putExtra("userId", userId);
					intent.putExtra("userName", userName);
					intent.setClass(getActivity(), ThoughtsListPage.class);
				} else {
					intent.putExtra("id", 0);
					intent.putExtra("idList", new ArrayList<Integer>());
					intent.putExtra("type", SearchPage.THOUGHT_PEOPLE);
					intent.putExtra("keyword", userId);
					intent.setClass(getActivity(), SearchResultPage.class);
				}
				startActivity(intent);
			}
		});
		Bundle bundle = getArguments();
		if(null != bundle && null != bundle.getString("userId")){
			userId = bundle.getString("userId");
			userName = bundle.getString("userName");
		} else {
			userId = localId;
			userName = "Yourself";
			profileChat.setVisibility(View.INVISIBLE);
		}
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, new IntentFilter(MyApp.BROADCAST_UPDATE_PROFILE));
		updateUserProfile();
		return profileView;
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		updateUserProfile();
	}
	
	private void updateUserProfile(){
		String url = HTTPUtil.getInstance().composePreURL(getActivity()) + getResources().getString(R.string.url_load_users);
		User user = new User();
		user.setId(userId);
		new UsersAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url, this, 
				UsersAsyncTask.USER_LOAD_BY_ID, user);
		String avatarUrl = HTTPUtil.getInstance().composePreURL(getActivity()) +
				getResources().getString(R.string.url_down_img) + "?path=" + userId;		
		ImageLoader.getInstance().displayImage(avatarUrl, profileFigureView);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
	}
	
	@Override
	public void onLoadUsersResult(JSONObject result) {
		if(null != result){
			try {
				JSONObject userJSON = (JSONObject) result.get("user");
				User user = (User) JSONUtil.fromJSONToObject(userJSON, User.class);
				ContentValues values = user.putValues();
				getActivity().getContentResolver().insert(UserDAO.CONTENT_URI_USER, values);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		Cursor cursor = getActivity().getContentResolver().query(UserDAO.CONTENT_URI_USER, null, null, new String[]{userId}, null);
		if(null != cursor){
			if(cursor.moveToFirst()){
				if(userId.equals(localId)){
					SPUtil.put(getActivity(), MyApp.PREF_TYPE_LOGIN, MyApp.LOGIN_USERNAME, cursor.getString(cursor.getColumnIndex("username")));
					SPUtil.put(getActivity(), MyApp.PREF_TYPE_LOGIN, MyApp.LOGIN_VERIFIED, cursor.getInt(cursor.getColumnIndex("verified")));				
				}
				String args[] = new String[]{userId};
				profileGender.setText(cursor.getString(cursor.getColumnIndex("gender")));
				profileTitle.setText(cursor.getString(cursor.getColumnIndex("username")));
				profileBio.setText(cursor.getString(cursor.getColumnIndex("bio")));
				profileDNA.setText(cursor.getString(cursor.getColumnIndex("sth_interesting")));
				profileTrophy.setText(cursor.getString(cursor.getColumnIndex("amz_exp")));
				profileTodo.setText(cursor.getString(cursor.getColumnIndex("to_do")));
				profilePhilo.setText(cursor.getString(cursor.getColumnIndex("philosophy")));
				profileEarth.setText(cursor.getString(cursor.getColumnIndex("home_name")));
				profileDesc.setText(cursor.getString(cursor.getColumnIndex("friends_desc")));
				profileHeart.setText(cursor.getString(cursor.getColumnIndex("interest")));
				profileSec.setText(cursor.getString(cursor.getColumnIndex("little_secret")));
				profileLang.setText(cursor.getString(cursor.getColumnIndex("lang_name")));
				Cursor tCursor = getActivity().getContentResolver().query(TagDAO.CONTENT_URI_SELF, null, null, args, null);
				Cursor gCursor = getActivity().getContentResolver().query(GroupDAO.CONTENT_URI_FOLLOW, null, null, args, null);
				String tags = "";
				if(null != tCursor){
					while(tCursor.moveToNext()){
						tags += "#" + tCursor.getString(tCursor.getColumnIndex("name")) + " ";
					}
				}
				tCursor.close();
				String groups = "";
				if(null != gCursor){
					while(gCursor.moveToNext()){
						groups += gCursor.getString(gCursor.getColumnIndex("name"));
						if(!gCursor.isLast()){
							groups += ",";
						}
					}
				}
				gCursor.close();
				profileTag.setText(tags);
				profileGroup.setText(groups);
			}
			cursor.close();
		}
	}

	@Override
	public void onUpdateUserResult(JSONObject result) {
		
	}
}
