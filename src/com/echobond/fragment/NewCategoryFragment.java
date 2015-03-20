package com.echobond.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import com.echobond.R;
import com.echobond.connector.CategoryAsyncTask;
import com.echobond.entity.Category;
import com.echobond.intf.CategoryCallback;
import com.echobond.intf.NewPostFragmentsSwitchAsyncTaskCallback;
import com.echobond.util.HTTPUtil;
import com.echobond.util.JSONUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.reflect.TypeToken;
/**
 * 
 * @author Ao Huijun
 * @author Luck
 *
 */
public class NewCategoryFragment extends Fragment implements CategoryCallback {

	private ArrayList<Drawable> bgList;
	private ArrayList<Category> categories;
	private ListView categoryList;
	private NewPostFragmentsSwitchAsyncTaskCallback categorySelected;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View categoryView = inflater.inflate(R.layout.fragment_new_post_category, container, false);
		
		bgList = new ArrayList<Drawable>();
		bgList.add((Drawable) getResources().getDrawable(R.drawable.corners_bg_blue));
		bgList.add((Drawable) getResources().getDrawable(R.drawable.corners_bg_red));
		bgList.add((Drawable) getResources().getDrawable(R.drawable.corners_bg_orange));
		bgList.add((Drawable) getResources().getDrawable(R.drawable.corners_bg_green));
		bgList.add((Drawable) getResources().getDrawable(R.drawable.corners_bg_yellow));
		bgList.add((Drawable) getResources().getDrawable(R.drawable.corners_bg_cyan));
		
		categoryList = (ListView)categoryView.findViewById(R.id.list_category);
		categoryList.setOverScrollMode(View.OVER_SCROLL_NEVER);
		new CategoryAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,CategoryAsyncTask.CATEGORY_LOAD,
				HTTPUtil.getInstance().composePreURL(getActivity())+getResources().getString(R.string.url_load_categories), this);
		return categoryView;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			categorySelected = (NewPostFragmentsSwitchAsyncTaskCallback) activity;
		} catch (Exception e) {
			throw new ClassCastException(activity.toString() + "must implement CategoryInterface. ");
		}
	}

	@SuppressWarnings("unchecked")
	@SuppressLint("NewApi")
	@Override
	public void onCategoryResult(JSONObject result) {
		if(null == result){
			Toast.makeText(getActivity(), "Failed loading categories", Toast.LENGTH_LONG).show();
		} else {
			TypeToken<ArrayList<Category>> token = new TypeToken<ArrayList<Category>>(){};
			categories = (ArrayList<Category>) JSONUtil.fromJSONToList(result, "categories", token);
			ArrayList<HashMap<String, Object>> listItems = new ArrayList<HashMap<String, Object>>();
			for(int i = 0; i < categories.size(); i++){
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("category", categories.get(i).getName());
				listItems.add(map);
			}
			SimpleAdapter adapter = new SimpleAdapter(getActivity(), listItems, R.layout.item_category, new String[]{"category"}, new int[]{R.id.text_category});
			categoryList.setAdapter(adapter);
			categoryList.post(new Runnable() {
				
				@Override
				public void run() {
					for(int i = 0; i < categoryList.getChildCount(); i++){
						ViewGroup view = (ViewGroup) categoryList.getChildAt(i);
						int index = i;
						if(index >= bgList.size()){
							index = bgList.size()%index;
						}
						view.getChildAt(0).setBackground(bgList.get(index));
					}
				}
			});
			categoryList.setOnItemClickListener(new CategoryItemClickListener());
		}
	}
	
	public class CategoryItemClickListener implements AdapterView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			view.setSelected(true);
			TextView txt = (TextView) view.findViewById(R.id.text_category);
			categorySelected.getCategory(getCategoryId(txt.getText().toString()));
		}
		
	}
	
	private int getCategoryId(String name){
		int pos = -1;
		for (Category category : categories) {
			if(category.getName().equals(name))
				pos = category.getId();
		}
		return pos;
	}
}
