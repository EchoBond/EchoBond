package com.echobond.widget;

import com.echobond.R;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ThoughtListAdapter extends SimpleCursorAdapter {

	private ViewBinder binder;
	
	public ThoughtListAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to, int flags) {
		super(context, layout, c, from, to, flags);
		
		/* define ViewBinder*/
		binder = new ViewBinder() {
			@Override
			public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
				if(view.getId() == R.id.thought_list_pic){
					cursor.getString(columnIndex);
					((ImageView)view).setImageResource(R.drawable.temp_1);
					return true;
				}
				return false;
			}
		};
		setViewBinder(binder);

	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return super.newView(context, cursor, parent);
	}
	
	@Override
	public void bindView(View view, Context ctx, Cursor cursor) {
		super.bindView(view, ctx, cursor);
	}
	
	@Override
	public void setViewBinder(ViewBinder viewBinder) {
		// TODO Auto-generated method stub
		super.setViewBinder(viewBinder);
	}
	

}
