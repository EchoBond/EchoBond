package com.echobond.widget;

import com.echobond.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class CommentDialog extends AlertDialog {

	private Activity activity;
	private EditText commentText;
	private ImageView sendButton;
	private String comment;
	
	public CommentDialog(Activity a) {
		super(a);
		// TODO Auto-generated constructor stub
		this.activity = a;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_comment);
		
		commentText = (EditText)findViewById(R.id.dialog_comment_input);		
		sendButton = (ImageView)findViewById(R.id.dialog_comment_send);
		sendButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO SEND COMMENTS(String comment)
				Toast.makeText(activity.getApplicationContext(), "Sent. ", Toast.LENGTH_SHORT).show();
			}
		});
		
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
	}

	public String getComment() {
		return commentText.getText().toString();
	}
	
}
