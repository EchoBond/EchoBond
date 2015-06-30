package com.echobond.fragment;

import java.util.ArrayList;

import com.echobond.R;
import com.echobond.activity.ViewMorePage;
import com.echobond.application.MyApp;
import com.echobond.intf.NewPostFragmentsSwitchAsyncTaskCallback;
import com.echobond.util.CommUtil;

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
	
	public static final int REQ = 1;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View contentsView = inflater.inflate(R.layout.fragment_new_post_contents, container, false);
		moreTagsView = (ImageView)contentsView.findViewById(R.id.view_hashtag_more);
		moreTagsView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("title", MyApp.VIEW_MORE_TAG);
				intent.putExtra("mode", MyApp.VIEW_MORE_FROM_PROFILE);
				intent.setClass(getActivity(), ViewMorePage.class);
				startActivityForResult(intent, REQ);
			}
		});
		
		thoughtsContent = (EditText)contentsView.findViewById(R.id.thoughts_content);
		tagsContent = (EditText)contentsView.findViewById(R.id.tags_content);
		thoughtsContent.addTextChangedListener(new MyTextWatcher());
		tagsContent.addTextChangedListener(new MyTextWatcher());
		
		return contentsView;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(null == data || null == data.getExtras()){
			super.onActivityResult(requestCode, resultCode, data);
			return;
		}
		Bundle bundle = data.getExtras();
		ArrayList<Integer> idList = bundle.getIntegerArrayList("idList");
		ArrayList<String> nameList = bundle.getStringArrayList("nameList");
		if(null != idList && !idList.isEmpty()){
			switch (requestCode) {
			case REQ:
				String[] tags = getTags();
				if(null != tags && tags.length > 0){
					for(String tag: tags){
						if(!nameList.contains(tag)){
							nameList.add(tag);
						}
					}
				}
				tagsContent.setText(CommUtil.arrayListToString(nameList, ","));
				break;
			default:
				break;
			}
		}
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
	
	public String[] getTags(){
		return CommUtil.parseString(tagsContent.getText().toString(), ",");
	}
	
}
