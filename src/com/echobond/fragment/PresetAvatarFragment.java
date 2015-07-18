package com.echobond.fragment;

import com.echobond.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
/**
 * 
 * @author aohuijun
 *
 */
public class PresetAvatarFragment extends Fragment {

	private int[] avatarList = {R.drawable.avatar_1, R.drawable.avatar_2, R.drawable.avatar_3, 
							R.drawable.avatar_4, R.drawable.avatar_5, R.drawable.avatar_6, 
							R.drawable.avatar_7, R.drawable.avatar_8, R.drawable.avatar_9, 
							R.drawable.avatar_10, R.drawable.avatar_11, R.drawable.avatar_12};
	private GridView avatarGridView;
	private ArrayAdapter<Integer> adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View presetAvatarView = inflater.inflate(R.layout.fragment_edit_profile_preset_avatar, container, false);
		return presetAvatarView;
	}
	
}
