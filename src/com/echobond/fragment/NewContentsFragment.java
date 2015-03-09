package com.echobond.fragment;

import com.echobond.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class NewContentsFragment extends Fragment {
	
	private ContentsInterface contentsSelected;
	private EditText thoughtsContent, tagsContent;
	private String thoughtsText, tagsText;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View addContentsView = inflater.inflate(R.layout.fragment_new_post_contents, container, false);
		
		thoughtsContent = (EditText)addContentsView.findViewById(R.id.thoughts_content);
		tagsContent = (EditText)addContentsView.findViewById(R.id.tags_content);
		thoughtsText = thoughtsContent.getText().toString();
		tagsText = tagsContent.getText().toString();
		
		contentsSelected.getIndex(2);
		contentsSelected.getContent(thoughtsText, tagsText);
		return addContentsView;
	}
	
	public interface ContentsInterface {
		public void getIndex(int index);
		public void getContent(String content, String tags);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			contentsSelected = (ContentsInterface) activity;
		} catch (Exception e) {
			throw new ClassCastException(activity.toString() + "must implement ContentsInterface. ");
		}
	}
}
