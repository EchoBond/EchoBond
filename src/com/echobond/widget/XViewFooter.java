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
	
	private LinearLayout mContainer;
	private View mContentView;
	private ProgressBar mProgressBar;
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
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		mContainer = (LinearLayout)LayoutInflater.from(context).inflate(R.layout.refresh_pull_up, null);
		addView(mContainer, params);
		
		mContentView = mContainer.findViewById(R.id.pullup_footer_content);
		mProgressBar = (ProgressBar)findViewById(R.id.pullup_footer_loading);
		mHintTextView = (TextView)findViewById(R.id.pullup_footer_text);
		
	}
	
	public void setState(int state) {
//		mContentView.setVisibility(View.INVISIBLE);
		mHintTextView.setVisibility(View.INVISIBLE);
		mProgressBar.setVisibility(View.INVISIBLE);
		if (state == STATE_READY) {
			mHintTextView.setVisibility(View.VISIBLE);
			mHintTextView.setText("Release to load the previous. ");
		} else if (state == STATE_LOADING) {
			mProgressBar.setVisibility(View.VISIBLE);
		} else {
			mHintTextView.setVisibility(View.VISIBLE);
			mHintTextView.setText("Load More…");
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
	
	public void normal() {
		mHintTextView.setVisibility(View.VISIBLE);
		mProgressBar.setVisibility(View.GONE);
	}
	
	public void loading() {
//		mHintTextView.setVisibility(View.GONE);
		mProgressBar.setVisibility(View.VISIBLE);
	}
	
	public void hide() {
		LinearLayout.LayoutParams mParams = (LinearLayout.LayoutParams)mContentView.getLayoutParams();
		mParams.height = 0;
		mContentView.setLayoutParams(mParams);
	}
	
	public void show() {
		LinearLayout.LayoutParams mParams = (LinearLayout.LayoutParams)mContentView.getLayoutParams();
		mParams.height = LayoutParams.WRAP_CONTENT;
		mContentView.setLayoutParams(mParams);
	}
	
}
