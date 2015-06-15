package com.echobond.fragment;

import com.echobond.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @author aohuijun
 *
 */
public class MoreGroupsFragment extends Fragment {

	private TextView textView;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View moreGroupsView = inflater.inflate(R.layout.fragment_more_groups, container, false);
		textView = (TextView)moreGroupsView.findViewById(R.id.more_groups_text);
		Bundle bundle = this.getArguments();
		textView.setText(bundle.getString("type"));
		textView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent upIntent = NavUtils.getParentActivityIntent(getActivity());
				upIntent.putExtra("string", textView.getText().toString());
				if (NavUtils.shouldUpRecreateTask(getActivity(), upIntent)) {
					TaskStackBuilder.create(getActivity()).addNextIntentWithParentStack(upIntent).startActivities();
				}else {
					upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					NavUtils.navigateUpTo(getActivity(), upIntent);
				}
				Toast.makeText(getActivity(), "BACK", Toast.LENGTH_SHORT).show();
			}
		});
		
		return moreGroupsView;
	}
	
}
