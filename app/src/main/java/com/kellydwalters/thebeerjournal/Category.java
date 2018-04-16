package com.kellydwalters.thebeerjournal;

import java.io.Serializable;

/**
 * models a Category object
 * Categories just have a name and an ID
 */
public class Category implements Serializable {

	public static final String TAG = "Category";
	// this was in the demo i followed.
	private static final long serialVersionUID = -7406082437623008161L;
	
	private long mId;
	private String mName;

	public Category() {}

	public Category(String name) {
		this.mName = name;
	}
	
	public long getId() {
		return mId;
	}
	public void setId(long mId) {
		this.mId = mId;
	}
	public String getName() {
		return mName;
	}
	public void setName(String mCategory) {
		this.mName = mCategory;
	}
	
}
