package com.echobond.fragment;

import com.echobond.R;

import android.app.DialogFragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
/**
 * 
 * @author aohuijun
 *
 */
public class CommentDialogFragment extends DialogFragment {

	private EditText commentInput;
	private ImageView sendButton;
	private String commentText;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View commentDialogView = inflater.inflate(R.layout.dialog_comment, container, false);
		commentInput = (EditText)commentDialogView.findViewById(R.id.dialog_comment_input);
		sendButton = (ImageView)commentDialogView.findViewById(R.id.dialog_comment_send);
		commentInput.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				sendButton.setImageDrawable(getResources().getDrawable(R.drawable.button_send_ready));
				if ("".equals(commentInput.getText().toString().trim())) {
					sendButton.setImageDrawable(getResources().getDrawable(R.drawable.button_send_normal));
				}
			}
		});
		sendButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				commentText = commentInput.getText().toString().trim();
				
			}
		});
		return commentDialogView;
	}
	
}
