package com.echobond.widget;

import com.echobond.intf.Pullable;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class PullableListView extends ListView implements Pullable {

	public PullableListView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	public PullableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public PullableListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean canPullDown() {
		if (getCount() == 0) {
			return true;
		} else if (getFirstVisiblePosition() == 0 && getChildAt(0).getTop() >= 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean canPullUp() {
		if (getCount() == 0) {
			return true;
		} else if (getLastVisiblePosition() == (getCount() - 1)) {
			if (getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()) != null && 
					getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()).getBottom() <= getMeasuredHeight()) {
				return true;
			}
		}
		return false;
	}

}
