package com.example.quikpik;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface YelpServiceInterface {
    @GET("businesses/search")
    Call<YelpSearchResult> getTasks(
            @Header("Authorization") String authHeader,
            @Query("term") String searchTerm,
            @Query("latitude") Double latitude,
            @Query("longitude") Double longitude
    );

}
