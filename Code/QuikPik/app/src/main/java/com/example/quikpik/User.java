package com.example.quikpik;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {
    public String email;
    public ArrayList<String> restaurantChoices;
    public ArrayList<String> dressChoices;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    /**
     * Constructor for the current user
     * @param email is used as an id to identify the user
     * initializes the email and the array lists of choices
     */
    public User(String email) {
        this.email = email;
        this.restaurantChoices = new ArrayList<String>();
        this.dressChoices = new ArrayList<String>();
    }

    /**
     *
     * @param choices is set for the restaurants choices
     */
    public void addRestaurantChoices(ArrayList<String> choices) {
        this.restaurantChoices = choices;
    }

    /**
     *
     * @param choices is set for the dress choices
     */
    public void addDressChoices(ArrayList<String> choices) {
        this.dressChoices = choices;
    }

    /**
     *
     * @return email of the user
     */
    public String getEmail() {
        return this.email;
    }

    /**
     *
     * @return the array list of restaurant choices
     */
    public ArrayList<String> getRestaurantChoices() {
        return this.restaurantChoices;
    }

    /**
     *
     * @return the array list of dress choices
     */
    public ArrayList<String> getDressChoices() {
        return this.dressChoices;
    }

    /**
     * @return a map object with all the user data
     */
    public Map<String, Object> getUser() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("email", this.email);
        result.put("Restaurant Choices", this.restaurantChoices);
        result.put("Dress Choices", this.dressChoices);

        return result;
    }
}
