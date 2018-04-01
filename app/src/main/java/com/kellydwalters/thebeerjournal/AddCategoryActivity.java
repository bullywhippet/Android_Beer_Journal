package com.kellydwalters.thebeerjournal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class AddCategoryActivity extends Activity implements OnClickListener {

	public static final String TAG = "AddCategoryActivity";

	private EditText mTxtCategoryName;
	private Button mBtnAdd;

	private CategoryDAO mCategoryDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_category);

		initViews();

		this.mCategoryDao = new CategoryDAO(this);
	}

	private void initViews() {
		this.mTxtCategoryName = findViewById(R.id.txt_category_name);

		this.mBtnAdd = findViewById(R.id.btn_add);

		this.mBtnAdd.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_add:
			Editable categoryName = mTxtCategoryName.getText();

			if (!TextUtils.isEmpty(categoryName)) {
				// add the company to database
				Category createdCategory = mCategoryDao.createCategory(
						categoryName.toString());
				
				Log.d(TAG, "added company : "+ createdCategory.getName());
				Intent intent = new Intent();
				intent.putExtra(ListCategoriesActivity.EXTRA_ADDED_CATEGORY, createdCategory);
				setResult(RESULT_OK, intent);
				finish();
			}
			else {
				Toast.makeText(this, R.string.empty_fields_message, Toast.LENGTH_LONG).show();
			}
			break;

		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mCategoryDao.close();
	}
}
