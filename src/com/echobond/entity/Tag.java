package com.echobond.entity;

import java.util.ArrayList;

/**
 * 
 * @author Luck
 *
 */
public class Tag {
	private int id;
	private String name;
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
			Tag tag = new Tag();
			tag.setName(name);
			tags.add(tag);
		}
		return tags;
	}
	
}
