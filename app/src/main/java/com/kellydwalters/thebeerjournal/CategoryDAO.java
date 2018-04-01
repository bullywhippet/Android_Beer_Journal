package com.kellydwalters.thebeerjournal;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class CategoryDAO {

	public static final String TAG = "CategoryDAO";

	// Database fields
	private SQLiteDatabase mDatabase;
	private DBHelper mDbHelper;
	private Context mContext;
	private String[] mAllColumns = { DBHelper.COLUMN_CATEGORY_ID,
			DBHelper.COLUMN_CATEGORY_NAME };

	public CategoryDAO(Context context) {
		this.mContext = context;
		mDbHelper = new DBHelper(context);
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

	public Category createCategory(String name) {
		ContentValues values = new ContentValues();
		values.put(DBHelper.COLUMN_CATEGORY_NAME, name);
		
		long insertId = mDatabase
				.insert(DBHelper.TABLE_CATEGORIES, null, values);
		Cursor cursor = mDatabase.query(DBHelper.TABLE_CATEGORIES, mAllColumns,
				DBHelper.COLUMN_CATEGORY_ID + " = " + insertId, null, null,
				null, null);
		cursor.moveToFirst();
		Category newCategory = cursorToCategory(cursor);
		cursor.close();
		return newCategory;
	}

	public void deleteCategory(Category category) {
		long id = category.getId();
		// delete all employees of this category
		ReviewDAO reviewDao = new ReviewDAO(mContext);
		List<Review> listReviews = reviewDao.getReviewsOfCategory(id);
		if (listReviews != null && !listReviews.isEmpty()) {
			for (Review e : listReviews) {
				reviewDao.deleteReview(e);
			}
		}

		System.out.println("the deleted category has the id: " + id);
		mDatabase.delete(DBHelper.TABLE_CATEGORIES, DBHelper.COLUMN_CATEGORY_ID
				+ " = " + id, null);
	}

	public List<Category> getAllCategories() {
		List<Category> listCategories = new ArrayList<Category>();

		Cursor cursor = mDatabase.query(DBHelper.TABLE_CATEGORIES, mAllColumns,
				null, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Category category = cursorToCategory(cursor);
				listCategories.add(category);
				cursor.moveToNext();
			}

			// make sure to close the cursor
			cursor.close();
		}
		return listCategories;
	}

	public Category getCategoryById(long id) {
		Cursor cursor = mDatabase.query(DBHelper.TABLE_CATEGORIES, mAllColumns,
				DBHelper.COLUMN_CATEGORY_ID + " = ?",
				new String[] { String.valueOf(id) }, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}

		Category category = cursorToCategory(cursor);
		return category;
	}

	protected Category cursorToCategory(Cursor cursor) {
		Category category = new Category();
		category.setId(cursor.getLong(0));
		category.setName(cursor.getString(1));

		return category;
	}

}
