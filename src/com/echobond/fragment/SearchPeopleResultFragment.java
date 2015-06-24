package com.echobond.fragment;

import com.echobond.R;
import com.echobond.widget.XListView;
import com.echobond.widget.XListView.IXListViewListener;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
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
public class SearchPeopleResultFragment extends Fragment implements IXListViewListener {
	
	private XListView searchPeopleResultList;
	private PeopleResultAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View searchPeopleResultView = inflater.inflate(R.layout.fragment_search_result_people, container, false);
		adapter = new PeopleResultAdapter(getActivity(), R.layout.item_people, null, 0);
		searchPeopleResultList = (XListView)searchPeopleResultView.findViewById(R.id.search_result_list_people);
		searchPeopleResultList.setOverScrollMode(View.OVER_SCROLL_NEVER);
		searchPeopleResultList.setPullRefreshEnable(false);
		searchPeopleResultList.setAdapter(adapter);
		
		return searchPeopleResultView;
	}

	public class PeopleResultAdapter extends CursorAdapter {

		private LayoutInflater inflater;
		private int layout;
		
		public PeopleResultAdapter(Context context, int layout, Cursor c, int flags) {
			super(context, c, flags);
			this.layout = layout;
			this.inflater = LayoutInflater.from(context);
		}

		@Override
		public void bindView(View convertView, Context ctx, Cursor c) {
			
			ImageView peoplePic = (ImageView)convertView.findViewById(R.id.item_people_pic);
			TextView peopleTitle = (TextView)convertView.findViewById(R.id.item_people_title);
			TextView peopleBio = (TextView)convertView.findViewById(R.id.item_people_content);
			TextView peopleGender = (TextView)convertView.findViewById(R.id.item_people_gender);
			
			peopleTitle.setText("");
			peopleBio.setText("");
			peopleGender.setText("");

		}

		@Override
		public View newView(Context context, Cursor c, ViewGroup parent) {
			return inflater.inflate(layout, parent, false);
		}
		
	}
	
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		
	}
	
}
