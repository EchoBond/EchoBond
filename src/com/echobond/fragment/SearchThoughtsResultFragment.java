package com.echobond.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.R;
import com.echobond.activity.ChatPage;
import com.echobond.activity.CommentPage;
import com.echobond.activity.ImagePage;
import com.echobond.activity.SearchPage;
import com.echobond.application.MyApp;
import com.echobond.connector.BoostAsyncTask;
import com.echobond.connector.LoadThoughtAsyncTask;
import com.echobond.dao.CommentDAO;
import com.echobond.dao.HomeThoughtDAO;
import com.echobond.dao.HotThoughtDAO;
import com.echobond.dao.ThoughtTagDAO;
import com.echobond.dao.UserDAO;
import com.echobond.entity.Comment;
import com.echobond.entity.Tag;
import com.echobond.entity.Thought;
import com.echobond.entity.User;
import com.echobond.intf.BoostCallback;
import com.echobond.intf.LoadThoughtCallback;
import com.echobond.util.HTTPUtil;
import com.echobond.util.JSONUtil;
import com.echobond.util.SPUtil;
import com.echobond.widget.XListView;
import com.echobond.widget.XListView.IXListViewListener;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @author aohuijun
 *
 */
public class SearchThoughtsResultFragment extends Fragment implements IXListViewListener, LoadThoughtCallback, BoostCallback {
	
	private XListView searchThoughtsResultList;
	private ThoughtsResultAdapter adapter;
	private Integer currentLimit;
	private long lastLoadTime;
	private ArrayList<ThoughtView> thoughtViews;
	private ArrayList<Thought> thoughtList;
	
	private static final int POST = 0;
	private static final int MESSAGE = 1;
	private static final int BOOST = 2;
	private static final int COMMENT = 3;
	private static final int SHARE = 4;
	
	private int searchType;
	private int searchId;
	private String searchText;
	private ArrayList<Integer> searchIDList;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View searchThoughtsResultView = inflater.inflate(R.layout.fragment_search_result_thoughts, container, false);
		
		thoughtViews = new ArrayList<ThoughtView>();
		adapter = new ThoughtsResultAdapter(getActivity(), R.layout.item_thought, thoughtViews);
		
		searchThoughtsResultList = (XListView)searchThoughtsResultView.findViewById(R.id.search_result_list_thoughts);
		searchThoughtsResultList.setOverScrollMode(View.OVER_SCROLL_NEVER);
		searchThoughtsResultList.setPullRefreshEnable(true);
		searchThoughtsResultList.setPullLoadEnable(true);
		searchThoughtsResultList.setXListViewListener(this);
		searchThoughtsResultList.setAdapter(adapter);
		
		Bundle bundle = this.getArguments();
		searchType = bundle.getInt("type");
		searchId = bundle.getInt("id");
		searchText = bundle.getString("keyword");
		searchIDList = bundle.getIntegerArrayList("idList");

