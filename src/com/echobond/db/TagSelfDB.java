package com.echobond.db;

import com.echobond.R;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;

public class TagSelfDB extends MyDBHelper {
	
	/*Singleton pattern implementation*/
	private static TagSelfDB INSTANCE;
	public static TagSelfDB getInstance(Context ctx){
		if(null == INSTANCE){
			INSTANCE = new TagSelfDB(ctx.getApplicationContext());
		}
		return INSTANCE;
	}
	
	public TagSelfDB(Context ctx){
		super(ctx);
		Resources res = ctx.getResources();
		this.tblName = res.getString(R.string.tbl_tag_slf);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
	}
	
}
