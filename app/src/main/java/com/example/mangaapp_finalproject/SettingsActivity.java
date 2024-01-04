package com.example.mangaapp_finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    androidx.appcompat.widget.Toolbar toolbarSettings;
    LinearLayout layoutDarkMode, layoutLanguage;
    TextView tvDarkModeHint, tvLanguage;
    ImageView ivDarkMode;
    SharedPreferences darkModeSharePref, dataSaverPref, languagePref;
    SharedPreferences.Editor editor;
    Switch switchDataSaver;
    int darkMode;
    int currentDarkMode;
    int[] selectedIndex = {0, 0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toolbarSettings = findViewById(R.id.toolbarSettings);
        setSupportActionBar(toolbarSettings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvDarkModeHint = findViewById(R.id.tvDarkModeHint);
        ivDarkMode = findViewById(R.id.ivDarkMode);
        switchDataSaver = findViewById(R.id.switchDataSaver);
        layoutLanguage = findViewById(R.id.layoutLanguage);
        tvLanguage = findViewById(R.id.tvLanguageHint);

        dataSaverPref = getSharedPreferences("DATA_SAVER", Context.MODE_PRIVATE);
        boolean dataSaver = dataSaverPref.getBoolean("dataSaver", false);
        switchDataSaver.setChecked(dataSaver);
        switchDataSaver.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                editor = dataSaverPref.edit().putBoolean("dataSaver", b);
                editor.commit();
            }
        });

        darkModeSharePref = getSharedPreferences("DARK_MODE", Context.MODE_PRIVATE);
        selectedIndex[0] = darkModeSharePref.getInt("darkMode", 2);

        if(selectedIndex[0] == 1){
            tvDarkModeHint.setHint(R.string.Dark_mode_ON);
            ivDarkMode.setImageResource(R.drawable.ic_mode_dark_sett);

        } else if (selectedIndex[0] == 2) {
            tvDarkModeHint.setHint(R.string.Dark_mode_OFF);
            ivDarkMode.setImageResource(R.drawable.ic_mode_light_sett);

        } else if (selectedIndex[0] == 0) {
            tvDarkModeHint.setHint(R.string.Dark_mode_Auto);

            currentDarkMode = getResources().getConfiguration().uiMode
                    & Configuration.UI_MODE_NIGHT_MASK;

            switch (currentDarkMode){
                case Configuration.UI_MODE_NIGHT_YES:
                    ivDarkMode.setImageResource(R.drawable.ic_mode_dark_sett);
                    break;

                case Configuration.UI_MODE_NIGHT_NO:
                    ivDarkMode.setImageResource(R.drawable.ic_mode_light_sett);
                    break;
//                case Configuration.UI_MODE_NIGHT_UNDEFINED:
//                    ivDarkMode.setImageResource(R.drawable.ic_mode_light_sett);
            }
        }

        layoutDarkMode = findViewById(R.id.layoutDarkMode);
        layoutDarkMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setChoiceDialog(selectedIndex);
            }
        });

        languagePref = getSharedPreferences("LANGUAGE", Context.MODE_PRIVATE);
        selectedIndex[1] = languagePref.getInt("language", 0);

        String[] lang = new String[]{"English", "Vietnamese", "Japanese"};
        tvLanguage.setHint(lang[selectedIndex[1]]);
        layoutLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLanguageDialog(selectedIndex);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.maintoolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();
        if (itemID == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setChoiceDialog(int[] selectedIndex){
        String[] optionList = {"System settings", "On", "Off"};


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Turn on dark mode");
        builder.setSingleChoiceItems(optionList, selectedIndex[0], new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selectedIndex[0] = i;

                switch(optionList[i]) {
                    case "On":
                        tvDarkModeHint.setHint(R.string.Dark_mode_ON);
                        ivDarkMode.setImageResource(R.drawable.ic_mode_dark_sett);
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        editor = darkModeSharePref
                                .edit()
                                .putInt("darkMode", 1);
                        darkMode = 1;
                        break;
                    case "Off":
                        tvDarkModeHint.setHint(R.string.Dark_mode_OFF);
                        ivDarkMode.setImageResource(R.drawable.ic_mode_light_sett);
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        editor = darkModeSharePref
                                .edit()
                                .putInt("darkMode", 2);
                        darkMode = 2;
                        break;
                    case "System settings":
                        tvDarkModeHint.setHint(R.string.Dark_mode_Auto);
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

                        currentDarkMode = getResources().getConfiguration().uiMode
                                & Configuration.UI_MODE_NIGHT_MASK;

                        switch (currentDarkMode){
                            case Configuration.UI_MODE_NIGHT_YES:
                                ivDarkMode.setImageResource(R.drawable.ic_mode_dark_sett);
                                break;
                            case Configuration.UI_MODE_NIGHT_NO:
                                ivDarkMode.setImageResource(R.drawable.ic_mode_light_sett);
                                break;
                }

                    editor = darkModeSharePref
                            .edit()
                            .putInt("darkMode", 0);
                    darkMode = 0;
                }
                editor.apply();
                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void setLanguageDialog(int[] selectedIndex){
        String[] optionList = {"English", "Vietnamese", "Japanese"};


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change Language");
        builder.setSingleChoiceItems(optionList, selectedIndex[1], new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selectedIndex[1] = i;

                switch(optionList[i]) {
                    case "English":
                        tvLanguage.setHint("English");
                        editor = languagePref
                                .edit()
                                .putInt("language", 0);
                        break;
                    case "Vietnamese":
                        tvLanguage.setHint("Vietnamese");
                        editor = languagePref
                                .edit()
                                .putInt("language", 1);
                        break;
                    case "Japanese":
                        tvLanguage.setHint("Japanese");
                        editor = languagePref
                                .edit()
                                .putInt("language", 2);
                        break;
                }
                editor.apply();
                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}