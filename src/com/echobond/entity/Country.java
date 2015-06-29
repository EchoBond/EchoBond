package com.echobond.entity;

import android.content.ContentValues;

/**
 * 
 * @author Luck
 *
 */
public class Country {
	private int id;
	private String name;
	private String region;
	public ContentValues putValues(){
		ContentValues values = new ContentValues();
		values.put("_id", id);
		values.put("name", name);
		values.put("region", region);
		return values;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	
}
