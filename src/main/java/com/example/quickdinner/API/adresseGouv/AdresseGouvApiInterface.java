package com.example.quickdinner.API.adresseGouv;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AdresseGouvApiInterface {

    @GET("/search")
    Call<AdresseGouvResponse> getAdresseInfo(@Query("q") String adresse);
}
