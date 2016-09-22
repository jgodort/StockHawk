package com.sam_chordas.android.stockhawk.service;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.util.Log;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;
import com.sam_chordas.android.stockhawk.data.StockQuoteContract;
import com.sam_chordas.android.stockhawk.rest.ServiceGenerator;
import com.sam_chordas.android.stockhawk.rest.Utils;
import com.sam_chordas.android.stockhawk.rest.client.YahooFinanceAPIClient;
import com.sam_chordas.android.stockhawk.rest.model.HistoricalQuote;
import com.sam_chordas.android.stockhawk.rest.model.HistoricalStockQuoteModel;
import com.sam_chordas.android.stockhawk.rest.model.Quote;
import com.sam_chordas.android.stockhawk.rest.model.StockQuoteModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by sam_chordas on 9/30/15.
 * The GCMTask service is primarily for periodic tasks. However, OnRunTask can be called directly
 * and is used for the initialization and adding task as well.
 */
public class StockTaskService extends GcmTaskService {


    private String LOG_TAG = StockTaskService.class.getSimpleName();

    public static final String INIT_PARAM = "init";

    public static final String PERIODIC_PARAM = "periodic";

    public static final String ADD_PARAM = "add";


    /**
     * Client to retrive data from Yahoo Finance API.
     */
    YahooFinanceAPIClient yahooClient = ServiceGenerator.createService(YahooFinanceAPIClient.class);

    private Context mContext;

    /**
     * Flag to check if the data is update.
     */
    private boolean isUpdate;

    /**
     * Minimal Constructor.
     */
    public StockTaskService() {
    }


    /**
     * Default Constructor.
     *
     * @param context
     */
    public StockTaskService(Context context) {
        mContext = context;
    }


    /**
     * Method  that handle the retrofit response and  return the list of quotes.
     *
     * @param url String that represents the YQL query to retrive the data from the API.
     * @return returns a list of Quotes that match with the criteria.
     * @throws IOException
     */
    private List<Quote> fetchData(String url) throws IOException {
        Log.d(LOG_TAG, "fetching data from the API.");
        Log.d(LOG_TAG, "QUERY: " + url);

        List<Quote> quoteList = null;

        Call<StockQuoteModel> call = yahooClient.getStocks(url);
        Response<StockQuoteModel> response = call.execute();
        if (response.isSuccessful()) {
            Log.d(LOG_TAG, "The request to the API bring back " + response.body().query.getCount() + " Quotes");
            quoteList = response.body().query.getResults().getQuote();
        } else {
            Log.e(LOG_TAG, "The request to the API return an error");
        }

        return quoteList;
    }


    private List<HistoricalQuote> fetchHistoricalData(String url) throws IOException {
        Log.d(LOG_TAG, "Fetching historical data from the API.");
        Log.d(LOG_TAG, "QUERY: " + url);

        List<HistoricalQuote> quoteList = null;

        Call<HistoricalStockQuoteModel> call = yahooClient.getHistorialStockData(url);
        Response<HistoricalStockQuoteModel> response = call.execute();
        if (response.isSuccessful()) {
            Log.d(LOG_TAG, "The request to the REST API bring back " + response.body().query.getCount() + " HistoricalQuotes");
            quoteList = response.body().query.getResults().getQuote();
        } else {
            Log.e(LOG_TAG, "The request to the REST API return an error");
        }

        return quoteList;
    }


    @Override
    public int onRunTask(TaskParams params) {
        /**
         * Variable to manage the status of the Service.
         */

        if (mContext == null) {
            return GcmNetworkManager.RESULT_FAILURE;
        }

        //check if is update
        if (params.getTag().equals(INIT_PARAM) ||
                params.getTag().equals(PERIODIC_PARAM) ||
                params.getTag().equals(ADD_PARAM)) {
            isUpdate = true;
        }
        //Represents the query to fetch the data from the Yahoo API.
        String queryYQL = Utils.generateQuoteQuery(mContext, params);
        try {
            List<Quote> fetchedQuotes = fetchData(queryYQL);

            if (fetchedQuotes != null && !fetchedQuotes.isEmpty()) {
                storeOnDatabase(fetchedQuotes);

                for (Quote itQuote : fetchedQuotes) {
                    List<HistoricalQuote> historicalQuotes = fetchHistoricalData(Utils.generateHistoricalYQLQuery(itQuote.symbol));
                    storeOnDatabase(historicalQuotes, itQuote.id);
                }
            }
        } catch (IOException | RemoteException | OperationApplicationException e) {
            Log.e(LOG_TAG, e.getMessage());
            return GcmNetworkManager.RESULT_FAILURE;
        }
        return GcmNetworkManager.RESULT_SUCCESS;
    }

    private void storeOnDatabase(List<Quote> quotes) throws RemoteException, OperationApplicationException {

        ArrayList<ContentProviderOperation> inserts = new ArrayList<>();

        for (Quote iterator : quotes) {

            inserts.add(Utils.buildBatchOperation(iterator, mContext));
        }

        if (!Collections.EMPTY_LIST.equals(inserts)) {
            if (isUpdate) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(StockQuoteContract.StockQuoteEntry.COLUMN_ISCURRENT, 0);
                mContext.getContentResolver().update(StockQuoteContract.StockQuoteEntry.CONTENT_URI, contentValues,
                        null, null);
            }


            //perform a masive bulk insert
            ContentProviderResult[] persitedEntities = mContext.getContentResolver().applyBatch(StockQuoteContract.CONTENT_AUTHORITY, inserts);

            for (int i = 0; i < persitedEntities.length; i++) {
                quotes.get(i).id = Integer.valueOf(persitedEntities[i].uri.getLastPathSegment());
            }
        }

    }

    private void storeOnDatabase(List<HistoricalQuote> historicalQuotes, int quoteId) throws RemoteException, OperationApplicationException {
        ArrayList<ContentProviderOperation> inserts = new ArrayList<>();

        for (HistoricalQuote iterator : historicalQuotes) {

            inserts.add(Utils.buildBatchOperation(iterator, quoteId, mContext));
        }

        if (!Collections.EMPTY_LIST.equals(inserts)) {
            //perform a masive bulk insert
            mContext.getContentResolver().applyBatch(StockQuoteContract.CONTENT_AUTHORITY, inserts);
        }
    }

}
