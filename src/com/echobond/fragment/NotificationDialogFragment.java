package com.echobond.fragment;

import com.echobond.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/**
 * 
 * @author aohuijun
 *
 */
public class NotificationDialogFragment extends DialogFragment {

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View notificationDialogView = inflater.inflate(R.layout.dialog_comment, container, false);
		return notificationDialogView;
	}
	
}
