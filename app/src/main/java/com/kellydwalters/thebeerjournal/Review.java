package com.kellydwalters.thebeerjournal;

import java.io.Serializable;

/**
 * Models a review of a beer
 */
public class Review implements Serializable {

	public static final String TAG = "Review";
	// this serial number was part of one of the tutorials.
	private static final long serialVersionUID = -7406082437623008161L;

	private long mId;
	private String mBeerName, mDescription, mAbv, mReview, mRating;
	private Category mCategory;

	public Review() {
		
	}

	public Review(String beerName, String description, String abv, String review, String rating) {
		this.mBeerName = beerName;
		this.mDescription = description;
		this.mAbv = abv;
		this.mReview = review;
		this.mRating = rating;
	}

	public long getId() {
		return mId;
	}

	public void setId(long mId) {
		this.mId = mId;
	}

	public String getBeerName() {
		return mBeerName;
	}

	public void setBeerName(String mBeerName) {
		this.mBeerName = mBeerName;
	}

	public String getDescription() {
		return mDescription;
	}

	public void setDescription(String mDescription) {
		this.mDescription = mDescription;
	}

	public String getAbv() {
		return mAbv;
	}

	public void setAbv(String mAbv) {
		this.mAbv = mAbv;
	}

	public String getReview() {
		return mReview;
	}

	public void setReview(String mReview) {
		this.mReview = mReview;
	}

	public String getRating() {
		return mRating;
	}

	public void setRating(String mRating) {
		this.mRating = mRating;
	}


	public Category getCategory() {
		return mCategory;
	}

	public void setCategory(Category mCategory) {
		this.mCategory = mCategory;
	}
}
