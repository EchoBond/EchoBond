package com.echobond.fragment;

import org.json.JSONObject;

import com.echobond.R;
import com.echobond.application.MyApp;
import com.echobond.connector.RequestResetPassAsyncTask;
import com.echobond.intf.RequestResetPassCallback;
import com.echobond.util.HTTPUtil;
import com.echobond.util.SPUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
/**
 * 
 * @author aohuijun
 *
 */
public class ResetPasswordDialogFragment extends DialogFragment implements RequestResetPassCallback {

	private EditText originPasswordText, newPasswordText, newPasswordAgainText;
	private String originPasswordString, newPasswordString, newPasswordAgainString;
	private AlertDialog dialog;
	private Activity mActivity;
	
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
		
		dialog = new AlertDialog.Builder(getActivity())
				.setView(resetPasswordDialogView)
				.setTitle(R.string.edit_profile_reset_password)
				.setPositiveButton(MyApp.DIALOG_CONFIRM, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//	do NOTHING here
					}
				})
				.setNegativeButton(MyApp.DIALOG_CANCEL, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//	do NOTHING here
					}
				})
				.create();
		
		return dialog;
	}

	@Override
	public void onStart() {
		super.onStart();
		AlertDialog dialog = (AlertDialog)getDialog();
		if (dialog != null) {
			Button button = (Button)dialog.getButton(Dialog.BUTTON_POSITIVE);
			button.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					originPasswordString = originPasswordText.getText().toString();
					newPasswordString = newPasswordText.getText().toString();
					newPasswordAgainString = newPasswordAgainText.getText().toString();
					
					if (newPasswordString.equals("")) {
						Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.hint_reset_password_empty_new), Toast.LENGTH_SHORT).show();
					} else if (newPasswordAgainString.equals("")) {
						Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.hint_reset_password_empty_new_again), Toast.LENGTH_SHORT).show();
					} else if (originPasswordString.equals(newPasswordString)) {
						Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.hint_reset_password_origin_duplicate), Toast.LENGTH_SHORT).show();
					} else if (!newPasswordString.equals(newPasswordAgainString)) {
						Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.hint_reset_password_new_dismatched), Toast.LENGTH_SHORT).show();
					} else {
						String id = (String) SPUtil.get(getActivity(), MyApp.PREF_TYPE_LOGIN, MyApp.LOGIN_ID, "", String.class);
						String url = HTTPUtil.getInstance().composePreURL(getActivity()) + getResources().getString(R.string.url_request_reset_pass);
						new RequestResetPassAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, id, url, this, originPasswordString, newPasswordString);
						dismiss();
					}
				}
			});
		}
	}

	@Override
	public void onRequestResetPassFinished(JSONObject result) {
		if (null != result) {
			if (result.has("success")) {
				SPUtil.put(mActivity, MyApp.PREF_TYPE_LOGIN, MyApp.LOGIN_PASS, newPasswordString);
				dialog.dismiss();
				Toast.makeText(mActivity, getResources().getString(R.string.hint_reset_password_success), Toast.LENGTH_SHORT).show();		
			} else if (result.has("wrongOld")) {
				Toast.makeText(mActivity, getResources().getString(R.string.hint_reset_password_origin_wrong), Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(mActivity, getResources().getString(R.string.hint_network_issue), Toast.LENGTH_SHORT).show();		
		}
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
	}
}
