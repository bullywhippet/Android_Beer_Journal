package com.kellydwalters.thebeerjournal;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class SpinnerCategoriesAdapter extends BaseAdapter {
	
	public static final String TAG = "SpinnerCategoriesAdapter";
	
	private List<Category> mItems;
	private LayoutInflater mInflater;
	
	public SpinnerCategoriesAdapter(Context context, List<Category> listCompanies) {
		this.setItems(listCompanies);
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return (getItems() != null && !getItems().isEmpty()) ? getItems().size() : 0 ;
	}

	@Override
	public Category getItem(int position) {
		return (getItems() != null && !getItems().isEmpty()) ? getItems().get(position) : null ;
	}

	@Override
	public long getItemId(int position) {
		return (getItems() != null && !getItems().isEmpty()) ? getItems().get(position).getId() : position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		ViewHolder holder;
		if(v == null) {
			v = mInflater.inflate(R.layout.spinner_item_category, parent, false);
			holder = new ViewHolder();
			holder.txtCategoryName = v.findViewById(R.id.tvCatName);
			v.setTag(holder);
		}
		else {
			holder = (ViewHolder) v.getTag();
		}
		
		// fill row data
		Category currentItem = getItem(position);
		if(currentItem != null) {
			holder.txtCategoryName.setText(currentItem.getName());
		}
		
		return v;
	}
	
	public List<Category> getItems() {
		return mItems;
	}

	public void setItems(List<Category> mItems) {
		this.mItems = mItems;
	}

	class ViewHolder {
		TextView txtCategoryName;
	}
}
