package com.echobond.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.R;
import com.echobond.activity.MainPage;
import com.echobond.dao.HomeThoughtDAO;
import com.echobond.entity.Thought;
import com.echobond.entity.User;
import com.echobond.intf.LoadThoughtCallback;
import com.echobond.util.JSONUtil;
import com.echobond.util.SPUtil;
import com.echobond.widget.XListView;
import com.echobond.widget.XListView.IXListViewListener;
import com.google.gson.reflect.TypeToken;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
/**
 * 
 * @author Luck
 *
 */
public class HomeThoughtFragment extends Fragment implements AdapterView.OnItemClickListener, IXListViewListener, LoadThoughtCallback, LoaderCallbacks<Cursor>{
	
	private final String[] from = new String[] {"pic", "username", "boosts", "comments", "content"};
	private final int[] to = new int[] {R.id.thought_list_pic, R.id.thought_list_title, R.id.thought_list_boosts, R.id.thought_list_comments, R.id.thought_list_content};
	private SimpleCursorAdapter adapter;
	private XListView mListView;
	private Handler handler;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View thoughtView = inflater.inflate(R.layout.fragment_main_thoughts, container, false);
		mListView = (XListView)thoughtView.findViewById(R.id.list_thoughts);
		User user = new User();
		user.setId((String) SPUtil.get(getActivity(), "login", "loginUser_id", null, String.class));
//		new LoadThoughtAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,HTTPUtil.getInstance().composePreURL(getActivity())+getResources().getString(R.string.url_load_thoughts),
//				LoadThoughtAsyncTask.LOAD_T_HOME,this,0,10,user);
		String[] _from = {"image", "username", "boost", "num_of_cmt", "content"};
		adapter = new SimpleCursorAdapter(getActivity(), R.layout.item_thoughts_list, null, _from, to, 0); 
		mListView.setAdapter(adapter);
		mListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
		mListView.setOnItemClickListener(this);				
		getLoaderManager().initLoader(MainPage.LOADER_HOME, null, this);
		return thoughtView;
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	}
	
	public void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
	}

	@Override
	public void onRefresh() {
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				adapter = new SimpleCursorAdapter(getActivity(), R.layout.item_thoughts_list, null, from, to, 0); 
				mListView.setAdapter(adapter);
				onLoad();
			}
		}, 2000);
	}

	@Override
	public void onLoadMore() {
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				adapter.notifyDataSetChanged();
				onLoad();
			}
		}, 2000);
	}

	@Override
	public void onLoadThoughtResult(JSONObject result) {
		List<Map<String, Object>> data = getData(result);
		if(null == data){
			data = new ArrayList<Map<String, Object>>();
		}
		String[] _from = {"image", "userName", "boosts", "numComments", "content"};
		adapter = new SimpleCursorAdapter(getActivity(), R.layout.item_thoughts_list, null, _from, to, 0); 
		mListView.setAdapter(adapter);
		mListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
		mListView.setOnItemClickListener(this);		
	}
	
	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> getData(JSONObject result){
		if(null != result){
			List<Map<String, Object>> thoughtList = new ArrayList<Map<String,Object>>();
			TypeToken<ArrayList<Thought>> token = new TypeToken<ArrayList<Thought>>(){};
			ArrayList<Thought> thoughts = null;
			try {
				thoughts = (ArrayList<Thought>) JSONUtil.fromJSONArrayToList(result.getJSONArray("thoughts"), token);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			for(int i=0;i<thoughts.size();i++){
				Map<String, Object> thoughtMap = new HashMap<String, Object>();
				Thought t = thoughts.get(i);
				if(null != t.getImage() && t.getImage().length()>0){
				}
				else{
					thoughtMap.put("pic", getResources().getDrawable(R.drawable.logo_welcome));
				}
				thoughtMap.put("username", t.getUser().getUserName());
				thoughtMap.put("boost", t.getBoost()+" boosts");
				thoughtMap.put("comments", t.getComments().size()+" comments");
				thoughtMap.put("content", t.getContent());
				thoughtList.add(thoughtMap);
			}
			return thoughtList;
		}
		return null;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int loader, Bundle arg1) {
		switch(loader){
		case MainPage.LOADER_HOME:
			Uri uri = HomeThoughtDAO.CONTENT_URI;
			return new CursorLoader(getActivity(), uri, null, null, null, null);
		}
		return null;
	}
	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		Cursor _cursor = null;
		if(null != cursor && cursor.moveToFirst()){
			if(cursor.getInt(0) > 0)
				_cursor = cursor;
		}
		adapter.swapCursor(_cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		adapter.swapCursor(null);
	}

	
}
