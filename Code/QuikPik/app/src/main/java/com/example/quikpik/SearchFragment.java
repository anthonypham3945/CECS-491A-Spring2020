package com.example.quikpik;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class SearchFragment extends AppCompatActivity {

    private Object JsonObjectRequest;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);//hooks up this class with the corresponding xml file\
        EditText restaurantName = (EditText) findViewById(R.id.restaurantName);
        Button button = (Button) findViewById(R.id.button);
        String searchKeyword = (String) restaurantName.getText().toString();
        TextView restaurantAddress = (TextView) findViewById(R.id.restaurantAddress);
        TextView restaurantRating = (TextView) findViewById(R.id.restaurantRating);

    }

    public void onBackPressed() {
        Intent intent = new Intent(SearchFragment.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void searchByID(View View) {
        EditText restaurantName = (EditText) findViewById(R.id.restaurantName);
        Button button = (Button) findViewById(R.id.button);
        String searchKeyword = (String) restaurantName.getText().toString();
        TextView text = (TextView) findViewById(R.id.testingText);
        text.setText(searchKeyword);
        String requestString = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?key=AIzaSyC9MJnShWNpXjEHIutM9CI1lKhwUO7Rhc4&input=" + searchKeyword + "&inputtype=textquery";
        System.out.println("url: " + requestString);

        //TODO: HTTP API REQUESTS
        final TextView textView = (TextView) findViewById(R.id.restaurantAddress);
        // Instantiate the RequestQueue.
        String url = requestString;
        System.out.println("url: " + url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Response: " + response.toString());
                        try {
                            System.out.println("Response: " + response.getString("candidates"));
                            textView.setText("Response: " + response.getString("candidates"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });

        RequestQueue requestQueue;

        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());
        // Instantiate the RequestQueue with the cache and network.
        requestQueue = new RequestQueue(cache, network);
        // Start the queue
        requestQueue.start();
        System.out.println("before request: " + requestQueue);
        requestQueue.add(jsonObjectRequest);
        System.out.println("after request: " + requestQueue);
        System.out.println("request: " + jsonObjectRequest);
    }
}