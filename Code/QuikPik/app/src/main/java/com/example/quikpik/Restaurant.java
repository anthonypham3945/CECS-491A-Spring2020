package com.example.quikpik;

import java.util.HashMap;
import java.util.Map;

public class Restaurant {
    private String name, rating, imageURL, location;
    private int reviewCount;

    public Restaurant(String name, String rating, String imageURL, String location, int reviewCount) {
        this.name = name;
        this.rating = rating;
        this.imageURL = imageURL;
        this.location = location;
        this.reviewCount = reviewCount;
    }
    public Restaurant() {
        this.name = "";
        this.rating = "";
        this.imageURL = "";
        this.location = "";
        this.reviewCount = 0;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }
}
