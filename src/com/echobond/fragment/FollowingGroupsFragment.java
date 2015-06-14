package com.echobond.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.echobond.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @author aohuijun
 *
 */
public class FollowingGroupsFragment extends Fragment {
	
	private int[] colorBgd = new int[] {0xffffb8b8, 0xffdbc600, 0xffac97ef, 0xff8cd19d, 0xff5cacc4, 0xfff49e40};
	private String[] testGroups = {"HKU", "CityU", "CUHK", "HKBU", "UST", "PolyU", "LingU", "SYU", "IVE", 
			"HKDI", "HKCC", "Vintage", "名校", "HKMC", "HSBC", "Swire", "RCLee", "Starr", "laoliu", 
			"HSBC", "Swire", "RCLee", "Starr", "laoliu", 
			"HSBC", "Swire", "RCLee", "Starr", "laoliu", 
			"HKU", "CityU", "CUHK", "HKBU", "UST", "PolyU", "LingU", "SYU", "IVE",
			"HKU", "CityU", "CUHK", "HKBU", "UST", "PolyU", "LingU", "SYU", "IVE",
			"HKU", "CityU", "CUHK", "HKBU", "UST", "PolyU", "LingU", "SYU", "IVE"};
	private GridView groups2Follow;
	private MySimpleAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View followingGroupsView = inflater.inflate(R.layout.fragment_following_groups, container, false);
		groups2Follow = (GridView)followingGroupsView.findViewById(R.id.grid_groups);
		adapter = new MySimpleAdapter(this.getActivity(), data(), 
				R.layout.item_following, new String[]{"group"}, new int[]{R.id.item_following_text});
		MySimpleAdapter2 adapter2 = new MySimpleAdapter2(this.getActivity(), R.layout.item_following, null, 0);
		groups2Follow.setAdapter(adapter);
		groups2Follow.setOverScrollMode(View.OVER_SCROLL_NEVER);
		groups2Follow.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
//		groups2Follow.setOnItemClickListener(new groupSelectedListener());

		return followingGroupsView;
	}

	private List<Map<String, Object>> data() {
		List<Map<String, Object>> groupsList = new ArrayList<Map<String,Object>>();
		for (int i = 0; i < testGroups.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("group", testGroups[i]);
			groupsList.add(map);
		}
		return groupsList;
	}
	
	public class MySimpleAdapter extends SimpleAdapter {
		
		private LayoutInflater mInflater;
		public ViewHolder holder;
		
		class ViewHolder {
			ImageView selection;
			RelativeLayout groupLayout;
		}
		
		public MySimpleAdapter(Context context,
				List<? extends Map<String, ?>> data, int resource,
				String[] from, int[] to) {
			super(context, data, resource, from, to);
			this.mInflater = LayoutInflater.from(context);
		}
		
		@SuppressLint("InflateParams") 
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			holder = new ViewHolder();
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.item_following, null);
				holder.selection = (ImageView)convertView.findViewById(R.id.item_following_selected);
				holder.groupLayout = (RelativeLayout)convertView.findViewById(R.id.item_following_view);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder)convertView.getTag();
			}
			groups2Follow.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Fix bugs
					if (view.isSelected()) {
						holder.selection.setImageDrawable(getResources().getDrawable(R.drawable.button_back));
						view.setSelected(false);
						Toast.makeText(getActivity().getApplicationContext(), "Position " + position, Toast.LENGTH_SHORT).show();
					} 
					if (!view.isSelected()) {
						holder.selection.setImageDrawable(getResources().getDrawable(R.drawable.button_done));
						view.setSelected(true);
						Toast.makeText(getActivity().getApplicationContext(), "Position " + position, Toast.LENGTH_SHORT).show();
					}
				}
			});

			View view = super.getView(position, convertView, parent);
			int colorPos = position % (3 * colorBgd.length);
			view.setBackgroundColor(colorBgd[colorPos/3]);
			return view;
		}
	}
	
	public class MySimpleAdapter2 extends CursorAdapter {

		private LayoutInflater mInflater;
		private int layout;
		
		public MySimpleAdapter2(Context context, int layout, Cursor c, int flags) {
			super(context, c, flags);
			this.layout = layout;
			this.mInflater = LayoutInflater.from(context);
		}
		
		@Override
		public View newView(Context ctx, Cursor c, ViewGroup parent) {
			View view = mInflater.inflate(layout, parent, false);
			return view;
		}
		
		@Override
		public void bindView(View convertView, Context ctx, Cursor c) {
			ImageView selectionView = (ImageView)convertView.findViewById(R.id.item_following_selected);
			TextView groupView = (TextView)convertView.findViewById(R.id.item_following_text);
			
			groupView.setText(testGroups[groups2Follow.getPositionForView(convertView)]);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = super.getView(position, convertView, parent);
			int colorPos = position % (3 * colorBgd.length);
			view.setBackgroundColor(colorBgd[colorPos/3]);
			return view;
		}
		
	}
	
	public class groupSelectedListener implements AdapterView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			String s = groups2Follow.getItemAtPosition(position).toString();
			Toast.makeText(getActivity().getApplicationContext(), "Position " + position + ": " + s, Toast.LENGTH_SHORT).show();
		}
	}

}
