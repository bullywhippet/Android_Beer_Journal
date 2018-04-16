package com.kellydwalters.thebeerjournal;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListReviewsActivity extends Activity implements OnItemLongClickListener, OnItemClickListener, OnClickListener {

	public static final String TAG = "ListReviewsActivity";

    SharedPreferences sharedPreferences;

    public static final int REQUEST_CODE_ADD_REVIEW = 40;
	public static final String EXTRA_ADDED_REVIEW = "extra_key_added_review";
	public static final String EXTRA_SELECTED_CATEGORY_ID = "extra_key_selected_category_id";

	private ListView mListviewReviews;
	private TextView mTxtEmptyListReviews;
	private ImageButton mBtnAddReview;

	private ListReviewsAdapter mAdapter;
	private List<Review> mListReviews;
	private ReviewDAO mReviewDao;

	private long mCategoryId = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sharedPreferences = getSharedPreferences("settings",0);
		switchTheme();
		setContentView(R.layout.activity_list_reviews);

		// initialize views
		initViews();

		// get the category id from extras
		mReviewDao = new ReviewDAO(this);
		Intent intent  = getIntent();
		if(intent != null) {
			this.mCategoryId = intent.getLongExtra(EXTRA_SELECTED_CATEGORY_ID, -1);
		}

        // fill the listView
		if(mCategoryId != -1) {
			mListReviews = mReviewDao.getReviewsOfCategory(mCategoryId);
			if(mListReviews != null && !mListReviews.isEmpty()) {
				mAdapter = new ListReviewsAdapter(this, mListReviews);
				mListviewReviews.setAdapter(mAdapter);
			}
			else {
				mTxtEmptyListReviews.setVisibility(View.VISIBLE);
				mListviewReviews.setVisibility(View.GONE);
			}
		} // set view based on selected category
		else{
			// fill the listView
			mListReviews = mReviewDao.getAllReviews();
            if(mListReviews != null && !mListReviews.isEmpty()) {
                mAdapter = new ListReviewsAdapter(this, mListReviews);
                mListviewReviews.setAdapter(mAdapter);
            }
            else {
                mTxtEmptyListReviews.setVisibility(View.VISIBLE);
                mListviewReviews.setVisibility(View.GONE);
            }
		}
	}

	private void initViews() {
		this.mListviewReviews = findViewById(R.id.list_reviews);
		this.mTxtEmptyListReviews = findViewById(R.id.txt_empty_list_reviews);
		this.mBtnAddReview = findViewById(R.id.btn_add_review);
		this.mListviewReviews.setOnItemClickListener(this);
		this.mListviewReviews.setOnItemLongClickListener(this);
		this.mBtnAddReview.setOnClickListener(this);
	}

	private void switchTheme() {
		String theme = sharedPreferences.getString("theme", "regular");

		switch(theme)
		{
			case "regular":
				setTheme(R.style.AppTheme);
				break;
			case "dark":
				setTheme(R.style.nightMode);
				break;
			default:
				break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_add_review:
			Intent intent = new Intent(this, AddReviewActivity.class);
			startActivityForResult(intent, REQUEST_CODE_ADD_REVIEW);
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUEST_CODE_ADD_REVIEW) {
			if(resultCode == RESULT_OK) {
				//refresh the listView
				if(mListReviews == null || !mListReviews.isEmpty()) {
					mListReviews = new ArrayList<Review>();
				}

				if(mReviewDao == null) {
                    mReviewDao = new ReviewDAO(this);
                }
//				    mListReviews = mReviewDao.getReviewsOfCategory(mCategoryId);
                mListReviews = mReviewDao.getAllReviews();
				if(mAdapter == null) {

					mAdapter = new ListReviewsAdapter(this, mListReviews);
					mListviewReviews.setAdapter(mAdapter);

					// handle message that appears if list is empty
					if(mListviewReviews.getVisibility() != View.VISIBLE) {
						mTxtEmptyListReviews.setVisibility(View.GONE);
						mListviewReviews.setVisibility(View.VISIBLE);
					}
				}
				else {
					mAdapter.setItems(mListReviews);
					mAdapter.notifyDataSetChanged();
				}
			}
		}
		else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

    @Override
    protected void onResume() {
        super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mReviewDao.close();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Review clickedReview = mAdapter.getItem(position);
		Log.d(TAG, "clickedItem : "+ clickedReview.getBeerName()+" "+ clickedReview.getRating());

		// when a review is clicked take its values and send them to the detail activity
		try {
            Intent intent = new Intent(ListReviewsActivity.this, ReviewDetail.class);
            intent.putExtra("name", clickedReview.getBeerName());
            intent.putExtra("description", clickedReview.getDescription());
            intent.putExtra("abv", clickedReview.getAbv());
            intent.putExtra("review", clickedReview.getReview());
            intent.putExtra("rating", clickedReview.getRating());
            intent.putExtra("category", clickedReview.getCategory().getName());
            startActivity(intent);

        }catch (Exception e) {
            Toast.makeText(ListReviewsActivity.this, "There was an error loading that Item", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


    }

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
	    // delete on long click
		Review clickedReview = mAdapter.getItem(position);
		Log.d(TAG, "longClickedItem : "+ clickedReview.getBeerName()+" "+ clickedReview.getRating());
		
		showDeleteDialogConfirmation(clickedReview);
		return true;
	}
	
	private void showDeleteDialogConfirmation(final Review review) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		 
        alertDialogBuilder.setTitle("Delete");
		alertDialogBuilder
				.setMessage("Are you sure you want to delete the review \""
						+ review.getBeerName() + " "
						+ review.getRating() + "\"");
 
        // set positive button YES message
        alertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// delete the review and refresh the list
				if(mReviewDao != null) {
					mReviewDao.deleteReview(review);
					//refresh the listView
					mListReviews.remove(review);
					if(mListReviews.isEmpty()) {
						mListviewReviews.setVisibility(View.GONE);
						mTxtEmptyListReviews.setVisibility(View.VISIBLE);
					}

					mAdapter.setItems(mListReviews);
					mAdapter.notifyDataSetChanged();
				}
				
				dialog.dismiss();
				Toast.makeText(ListReviewsActivity.this, R.string.review_deleted_successfully, Toast.LENGTH_SHORT).show();
			}
		});
        
        // set neutral button OK
        alertDialogBuilder.setNeutralButton(android.R.string.no, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Dismiss the dialog
                dialog.dismiss();
			}
		});
        
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show alert
        alertDialog.show();
	}
}
