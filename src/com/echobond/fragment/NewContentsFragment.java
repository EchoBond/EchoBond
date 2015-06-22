package com.echobond.fragment;

import com.echobond.R;
import com.echobond.activity.NewPostPage;
import com.echobond.activity.ViewMorePage;
import com.echobond.intf.NewPostFragmentsSwitchAsyncTaskCallback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
/**
 * 
 * @author aohuijun
 *
 */
public class NewContentsFragment extends Fragment {
	
	private NewPostFragmentsSwitchAsyncTaskCallback contentsSelected;
	private ImageView moreTagsView;
	private EditText thoughtsContent, tagsContent;
	private String thoughtsText, tagsText;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View contentsView = inflater.inflate(R.layout.fragment_new_post_contents, container, false);
		moreTagsView = (ImageView)contentsView.findViewById(R.id.view_hashtag_more);
		moreTagsView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), "More Tags CLICKED", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				intent.putExtra("title", NewPostPage.TAG);
				intent.setClass(getActivity(), ViewMorePage.class);
				startActivity(intent);
			}
		});
		
		thoughtsContent = (EditText)contentsView.findViewById(R.id.thoughts_content);
		tagsContent = (EditText)contentsView.findViewById(R.id.tags_content);
		thoughtsContent.addTextChangedListener(new MyTextWatcher());
		tagsContent.addTextChangedListener(new MyTextWatcher());
		
		return contentsView;
	}
	
	public class MyTextWatcher implements TextWatcher {

		@Override
		public void afterTextChanged(Editable s) {
			thoughtsText = thoughtsContent.getText().toString();
			tagsText = tagsContent.getText().toString();
			contentsSelected.passContent(thoughtsText, tagsText);
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			contentsSelected = (NewPostFragmentsSwitchAsyncTaskCallback) activity;
		} catch (Exception e) {
			throw new ClassCastException(activity.toString() + "must implement ContentsInterface. ");
		}
	}

	public String getThoughtsText() {
		return thoughtsText;
	}

	public String getTagsText() {
		return tagsText;
	}
	
}
