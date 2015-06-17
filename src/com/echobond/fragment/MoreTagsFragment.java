package com.echobond.fragment;

import com.echobond.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
/**
 * 
 * @author aohuijun
 *
 */
public class MoreTagsFragment extends Fragment{

	private TextView tagTextView;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View moreTagsView = inflater.inflate(R.layout.fragment_more_tags, container, false);
		tagTextView = (TextView)moreTagsView.findViewById(R.id.more_tags_text);
		
		return moreTagsView;
	}
	
}
