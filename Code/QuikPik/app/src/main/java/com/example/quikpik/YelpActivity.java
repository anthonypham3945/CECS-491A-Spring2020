package com.example.quikpik;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yelp);
        //initialize the recycler view
        restaurantRecyclerView = findViewById(R.id.restaurant_list);
        //ImageView imageView = (ImageView) findViewById(R.id.restaurantPic);
        //Glide.with(this).load("https://i.imgur.com/i4ZEaLM.gif").into(imageView);
        //restaurant = new Restaurant("Restaurant", "Placeholder alias", "https://upload.wikimedia.org/wikipedia/en/a/a3/Halo_TMCC_KeyArt_Vert_2019.png");
        List<YelpRestaurant> restaurants = new ArrayList<>();
        adapter = new RestaurantRecyclerViewAdapter(this, restaurants);
        restaurantRecyclerView.setAdapter(adapter);
        restaurantRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        restaurantRecyclerView.getLayoutManager().setMeasurementCacheEnabled(false);
        //RestaurantAdapter adapter = new RestaurantAdapter(this, restaurants);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        YelpServiceInterface service = retrofit.create(YelpServiceInterface.class);
        //initiates network request
        service.getTasks(API_KEY, "Tacos", "California").enqueue(new retrofit2.Callback<YelpSearchResult>() {
            @Override
            public void onResponse(Call<YelpSearchResult> call, Response<YelpSearchResult> response) {
                //Log.i(TAG, response.toString());
                if(response.body() == null) {
                    Log.i(TAG, "Did not receive valid response");
                }
                restaurants.addAll(response.body().restaurants);
                adapter.notifyDataSetChanged();
                /*if(response.isSuccessful()) {

                }*/
            }

            @Override
            public void onFailure(retrofit2.Call<YelpSearchResult> call, Throwable t) {
                Log.v(TAG, "Failed");
                //Log.v("onFailure", t.toString());
            }
        });

        //Glide.with(this).load(restaurants.get(1)).into(imageView);

        //getRestaurants("90745");
    }

    /*private void getRestaurants(String location) {
        final YelpService yelpService = new YelpService();
        yelpService.findRestaurants(location, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String jsonData = response.body().string();
                    Log.v(TAG, jsonData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }*/
}