package com.example.mangaapp_finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

public class SettingsActivity extends AppCompatActivity {

    androidx.appcompat.widget.Toolbar toolbarSettings;
    LinearLayout layoutDarkMode;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    boolean darkMode;
    int[] selectedIndex = {0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toolbarSettings = findViewById(R.id.toolbarSettings);
        setSupportActionBar(toolbarSettings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferences = getSharedPreferences("DARK_MODE", Context.MODE_PRIVATE);
        darkMode = sharedPreferences.getBoolean("darkMode", true);

        layoutDarkMode = findViewById(R.id.layoutDarkMode);

        if(darkMode){
            selectedIndex[0] = 0;
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            selectedIndex[0] = 1;
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        layoutDarkMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setChoiceDialog(selectedIndex);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemID =item.getItemId();
        if (itemID == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setChoiceDialog(int[] selectedIndex){
        String[] optionList = {"On", "Off"};


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Turn on dark mode");
        builder.setSingleChoiceItems(optionList, selectedIndex[0], new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selectedIndex[0] = i;
                if(optionList[i].contentEquals("On")){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor = sharedPreferences
                            .edit()
                            .putBoolean("darkMode", true);
                } else if (optionList[i].contentEquals("Off")) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor = sharedPreferences
                            .edit()
                            .putBoolean("darkMode", false);
                }
                editor.apply();
                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


}