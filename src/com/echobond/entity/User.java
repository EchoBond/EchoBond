package com.echobond.entity;

import java.io.Serializable;
import java.util.ArrayList;

import android.content.ContentValues;

/**
 * 
 * @author Luck
 *
 */
public class User implements Serializable{
	private static final long serialVersionUID = 1L;
	public static final int UNVERFIED = 0;
	public static final int VERIFIED = 1;
	
	public static final int TYPE_INVALID = -1;
	public static final int TYPE_FB = 0;
	public static final int TYPE_EMAIL = 1;
	
	private int type;
	
	private String id;
	private String avatar;
	private String userName;
	private String password;
	private String email;
	private String FBId;
	private String firstName;
	private String lastName;
	private String name;
	private int timeZone;
	private int age;
	private String gender;
	private String birthday;
	private int countryId;
	private int homeId;
	private String bio;
	private String sthInteresting;
	private String amzExp;
	private String toDo;
	private String philosophy;
	private String friendsDesc;
	private String interest;
	private String littleSecret;
	private int langId;
	private String locale;
	private int verified;
	
	private Country country;
	private Country home;
	private Language lang;
	
	private ArrayList<UserTag> userTags;
	private ArrayList<Tag> tags;
	private ArrayList<Group> groups;
	
	public ContentValues putValues(){
		ContentValues values = new ContentValues();
		values.put("_id", id);
		values.put("avatar", avatar);
		values.put("username", userName);
		values.put("password", password);
		values.put("email", email);
		values.put("fb_id", FBId);
		values.put("first_name", firstName);
		values.put("last_name", lastName);
		values.put("name", name);
		values.put("time_zone", timeZone);
		values.put("age", age);
		values.put("gender", gender);
		values.put("birthday", birthday);
		values.put("country_id", countryId);
		values.put("home_id", homeId);
		values.put("bio", bio);
		values.put("sth_interesting", sthInteresting);
		values.put("amz_exp", amzExp);
		values.put("to_do", toDo);
		values.put("philosophy", philosophy);
		values.put("friends_desc", friendsDesc);
		values.put("interest", interest);
		values.put("little_secret", littleSecret);
		values.put("lang_id", langId);
		values.put("verified", verified);
		return values;
	}
	
	public int getType(){
		return type;
	}
	public void setType(int type){
		this.type = type;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getFBId() {
		return FBId;
	}
	public void setFBId(String fBId) {
		FBId = fBId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(int timeZone) {
		this.timeZone = timeZone;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public int getCountryId() {
		return countryId;
	}
	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}
	public int getHomeId() {
		return homeId;
	}
	public void setHomeId(int homeId) {
		this.homeId = homeId;
	}
	public String getBio() {
		return bio;
	}
	public void setBio(String bio) {
		this.bio = bio;
	}
	public String getSthInteresting() {
		return sthInteresting;
	}
	public void setSthInteresting(String sthInteresting) {
		this.sthInteresting = sthInteresting;
	}
	public String getAmzExp() {
		return amzExp;
	}
	public void setAmzExp(String amzExp) {
		this.amzExp = amzExp;
	}
	public String getToDo() {
		return toDo;
	}
	public void setToDo(String toDo) {
		this.toDo = toDo;
	}
	public String getPhilosophy() {
		return philosophy;
	}
	public void setPhilosophy(String philosophy) {
		this.philosophy = philosophy;
	}
	public String getFriendsDesc() {
		return friendsDesc;
	}
	public void setFriendsDesc(String friendsDesc) {
		this.friendsDesc = friendsDesc;
	}
	public String getInterest() {
		return interest;
	}
	public void setInterest(String interest) {
		this.interest = interest;
	}
	public String getLittleSecret() {
		return littleSecret;
	}
	public void setLittleSecret(String littleSecret) {
		this.littleSecret = littleSecret;
	}
	public int getLangId() {
		return langId;
	}
	public void setLangId(int langId) {
		this.langId = langId;
	}
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	public int getVerified() {
		return verified;
	}
	public void setVerified(int verified) {
		this.verified = verified;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Country getCountry() {
		return country;
	}
	public void setCountry(Country country) {
		this.country = country;
	}
	public Country getHome() {
		return home;
	}
	public void setHome(Country home) {
		this.home = home;
	}
	public Language getLang() {
		return lang;
	}
	public void setLang(Language lang) {
		this.lang = lang;
	}
	public ArrayList<UserTag> getUserTags() {
		return userTags;
	}
	public void setUserTags(ArrayList<UserTag> userTags) {
		this.userTags = userTags;
	}
	public ArrayList<Tag> getTags() {
		return tags;
	}
	public void setTags(ArrayList<Tag> tags) {
		this.tags = tags;
	}
	public ArrayList<Group> getGroups() {
		return groups;
	}
	public void setGroups(ArrayList<Group> groups) {
		this.groups = groups;
	}
	
}
