package com.example.quikpik;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;
import java.util.List;

public class YelpRestaurant {
    String name;
    String price;
    @SerializedName("review_count") int numReviews;
    @SerializedName("url") String url;
    @SerializedName("distance") double distanceInMeters;
    @SerializedName("location") @JsonAdapter(LocationDeserializer.class) String address;
    @SerializedName("rating")  String rating;
    @SerializedName("image_url") String imageURL;
    List<YelpCategory> categories;

    public String displayDistance() {
        double distance = 0.0006721371 * distanceInMeters;
        return Double.toString(distance) + "mi";
    }

    public static class LocationDeserializer implements JsonDeserializer<String> {
        @Override
        public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
            return json.getAsJsonObject().get("address1").getAsString();
        }
    }
}
