package com.echobond.fragment;

import java.lang.reflect.Field;

import org.json.JSONException;
import org.json.JSONObject;

import com.echobond.R;
import com.echobond.application.MyApp;
import com.echobond.connector.RequestResetPassAsyncTask;
import com.echobond.intf.RequestResetPassCallback;
import com.echobond.util.SPUtil;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
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
public class ResetPasswordDialogFragment extends DialogFragment implements RequestResetPassCallback{

	private EditText originPasswordText, newPasswordText, newPasswordAgainText;
	private String originPasswordString, newPasswordString, newPasswordAgainString;
	private DialogInterface dialog;
	
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
						verifyPassword(dialog);
					}
				})
				.setNegativeButton(MyApp.DIALOG_CANCEL, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						maintainDialog(dialog, true);
					}
				})
				.create();
	}

	private void verifyPassword(DialogInterface dialog) {
		originPasswordString = originPasswordText.getText().toString();
		newPasswordString = newPasswordText.getText().toString();
		newPasswordAgainString = newPasswordAgainText.getText().toString();
		if (originPasswordString.equals("")) {
			Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.hint_reset_password_empty_origin), Toast.LENGTH_SHORT).show();
			maintainDialog(dialog, false);
		} else if (newPasswordString.equals("")) {
			Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.hint_reset_password_empty_new), Toast.LENGTH_SHORT).show();
			maintainDialog(dialog, false);
		} else if (newPasswordAgainString.equals("")) {
			Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.hint_reset_password_empty_new_again), Toast.LENGTH_SHORT).show();
			maintainDialog(dialog, false);
		} else if (originPasswordString.equals(newPasswordString)) {
			Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.hint_reset_password_origin_duplicate), Toast.LENGTH_SHORT).show();
			maintainDialog(dialog, false);
		} else if (!newPasswordString.equals(newPasswordAgainString)) {
			Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.hint_reset_password_new_dismatched), Toast.LENGTH_SHORT).show();
			maintainDialog(dialog, false);
		} else {
			String id = (String) SPUtil.get(getActivity(), MyApp.PREF_TYPE_LOGIN, MyApp.LOGIN_ID, "", String.class);
			String url = getResources().getString(R.string.url_request_reset_pass);
			this.dialog = dialog;
			new RequestResetPassAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, id, url, this, originPasswordString, newPasswordString);
		}
	}
	
	private void maintainDialog(DialogInterface dialogInterface, boolean closeDialog) {
		try {
			Field field = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
			field.setAccessible(true);
			field.set(dialogInterface, closeDialog);
			dialogInterface.dismiss();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void onRequestResetPassFinished(JSONObject result) {
		if(null != result){
			try {
				if(null != result.get("success")){
					SPUtil.put(getActivity(), MyApp.PREF_TYPE_LOGIN, MyApp.LOGIN_PASS, newPasswordString);
					maintainDialog(dialog, true);
					Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.hint_reset_password_success), Toast.LENGTH_SHORT).show();		
				} else if(null != result.get("wrongOld")){
					maintainDialog(dialog, false);
					Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.hint_reset_password_origin_wrong), Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			maintainDialog(dialog, false);
			Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.hint_network_issue), Toast.LENGTH_SHORT).show();		
		}
	}
}
