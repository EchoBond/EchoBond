package com.echobond.fragment;

import com.echobond.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
/**
 * 
 * @author aohuijun
 *
 */
public class SelectAvatarDialogFragment extends DialogFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogTheme);
	}
	
	@SuppressLint("InflateParams") 
	@Override
	@NonNull
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		View selectAvatarDialogView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_dialog_select_avatar, null);
		return new AlertDialog.Builder(getActivity())
				.setView(selectAvatarDialogView)
				.create();
	}
	
}
