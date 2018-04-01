package com.kellydwalters.thebeerjournal;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class ListReviewsAdapter extends BaseAdapter {
	
	public static final String TAG = "ListReviewsAdapter";
	
	private List<Review> mItems;
	private LayoutInflater mInflater;
	
	public ListReviewsAdapter(Context context, List<Review> listCompanies) {
		this.setItems(listCompanies);
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return (getItems() != null && !getItems().isEmpty()) ? getItems().size() : 0 ;
	}

	@Override
	public Review getItem(int position) {
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
			v = mInflater.inflate(R.layout.list_item_review, parent, false);
			holder = new ViewHolder();
			holder.tvBeerName= v.findViewById(R.id.tvBeerName);
			holder.tvDescription = v.findViewById(R.id.tvDescription);
			holder.tvAbv = v.findViewById(R.id.tvAbv);
			holder.tvReview = v.findViewById(R.id.tvReview);
			holder.tvCategory = v.findViewById(R.id.tvCategoryName);
			holder.tvRating = v.findViewById(R.id.tvRating);
			v.setTag(holder);
		}
		else {
			holder = (ViewHolder) v.getTag();
		}
		
		// fill row data
		Review currentItem = getItem(position);
		if(currentItem != null) {
			holder.tvBeerName.setText(currentItem.getBeerName().toString());
			holder.tvDescription.setText(currentItem.getDescription());
			holder.tvAbv.setText(currentItem.getAbv());
			holder.tvReview.setText(currentItem.getReview());
			holder.tvRating.setText(String.valueOf(currentItem.getRating()));
			holder.tvCategory.setText(currentItem.getCategory().getName());
		}
		
		return v;
	}
	
	public List<Review> getItems() {
		return mItems;
	}

	public void setItems(List<Review> mItems) {
		this.mItems = mItems;
	}

	class ViewHolder {
		TextView tvBeerName, tvDescription, tvAbv, tvReview, tvRating, tvCategory;

	}

}
