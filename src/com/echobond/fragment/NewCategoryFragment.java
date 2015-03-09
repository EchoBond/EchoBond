package com.echobond.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import com.echobond.R;
import com.echobond.connector.CategoryAsyncTask;
import com.echobond.entity.Category;
import com.echobond.intf.CategoryAsyncTaskCallback;
import com.echobond.util.HTTPUtil;
import com.echobond.util.JSONUtil;
import com.google.gson.Gson;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class NewCategoryFragment extends Fragment implements CategoryAsyncTaskCallback{

	private ArrayList<ShapeDrawable> bgList;
	private ListView categoryList;
	
	private CategoryInterface categorySelected;
	private String categoryName = "";
	
	private ArrayList<Category> categories; 
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View categoryView = inflater.inflate(R.layout.fragment_new_post_category, container, false);
		
		bgList = new ArrayList<ShapeDrawable>();		
		bgList.add((ShapeDrawable) getResources().getDrawable(R.drawable.corners_bg_blue));
		bgList.add((ShapeDrawable) getResources().getDrawable(R.drawable.corners_bg_red));
		bgList.add((ShapeDrawable) getResources().getDrawable(R.drawable.corners_bg_orange));
		bgList.add((ShapeDrawable) getResources().getDrawable(R.drawable.corners_bg_yellow));
		bgList.add((ShapeDrawable) getResources().getDrawable(R.drawable.corners_bg_cyan));
		
		categoryList = (ListView) categoryView.findViewById(R.id.list_category);
		
		new CategoryAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, CategoryAsyncTask.CATEGORY_LOAD,
				HTTPUtil.getInstance().composePreURL(getActivity())+getResources().getString(R.string.url_load_categories), this);
		
		return categoryView;
	}
	
	public interface CategoryInterface {
		public void getIndex(int index);
		public void getCategory(String category);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			categorySelected = (CategoryInterface) activity;
		} catch (Exception e) {
			throw new ClassCastException(activity.toString() + "must implement CategoryInterface. ");
		}
	}

	@Override
	public void onCategoryResult(JSONObject result) {
		if(null == result){
			Toast.makeText(getActivity(), "Failed loading categories", Toast.LENGTH_LONG).show();
		} else {
			categories = (ArrayList<Category>) JSONUtil.fromJSONToObject(result, ArrayList.class);
			ArrayList<HashMap<String, Object>> listItems = new ArrayList<HashMap<String, Object>>();
			for(int i=0;i < categories.size(); i++){
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("category", categories.get(i).getName());
				listItems.add(map);
			}
			SimpleAdapter adapter = new SimpleAdapter(getActivity(), listItems, R.layout.item_category, new String[]{"category"}, new int[]{R.id.text_category});
			for(int i=0;i<categoryList.getChildCount();i++){
				categoryList.getChildAt(i);
			}
		}
	}
}
