package com.kellydwalters.thebeerjournal;

import android.content.SharedPreferences;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

public class Settings extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    CheckBox cbTheme;
    private boolean themeCheck;
    private String themeSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("settings",0);
        switchTheme();
        setContentView(R.layout.activity_settings);

        initViews();

        // change theme to dark if box is checked
        cbTheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    themeSetting = "dark";
                    themeCheck = true;
                }
                else {
                    themeSetting = "regular";
                    themeCheck = false;
                }
            }
        });
    } // OnCreate

    private void initViews() {
        cbTheme = findViewById(R.id.cbTheme);
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
    protected void onResume() {
        super.onResume();
        // set state of checkbox
        cbTheme.setChecked(sharedPreferences.getBoolean("themeCheck", false));
    }

    @Override
    public void onBackPressed() {
        // save values to shared prefs when back is pressed
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("themeCheck", themeCheck);
        editor.putString("theme", themeSetting);
        boolean success =  editor.commit();

        if (success) {
            setResult(this.RESULT_OK);
            Toast.makeText(this, "Settings were saved", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(Settings.this, "Data was NOT saved successfully", Toast.LENGTH_SHORT).show();
        }

        super.onBackPressed(); //calls this.finish
    }
}
