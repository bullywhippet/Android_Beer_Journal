package com.kellydwalters.thebeerjournal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnAddBeer, btnAddCategory, btnViewBeer, btnViewCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAddBeer = findViewById(R.id.btnAdd);
        btnAddCategory = findViewById(R.id.btnAddCat);
        btnViewCategory =findViewById(R.id.btnViewCat);
        btnViewBeer = findViewById(R.id.btnViewBeer);

        btnAddBeer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(MainActivity.this,AddReviewActivity.class);
                startActivity(i);
            }
        });

        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(MainActivity.this, AddCategoryActivity.class);
                startActivity(i);
            }
        });

        btnViewCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(MainActivity.this, ListCategoriesActivity.class);
                startActivity(i);
            }
        });

        btnViewBeer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(MainActivity.this, ListReviewsActivity.class);
                startActivity(i);
            }
        });
    }
}
