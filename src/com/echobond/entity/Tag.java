package com.echobond.entity;

import java.io.Serializable;
import java.util.ArrayList;

import android.content.ContentValues;

/**
 * 
 * @author Luck
 *
 */
public class Tag implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	public ContentValues putValues(){
		ContentValues values = new ContentValues();
		values.put("_id", id);
		values.put("name", name);
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
	
	public static ArrayList<Tag> str2TagList(String src){
		ArrayList<Tag> tags = new ArrayList<Tag>();
		String[] tagNames = src.split(",");
		for (String name : tagNames) {
			if(!name.trim().equals("")){
				Tag tag = new Tag();
				tag.setName(name.trim());
				tags.add(tag);
			}
		}
		return tags;
	}
	
}