		currentLimit = MyApp.LIMIT_INIT;
		doSearch();
		return searchThoughtsResultView;
	}
	
	
	public void doSearch(){
		JSONObject condition = new JSONObject();
		try{
			switch (searchType) {
			case SearchPage.THOUGHT_CATEGORY:
				condition.put("key", "c");
				condition.put("id", searchId);
				break;
			case SearchPage.THOUGHT_GROUP:
				condition.put("key", "g");
				condition.put("id", searchId);
				break;
			case SearchPage.THOUGHT_TAG:
				condition.put("key", "t");
				JSONArray idArray = JSONUtil.fromListToJSONArray(searchIDList, new TypeToken<ArrayList<Integer>>(){});
				condition.put("idList", idArray);
				break;
			case SearchPage.THOUGHT_KEYWORD:
				condition.put("key", "k");
				condition.put("keyword", searchText);
				break;
			case SearchPage.THOUGHT_PEOPLE:
				condition.put("key", "u");
				condition.put("userId", searchText);
			default:
				break;
			}
		} catch (JSONException e){
			e.printStackTrace();
		}
		String url = HTTPUtil.getInstance().composePreURL(getActivity()) + getResources().getString(R.string.url_load_thoughts);
		User user = new User();
		user.setId((String) SPUtil.get(getActivity(), MyApp.PREF_TYPE_LOGIN, MyApp.LOGIN_ID, "", String.class));
		new LoadThoughtAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url, LoadThoughtAsyncTask.LOAD_T_SEARCH,
				this, MyApp.DEFAULT_OFFSET, currentLimit, user, condition);		
	}	
	
	static class ViewHolder {
		TextView titleView;
		TextView contentView;
		TextView boostsNum;
		TextView commentsNum;
		TextView thoughtIdView;
		TextView imagePathView;
		TextView isUserBoostView;
		TextView userIdView;
		ImageView postFigure;
		ImageView messageButton;
		ImageView boostButton;
		ImageView commentButton;
		ImageView shareButton;		
	}

	class ThoughtView{
		int id;
		String title;
		String content;
		String path;
		String userId;
		int boost;
		int cmt;
		int isUserBoost;
		public ThoughtView(int id, String title, String content,
				String path, String userId, int boost, int cmt, int isUserBoost) {
			super();
			this.id = id;
			this.title = title;
			this.content = content;
			this.path = path;
			this.userId = userId;
			this.boost = boost;
			this.cmt = cmt;
			this.isUserBoost = isUserBoost;
		}
		
	}

	public void onLoadFinished() {
		searchThoughtsResultList.stopRefresh();
		searchThoughtsResultList.stopLoadMore();
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
	
	@SuppressWarnings("unchecked")
	@Override
	public void onLoadThoughtResult(JSONObject result) {		
		if(null != result){
			TypeToken<ArrayList<Thought>> token = new TypeToken<ArrayList<Thought>>(){};
			try {
				thoughtList = (ArrayList<Thought>) JSONUtil.fromJSONArrayToList(result.getJSONArray("thoughts"), token);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			//ContentValues[] contentValues = new ContentValues[thoughts.size()];
			//int i = 0;
			/* Loading thoughts */
			ArrayList<ThoughtView> newViews = new ArrayList<ThoughtView>();
			for (Thought thought : thoughtList) {
				/* thoughts */
				//contentValues[i++] = thought.putValues();
				/* author */
				getActivity().getContentResolver().insert(UserDAO.CONTENT_URI_USER, thought.getUser().putValues());
				/* comments */
				if(null != thought.getComments()){
					int x = 0;
					ContentValues[] cmtValues = new ContentValues[thought.getComments().size()]; 
					for(Comment cmt: thought.getComments()){
						cmtValues[x++] = cmt.putValues();
					}
					getActivity().getContentResolver().bulkInsert(CommentDAO.CONTENT_URI, cmtValues);
				}
				/* tags */
				if(null != thought.getTags()){
					int x = 0;
					ContentValues[] thoughtTagValues = new ContentValues[thought.getTags().size()];
					for(Tag t: thought.getTags()){
						ContentValues value = new ContentValues();
						value.put("thought_id", thought.getId());
						value.put("tag_id", t.getId());
						thoughtTagValues[x++] = value;
					}
					getActivity().getContentResolver().bulkInsert(ThoughtTagDAO.CONTENT_URI, thoughtTagValues);
				}
				/* UI */
				ThoughtView v = new ThoughtView(thought.getId(), thought.getUser().getUserName(), thought.getContent(), 
						thought.getImage(), thought.getUserId(), thought.getBoost(), thought.getComments().size(), 
						thought.getIsUserBoost());
				newViews.add(v);
			}
			/* Inserting thoughts */
			adapter.clear();
			adapter.addAll(newViews);
			/* Update UI */
			updateListView();
			onLoadFinished();
		} else {
			onLoadFinished();
			Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.hint_network_issue), Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	public void onBoostResult(JSONObject result) {
		if(null != result){	
			try {
				int id = result.getInt("id");
				int total = result.getInt("total");
				int action = result.getInt("action");
				for(int i = 0; i<adapter.getCount();i++){					
					ThoughtView view = adapter.getItem(i);
					if(id == view.id){
						view.isUserBoost = action;
						view.boost = total;
						break;
					}
				}
				ContentValues values = new ContentValues();
				values.put("isUserBoost", action);
				values.put("boost", total);
				String where = "_id="+id;				
				/* Update HomeThought & HotThought if this thought is also there */
				updateListView();
				ContentResolver resolver = getActivity().getContentResolver();
				resolver.update(HotThoughtDAO.CONTENT_URI, values, where, null);
				resolver.update(HomeThoughtDAO.CONTENT_URI, values, where, null);
				Intent homeIntent = new Intent(MyApp.BROADCAST_BOOST), hotIntent = new Intent(MyApp.BROADCAST_BOOST);
				homeIntent.putExtra(MyApp.INTENT_MSG_UPDATE_HOME, true);
				hotIntent.putExtra(MyApp.INTENT_MSG_UPDATE_HOT, true);
				LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(homeIntent);
				LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(hotIntent);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			updateListView();
		} else {
			Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.hint_network_issue), Toast.LENGTH_LONG).show();			
		}
	}
	
	private void updateListView(){
		adapter.notifyDataSetChanged();
	}	

	public class ThoughtsResultAdapter extends ArrayAdapter<ThoughtView> {

		public ThoughtsResultAdapter(Context context, int resource, List<ThoughtView> thoughts) {
			super(context, resource, thoughts);
		}
		
		@Override
		public boolean isEnabled(int position) {
			return false;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return super.getCount();
		}
		
		@Override
		public View getView(int position, View view, ViewGroup parent) {
	
			ThoughtView t = getItem(position);
			
			ViewHolder holder;
			if(null == view){
				LayoutInflater inflater = LayoutInflater.from(getContext());
		        view = inflater.inflate(R.layout.item_thought, parent, false);
		        holder = new ViewHolder();
				holder.titleView = (TextView)view.findViewById(R.id.thought_list_title);
				holder.contentView = (TextView)view.findViewById(R.id.thought_list_content);
				holder.boostsNum = (TextView)view.findViewById(R.id.thought_list_boostsnum);
				holder.commentsNum = (TextView)view.findViewById(R.id.thought_list_commentsnum);
				holder.thoughtIdView = (TextView)view.findViewById(R.id.thought_list_id);
				holder.imagePathView = (TextView) view.findViewById(R.id.thought_list_image);
				holder.isUserBoostView = (TextView) view.findViewById(R.id.thought_list_isUserBoost);
				holder.userIdView = (TextView) view.findViewById(R.id.thought_list_poster_id);
				holder.postFigure = (ImageView)view.findViewById(R.id.thought_list_pic);
				holder.messageButton = (ImageView)view.findViewById(R.id.thought_list_message);
				holder.boostButton = (ImageView)view.findViewById(R.id.thought_list_boost);
				holder.commentButton = (ImageView)view.findViewById(R.id.thought_list_comment);
				holder.shareButton = (ImageView)view.findViewById(R.id.thought_list_share);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			
			holder.titleView.setText(t.title);
			holder.contentView.setText(t.content);
			holder.boostsNum.setText(t.boost+"");
			holder.commentsNum.setText(t.cmt+"");
			holder.thoughtIdView.setText(t.id+"");
			holder.imagePathView.setText(t.path);
			holder.isUserBoostView.setText(t.isUserBoost+"");
			holder.userIdView.setText(t.userId);
			
			holder.postFigure.setOnClickListener(new FunctionOnClickListener(POST));
			holder.messageButton.setOnClickListener(new FunctionOnClickListener(MESSAGE));
			holder.boostButton.setOnClickListener(new FunctionOnClickListener(BOOST));
			holder.commentButton.setOnClickListener(new FunctionOnClickListener(COMMENT));
			holder.shareButton.setOnClickListener(new FunctionOnClickListener(SHARE));
			
			if(t.isUserBoost == 1){
				holder.boostButton.setImageResource(R.drawable.thoughts_rocket_up_boost);
			} else {
				holder.boostButton.setImageResource(R.drawable.thoughts_rocket_up_normal);
			}
			
			String fileName;
			fileName = holder.imagePathView.getText().toString();
			String url = HTTPUtil.getInstance().composePreURL(getActivity()) 
					+ getResources().getString(R.string.url_down_img)
					+ "?path=" + fileName;
			DisplayImageOptions opt = new DisplayImageOptions.Builder().showImageOnFail(R.drawable.no_image).cacheInMemory(true).cacheOnDisk(true).build();
			ImageLoader.getInstance().displayImage(url, holder.postFigure, opt, new SimpleImageLoadingListener(){
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					RelativeLayout layout = (RelativeLayout) view.getParent();
					ProgressBar spinner = (ProgressBar) layout.findViewById(R.id.thought_list_spinner);
					spinner.setVisibility(View.VISIBLE);
					spinner.setProgress(10);
				}

				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
					RelativeLayout layout = (RelativeLayout) view.getParent();
					ProgressBar spinner = (ProgressBar) layout.findViewById(R.id.thought_list_spinner);
					spinner.setVisibility(View.INVISIBLE);
				}

				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					RelativeLayout layout = (RelativeLayout) view.getParent();
					ProgressBar spinner = (ProgressBar) layout.findViewById(R.id.thought_list_spinner);
					spinner.setVisibility(View.INVISIBLE);
				}
			}, new ImageLoadingProgressListener() {
				
				/**
				 * Will only be called when cacheInDisk is enabled
				 */
				@Override
				public void onProgressUpdate(String imageUri, View view, int current,
						int total) {
					RelativeLayout layout = (RelativeLayout) view.getParent();
					ProgressBar spinner = (ProgressBar) layout.findViewById(R.id.thought_list_spinner);
					spinner.setProgress(Math.round(100.0f * current / total));
				}
			});			
			
			return view;
		}
		
	}

	public class FunctionOnClickListener implements OnClickListener {

		private int buttonIndex = 1;
		public FunctionOnClickListener(int i) {	
			buttonIndex = i;
		}

		@Override
		public void onClick(View v) {
			RelativeLayout root;
			if(null != v.getParent().getParent() && v.getParent().getParent() instanceof RelativeLayout)
				root = (RelativeLayout) v.getParent().getParent();
			else root = (RelativeLayout) v.getParent();
			TextView idView = (TextView) root.findViewById(R.id.thought_list_id);			
			Integer id = Integer.parseInt(idView.getText().toString());
			TextView imageView = (TextView) root.findViewById(R.id.thought_list_image);
			String postPath = imageView.getText().toString();
			TextView titleView = (TextView) root.findViewById(R.id.thought_list_title);
			String userName = titleView.getText().toString();
			TextView contentView = (TextView) root.findViewById(R.id.thought_list_content);
			String content = contentView.getText().toString();
			TextView userIdView = (TextView) root.findViewById(R.id.thought_list_poster_id);
			String userId = userIdView.getText().toString();
			switch (buttonIndex) {
			case POST:
				Intent imageIntent = new Intent();
				imageIntent.setClass(SearchThoughtsResultFragment.this.getActivity(), ImagePage.class);
				String imageUrl = HTTPUtil.getInstance().composePreURL(getActivity())
						+ getResources().getString(R.string.url_down_img)
						+ "?path=" + postPath;
				imageIntent.putExtra("url", imageUrl);
				imageIntent.putExtra("id", id);
				imageIntent.putExtra("type", "search");
				for(Thought t: thoughtList){
					if(t.getId() == id){
						imageIntent.putExtra("groupId",t.getGroupId());
						break;
					}
				}
				startActivity(imageIntent);
				break;
			case MESSAGE:
				String localId = (String) SPUtil.get(getActivity(), MyApp.PREF_TYPE_LOGIN, MyApp.LOGIN_ID, "", String.class);
				if(localId.equals(userId)){
					Toast.makeText(getActivity(), "Sorry but you can't talk to yourself!", Toast.LENGTH_SHORT).show();
				} else if("".equals(SPUtil.get(SearchThoughtsResultFragment.this.getActivity(), MyApp.PREF_TYPE_LOGIN, MyApp.LOGIN_USERNAME, "", String.class))){
					Toast.makeText(SearchThoughtsResultFragment.this.getActivity(), getResources().getString(R.string.hint_empty_username), Toast.LENGTH_SHORT).show();
					return;
				} else {
					Intent chatIntent = new Intent();
					chatIntent.setClass(SearchThoughtsResultFragment.this.getActivity(), ChatPage.class);
					chatIntent.putExtra("guestId", userId);
					chatIntent.putExtra("userName", userName);
					chatIntent.putExtra("notFromMain", true);
					startActivity(chatIntent);
				}
				break;
			case BOOST:				
				new BoostAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 
						HTTPUtil.getInstance().composePreURL(getActivity()) + getResources().getString(R.string.url_boost_thought), 
						SearchThoughtsResultFragment.this, id, SPUtil.get(getActivity(), MyApp.PREF_TYPE_LOGIN, MyApp.LOGIN_ID, null, String.class));
				break;
			case COMMENT:
				Intent commentIntent = new Intent();
				commentIntent.setClass(SearchThoughtsResultFragment.this.getActivity(), CommentPage.class);
				String posterAvatar = userId;
				String avatarUrl = HTTPUtil.getInstance().composePreURL(getActivity())
						+ getResources().getString(R.string.url_down_img)
						+ "?path=" + posterAvatar;
				String postUrl = HTTPUtil.getInstance().composePreURL(getActivity())
						+ getResources().getString(R.string.url_down_img)
						+ "?path=" + postPath;
				commentIntent.putExtra("avatarUrl", avatarUrl);
				commentIntent.putExtra("postUrl", postUrl);
				commentIntent.putExtra("id", id);
				commentIntent.putExtra("userName", userName);
				commentIntent.putExtra("content", content);
				startActivity(commentIntent);
				break;
			case SHARE:				
				break;
			default:
				break;
			}
		}
	}

}
