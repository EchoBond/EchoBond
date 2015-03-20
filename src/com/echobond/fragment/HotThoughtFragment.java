package com.echobond.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.R;
import com.echobond.connector.LoadThoughtAsyncTask;
import com.echobond.entity.Thought;
import com.echobond.intf.LoadThoughtCallback;
import com.echobond.util.HTTPUtil;
import com.echobond.util.JSONUtil;
import com.echobond.widget.XListView;
import com.echobond.widget.XListView.IXListViewListener;
import com.google.gson.reflect.TypeToken;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
/**
 * 
 * @author Luck
 *
 */
public class HotThoughtFragment extends Fragment implements AdapterView.OnItemClickListener, IXListViewListener, LoadThoughtCallback{
	
	private final String[] from = new String[] {"pic", "userName", "content"};
	private final int[] to = new int[] {R.id.thought_list_pic, R.id.thought_list_title, R.id.thought_list_content};
	private SimpleAdapter adapter;
	private XListView mListView;
	private Handler handler;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View thoughtView = inflater.inflate(R.layout.fragment_main_thoughts, container, false);
		mListView = (XListView)thoughtView.findViewById(R.id.list_thoughts);
		new LoadThoughtAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,HTTPUtil.getInstance().composePreURL(getActivity())+getResources().getString(R.string.url_load_thoughts),
				LoadThoughtAsyncTask.LOAD_T_HOT,this,0,10);		
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
				adapter = new SimpleAdapter(getActivity(), getData(null), R.layout.item_thoughts_list, from, to);
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
		adapter = new SimpleAdapter(this.getActivity(), data, R.layout.item_thoughts_list, from, to);
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
				thoughtMap.put("userName", t.getUser().getUserName());
				thoughtMap.put("content", t.getContent());
				thoughtList.add(thoughtMap);
			}
			return thoughtList;
		}
		return null;
	}

	
}
