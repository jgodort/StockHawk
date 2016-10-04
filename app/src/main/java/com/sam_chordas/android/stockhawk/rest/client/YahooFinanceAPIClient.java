package com.sam_chordas.android.stockhawk.rest.client;

import com.sam_chordas.android.stockhawk.rest.model.HistoricalStockQuoteModel;
import com.sam_chordas.android.stockhawk.rest.model.StockQuoteModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Javier Godino on 24/08/2016.
 * Email: jgodort.software@gmail.com
 */

/**
 * Interfaca to represent
 */
public interface YahooFinanceAPIClient {


    /**
     * API call to retrieve quotes.
     *
     * @param query YQL query to execute by the API.
     * @return Call of StockQuoteModel.
     */
    @GET("yql?format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys")
    Call<StockQuoteModel> getStocks(@Query("q") String query);

    /**
     * API call to retrieve historical information about a Quote.
     *
     * @param query YQL query to execute by the API.
     * @return Call of HistoricalStockQuoteModel.
     */
    @GET("yql?&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys")
    Call<HistoricalStockQuoteModel> getHistorialStockData(@Query("q") String query);


}
