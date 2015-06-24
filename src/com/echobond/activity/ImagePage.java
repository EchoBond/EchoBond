package com.echobond.activity;

import java.util.ArrayList;

import com.echobond.R;
import com.echobond.dao.GroupDAO;
import com.echobond.dao.TagDAO;
import com.echobond.entity.Tag;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ImagePage extends Activity {

	private LinearLayout layout;
	private ImageView image;
	private TextView groupView, tagsView;
	private String type;
	private Integer id;
	private ArrayList<TextView> tags;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_page);

		Bundle b = getIntent().getExtras();
		String url = b.getString("url");
		type = b.getString("type"); 
		id = b.getInt("id");
		tags = new ArrayList<TextView>();
		
		layout = (LinearLayout) findViewById(R.id.image_page_layout);
		image = (ImageView)findViewById(R.id.image_page_image);
		groupView = (TextView) findViewById(R.id.image_page_group_text);
		tagsView = (TextView) findViewById(R.id.image_page_tag_text);
		
		loadGroupName();
		loadTagNames();
		
		if(tags.isEmpty()){
			tagsView.setText(tagsView.getText() + "N/A");
		} else {
			for (TextView view: tags) {
				layout.addView(view);
			}
		}
		
		ImageLoader loader = ImageLoader.getInstance();
		loader.displayImage(url, image);
		
	}
	
	private void loadGroupName(){
		GroupDAO dao = new GroupDAO();
		String args[] = {id+""};
		Cursor cursor = null;
		if("home".equals(type)){
			cursor = dao.loadGroupByHomeThought(args);
		} else {
			cursor = dao.loadGroupByHotThought(args);
		}
		if(null != cursor){
			if(cursor.moveToFirst()){				
				groupView.setText(cursor.getString(cursor.getColumnIndex("name")));
				groupView.setTag(cursor.getInt(cursor.getColumnIndex("_id")));
				return;
			}
		}
		groupView.setText(groupView.getText() + "N/A");
	}
	
	private void loadTagNames(){
		TagDAO dao = new TagDAO();
		String args[] = {id+""};
		Cursor cursor = null;
		ArrayList<Tag> tagList = new ArrayList<Tag>();
		if("home".equals(type)){
			cursor = dao.loadTagsByHomeThought(args);
		} else {
			cursor = dao.loadTagsByHotThought(args);
		}
		if(null != cursor){
			while(cursor.moveToNext()){
				Tag t = new Tag();
				t.setId(cursor.getInt(cursor.getColumnIndex("_id")));
				t.setName(cursor.getString(cursor.getColumnIndex("name")));
				tagList.add(t);
			}
		}
		if(!tagList.isEmpty()){
			for (Tag tag : tagList) {
				TextView view = new TextView(this);
				view.setText("#"+tag.getName());
				view.setTextColor(Color.BLUE);
				view.setTag(tag.getId());
				tags.add(view);
			}
		}
	}
}
