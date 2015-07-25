package com.echobond.fragment;

import com.echobond.R;
import com.echobond.application.MyApp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
/**
 * 
 * @author aohuijun
 *
 */
public class ResetPasswordDialogFragment extends DialogFragment {

	private EditText originPasswordText, newPasswordText, newPasswordAgainText;
	private String originPasswordString, newPasswordString, newPasswordAgainString;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogTheme);
	}

	@SuppressLint("InflateParams") 
	@Override
	@NonNull
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		View resetPasswordDialogView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_dialog_reset_password, null);
		
		originPasswordText = (EditText)resetPasswordDialogView.findViewById(R.id.reset_password_origin_input);
		newPasswordText = (EditText)resetPasswordDialogView.findViewById(R.id.reset_password_new_input);
		newPasswordAgainText = (EditText)resetPasswordDialogView.findViewById(R.id.reset_password_new_again_input);
		
		return new AlertDialog.Builder(getActivity())
				.setView(resetPasswordDialogView)
				.setTitle(R.string.edit_profile_reset_password)
				.setPositiveButton(MyApp.DIALOG_CONFIRM, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						verifyPassword();
					}
				})
				.setNegativeButton(MyApp.DIALOG_CANCEL, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				})
				.create();
	}

	private void verifyPassword() {
		originPasswordString = originPasswordText.getText().toString();
		newPasswordString = newPasswordText.getText().toString();
		newPasswordAgainString = newPasswordAgainText.getText().toString();
		if (originPasswordString.equals("")) {
			Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.hint_reset_password_empty_origin), Toast.LENGTH_SHORT).show();
		} else if (newPasswordString.equals("")) {
			Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.hint_reset_password_empty_new), Toast.LENGTH_SHORT).show();
		} else if (newPasswordAgainString.equals("")) {
			Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.hint_reset_password_empty_new_again), Toast.LENGTH_SHORT).show();
		} else if (originPasswordString.equals(newPasswordString)) {
			Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.hint_reset_password_origin_duplicate), Toast.LENGTH_SHORT).show();
		} else if (!newPasswordString.equals(newPasswordAgainString)) {
			Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.hint_reset_password_new_dismatched), Toast.LENGTH_SHORT).show();
		} else {
			//	TODO send newPasswordString
		}
	}
}
