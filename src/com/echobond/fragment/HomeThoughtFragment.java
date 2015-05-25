package com.echobond.fragment;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.R;
import com.echobond.activity.MainPage;
import com.echobond.connector.LoadThoughtAsyncTask;
import com.echobond.dao.CommentDAO;
import com.echobond.dao.HomeThoughtDAO;
import com.echobond.dao.ThoughtTagDAO;
import com.echobond.dao.UserDAO;
import com.echobond.entity.Thought;
import com.echobond.entity.User;
import com.echobond.intf.LoadThoughtCallback;
import com.echobond.util.HTTPUtil;
import com.echobond.util.JSONUtil;
import com.echobond.util.SPUtil;
import com.echobond.widget.XListView;
import com.echobond.widget.XListView.IXListViewListener;
import com.google.android.gms.drive.internal.k;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @author Luck
 *
 */
public class HomeThoughtFragment extends Fragment implements AdapterView.OnItemClickListener, IXListViewListener, LoadThoughtCallback, LoaderCallbacks<Cursor>{
	
	private final String[] from = new String[] {"image", "username", "boost", "num_of_cmt", "content", "_id"};
	private final int[] to = new int[] {R.id.thought_list_pic, R.id.thought_list_title, R.id.thought_list_boostsnum, R.id.thought_list_commentsnum, 
			R.id.thought_list_content, R.id.thought_list_id};
	private SimpleCursorAdapter adapter;
	private ThoughtAdapter adapter2;
	private XListView mListView;
	private UserDAO userDAO;
	private CommentDAO commentDAO;
	private ThoughtTagDAO thoughtTagDAO;
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
		adapter2 = new ThoughtAdapter(getActivity(), R.layout.item_thoughts_list, null, from, to, 0);
		mListView.setAdapter(adapter2);
		mListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
		mListView.setOnItemClickListener(this);
		mListView.setXListViewListener(this);
		mListView.setPullLoadEnable(true);
		
		userDAO = new UserDAO(getActivity());
		currentLimit = LIMIT_INIT;
		lastLoadTime = 0;
		commentDAO = new CommentDAO(getActivity());
		thoughtTagDAO = new ThoughtTagDAO(getActivity());
		getLoaderManager().initLoader(MainPage.LOADER_HOME, null, this);
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

		@Override
		public void bindView(View arg0, Context arg1, Cursor arg2) {
			// TODO Auto-generated method stub
			super.bindView(arg0, arg1, arg2);
		}
		
		@SuppressLint("InflateParams") 
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_thoughts_list, null);
			}
			
			TextView titleView = (TextView)convertView.findViewById(R.id.thought_list_title);
			TextView contentView = (TextView)convertView.findViewById(R.id.thought_list_content);
			TextView boostsNum = (TextView)convertView.findViewById(R.id.thought_list_boostsnum);
			TextView commentsNum = (TextView)convertView.findViewById(R.id.thought_list_commentsnum);
			TextView thoughtIdView = (TextView)convertView.findViewById(R.id.thought_list_id);
			
			ImageView postFigure = (ImageView)convertView.findViewById(R.id.thought_list_pic);
			ImageView messageButton = (ImageView)convertView.findViewById(R.id.thought_list_message);
			ImageView boostButton = (ImageView)convertView.findViewById(R.id.thought_list_boost);
			ImageView commentButton = (ImageView)convertView.findViewById(R.id.thought_list_comment);
			ImageView shareButton = (ImageView)convertView.findViewById(R.id.thought_list_share);
			
			String ctt = contentView.getText().toString();
			messageButton.setOnClickListener(new FunctionOnClickListener(MESSAGE, ctt));
			boostButton.setOnClickListener(new FunctionOnClickListener(BOOST, ctt));
			commentButton.setOnClickListener(new FunctionOnClickListener(COMMENT, ctt));
			shareButton.setOnClickListener(new FunctionOnClickListener(SHARE, ctt));
			
			return super.getView(position, convertView, parent);
		}
	}
	
	public class FunctionOnClickListener implements OnClickListener {

		private int buttonIndex = 1;
		private String ctt;
		public FunctionOnClickListener(int i, String ctt) {	
			buttonIndex = i;
			this.ctt = ctt;
		}

		@Override
		public void onClick(View v) {
//			switch (buttonIndex) {
//			case MESSAGE:
//				Toast.makeText(getActivity().getApplicationContext(), "Thank you for your message. " + ctt, Toast.LENGTH_SHORT).show();
//				break;
//			case BOOST:
//				Toast.makeText(getActivity().getApplicationContext(), "Thank you for your boost. " + ctt, Toast.LENGTH_SHORT).show();
//				break;
//			case COMMENT:
//				Toast.makeText(getActivity().getApplicationContext(), "Thank you for your contact. " + ctt, Toast.LENGTH_SHORT).show();
//				break;
//			case SHARE:
//				Toast.makeText(getActivity().getApplicationContext(), "Thank you for your sharing! " + ctt, Toast.LENGTH_SHORT).show();
//				break;
//			default:
//				break;
//			}
			RelativeLayout l = (RelativeLayout) v.getParent().getParent();
			TextView t = (TextView) l.findViewById(R.id.thought_list_id);
			Toast.makeText(getActivity().getApplicationContext(), t.getText(), Toast.LENGTH_SHORT).show();
			
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
			User user = new User();
			user.setId((String) SPUtil.get(getActivity(), "login", "loginUser_id", null, String.class));
			new LoadThoughtAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 
					HTTPUtil.getInstance().composePreURL(getActivity()) + getResources().getString(R.string.url_load_thoughts), 
					LoadThoughtAsyncTask.LOAD_T_HOME, this, 0, currentLimit, user);
		} else {
			onLoadFinished();
		}
	}

	@Override
	public void onLoadMore() {
		if(System.currentTimeMillis() - lastLoadTime > LOAD_INTERVAL){
			lastLoadTime = System.currentTimeMillis();
			User user = new User();
			user.setId((String) SPUtil.get(getActivity(), "login", "loginUser_id", null, String.class));
			currentLimit += LIMIT_INCREMENT;
			new LoadThoughtAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 
					HTTPUtil.getInstance().composePreURL(getActivity())+getResources().getString(R.string.url_load_thoughts), 
					LoadThoughtAsyncTask.LOAD_T_HOME, this, 0, currentLimit, user);
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
			getActivity().getContentResolver().bulkInsert(HomeThoughtDAO.CONTENT_URI, contentValues);
			onLoadFinished();
		} else {
			onLoadFinished();
			Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.network_issue), Toast.LENGTH_LONG).show();
		}
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
		adapter2.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		adapter2.swapCursor(null);
	}
	
}
