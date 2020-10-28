package com.example.quikpik;

import com.example.quikpik.Model.MyPlaces;
import com.example.quikpik.Model.Results;
import com.example.quikpik.Remote.IGoogleAPIService;
import com.example.quikpik.Remote.RetrofitClient;

public class Common {

    public static Results currentResult;

    private static final String GOOGLE_API_URL = "https://maps.googleapis.com/";
    public static IGoogleAPIService getGoogleAPIService(){
        return RetrofitClient.getClient(GOOGLE_API_URL).create(IGoogleAPIService.class);
    }
}
