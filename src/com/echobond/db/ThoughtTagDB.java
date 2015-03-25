package com.echobond.db;

import java.util.ArrayList;

import com.echobond.R;
import com.echobond.entity.Tag;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;

public class ThoughtTagDB extends MyDBHelper {
	
	/*Singleton pattern implementation*/
	private static ThoughtTagDB INSTANCE;
	public static ThoughtTagDB getInstance(Context ctx){
		if(null == INSTANCE){
			INSTANCE = new ThoughtTagDB(ctx.getApplicationContext());
		}
		return INSTANCE;
	}
	
	public ThoughtTagDB(Context ctx){
		super(ctx);
		Resources res = ctx.getResources();
		this.tblName = res.getString(R.string.tbl_t_tag);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
	}
	
	public void addThoughtTags(int thoughtId, ArrayList<Tag> tags){
		for (Tag tag : tags) {
			ContentValues values = new ContentValues();
			values.put("thought_id", thoughtId);
			values.put("tag_id", tag.getId());
			replace(tblName, values);
		}
	}
	
}
