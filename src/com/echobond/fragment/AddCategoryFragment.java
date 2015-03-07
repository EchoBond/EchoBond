package com.echobond.fragment;

import com.echobond.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

public class AddCategoryFragment extends ListFragment implements AdapterView.OnItemClickListener {
	
	private String[] categoryItemList;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View categoryView = inflater.inflate(R.layout.fragment_main_add_category, container, false);
		return categoryView;
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		categoryItemList = getResources().getStringArray(R.array.category_list_array);
		super.onActivityCreated(savedInstanceState);
		ArrayAdapter<String> categoryListAdapter = new ArrayAdapter<String>(
				this.getActivity(), R.layout.centralized_textview, categoryItemList);
		this.setListAdapter(categoryListAdapter);
		getListView().setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		AddContentsFragment contentsFragment = new AddContentsFragment();
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.add(R.id.main_content, contentsFragment);
		transaction.show(contentsFragment).commit();
	}
}
