package com.example.quikpik;

import com.google.android.gms.tasks.Task;
import com.google.protobuf.Any;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface YelpServiceInterface {
    @GET("businesses/search")
    Call<YelpSearchResult> getTasks(
            @Header("Authorization") String authHeader,
            @Query("term") String searchTerm,
            @Query("location") String location
    );

}
