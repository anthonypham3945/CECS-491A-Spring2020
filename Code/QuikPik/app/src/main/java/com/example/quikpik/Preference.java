package com.example.quikpik;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class Preference extends PreferenceActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    Toolbar toolbar; // toolbar of the navigation

    protected void onCreate(Bundle savedInstatanceState) {
        super.onCreate(savedInstatanceState);
        addPreferencesFromResource(R.xml.prefs);
        Load_setting();
        //drawerLayout = findViewById(R.id.drawer);//the actual navigation drawer
        //navigationView = findViewById(R.id.navigationView);//how it looks
        //toolbar = findViewById(R.id.toolbar);//assigns tool bar to the toolbar in the xml file


    }

    private void Load_setting(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean chk_night = sp.getBoolean("NIGHT",false);
        if(chk_night){
            getListView().setBackgroundColor(Color.parseColor("#222222"));
        }
        else{
            getListView().setBackgroundColor(Color.parseColor("#ffffff"));
        }

        CheckBoxPreference chk_night_instance = (CheckBoxPreference)findPreference("NIGHT");
        chk_night_instance.setOnPreferenceChangeListener(new android.preference.Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(android.preference.Preference prefs, Object obj) {
                boolean yes = (boolean) obj;
                if(yes){
                    getListView().setBackgroundColor(Color.parseColor("#222222"));
                }
                else{
                    getListView().setBackgroundColor(Color.parseColor("#ffffff"));
                }

                return true;
            }
        });

    }

    @Override
    protected void onResume() {
        Load_setting();
        super.onResume();
    }
    public void onBackPressed() {
        Intent intent=new Intent(Preference.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
