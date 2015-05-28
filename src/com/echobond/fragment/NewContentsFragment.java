package com.echobond.fragment;

import com.echobond.R;
import com.echobond.intf.NewPostFragmentsSwitchAsyncTaskCallback;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
/**
 * 
 * @author aohuijun
 *
 */
public class NewContentsFragment extends Fragment {
	
	private NewPostFragmentsSwitchAsyncTaskCallback contentsSelected;
	private EditText thoughtsContent, tagsContent;
	private String thoughtsText, tagsText;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View contentsView = inflater.inflate(R.layout.fragment_new_post_contents, container, false);
		
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
