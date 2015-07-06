package com.echobond.widget;

import com.echobond.intf.Pullable;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class PullableGridView extends GridView implements Pullable {

	public PullableGridView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public PullableGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PullableGridView(Context context) {
		super(context);
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
