package com.echobond.fragment;

import org.json.JSONObject;

import com.echobond.R;
import com.echobond.activity.ChatPage;
import com.echobond.application.MyApp;
import com.echobond.connector.UserMsgAsyncTask;
import com.echobond.dao.ChatDAO;
import com.echobond.entity.User;
import com.echobond.intf.UserMsgCallback;
import com.echobond.util.HTTPUtil;
import com.echobond.util.SPUtil;
import com.echobond.widget.XListView;
import com.echobond.widget.XListView.IXListViewListener;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @author aohuijun
 *
 */
public class MessageChildFragment extends Fragment implements IXListViewListener, LoaderCallbacks<Cursor>, OnClickListener, UserMsgCallback{
	
	private XListView messagesList;
	private MsgListCursorAdapter adapter;
	private String userId;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View messageChildView = inflater.inflate(R.layout.fragment_child_message, container, false);

		adapter = new MsgListCursorAdapter(this.getActivity(), R.layout.item_message, null, 0);
		
		messagesList = (XListView)messageChildView.findViewById(R.id.list_messages);
		messagesList.setAdapter(adapter);
		messagesList.setOverScrollMode(View.OVER_SCROLL_NEVER);
		//messagesList.setOnItemClickListener(new ClickListener());
		messagesList.setXListViewListener(this);
		userId = (String) SPUtil.get(getActivity(), MyApp.PREF_TYPE_LOGIN, MyApp.LOGIN_ID, "", String.class);
		getLoaderManager().initLoader(MyApp.LOADER_MSG_LIST, null, this);
		
		return messageChildView;
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
			String senderId = c.getString(c.getColumnIndex("sender_id"));
			String recverId = c.getString(c.getColumnIndex("recver_id"));
			String userName = c.getString(c.getColumnIndex("username"));
			String time = c.getString(c.getColumnIndex("time"));
			
			TextView contentView = (TextView) view.findViewById(R.id.item_message_text);
			TextView titleView = (TextView) view.findViewById(R.id.item_message_title);
			ImageView avatarView = (ImageView) view.findViewById(R.id.item_message_pic);
			TextView guestIdView = (TextView) view.findViewById(R.id.item_message_guestId);
			
			contentView.setText(content);
			titleView.setText(userName);
			if(senderId.equals(userId)){
				guestIdView.setText(recverId);
			} else {
				guestIdView.setText(senderId);
			}
			
			view.setOnClickListener(MessageChildFragment.this);
		}

		@Override
		public View newView(Context ctx, Cursor c, ViewGroup parent) {
			View view = inflater.inflate(layout, parent);
			return view;
		}
		
	}
	
	public class ClickListener implements AdapterView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Toast.makeText(getActivity().getApplicationContext(), "Message " + position, Toast.LENGTH_SHORT).show();
			Intent intent = new Intent();
			intent.setClass(getActivity(), ChatPage.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);;
			startActivity(intent);
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

	@Override
	public void onClick(View v) {
		LinearLayout root = (LinearLayout) v;
		TextView guestIdView = (TextView) v.findViewById(R.id.item_message_guestId);
		
		Intent intent = new Intent();
		intent.setClass(getActivity(), ChatPage.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);;
		intent.putExtra("guestId", guestIdView.getText().toString());
		startActivity(intent);
	}

	@Override
	public void onSendResult(JSONObject result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadResult(JSONObject result) {
		if(null != result){
			
		}		
	}
}