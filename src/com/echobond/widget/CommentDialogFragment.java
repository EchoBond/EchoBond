package com.echobond.widget;

import com.echobond.R;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CommentDialogFragment extends DialogFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View commentDialogView = inflater.inflate(R.layout.dialog_comment, container, false);
		
		return commentDialogView;
	}
}
