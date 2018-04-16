package com.kellydwalters.thebeerjournal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Database helper class to create tables and cols
 */
public class DBHelper extends SQLiteOpenHelper {

	public static final String TAG = "DBHelper";

	// columns of the categories table
	public static final String TABLE_CATEGORIES = "categories";
	public static final String COLUMN_CATEGORY_ID = "_id";
	public static final String COLUMN_CATEGORY_NAME = "category_name";


	// columns of the reviews table
	public static final String TABLE_REVIEWS = "reviews";
	public static final String COLUMN_REVIEW_ID = "_id";
	public static final String COLUMN_REVIEW_BEER_NAME = "beer_name";
	public static final String COLUMN_REVIEW_DESCRIPTION = "description";
	public static final String COLUMN_REVIEW_ABV = "abv";
	public static final String COLUMN_REVIEW_REVIEW = "review";
	public static final String COLUMN_REVIEW_RATING = "rating";
	public static final String COLUMN_REVIEW_CATEGORY_ID = "category_id";

	private static final String DATABASE_NAME = "categories.db";
	private static final int DATABASE_VERSION = 2;

	// SQL statement of the reviews table creation
	private static final String SQL_CREATE_TABLE_REVIEWS = "CREATE TABLE " + TABLE_REVIEWS + "(" 
			+ COLUMN_REVIEW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ COLUMN_REVIEW_BEER_NAME + " TEXT NOT NULL, "
			+ COLUMN_REVIEW_DESCRIPTION + " TEXT NOT NULL, "
			+ COLUMN_REVIEW_ABV + " TEXT NOT NULL, "
			+ COLUMN_REVIEW_REVIEW + " TEXT NOT NULL, "
			+ COLUMN_REVIEW_RATING + " TEXT NOT NULL, "
			+ COLUMN_REVIEW_CATEGORY_ID + " INTEGER NOT NULL "
			+");";

	// SQL statement of the categories table creation
	private static final String SQL_CREATE_TABLE_CATEGORIES = "CREATE TABLE " + TABLE_CATEGORIES + "(" 
			+ COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ COLUMN_CATEGORY_NAME + " TEXT NOT NULL "
			+");";

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(SQL_CREATE_TABLE_CATEGORIES);
		database.execSQL(SQL_CREATE_TABLE_REVIEWS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TAG,
				"Upgrading the database from version " + oldVersion + " to "+ newVersion);
		// clear all data
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_REVIEWS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
		
		// recreate the tables
		onCreate(db);
	}

	public DBHelper(Context context, String name, CursorFactory factory,int version) {
		super(context, DATABASE_NAME, factory, DATABASE_VERSION);
	}
}
