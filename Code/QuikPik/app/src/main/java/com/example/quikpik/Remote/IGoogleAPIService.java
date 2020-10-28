package com.example.quikpik.Remote;

import com.example.quikpik.Model.MyPlaces;
import com.example.quikpik.Model.PlaceDetail;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface IGoogleAPIService {
    @GET
    Call<MyPlaces> getNearbyPlaces (@Url String url);

    @GET
    Call<PlaceDetail> getPlaceDetail (@Url String url);
}
