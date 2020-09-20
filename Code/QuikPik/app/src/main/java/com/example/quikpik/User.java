package com.example.quikpik;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {
    public String email, firstName, lastName;
    public ArrayList<String> restaurantChoices, dressChoices, flavorChoices, allergyChoices;
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
        this.flavorChoices = new ArrayList<String>();
        this.allergyChoices = new ArrayList<String>();
    }
    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public String getFirstName() {
        return this.firstName;
    }
    public String getLastName() {
        return this.lastName;
    }
    /**
     *
     * @param choices is set for the restaurants choices
     */
    public void addRestaurantChoices(ArrayList<String> choices) { this.restaurantChoices = choices; }

    /**
     *
     * @param choices is set for the dress choices
     */
    public void addDressChoices(ArrayList<String> choices) {
        this.dressChoices = choices;
    }

    /**
     *
     * @param choices is set for the flavor choices
     */
    public void addFlavorChoices(ArrayList<String> choices) { this.flavorChoices = choices;}

    /**
     *
     * @param choices is set for the allergy choices
     */
    public void addAllergyChoices(ArrayList<String> choices) { this.allergyChoices = choices;}

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
     *
     * @return the array list of flavor choices
     */
    public ArrayList<String> getFlavorChoices() { return this.flavorChoices;}

    /**
     *
     * @return the array list of allergy choices
     */
    public ArrayList<String> getAllergyChoices() { return this.allergyChoices;}
    /**
     * @return a map object with all the user data
     */
    public Map<String, Object> getUser() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("First Name", this.firstName);
        result.put("Last Name", this.lastName);
        //result.put("email", this.email);
        //result.put("Restaurant Choices", this.restaurantChoices);
        //result.put("Dress Choices", this.dressChoices);
        //result.put("Flavor Choices", this.flavorChoices);
        //result.put("Allergy Choices", this.allergyChoices);
        return result;
    }
    /**
     * @return a map object with all the user data
     */
    public Map<String, Object> getPreferences() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("email", this.email);
        result.put("Restaurant Choices", this.restaurantChoices);
        result.put("Dress Choices", this.dressChoices);
        result.put("Flavor Choices", this.flavorChoices);
        result.put("Allergy Choices", this.allergyChoices);
        return result;
    }
}
