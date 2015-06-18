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
	private FollowingGroupsAdapter adapter2;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View followingGroupsView = inflater.inflate(R.layout.fragment_following_groups, container, false);
		groups2Follow = (GridView)followingGroupsView.findViewById(R.id.grid_groups);
		adapter = new MySimpleAdapter(this.getActivity(), data(), 
				R.layout.item_following, new String[]{"group"}, new int[]{R.id.item_following_text});
		adapter2 = new FollowingGroupsAdapter(this.getActivity(), null, 0);
		groups2Follow.setAdapter(adapter);
		groups2Follow.setOverScrollMode(View.OVER_SCROLL_NEVER);
		groups2Follow.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);

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
			TextView groupName;
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
				holder.groupName = (TextView)convertView.findViewById(R.id.item_following_text);
				holder.groupLayout = (RelativeLayout)convertView.findViewById(R.id.item_following_view);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder)convertView.getTag();
			}
			convertView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (v.isSelected()) {
						holder.selection.setImageDrawable(null);
						v.setSelected(false);
					} else {
						holder.selection.setImageDrawable(getResources().getDrawable(R.drawable.button_done));
						v.setSelected(true);
					}
					Toast.makeText(getActivity().getApplicationContext(), "CLICKED", Toast.LENGTH_SHORT).show();
					
				}
			});

			View view = super.getView(position, convertView, parent);
			int colorPos = position % (3 * colorBgd.length);
			view.setBackgroundColor(colorBgd[colorPos/3]);
			return view;
		}
	}
	
	public class FollowingGroupsAdapter extends CursorAdapter {

		private LayoutInflater mInflater;
		private int layout;
		
		public FollowingGroupsAdapter(Context context, Cursor c, int flags) {
			super(context, c, flags);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void bindView(View convertView, Context ctx, Cursor c) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public View newView(Context context, Cursor c, ViewGroup parent) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

}
