package com.example.quikpik;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class SearchFragment extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);//hooks up this class with the corresponding xml file\
        EditText restaurantName = (EditText) findViewById(R.id.restaurantName);
        Button button = (Button) findViewById(R.id.button);
        String searchKeyword = (String) restaurantName.getText().toString();
    }

    public void onBackPressed() {
        Intent intent=new Intent(SearchFragment.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void searchByID(View view) {
        EditText restaurantName = (EditText) findViewById(R.id.restaurantName);
        Button button = (Button) findViewById(R.id.button);
        String searchKeyword = (String) restaurantName.getText().toString();
        TextView text = (TextView) findViewById(R.id.testingText);
        text.setText(searchKeyword);

        //TODO: HTTP API REQUESTS
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https:\\";

        //GeoPoint location = new GeoPoint(location.getLatitude(), location.getLongitude());
    }
}