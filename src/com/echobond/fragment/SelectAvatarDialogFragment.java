package com.echobond.fragment;

import com.echobond.R;
import com.echobond.activity.EditProfilePage;
import com.echobond.intf.EditProfileSwitchCallback;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
/**
 * 
 * @author aohuijun
 *
 */
public class SelectAvatarDialogFragment extends DialogFragment {

	private EditProfileSwitchCallback avatarSwitchCallback;
	private RelativeLayout presetAvatarLayout, posterAvatarLayout, canvasAvatarLayout;
	
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
		presetAvatarLayout = (RelativeLayout)selectAvatarDialogView.findViewById(R.id.select_avatar_preset);
		posterAvatarLayout = (RelativeLayout)selectAvatarDialogView.findViewById(R.id.select_avatar_poster);
		canvasAvatarLayout = (RelativeLayout)selectAvatarDialogView.findViewById(R.id.select_avatar_canvas);
		presetAvatarLayout.setOnClickListener(new AvatarSelectListener(EditProfilePage.PAGE_PRESET));
		posterAvatarLayout.setOnClickListener(new AvatarSelectListener(EditProfilePage.PAGE_POSTER));
		canvasAvatarLayout.setOnClickListener(new AvatarSelectListener(EditProfilePage.PAGE_CANVAS));
		
		return new AlertDialog.Builder(getActivity()).setView(selectAvatarDialogView).setTitle(getArguments().getString("title")).create();
	}
	
	public class AvatarSelectListener implements View.OnClickListener {

		private int pageType;
		
		public AvatarSelectListener(int type) {
			this.pageType = type;
		}

		@Override
		public void onClick(View v) {
			switch (pageType) {
			case EditProfilePage.PAGE_PRESET:
				avatarSwitchCallback.setAvatarType(EditProfilePage.PAGE_PRESET);
				dismiss();
				break;
			case EditProfilePage.PAGE_POSTER:
				avatarSwitchCallback.setAvatarType(EditProfilePage.PAGE_POSTER);
				dismiss();
				break;
			case EditProfilePage.PAGE_CANVAS:
				avatarSwitchCallback.setAvatarType(EditProfilePage.PAGE_CANVAS);
				dismiss();
				break;
			default:
				break;
			}
		}
		
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			avatarSwitchCallback = (EditProfileSwitchCallback) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + "must implement EditProfileSwitchCallback. ");
		}
	}
}
