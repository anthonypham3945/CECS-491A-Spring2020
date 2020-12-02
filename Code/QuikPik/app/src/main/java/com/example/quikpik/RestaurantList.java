package com.example.quikpik;

import java.util.ArrayList;

public class RestaurantList {
    private ArrayList<Restaurant> restaurants;

    public RestaurantList () {
        this.restaurants = new ArrayList<Restaurant>();
    }
    public ArrayList<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void addRestaurants(Restaurant restaurants) {
        this.restaurants.add(restaurants);
    }
}
