package com.example.quikpik;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

public class RestaurantRecyclerViewAdapter extends RecyclerView.Adapter<RestaurantViewHolder> {
    private List<YelpRestaurant> restaurants;

    public RestaurantRecyclerViewAdapter(Context context, List<YelpRestaurant> restaurants) {
        this.restaurants = restaurants;
    }
    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_restaurant, parent, false);

        return new RestaurantViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
        YelpRestaurant restaurant = restaurants.get(position);
        holder.bind(restaurant);
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }
    public void setGames(List<YelpRestaurant> restaurants) {
        this.restaurants = restaurants;
        //notifyDataSetChanged();
    }

}
