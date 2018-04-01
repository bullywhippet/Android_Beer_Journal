package com.kellydwalters.thebeerjournal;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ReviewDAO {

	public static final String TAG = "ReviewDAO";

	private Context mContext;

	// Database fields
	private SQLiteDatabase mDatabase;
	private DBHelper mDbHelper;
	private String[] mAllColumns = { DBHelper.COLUMN_REVIEW_ID,
			DBHelper.COLUMN_REVIEW_BEER_NAME,
			DBHelper.COLUMN_REVIEW_DESCRIPTION,
			DBHelper.COLUMN_REVIEW_ABV,
			DBHelper.COLUMN_REVIEW_REVIEW,
			DBHelper.COLUMN_REVIEW_RATING,
			DBHelper.COLUMN_REVIEW_CATEGORY_ID };

	public ReviewDAO(Context context) {
		mDbHelper = new DBHelper(context);
		this.mContext = context;
		// open the database
		try {
			open();
		} catch (SQLException e) {
			Log.e(TAG, "SQLException on opening database " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void open() throws SQLException {
		mDatabase = mDbHelper.getWritableDatabase();
	}

	public void close() {
		mDbHelper.close();
	}

	public Review createReview(String beerName, String description, String abv, String review, String rating,
								long categoryId) {
		ContentValues values = new ContentValues();
		values.put(DBHelper.COLUMN_REVIEW_BEER_NAME, beerName);
		values.put(DBHelper.COLUMN_REVIEW_DESCRIPTION, description);
		values.put(DBHelper.COLUMN_REVIEW_ABV, abv);
		values.put(DBHelper.COLUMN_REVIEW_REVIEW, review);
		values.put(DBHelper.COLUMN_REVIEW_RATING, rating);
		values.put(DBHelper.COLUMN_REVIEW_CATEGORY_ID, categoryId);
		long insertId = mDatabase
				.insert(DBHelper.TABLE_REVIEWS, null, values);
		Cursor cursor = mDatabase.query(DBHelper.TABLE_REVIEWS, mAllColumns,
				DBHelper.COLUMN_REVIEW_ID + " = " + insertId, null, null,
				null, null);
		cursor.moveToFirst();
		Review newReview = cursorToReview(cursor);
		cursor.close();
		return newReview;
	}

	public void deleteReview(Review review) {
		long id = review.getId();
		System.out.println("the deleted review has the id: " + id);
		mDatabase.delete(DBHelper.TABLE_REVIEWS, DBHelper.COLUMN_REVIEW_ID
				+ " = " + id, null);
	}

	public List<Review> getAllReviews() {
		List<Review> listReviews = new ArrayList<Review>();

		Cursor cursor = mDatabase.query(DBHelper.TABLE_REVIEWS, mAllColumns,
				null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Review review = cursorToReview(cursor);
			listReviews.add(review);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return listReviews;
	}

	public List<Review> getReviewsOfCategory(long categoryId) {
		List<Review> listReviews = new ArrayList<Review>();

		Cursor cursor = mDatabase.query(DBHelper.TABLE_REVIEWS, mAllColumns,
				DBHelper.COLUMN_CATEGORY_ID + " = ?",
				new String[] { String.valueOf(categoryId) }, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Review review = cursorToReview(cursor);
			listReviews.add(review);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return listReviews;
	}

	private Review cursorToReview(Cursor cursor) {
		Review review = new Review();
		review.setId(cursor.getLong(0));
		review.setBeerName(cursor.getString(1));
		review.setDescription(cursor.getString(2));
		review.setAbv(cursor.getString(3));
		review.setReview(cursor.getString(4));
		review.setRating(cursor.getString(5));

		// get The category by id
		long categoryId = cursor.getLong(6);
		CategoryDAO dao = new CategoryDAO(mContext);
		Category category = dao.getCategoryById(categoryId);
		if (category != null)
			review.setCategory(category);

		return review;
	}

}
