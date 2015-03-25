package com.echobond.db;

import com.echobond.R;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;

public class TagDB extends MyDBHelper {
	
	/*Singleton pattern implementation*/
	private static TagDB INSTANCE;
	public static TagDB getInstance(Context ctx){
		if(null == INSTANCE){
			INSTANCE = new TagDB(ctx.getApplicationContext());
		}
		return INSTANCE;
	}
	
	public TagDB(Context ctx){
		super(ctx);
		Resources res = ctx.getResources();
		this.tblName = res.getString(R.string.tbl_tag);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
	}

	
}
