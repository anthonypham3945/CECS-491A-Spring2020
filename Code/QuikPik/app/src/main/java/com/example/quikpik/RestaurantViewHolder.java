package com.example.quikpik;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class RestaurantViewHolder extends RecyclerView.ViewHolder implements OnClickListener{
    private TextView title, reviewCount, addressText;
    private ImageView restaurantImage;
    private String restaurantUrl;
    public RestaurantViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        title = itemView.findViewById(R.id.restaurant_name);
        reviewCount = itemView.findViewById(R.id.textReview);
        restaurantImage = itemView.findViewById(R.id.imageView);
        addressText = itemView.findViewById(R.id.textAddress);
        //alias = itemView.findViewById(R.id.fragment_alias);
        //boxArt = itemView.findViewById(R.id.fragment_restaurant_image);
    }

    public void bind(YelpRestaurant restaurant) {
        //title.setText(restaurant.getTitle());
        //alias.setText(restaurant.getAlias());
        //Glide.with(itemView).load(restaurant.getRestaurantUri()).into(boxArt);
        title.setText(restaurant.name);
        reviewCount.setText(restaurant.rating + "/5 ★ (" + restaurant.numReviews + " Reviews)");
        addressText.setText(restaurant.address);
        Glide.with(itemView).load(restaurant.imageURL).into(restaurantImage);
        restaurantUrl = restaurant.url;
        //alias.setText(restaurant.price);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(restaurantUrl));
        view.getContext().startActivity(intent);
    }
}
