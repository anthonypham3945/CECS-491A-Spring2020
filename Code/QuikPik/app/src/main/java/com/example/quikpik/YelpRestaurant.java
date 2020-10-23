package com.example.quikpik;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class YelpRestaurant {
    String name;
    Double rating;
    String price;
    @SerializedName("review_count") int numReviews;
    @SerializedName("distance") double distanceInMeters;
    @SerializedName("image_url") String imageURL;
    List<YelpCategory> categories;

    public String displayDistance() {
        double distance = 0.0006721371 * distanceInMeters;
        return Double.toString(distance) + "mi";
    }
}
