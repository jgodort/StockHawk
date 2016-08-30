package com.sam_chordas.android.stockhawk.rest;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Javier Godino on 24/08/2016.
 * Email: jgodort.software@gmail.com
 */

public class ServiceGenerator {

    public static final String API_BASE_URL = "https://query.yahooapis.com/v1/public/";

    private static OkHttpClient.Builder httpClientBuider = new OkHttpClient.Builder();

    private static Retrofit.Builder retrofitBuilder =
            new Retrofit.Builder().
                    baseUrl(API_BASE_URL).
                    addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = retrofitBuilder.client(httpClientBuider.build()).build();
        return retrofit.create(serviceClass);
    }
}
