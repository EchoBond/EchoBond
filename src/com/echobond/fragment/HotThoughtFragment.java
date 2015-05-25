package com.echobond.fragment;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.R;
import com.echobond.activity.MainPage;
import com.echobond.connector.LoadThoughtAsyncTask;
import com.echobond.dao.CommentDAO;
import com.echobond.dao.HotThoughtDAO;
import com.echobond.dao.ThoughtTagDAO;
import com.echobond.dao.UserDAO;
import com.echobond.entity.Thought;
import com.echobond.fragment.HomeThoughtFragment.FunctionOnClickListener;
import com.echobond.fragment.HomeThoughtFragment.ThoughtAdapter;
import com.echobond.fragment.HomeThoughtFragment.ViewHolder;
import com.echobond.intf.LoadThoughtCallback;
import com.echobond.util.HTTPUtil;
import com.echobond.util.JSONUtil;
import com.echobond.widget.XListView;
import com.echobond.widget.XListView.IXListViewListener;
import com.google.gson.reflect.TypeToken;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;
/**
 * 
 * @author Luck
 *
 */
public class HotThoughtFragment extends Fragment implements AdapterView.OnItemClickListener, IXListViewListener, LoadThoughtCallback, LoaderCallbacks<Cursor>{
	
	private final String[] from = new String[] {"image", "username", "boost", "num_of_cmt", "content"};
	private final int[] to = new int[] {R.id.thought_list_pic, R.id.thought_list_title, R.id.thought_list_boostsnum, R.id.thought_list_commentsnum, R.id.thought_list_content};
	private SimpleCursorAdapter adapter;
	private XListView mListView;
	private UserDAO userDAO;
	private CommentDAO commentDAO;
	private ThoughtTagDAO thoughtTagDAO;
	public final class ViewHolder {
		public ImageView messageButton, boostButton, commentButton, shareButton;
	}
	private static final int MESSAGE = 1;
	private static final int BOOST = 2;
	private static final int COMMENT = 3;
	private static final int SHARE = 4;
	private int currentLimit;
	private static final int LIMIT_INIT = 10;
	private static final int LIMIT_INCREMENT = 10;
	private static final long LOAD_INTERVAL = 2000;
	private long lastLoadTime;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View thoughtView = inflater.inflate(R.layout.fragment_main_thoughts, container, false);
		mListView = (XListView)thoughtView.findViewById(R.id.list_thoughts);

		adapter = new SimpleCursorAdapter(getActivity(), R.layout.item_thoughts_list, null, from, to, 0); 
		ThoughtAdapter adapter2 = new ThoughtAdapter(getActivity(), R.layout.item_thoughts_list, null, from, to, 0);
		mListView.setAdapter(adapter);
		mListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
		mListView.setOnItemClickListener(this);
		mListView.setXListViewListener(this);
		mListView.setPullLoadEnable(true);
		userDAO = new UserDAO(getActivity());
		currentLimit = LIMIT_INIT;
		lastLoadTime = 0;
		commentDAO = new CommentDAO(getActivity());
		thoughtTagDAO = new ThoughtTagDAO(getActivity());
		getLoaderManager().initLoader(MainPage.LOADER_HOT, null, this);
		return thoughtView;
	}
	
	public class ThoughtAdapter extends SimpleCursorAdapter {

		private LayoutInflater inflater;
		
		public ThoughtAdapter(Context context, int layout, Cursor c,
				String[] from, int[] to, int flags) {
			super(context, layout, c, from, to, flags);
			this.inflater = LayoutInflater.from(context);
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return super.getCount();
		}
		
		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = new ViewHolder();
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_thoughts_list, null);
				holder.messageButton = (ImageView)convertView.findViewById(R.id.thought_list_message);
				holder.boostButton = (ImageView)convertView.findViewById(R.id.thought_list_boost);
				holder.commentButton = (ImageView)convertView.findViewById(R.id.thought_list_comment);
				holder.shareButton = (ImageView)convertView.findViewById(R.id.thought_list_share);
				convertView.setTag(holder);
			}else {
				holder = (ViewHolder)convertView.getTag();
			}
			holder.messageButton.setOnClickListener(new FunctionOnClickListener(MESSAGE));
			return convertView;
		}
	}
	
	public class FunctionOnClickListener implements OnClickListener {

		private int buttonIndex = 1;
		public FunctionOnClickListener(int i) {	buttonIndex = i;	}

		@Override
		public void onClick(View v) {
			switch (buttonIndex) {
			case MESSAGE:
				Toast.makeText(getActivity().getApplicationContext(), "Thank you for your message. ", Toast.LENGTH_LONG).show();
				break;
			case BOOST:
				Toast.makeText(getActivity().getApplicationContext(), "Thank you for your boost. ", Toast.LENGTH_LONG).show();
				break;
			case COMMENT:
				Toast.makeText(getActivity().getApplicationContext(), "Thank you for your contact. ", Toast.LENGTH_LONG).show();
				break;
			case SHARE:
				Toast.makeText(getActivity().getApplicationContext(), "Thank you for your sharing! ", Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}
			
		}
		
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	}
	
	public void onLoadFinished() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
	}

	@Override
	public void onRefresh() {
		if(System.currentTimeMillis() - lastLoadTime > LOAD_INTERVAL){
			lastLoadTime = System.currentTimeMillis();
			new LoadThoughtAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
				HTTPUtil.getInstance().composePreURL(getActivity())+getResources().getString(R.string.url_load_thoughts),
				LoadThoughtAsyncTask.LOAD_T_HOT,this,0,currentLimit);
		} else {
			onLoadFinished();
		}
	}

	@Override
	public void onLoadMore() {
		if(System.currentTimeMillis() - lastLoadTime > LOAD_INTERVAL){
			lastLoadTime = System.currentTimeMillis();
			currentLimit += LIMIT_INCREMENT;
			new LoadThoughtAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
				HTTPUtil.getInstance().composePreURL(getActivity())+getResources().getString(R.string.url_load_thoughts),
				LoadThoughtAsyncTask.LOAD_T_HOT,this,0,currentLimit);
		} else {
			onLoadFinished();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onLoadThoughtResult(JSONObject result) {
		if(null != result){
			TypeToken<ArrayList<Thought>> token = new TypeToken<ArrayList<Thought>>(){};
			ArrayList<Thought> thoughts = null;
			try {
				thoughts = (ArrayList<Thought>) JSONUtil.fromJSONArrayToList(result.getJSONArray("thoughts"), token);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			ContentValues[] contentValues = new ContentValues[thoughts.size()];
			int i = 0;
			/* Loading thoughts */
			for (Thought thought : thoughts) {
				/* thoughts */
				contentValues[i++] = thought.putValues();
				/* author */
				userDAO.addUser(thought.getUser());
				/* comments */
				commentDAO.addComments(thought.getComments());
				/* tags */
				thoughtTagDAO.addThoughtTags(thought.getId(), thought.getTags());
			}
			/* Inserting thoughts */
			getActivity().getContentResolver().bulkInsert(HotThoughtDAO.CONTENT_URI, contentValues);
			onLoadFinished();
		} else {
			onLoadFinished();
			Toast.makeText(getActivity(), getResources().getString(R.string.network_issue), Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int loader, Bundle arg1) {
		switch(loader){
		case MainPage.LOADER_HOT:
			Uri uri = HotThoughtDAO.CONTENT_URI;
			return new CursorLoader(getActivity(), uri, null, null, null, null);
		}
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		adapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		adapter.swapCursor(null);
	}

	
}
