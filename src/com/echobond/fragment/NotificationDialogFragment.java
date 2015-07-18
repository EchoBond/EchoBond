package com.echobond.fragment;

import com.echobond.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
/**
 * 
 * @author aohuijun
 *
 */
public class NotificationDialogFragment extends DialogFragment {
	
	private ImageView vibrateSelectView;
	private ImageView soundSelectView;
	
	private boolean isVibrateEnabled = true;
	private boolean isSoundEnabled = true;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogTheme);
	}
	
	@SuppressLint("InflateParams") 
	@Override
	@NonNull
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		View notificationDialogView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_dialog_notification, null);
		
		vibrateSelectView = (ImageView)notificationDialogView.findViewById(R.id.ntc_vibrate_seletor);
		soundSelectView = (ImageView)notificationDialogView.findViewById(R.id.ntc_sound_seletor);
		vibrateSelectView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (isVibrateEnabled) {
					isVibrateEnabled = false;
					vibrateSelectView.setImageDrawable(getResources().getDrawable(R.drawable.color_selection_0));
				} else if (!isVibrateEnabled) {
					isVibrateEnabled = true;
					vibrateSelectView.setImageDrawable(getResources().getDrawable(R.drawable.color_selection_2));
				}
			}
		});
		soundSelectView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (isSoundEnabled) {
					isSoundEnabled = false;
					soundSelectView.setImageDrawable(getResources().getDrawable(R.drawable.color_selection_0));
				} else if (!isSoundEnabled) {
					isSoundEnabled = true;
					soundSelectView.setImageDrawable(getResources().getDrawable(R.drawable.color_selection_2));
				}
			}
		});
		
		return new AlertDialog.Builder(getActivity()).setView(notificationDialogView)
				.setTitle(getArguments().getString("title"))
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (isVibrateEnabled && isSoundEnabled) {
							
						} else if (isVibrateEnabled && !isSoundEnabled) {
							
						} else if (!isVibrateEnabled && isSoundEnabled) {
							
						} else if (!isVibrateEnabled && !isSoundEnabled) {
							
						}
					}
				})
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				})
				.create();
	}

}
