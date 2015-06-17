package com.echobond.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Scroller;
/**
 * 
 * @author aohuijun
 *
 */
public class PullUpListView extends ListView {

	private Scroller scroller;
	
	private XViewFooter footer;
	private boolean mEnablePullLoad;	// can be pull up or not
	private boolean mPullLoading;	// is loading or not
	private boolean mIsFooterReady = false;	// load the footer or not
	
	public PullUpListView(Context context) {
		super(context);
		initWithContext(context);
	}

	public PullUpListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initWithContext(context);
	}
	
	public PullUpListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	private void initWithContext(Context context) {
		// TODO Auto-generated method stub
		
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
