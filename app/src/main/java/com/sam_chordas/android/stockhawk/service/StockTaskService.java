package com.sam_chordas.android.stockhawk.service;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.RemoteException;
import android.util.Log;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;
import com.sam_chordas.android.stockhawk.rest.ServiceGenerator;
import com.sam_chordas.android.stockhawk.rest.Utils;
import com.sam_chordas.android.stockhawk.rest.client.YahooFinanceAPIClient;
import com.sam_chordas.android.stockhawk.rest.model.Quote;
import com.sam_chordas.android.stockhawk.rest.model.YahooModel;

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

    private static final String BASE_QUERY = "select * from yahoo.finance.quotes" +
            " where symbol in (";
    private static final String BASE_QUERY_ADD = "select * from yahoo.finance.quote" +
            " where symbol in (";
    private static final String SAMPLE_STOCK_QUOTES = "\"YHOO\",\"AAPL\",\"GOOG\",\"MSFT\"";

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
    private List fetchData(String url) throws IOException {
        Log.d(LOG_TAG, "fetching data from the API.");
        Log.d(LOG_TAG, "QUERY: " + url);
        List<Quote> quoteList = null;

        Call<YahooModel> call = yahooClient.getStocks(url);
        Response<YahooModel> response = call.execute();
        if (response.isSuccessful()) {
            Log.d(LOG_TAG, "The request to the API bring back " + response.body().query.getCount() + " Quotes");
            quoteList = response.body().query.getResults().getQuote();
        } else {
            Log.e(LOG_TAG, "The request to the API return an error");
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
        //Represents the query to fetch the data from the Yahoo API.
        String queryYQL = BASE_QUERY + generateQuery(params);


        try {
            List<Quote> fetchedQuotes = fetchData(queryYQL);

            if (fetchedQuotes != null && !fetchedQuotes.isEmpty()) {
                storeOnDatabase(fetchedQuotes);
                ContentValues contentValues = new ContentValues();
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
                contentValues.put(QuoteColumns.ISCURRENT, 0);
                mContext.getContentResolver().update(QuoteProvider.Quotes.CONTENT_URI, contentValues,
                        null, null);
            }


            //perform a masive bulk insert
            mContext.getContentResolver().applyBatch(QuoteProvider.AUTHORITY, inserts);
        }

    }

    /**
     * [Refactorization of the Original Code]
     * <p>
     * Method to generate the query with the correct parameters depending on base
     * to the input Task-param value.
     *
     * @param params
     * @return
     */
    private String generateQuery(TaskParams params) {
        ContentResolver contentResolver = mContext.getContentResolver();

        Cursor dataBaseCursor;
        if (params.getTag().equals("init") || params.getTag().equals("periodic") || params.getTag().equals("add")) {
            isUpdate = true;
            dataBaseCursor = contentResolver.query(
                    QuoteProvider.Quotes.CONTENT_URI,
                    new String[]{"Distinct " + QuoteColumns.SYMBOL},
                    null,
                    null,
                    null);


            if (dataBaseCursor != null && dataBaseCursor.getCount() > 0) {
                DatabaseUtils.dumpCursor(dataBaseCursor);
                dataBaseCursor.moveToFirst();
                StringBuilder mStoredSymbols = new StringBuilder();
                for (int i = 0; i < dataBaseCursor.getCount(); i++) {

                    mStoredSymbols.append("\"" +
                            dataBaseCursor.getString(dataBaseCursor.getColumnIndex("symbol")) + "\",");
                    dataBaseCursor.moveToNext();
                }
                dataBaseCursor.close();
                if (params.getTag().equals("add")) {
                    // get symbol from params.getExtra and build query
                    mStoredSymbols.append("\"" +
                            params.getExtras().getString("symbol") + "\",");
                }
                mStoredSymbols.replace(mStoredSymbols.length() - 1, mStoredSymbols.length(), ")");
                return mStoredSymbols.toString();
            } else {
                return SAMPLE_STOCK_QUOTES + ")";
            }
        }
        return null;
    }

}
