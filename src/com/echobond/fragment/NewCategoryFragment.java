package com.echobond.fragment;

import com.echobond.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class NewCategoryFragment extends Fragment {
	
	public static final int CATEGORY_FEELINGS = 0;
	public static final int CATEGORY_GOOD_IDEA = 1;
	public static final int CATEGORY_DREAM = 2;
	public static final int CATEGORY_INTEREST = 3;
	public static final int CATEGORY_PLAN = 4;
	public static final int CATEGORY_OTHER = 5;
	
	private CategoryInterface categorySelected;
	private LinearLayout feelingsButton, goodIdeaButton, dreamButton, interestButton, planButton, otherButton;
	private String categoryName = "";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View categoryView = inflater.inflate(R.layout.fragment_new_post_category, container, false);
		
		feelingsButton = (LinearLayout)categoryView.findViewById(R.id.button_category_feelings);
		goodIdeaButton = (LinearLayout)categoryView.findViewById(R.id.button_category_good_idea);
		dreamButton = (LinearLayout)categoryView.findViewById(R.id.button_category_dream);
		interestButton = (LinearLayout)categoryView.findViewById(R.id.button_category_interest);
		planButton = (LinearLayout)categoryView.findViewById(R.id.button_category_plan);
		otherButton = (LinearLayout)categoryView.findViewById(R.id.button_category_other);
		
		feelingsButton.setOnClickListener(new CategorySelectedListener(CATEGORY_FEELINGS));
		goodIdeaButton.setOnClickListener(new CategorySelectedListener(CATEGORY_GOOD_IDEA));
		dreamButton.setOnClickListener(new CategorySelectedListener(CATEGORY_DREAM));
		interestButton.setOnClickListener(new CategorySelectedListener(CATEGORY_INTEREST));
		planButton.setOnClickListener(new CategorySelectedListener(CATEGORY_PLAN));
		otherButton.setOnClickListener(new CategorySelectedListener(CATEGORY_OTHER));

		return categoryView;
	}
	
	public class CategorySelectedListener implements OnClickListener {
		
		private int categoryIndex = -1;
		public CategorySelectedListener(int i) {	categoryIndex = i;	}
		@Override
		public void onClick(View v) {
			switch (categoryIndex) {
			case CATEGORY_FEELINGS:
				categoryName = "feeling";
				break;
			case CATEGORY_GOOD_IDEA:
				categoryName = "idea";
				break;
			case CATEGORY_DREAM:
				categoryName = "dream";
				break;
			case CATEGORY_INTEREST:
				categoryName = "interest";
				break;
			case CATEGORY_PLAN:
				categoryName = "plan";
				break;
			case CATEGORY_OTHER:
				categoryName = "other";
				break;
			default:
				break;
			}
			categorySelected.getCategory(categoryName);
			categorySelected.onForwardClick(0);
			categorySelected.onBackClick(0);
		}
		
	}
	
	public interface CategoryInterface {
		public int onForwardClick(int index);
		public int onBackClick(int index);
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
}
