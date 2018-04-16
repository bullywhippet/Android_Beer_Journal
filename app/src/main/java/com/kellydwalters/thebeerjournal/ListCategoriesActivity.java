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

/**
 * Shows all the categories that have been entered
 */
public class ListCategoriesActivity extends Activity implements OnItemLongClickListener, OnItemClickListener, OnClickListener {
	
	public static final String TAG = "ListCategoriesActivity";
	
	public static final int REQUEST_CODE_ADD_CATEGORY = 40;
	public static final String EXTRA_ADDED_CATEGORY = "extra_key_added_category";
	
	private ListView mListviewCategories;
	private TextView mTxtEmptyListCategories;
	private ImageButton mBtnAddCategory;
	
	private ListCategoriesAdapter mAdapter;
	private List<Category> mListCategories;
	private CategoryDAO mCategoryDao;

    SharedPreferences sharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sharedPreferences = getSharedPreferences("settings",0);
		switchTheme();
		setContentView(R.layout.activity_list_categories);

        sharedPreferences = getSharedPreferences("settings",0);

        // initialize views
		initViews();
		
		// fill the listView
		mCategoryDao = new CategoryDAO(this);
		mListCategories = mCategoryDao.getAllCategories();
		if(mListCategories != null && !mListCategories.isEmpty()) {
			mAdapter = new ListCategoriesAdapter(this, mListCategories);
			mListviewCategories.setAdapter(mAdapter);
		}
		else {
		    // if there is no categories show a message
			mTxtEmptyListCategories.setVisibility(View.VISIBLE);
			mListviewCategories.setVisibility(View.GONE);
		}
	}

	private void initViews() {
		this.mListviewCategories = findViewById(R.id.list_categories);
		this.mTxtEmptyListCategories =  findViewById(R.id.txt_empty_list_categories);
		this.mBtnAddCategory = findViewById(R.id.btn_add_category);
		this.mListviewCategories.setOnItemClickListener(this);
		this.mListviewCategories.setOnItemLongClickListener(this);
		this.mBtnAddCategory.setOnClickListener(this);
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
		case R.id.btn_add_category:
		    // go to the add category activity
			Intent intent = new Intent(this, AddCategoryActivity.class);
			startActivityForResult(intent, REQUEST_CODE_ADD_CATEGORY);
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUEST_CODE_ADD_CATEGORY) {
			if(resultCode == RESULT_OK) {
				// add the added category to the listCategories and refresh the listView
				if(data != null) {
					Category createdCategory = (Category) data.getSerializableExtra(EXTRA_ADDED_CATEGORY);

					if(createdCategory != null) {
						if(mListCategories == null)
						    // if there's no list, make a new one
							mListCategories = new ArrayList<Category>();
						// add the category to the list
						mListCategories.add(createdCategory);
						
						if(mAdapter == null) {
							if(mListviewCategories.getVisibility() != View.VISIBLE) {
								mListviewCategories.setVisibility(View.VISIBLE);
								mTxtEmptyListCategories.setVisibility(View.GONE);
							}
								
							mAdapter = new ListCategoriesAdapter(this, mListCategories);
							mListviewCategories.setAdapter(mAdapter);
						}
						else {
							mAdapter.setItems(mListCategories);
							mAdapter.notifyDataSetChanged();
						}
					}
				}
			}
		}
		else 
			super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mCategoryDao.close();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Category clickedCategory = mAdapter.getItem(position);
		Log.d(TAG, "clickedItem : "+ clickedCategory.getName());
		// Set up to show all reviews within an individual category,
        // however, its kind of glitchy. I intend to work through it in the future so I didn't
        // want to completely get rid of it for my submission
		Intent intent = new Intent(this, ListReviewsActivity.class);
		intent.putExtra(ListReviewsActivity.EXTRA_SELECTED_CATEGORY_ID, clickedCategory.getId());
		startActivity(intent);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
	    // long click brings up delete dialog
		Category clickedCategory = mAdapter.getItem(position);
		Log.d(TAG, "longClickedItem : "+ clickedCategory.getName());
		showDeleteDialogConfirmation(clickedCategory);
		return true;
	}

	private void showDeleteDialogConfirmation(final Category clickedCategory) {
	    // make an alert dialog
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		 
        alertDialogBuilder.setTitle("Delete");
        alertDialogBuilder.setMessage("Are you sure you want to delete the \""+ clickedCategory.getName()+"\" category ?");
 
        // set positive button YES message
        alertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// delete the category and refresh the list
				if(mCategoryDao != null) {
					mCategoryDao.deleteCategory(clickedCategory);
					mListCategories.remove(clickedCategory);
					
					//refresh the listView
					if(mListCategories.isEmpty()) {
						mListviewCategories.setVisibility(View.GONE);
						mTxtEmptyListCategories.setVisibility(View.VISIBLE);
					}
					mAdapter.setItems(mListCategories);
					mAdapter.notifyDataSetChanged();
				}
				
				dialog.dismiss();
				Toast.makeText(ListCategoriesActivity.this, R.string.category_deleted_successfully, Toast.LENGTH_SHORT).show();
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
