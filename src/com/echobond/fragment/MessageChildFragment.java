package com.echobond.fragment;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.R;
import com.echobond.activity.ChatPage;
import com.echobond.application.MyApp;
import com.echobond.connector.UserMsgAsyncTask;
import com.echobond.dao.ChatDAO;
import com.echobond.entity.User;
import com.echobond.entity.UserMsg;
import com.echobond.intf.UserMsgCallback;
import com.echobond.util.HTTPUtil;
import com.echobond.util.JSONUtil;
import com.echobond.util.SPUtil;
import com.echobond.widget.XListView;
import com.echobond.widget.XListView.IXListViewListener;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 
 * @author aohuijun
 *
 */
public class MessageChildFragment extends Fragment implements IXListViewListener, LoaderCallbacks<Cursor>, OnClickListener, UserMsgCallback{
	
	private XListView messagesList;
	private MsgListCursorAdapter adapter;
	private String userId;
	
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			updateUI();			
		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View messageChildView = inflater.inflate(R.layout.fragment_child_message, container, false);

		adapter = new MsgListCursorAdapter(this.getActivity(), R.layout.item_message, null, 0);
		
		messagesList = (XListView)messageChildView.findViewById(R.id.list_messages);
		messagesList.setAdapter(adapter);
		messagesList.setOverScrollMode(View.OVER_SCROLL_NEVER);
		messagesList.setPullLoadEnable(false);
		messagesList.setXListViewListener(this);
		
		userId = (String) SPUtil.get(getActivity(), MyApp.PREF_TYPE_LOGIN, MyApp.LOGIN_ID, "", String.class);
		getLoaderManager().initLoader(MyApp.LOADER_MSG_LIST, null, this);
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, new IntentFilter("msgListUpdate"));
		return messageChildView;
	}
	@Override
	public void onResume() {
		super.onResume();
		updateUI();
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
	}
	
	public class MsgListCursorAdapter extends CursorAdapter{

		private int layout;
		private LayoutInflater inflater;
		
		public MsgListCursorAdapter(Context context, int layout, Cursor c, int flags) {
			super(context, c, flags);
			this.layout = layout;
			this.inflater = LayoutInflater.from(context);
		}

		@Override
		public void bindView(View view, Context ctx, Cursor c) {
			String content = c.getString(c.getColumnIndex("content"));
			if(content.length() > 10){
				content = content.substring(0, 10);
				content += "...";
			}			
			String senderId = c.getString(c.getColumnIndex("sender_id"));
			String recverId = c.getString(c.getColumnIndex("recver_id"));
			String userName = c.getString(c.getColumnIndex("username"));
			String time = c.getString(c.getColumnIndex("time"));
			Integer isRead = c.getInt(c.getColumnIndex("is_read"));
			
			TextView contentView = (TextView) view.findViewById(R.id.item_message_text);
			TextView titleView = (TextView) view.findViewById(R.id.item_message_title);
			TextView timeView = (TextView)view.findViewById(R.id.item_message_time);
			ImageView avatarView = (ImageView) view.findViewById(R.id.item_message_pic);
			TextView guestIdView = (TextView) view.findViewById(R.id.item_message_guestId);
			ImageView indicatorView = (ImageView) view.findViewById(R.id.item_message_indicator);
			
			contentView.setText(content);
			titleView.setText(userName);
			timeView.setText(time);
			
			if(senderId.equals(userId)){
				guestIdView.setText(recverId);
			} else {
				guestIdView.setText(senderId);
			}
			
			if(isRead == 0){
				indicatorView.setImageDrawable(getResources().getDrawable(R.drawable.button_notification));
			} else {
				indicatorView.setImageDrawable(null);
			}
			
			String url = HTTPUtil.getInstance().composePreURL(MessageChildFragment.this.getActivity())
					+ getResources().getString(R.string.url_down_img) + "?path=" + guestIdView.getText().toString();
			ImageLoader.getInstance().displayImage(url, avatarView);
			view.setOnClickListener(MessageChildFragment.this);
		}

		@Override
		public View newView(Context ctx, Cursor c, ViewGroup parent) {
			View view = inflater.inflate(layout, parent, false);
			return view;
		}
		
	}

	@Override
	public Loader<Cursor> onCreateLoader(int loader, Bundle arg1) {
		switch(loader){
		case MyApp.LOADER_MSG_LIST:
			Uri uri = ChatDAO.CONTENT_URI;
			String[] args = new String[]{(String) SPUtil.get(getActivity(), MyApp.PREF_TYPE_LOGIN, MyApp.LOGIN_ID, "", String.class)};
			return new CursorLoader(getActivity(), uri, null, null, args, null);
		}
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor c) {
		adapter.swapCursor(c);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> l) {
		adapter.swapCursor(null);
	}

	@Override
	public void onRefresh() {
		String url = HTTPUtil.getInstance().composePreURL(getActivity()) + getResources().getString(R.string.url_load_msg);
		User user = new User();		
		user.setId(userId);
		Integer limit = 1000, offset = 0;
		new UserMsgAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url, this, UserMsgAsyncTask.MSG_LOAD_ALL, 
			user, limit, offset);
	}

	@Override
	public void onLoadMore() {
		onLoadMore();
	}
	
	public void onLoadFinished(){
		messagesList.stopLoadMore();
		messagesList.stopRefresh();
	}

	@Override
	public void onClick(View v) {
		TextView guestIdView = (TextView) v.findViewById(R.id.item_message_guestId);
		TextView userNameView = (TextView) v.findViewById(R.id.item_message_title);
		
		Intent intent = new Intent();
		intent.setClass(getActivity(), ChatPage.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);;
		intent.putExtra("guestId", guestIdView.getText().toString());
		intent.putExtra("userName", userNameView.getText().toString());
		startActivity(intent);
	}

	@Override
	public void onSendResult(JSONObject result) {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onLoadResult(JSONObject result) {
		if(null != result){
			TypeToken<ArrayList<UserMsg>> token = new TypeToken<ArrayList<UserMsg>>(){};
			ArrayList<UserMsg> msgList = new ArrayList<UserMsg>();
			try {
				msgList = (ArrayList<UserMsg>) JSONUtil.fromJSONArrayToList(result.getJSONArray("msgList"), token);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			ContentValues[] values = new ContentValues[msgList.size()];
			int i = 0;
			for (UserMsg msg : msgList) {
				values[i++] = msg.putValues();
			}
			getActivity().getContentResolver().bulkInsert(ChatDAO.CONTENT_URI, values);
			updateUI();
		}
		onLoadFinished();
	}
	
	private void updateUI(){
		String[] args = new String[]{userId};
		Cursor cursor = getActivity().getContentResolver().query(ChatDAO.CONTENT_URI, null, null, args, null);
		adapter.swapCursor(cursor);
		adapter.notifyDataSetChanged();
	}
}