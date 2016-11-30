package com.telstra.androidjsonparsing.webservice;

import com.telstra.androidjsonparsing.model.Country;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RequestInterface {

    @GET("u/746330/facts.json")
    Call<Country> getCountryDetails();
}
