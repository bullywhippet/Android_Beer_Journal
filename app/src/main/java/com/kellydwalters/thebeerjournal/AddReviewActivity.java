package com.kellydwalters.thebeerjournal;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Rating;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

public class AddReviewActivity extends Activity implements OnClickListener, OnItemSelectedListener {

	public static final String TAG = "AddReviewActivity";

	private EditText txtBeerName, txtDescription, txtAbv, txtReview, txtRating;
	private RatingBar rbBeerRating;
	private Spinner mSpinnerCategory;
	private Button mBtnAdd, btnFind;

	private CategoryDAO mCategoryDao;
	private ReviewDAO mReviewDao;
	
	private Category mSelectedCategory;
	private SpinnerCategoriesAdapter mAdapter;

	SharedPreferences sharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("settings",0);
        switchTheme();
		setContentView(R.layout.activity_add_review);

		// get access to the buttons
		initViews();

		// get access to the helper daos
		this.mCategoryDao = new CategoryDAO(this);
		this.mReviewDao = new ReviewDAO(this);
		
		//fill the spinner with categories
		List<Category> listCategories = mCategoryDao.getAllCategories();
		if(listCategories != null) {
			mAdapter = new SpinnerCategoriesAdapter(this, listCategories);
			mSpinnerCategory.setAdapter(mAdapter);
			mSpinnerCategory.setOnItemSelectedListener(this);
		}
	}

	private void initViews() {
		this.txtBeerName = findViewById(R.id.txt_beer_name);
		this.txtDescription =  findViewById(R.id.txt_description);
		this.txtAbv = findViewById(R.id.txt_abv);
		this.txtReview =  findViewById(R.id.txt_review);
//		this.txtRating =  findViewById(R.id.txt_rating);
		this.mSpinnerCategory = findViewById(R.id.spinner_categories);
		this.mBtnAdd = findViewById(R.id.btn_add_review_to_DB);
		this.btnFind =findViewById(R.id.btnFind);

		this.rbBeerRating = findViewById(R.id.rbBeerRating);

		this.btnFind.setOnClickListener(this);
		this.mBtnAdd.setOnClickListener(this);
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
		case R.id.btn_add_review_to_DB:

		    //get the values from all the fields
			Editable beerName = txtBeerName.getText();
			Editable description = txtDescription.getText();
			Editable abv = txtAbv.getText();
			Editable review = txtReview.getText();
            String rating = String.valueOf(rbBeerRating.getRating());

			mSelectedCategory = (Category) mSpinnerCategory.getSelectedItem();

			// if all the fields are set up
			if (!TextUtils.isEmpty(beerName) && !TextUtils.isEmpty(description)
					&& !TextUtils.isEmpty(abv) 
					&& !TextUtils.isEmpty(rating) && mSelectedCategory != null
					&& !TextUtils.isEmpty(review)) {
				// add the Review to database

				Review createdReview = mReviewDao.createReview(beerName.toString(),
                        description.toString(), abv.toString(), review.toString(), rating, mSelectedCategory.getId());
				
				Log.d(TAG, "added review : "+ createdReview.getBeerName()+" "+ createdReview.getDescription());
				setResult(RESULT_OK);
				finish();
			}
			else {
				Toast.makeText(this, R.string.empty_fields_message, Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.btnFind:
		    // if user wants to find a beer, send value to the find results activity
			Intent intent = new Intent(AddReviewActivity.this,FindResults.class);
			intent.putExtra("beerName", txtBeerName.getText().toString());
			startActivityForResult(intent, 0);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK){
			// set text fields to selected beer data
			txtBeerName.setText(data.getStringExtra("name"));
			txtAbv.setText(data.getStringExtra("abv"));
			txtDescription.setText(data.getStringExtra("description"));

		} else {

		    // if they don't select a beer, make sure that anything that
            // was  entered is gone for a fresh start
			clearTextFields();

			//cancelled
			Log.d("KELLLY", "Nothing selected " );
		}
	}

	private void clearTextFields(){
		txtAbv.setText("");
		txtDescription.setText("");
		txtReview.setText("");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mCategoryDao.close();
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		// select category
	    mSelectedCategory = mAdapter.getItem(position);
		Log.d(TAG, "selectedCategory : "+ mSelectedCategory.getName());
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(this, "Nothing selected", Toast.LENGTH_SHORT).show();
    }
}
