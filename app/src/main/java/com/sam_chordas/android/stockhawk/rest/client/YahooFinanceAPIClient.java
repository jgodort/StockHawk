package com.sam_chordas.android.stockhawk.rest.client;

import com.sam_chordas.android.stockhawk.rest.model.YahooModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Javier Godino on 24/08/2016.
 * Email: jgodort.software@gmail.com
 */

public interface YahooFinanceAPIClient {


    @GET("yql?format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys")
    Call<YahooModel> getStocks(@Query("q") String query);


}
