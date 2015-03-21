package com.echobond.widget;

import com.echobond.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
/**
 * 
 * @author aohuijun
 *
 */
@SuppressLint("InflateParams") public class XViewFooter extends LinearLayout {

	public static final int STATE_NORMAL = 0;
	public static final int STATE_READY = 1;
	public static final int STATE_LOADING = 2;
	
	private Context mContext;
	private View mContentView;
	private View mProgressBar;
	private TextView mHintTextView;
	
	public XViewFooter(Context context) {
		super(context);
		initView(context);
	}

	public XViewFooter(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}
	
	private void initView(Context context) { 
		mContext = context;
		LinearLayout moreView = (LinearLayout)LayoutInflater.from(mContext).inflate(R.layout.refresh_pull_up, null);
		addView(moreView);
		moreView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		mContentView = moreView.findViewById(R.id.pullup_footer_content);
		mProgressBar = moreView.findViewById(R.id.pullup_footer_loading);
		mHintTextView = (TextView)moreView.findViewById(R.id.pullup_footer_textview);
		
	}
	
	public void setState(int state) {
//		mContentView.setVisibility(View.INVISIBLE);
		mHintTextView.setVisibility(View.INVISIBLE);
		mProgressBar.setVisibility(View.INVISIBLE);
		mHintTextView.setVisibility(View.INVISIBLE);
		if (state == STATE_READY) {
			mHintTextView.setVisibility(View.VISIBLE);
			mHintTextView.setText(R.string.hint_xview_footer_ready);
		} else if (state == STATE_LOADING) {
			mProgressBar.setVisibility(View.VISIBLE);
		} else {
			mHintTextView.setVisibility(View.VISIBLE);
			mHintTextView.setText(R.string.hint_xview_footer_normal);
		}
	}
	
	public void setBottomMargin(int height) {
		if (height < 0) {
			return;
		}
		LinearLayout.LayoutParams mParams = (LinearLayout.LayoutParams)mContentView.getLayoutParams();
		mParams.bottomMargin = height;
		mContentView.setLayoutParams(mParams);
	}
	
	public int getBottomMargin() {
		LinearLayout.LayoutParams mParams = (LinearLayout.LayoutParams)mContentView.getLayoutParams();
		return mParams.bottomMargin;
	}
	
	/**
	 * normal status
	 */
	public void normal() {
		mHintTextView.setVisibility(View.VISIBLE);
		mProgressBar.setVisibility(View.GONE);
	}
	
	/**
	 * loading status
	 */
	public void loading() {
//		mHintTextView.setVisibility(View.GONE);
		mProgressBar.setVisibility(View.VISIBLE);
	}
	
	/**
	 * hide footer when disable the function of load MORE
	 */
	public void hide() {
		LinearLayout.LayoutParams mParams = (LinearLayout.LayoutParams)mContentView.getLayoutParams();
		mParams.height = 0;
		mContentView.setLayoutParams(mParams);
	}
	
	/**
	 * show footer
	 */
	public void show() {
		LinearLayout.LayoutParams mParams = (LinearLayout.LayoutParams)mContentView.getLayoutParams();
		mParams.height = LayoutParams.WRAP_CONTENT;
		mContentView.setLayoutParams(mParams);
	}
	
}
