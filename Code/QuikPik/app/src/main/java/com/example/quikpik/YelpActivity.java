package com.example.quikpik;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class YelpActivity extends AppCompatActivity {
    private static final String BASE_URL = "https://api.yelp.com/v3/";
    private static final String API_KEY = "Bearer FzyeiETw6JAgMPWfM62hPYyBlK0OYtNDHwPZ0XK9rhLz1zlFOb_qlL09OaQqJBnCuQCYciWuaBjvhW6pXC5tNvuh63yvaolx_6GCKeLu-PJ7C2e5fFBdBZJpDzF2X3Yx";
    //public static final String TAG = YelpActivity.class.getSimpleName();
    public static final String TAG = "YelpActivity";
    TextView mLocationTextView;
    private TextView title, alias;
    private ImageView boxArt;
    private RestaurantRecyclerViewAdapter adapter;
    private RecyclerView restaurantRecyclerView;
    Button refresh_button;
    TextView information_text;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //links respective xml file activity_yelp.xml
        setContentView(R.layout.activity_yelp);
        //sets up button refresh_button
        refresh_button = (Button) findViewById(R.id.refresh_button);
        information_text = (TextView) findViewById(R.id.information_text);
        //Makes initial API call for restaurants.
        makeAPICall();
        //Handles Button Interactions
        refresh_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //simply makes another API call which will repopulate recycler list view on page.
                makeAPICall();
            }
        });

    }
    //when back pressed, returns to main activity.
    public void onBackPressed() {
        Intent intent=new Intent(YelpActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }


    //Code for making main API call
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void makeAPICall()
    {
        //list of restaurants to display
        List<YelpRestaurant> restaurants = new ArrayList<>();
        //retrofit initialization
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        YelpServiceInterface service = retrofit.create(YelpServiceInterface.class);
        //used for getting location
        Geocoder gcd = new Geocoder(this, Locale.getDefault());
        //list of addresses (used in getting city/state name based on location.)


        //sets up recyclerview
        adapter = new RestaurantRecyclerViewAdapter(this, restaurants);
        restaurantRecyclerView = findViewById(R.id.restaurant_list);
        restaurantRecyclerView.setAdapter(adapter);
        restaurantRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        restaurantRecyclerView.getLayoutManager().setMeasurementCacheEnabled(false);

        //location initialized to Mountain View, California
        //latitude and longitude included for Mountain View.
        Double lat=37.3861;
        Double lng=122.0839;
        String city = "Mountain View";
        String state = "CA";
        Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geoCoder.getFromLocation(lat, lng, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //starts location manager
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //Permissions check
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //gets location
            Location location = lm.getLastKnownLocation(lm.GPS_PROVIDER);
            int radius = 1000;
            //latitude and longitude coordinates
            lng = location.getLongitude();
            lat = location.getLatitude();
        }
        try {
            addresses = gcd.getFromLocation(lat, lng, 1);

        } catch (IOException e) {
            e.printStackTrace();
        }
        //grab city and state name if available, otherwise defaults to "Mountain View CA"
        if (addresses.size() > 0) {
            city = (addresses.get(0).getLocality());
            state = (addresses.get(0).getAdminArea());
        }
        else {

        }

        //Combines city/state to be formatted for API string
        String searchLocation = city + ", " + state;

        //TODO: Use Firebase Preferences instead of preset preferences!!!
        String[] foods = {"Tacos",  "Pizza", "Chicken", "Ramen", "Juice", "Ice Cream", "Chinese", "Mexican", "Burgers"};

        //random number to select from list of food preferences.
        //TODO: instead, make a shuffled list based on ALL user preferences.
        int randomNum = ThreadLocalRandom.current().nextInt(0, foods.length-1 + 1);

        //sets up refresh button to display text
        Button refreshButton;
        refreshButton = (Button) findViewById(R.id.refresh_button);
        refreshButton.setText("Refresh Listings");
        //sets up a string in HTML (the <b></b> allows for bold font)
        String informationText = String.format("Showing listings for <b>%s</b> <br> in <b>%s</b> <br> @ <b>(%.2f lat, %.2f long)</b>", foods[randomNum], searchLocation, lat, lng);
        //sets the TextView to the string created, displaying what they're searching for and the location.
        information_text.setText(Html.fromHtml(informationText));

        //The actual API call code.
        //consists of [KEY, TERM, LATITUDE, LONGITUDE]
        service.getTasks(API_KEY, foods[randomNum], lat, lng).enqueue(new retrofit2.Callback<YelpSearchResult>() {

            @Override
            public void onResponse(Call<YelpSearchResult> call, Response<YelpSearchResult> response) {
                //Log.i(TAG, response.toString());
                if(response.body() == null) {
                    Log.i(TAG, "Did not receive valid response");
                }
                restaurants.addAll(response.body().restaurants);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(retrofit2.Call<YelpSearchResult> call, Throwable t) {
                Log.v(TAG, "Failed");
            }
        });
    }
}