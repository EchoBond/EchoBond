package com.echobond.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.R;
import com.echobond.activity.ThoughtsListPage;
import com.echobond.application.MyApp;
import com.echobond.connector.UsersAsyncTask;
import com.echobond.entity.User;
import com.echobond.intf.UserAsyncTaskCallback;
import com.echobond.util.HTTPUtil;
import com.echobond.util.JSONUtil;
import com.echobond.util.SPUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 
 * @author aohuijun
 *
 */
public class ProfileFragment extends Fragment implements UserAsyncTaskCallback{
	
	private ImageView profileFigureView;
	private TextView profileTitle, profileBio, profileGender;
	private TextView profileDNA, profileTrophy, profileTodo, profilePhilo, profileEarth, 
					profileDesc, profileHeart, profileSec, profileLang, profileTag; 
	private TextView viewThoughtsButton;
	/*
	private String[] testString = {
			"I speak Chinese! Have spent 5 years in Beijing.", 
			"Have performed sexophone across India. Went to Grand Canyon!", 
			"Set up my startup office in Silicon Valley. Visit Israel.", 
			"You Only Live Once. Make it count. Be yourself and be nice.", 
			"People stop talking about war but ART! No racism, everyone is a global citizen.", 
			"Funny, care-free, music talent, nice, king of sexophone lol", 
			"hiking, meet new fds, chinese food, cooking, sexophone", 
			"didn’t know Santa Claus was a myth by 13 years old", 
			"English, Hindi, Mandarin.", 
			"#hkust #sexophone #india #beijing #mandarin #engineering #tech start up #hong kong #cooking #hiking #travelling #music #exchange #language exchange #party #chinese culture"
	};*/
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View profileView = inflater.inflate(R.layout.fragment_main_profile, container, false);
		profileFigureView = (ImageView)profileView.findViewById(R.id.profile_pic);
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
		
		viewThoughtsButton = (TextView)profileView.findViewById(R.id.profile_view_thoughts);
				
		String url = HTTPUtil.getInstance().composePreURL(getActivity()) + getResources().getString(R.string.url_load_users);
		User user = new User();
		user.setId((String) SPUtil.get(getActivity(), MyApp.PREF_TYPE_LOGIN, MyApp.LOGIN_ID, "", String.class));
		new UsersAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url, this, 
				UsersAsyncTask.USER_LOAD_BY_ID, user);
		String avatarUrl = HTTPUtil.getInstance().composePreURL(getActivity()) +
				getResources().getString(R.string.url_down_img) + 
				"?path=" + SPUtil.get(getActivity(), MyApp.PREF_TYPE_LOGIN, MyApp.LOGIN_ID, "", String.class);
		ImageLoader.getInstance().displayImage(avatarUrl, profileFigureView);
		
		viewThoughtsButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.putExtra("userName", "Yourself's ");
				intent.setClass(getActivity(), ThoughtsListPage.class);
				startActivity(intent);
			}
		});
		
		return profileView;
		
	}

	@Override
	public void onLoadUsersResult(JSONObject result) {
		if(null != result){
			try {
				JSONObject userJSON = (JSONObject) result.get("user");
				User user = (User) JSONUtil.fromJSONToObject(userJSON, User.class);
				profileGender.setText("");
				profileTitle.setText(user.getUserName());
				profileBio.setText(user.getBio());
				profileDNA.setText(user.getSthInteresting());
				profileTrophy.setText(user.getAmzExp());
				profileTodo.setText(user.getToDo());
				profilePhilo.setText(user.getPhilosophy());
				profileEarth.setText("");
				profileDesc.setText(user.getFriendsDesc());
				profileHeart.setText(user.getInterest());
				profileSec.setText(user.getLittleSecret());
				profileLang.setText(user.getLocale());
				profileTag.setText(user.getBio());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onUpdateUserResult(JSONObject result) {
		
	}
}
