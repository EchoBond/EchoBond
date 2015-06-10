package com.echobond.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.MotionEvent;
import android.widget.ListAdapter;
import android.widget.ListView;

public class PullUpListView extends ListView {

	private XViewFooter footer;
	
	public PullUpListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		// TODO Auto-generated method stub
		super.setAdapter(adapter);
	}
	
	@SuppressLint("ClickableViewAccessibility") 
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return super.onTouchEvent(ev);
	}
	
	@Override
	public void setOnScrollListener(OnScrollListener l) {
		// TODO Auto-generated method stub
		super.setOnScrollListener(l);
	}
	
}
