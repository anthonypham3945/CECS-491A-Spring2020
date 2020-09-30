package com.example.quikpik;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import java.util.ArrayList;

public class RestaurantLookup extends AppCompatActivity {
    EditText inputRestaurant; //user input of the restaurant
    Button btnLookup; //button on the screen
    ProgressBar progressBar;
    Toolbar toolbar;
    ArrayList<String> storeInput = new ArrayList<String>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_lookup);//hooks up this class with the corresponding xml file

        btnLookup = (Button) findViewById(R.id.search_btn);
        inputRestaurant = (EditText) findViewById(R.id.restaurant_lookup);
        btnLookup.setOnClickListener(new View.OnClickListener() {//when search button is clicked
            @Override
            public void onClick(View v) {
                String search = inputRestaurant.getText().toString().trim();
                if (TextUtils.isEmpty(search)) {//check if the user entered a restaurant
                    inputRestaurant.setError("Search Bar is Empty!");
                    return;
                }
                if (search.length() > 0) {
                    storeInput.add(inputRestaurant.toString());
                    //store in database somewhere?
                    inputRestaurant.getText().clear();
                }
            }
        });
    }
}