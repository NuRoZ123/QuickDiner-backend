package com.example.quickdinner.API.adresseGouv;

import com.example.quickdinner.utils.PairLongLat;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class AdresseGouvApi {
    private static final String BASE_URL = "https://api-adresse.data.gouv.fr/";

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static final AdresseGouvApiInterface adresseGouvApiInterface = retrofit.create(AdresseGouvApiInterface.class);

    public static PairLongLat getAdresseGouvService(String adresse, String ville) throws IOException {

        String adresseComplete = adresse + " " + ville;
        adresseComplete = adresseComplete.replace(" ", "+");

        Call<AdresseGouvResponse> call = adresseGouvApiInterface.getAdresseInfo(adresseComplete);
        Response<AdresseGouvResponse> response = call.execute();
        if (response.isSuccessful()) {
            AdresseGouvResponse adresseInfo = response.body();

            if(adresseInfo == null) {
                return null;
            }

            if(adresseInfo.getFeatures().length <= 0){
                return null;
            }

            double longitude = adresseInfo.getFeatures()[0].getGeometry().getCoordinates()[0];
            double latitude =adresseInfo.getFeatures()[0].getGeometry().getCoordinates()[1];

            return new PairLongLat(longitude, latitude);
        } else {
            return null;
        }
    }
}
