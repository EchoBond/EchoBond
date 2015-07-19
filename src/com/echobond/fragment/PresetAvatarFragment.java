package com.echobond.fragment;

import com.echobond.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
/**
 * 
 * @author aohuijun
 *
 */
public class PresetAvatarFragment extends Fragment {

	private int[] avatarsList = {R.drawable.avatar_1, R.drawable.avatar_2, R.drawable.avatar_3, 
							R.drawable.avatar_4, R.drawable.avatar_5, R.drawable.avatar_6, 
							R.drawable.avatar_7, R.drawable.avatar_8, R.drawable.avatar_9, 
							R.drawable.avatar_10, R.drawable.avatar_11, R.drawable.avatar_12};
	private int selectedAvatar = -1;
	private GridView avatarGridView;
	private AvatarAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View presetAvatarView = inflater.inflate(R.layout.fragment_edit_profile_preset_avatar, container, false);
		
		adapter = new AvatarAdapter(getActivity(), avatarsList);
		avatarGridView = (GridView)presetAvatarView.findViewById(R.id.grid_preset_avatar);
		avatarGridView.setAdapter(adapter);
		avatarGridView.setChoiceMode(GridView.CHOICE_MODE_SINGLE);
		avatarGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				adapter.changeState(position);
				selectedAvatar = avatarsList[position];
			}
		});
		
		return presetAvatarView;
	}
	
	public int getSelectedAvatar() {
		return selectedAvatar;
	}

	public class AvatarAdapter extends BaseAdapter {

		private SparseBooleanArray avatarStatusArray = new SparseBooleanArray();
		private int[] avatars;
		private LayoutInflater inflater = null;
		private int lastPosition = -1;
		
		public AvatarAdapter(Context ctx, int[] picList) {
			this.avatars = picList;
			this.inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public void setSelected(int position, boolean isSelected) {
			avatarStatusArray.put(position, isSelected);
		}
		
		@Override
		public int getCount() {
			return avatars.length;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public class ViewHolder {
			ImageView avatar, selector;
		}
		
		@SuppressLint("InflateParams") 
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder = new ViewHolder();
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_avatar, null);
				holder.avatar = (ImageView)convertView.findViewById(R.id.item_avatar_img);
				holder.selector = (ImageView)convertView.findViewById(R.id.item_avatar_selected);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder)convertView.getTag();
			}
			holder.avatar.setImageResource(avatars[position]);
			boolean isSelected = avatarStatusArray.get(position);
			if (isSelected) {
				holder.selector.setImageDrawable(getResources().getDrawable(R.drawable.image_seletor));
			} else if (!isSelected) {
				holder.selector.setImageDrawable(null);
			}
			return convertView;
		}
		
		public void changeState(int position) {
			if (lastPosition != -1 && lastPosition != position) {
				avatarStatusArray.put(lastPosition, false);
			}
			avatarStatusArray.put(position, !avatarStatusArray.get(position));
			lastPosition = position;
			notifyDataSetChanged();
		}
		
	}
}
