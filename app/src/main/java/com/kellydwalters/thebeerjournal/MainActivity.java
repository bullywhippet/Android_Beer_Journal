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


        //create a new event handler
        EventHandler eventHandler = new EventHandler();

        btnAddBeer.setOnClickListener(eventHandler);
        btnAddCategory.setOnClickListener(eventHandler);
        btnViewBeer.setOnClickListener(eventHandler);
        btnViewCategory.setOnClickListener(eventHandler);

    }

    class EventHandler implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = null;

            switch (v.getId()) {
                case R.id.btnAdd:
                    // add beer screen
                   intent = new Intent(MainActivity.this, AddReviewActivity.class);
                    break;
                case R.id.btnAddCat:
                    intent = new Intent(MainActivity.this, AddCategoryActivity.class);
                    break;
                case R.id.btnViewCat:
                    intent = new Intent(MainActivity.this, ListCategoriesActivity.class);
                    break;
                case R.id.btnViewBeer:
                    intent = new Intent(MainActivity.this, ListReviewsActivity.class);
                    break;
                default:
                    break;
            }

            startActivity(intent);
        }
    }
}
