package com.kellydwalters.thebeerjournal;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static com.kellydwalters.thebeerjournal.R.menu.main_menu;

public class MainActivity extends AppCompatActivity {

    Button btnAddBeer, btnAddCategory, btnViewBeer, btnViewCategory;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("settings",0);
        switchTheme();
        setContentView(R.layout.activity_main);

        // Get the views assigned
        initViews();

        //create a new event handler
        EventHandler eventHandler = new EventHandler();

        btnAddBeer.setOnClickListener(eventHandler);
        btnAddCategory.setOnClickListener(eventHandler);
        btnViewBeer.setOnClickListener(eventHandler);
        btnViewCategory.setOnClickListener(eventHandler);

    } // onCreate

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
    protected void onStart() {
        super.onStart();

    }

    private void initViews() {
        btnAddBeer = findViewById(R.id.btnAdd);
        btnAddCategory = findViewById(R.id.btnAddCat);
        btnViewCategory =findViewById(R.id.btnViewCat);
        btnViewBeer = findViewById(R.id.btnViewBeer);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // create / inflate the menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(main_menu, menu);
        return true;
    } // create menu

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent settingsIntent = new Intent(MainActivity.this, Settings.class);
                startActivityForResult(settingsIntent, 0);
                break;
            default:
                break;
        } //end switch

        return super.onOptionsItemSelected(item);
    } // menuSelection

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            // reload the app to set the new theme
            this.recreate();

        } else {
            //cancelled
            Toast.makeText(this, "It broke", Toast.LENGTH_SHORT).show();
            Log.d("KELLY", "it broke " );
        }
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
                    // add category screen
                    intent = new Intent(MainActivity.this, AddCategoryActivity.class);
                    break;
                case R.id.btnViewCat:
                    //view category
                    intent = new Intent(MainActivity.this, ListCategoriesActivity.class);
                    break;
                case R.id.btnViewBeer:
                    // view beer review
                    intent = new Intent(MainActivity.this, ListReviewsActivity.class);
                    break;
                default:
                    break;
            }

            startActivity(intent);
        }
    }
}
