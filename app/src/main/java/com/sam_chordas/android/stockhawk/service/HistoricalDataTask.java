package com.sam_chordas.android.stockhawk.service;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.util.Log;
import android.util.Pair;

import com.sam_chordas.android.stockhawk.Utilities.Utils;
import com.sam_chordas.android.stockhawk.data.StockQuoteContract;
import com.sam_chordas.android.stockhawk.rest.ServiceGenerator;
import com.sam_chordas.android.stockhawk.rest.client.YahooFinanceAPIClient;
import com.sam_chordas.android.stockhawk.rest.model.HistoricalQuote;
import com.sam_chordas.android.stockhawk.rest.model.HistoricalStockQuoteModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.sam_chordas.android.stockhawk.Utilities.Constants.ONE_WEEK_CRITERIA;


/**
 * Created by Javier Godino on 27/09/2016.
 * Email: jgodort.software@gmail.com
 */

/**
 * AsyncTask to obtain the historical data of the Quotes  from the Yahoo Financial API.
 * <br/>
 * The task is in charge to obtain the historical data from the API and perform a bulk insert on the BBDD.
 */
public class HistoricalDataTask extends AsyncTask<List<Pair<String, Integer>>, Void, Void> {
    private static final String LOG_TAG = HistoricalDataTask.class.getName();
    public static final String ACTION_DATA_UPDATED =
            "com.sam_chordas.android.stockhawk.ACTION_DATA_UPDATED";

    private Context mContext;

    /**
     * Default Constructor
     *
     * @param context a context instance.
     */
    public HistoricalDataTask(Context context) {
        mContext = context;
    }

    /**
     * @param params
     * @return
     */
    @Override
    protected Void doInBackground(List<Pair<String, Integer>>... params) {

        List<Pair<String, Integer>> quotes = params[0];
        for (Pair<String, Integer> pair : quotes) {

            try {
                List<HistoricalQuote> historicalQuotes = obtainHistoricalDataFromAPI(
                        Utils.generateHistoricalYQLQuery(pair.first, ONE_WEEK_CRITERIA));
                if (historicalQuotes != null && !historicalQuotes.isEmpty()) {
                    storeOnDatabase(historicalQuotes, pair.second);
                    updateWidgets();
                }
            } catch (IOException | OperationApplicationException | RemoteException e) {
                Log.e(LOG_TAG, "Error on insert: Not possible to persist the HistoricalQuotes.", e);
            }
        }

        return null;
    }


    /**
     * Method that returns a list of HistoricalQuotes obtaineds by the Yahoo Financial API.
     *
     * @param yqlQuery The query(YQL) to obtain data from the API.
     * @return A list of HistoricalQuote
     * @throws IOException
     */
    private List<HistoricalQuote> obtainHistoricalDataFromAPI(String yqlQuery) throws IOException {
        Log.d(LOG_TAG, "Fetching historical data from the API.");
        Log.d(LOG_TAG, "QUERY: " + yqlQuery);

        List<HistoricalQuote> quoteList = null;

        Call<HistoricalStockQuoteModel> call = ServiceGenerator.createService(YahooFinanceAPIClient.class).getHistorialStockData(yqlQuery);
        Response<HistoricalStockQuoteModel> response = call.execute();
        if (response.isSuccessful()) {
            Log.d(LOG_TAG, "The request to the REST API retrieve " + response.body().query.getCount() + " HistoricalQuotes");
            quoteList = response.body().query.getResults().getQuote();
        } else {
            Log.e(LOG_TAG, "The request to the REST API return an error");
        }

        return quoteList;
    }

    /**
     * Method that generate a list of ContentProviderOperations by a list of Historical Quotes and persist it on the BBDD.
     *
     * @param historicalQuotes The list of Historical Quotes to persist.
     * @param quoteId          The ID of the quote related.
     * @throws RemoteException
     * @throws OperationApplicationException
     */
    private void storeOnDatabase(List<HistoricalQuote> historicalQuotes, int quoteId) throws RemoteException, OperationApplicationException {
        ArrayList<ContentProviderOperation> inserts = new ArrayList<>();

        for (HistoricalQuote iterator : historicalQuotes) {
            inserts.add(Utils.buildBatchOperation(iterator, quoteId, mContext));
        }

        if (!Collections.EMPTY_LIST.equals(inserts)) {
            //perform a masive bulk insert
            mContext.getContentResolver().applyBatch(StockQuoteContract.CONTENT_AUTHORITY, inserts);
        } else {
            Log.i(LOG_TAG, "storeOnDatabase: Nothing to insert");
        }
    }


    private void updateWidgets() {
        // Setting the package ensures that only components in our app will receive the broadcast
        Intent dataUpdatedIntent = new Intent(ACTION_DATA_UPDATED)
                .setPackage(mContext.getPackageName());
        mContext.sendBroadcast(dataUpdatedIntent);
    }
}
