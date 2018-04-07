package com.kellydwalters.thebeerjournal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

public class ReviewDetail extends AppCompatActivity {
    private Intent intent;
    private TextView tvBeerName, tvDescription, tvABV, tvCategory, tvReview;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_detail);

        intent = getIntent();

        tvBeerName = findViewById(R.id.tvDetailBeerName);
        tvDescription = findViewById(R.id.tvDetailDescription);
        tvABV = findViewById(R.id.tvDetailABV);
        tvCategory = findViewById(R.id.tvDetailCategory);
        tvReview = findViewById(R.id.tvDetailReview);
        ratingBar = findViewById(R.id.ratingBar);

        tvBeerName.setText(intent.getStringExtra("name"));
        tvDescription.setText(intent.getStringExtra("description"));
        tvABV.setText(intent.getStringExtra("abv"));
        tvCategory.setText(intent.getStringExtra("category"));
        tvReview.setText(intent.getStringExtra("review"));

        ratingBar.setRating(Float.parseFloat(intent.getStringExtra("rating")));

//        (intent.getStringExtra("review")
//        (intent.getStringExtra("rating")
//        (intent.getStringExtra("category")

    }
}
