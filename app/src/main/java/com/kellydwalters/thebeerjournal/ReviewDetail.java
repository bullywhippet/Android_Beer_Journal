package com.kellydwalters.thebeerjournal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

public class ReviewDetail extends AppCompatActivity {
    private Intent intent;
    private TextView tvBeerName, tvDescription, tvABV, tvCategory, tvReview;
    private RatingBar ratingBar;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("settings",0);
        switchTheme();
        setContentView(R.layout.activity_review_detail);

        intent = getIntent();

        initViews();

        // display the values passed in from intent
        setValues();

    } // onCreate

    private void initViews() {
        tvBeerName = findViewById(R.id.tvDetailBeerName);
        tvDescription = findViewById(R.id.tvDetailDescription);
        tvABV = findViewById(R.id.tvDetailABV);
        tvCategory = findViewById(R.id.tvDetailCategory);
        tvReview = findViewById(R.id.tvDetailReview);
        ratingBar = findViewById(R.id.ratingBar);
    }

    private void setValues() {
        tvBeerName.setText(intent.getStringExtra("name"));
        tvDescription.setText(intent.getStringExtra("description"));
        tvABV.setText(intent.getStringExtra("abv"));
        tvCategory.setText(intent.getStringExtra("category"));
        tvReview.setText(intent.getStringExtra("review"));

        ratingBar.setRating(Float.parseFloat(intent.getStringExtra("rating")));
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
}
