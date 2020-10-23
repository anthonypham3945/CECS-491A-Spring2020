package com.example.quikpik;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RestaurantViewHolder extends RecyclerView.ViewHolder {
    private TextView title, reviewCount;
    private ImageView restaurantImage;

    public RestaurantViewHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.restaurant_name);
        reviewCount = itemView.findViewById(R.id.textReview);
        restaurantImage = itemView.findViewById(R.id.imageView);
        //alias = itemView.findViewById(R.id.fragment_alias);
        //boxArt = itemView.findViewById(R.id.fragment_restaurant_image);
    }

    public void bind(YelpRestaurant restaurant) {
        //title.setText(restaurant.getTitle());
        //alias.setText(restaurant.getAlias());
        //Glide.with(itemView).load(restaurant.getRestaurantUri()).into(boxArt);
        title.setText(restaurant.name);
        reviewCount.setText(restaurant.numReviews + " Reviews");
        Glide.with(itemView).load(restaurant.imageURL).into(restaurantImage);
        //alias.setText(restaurant.price);
    }
}
