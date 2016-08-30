package com.sam_chordas.android.stockhawk.rest;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;
import com.sam_chordas.android.stockhawk.rest.model.Quote;

/**
 * Created by sam_chordas on 10/8/15.
 */
public class Utils {

    private static String LOG_TAG = Utils.class.getSimpleName();

    public static boolean showPercent = true;


    public static String truncateBidPrice(String bidPrice) {
        bidPrice = String.format("%.2f", Float.parseFloat(bidPrice));
        return bidPrice;
    }

    public static String truncateChange(String change, boolean isPercentChange) {
        String weight = change.substring(0, 1);
        String ampersand = "";
        if (isPercentChange) {
            ampersand = change.substring(change.length() - 1, change.length());
            change = change.substring(0, change.length() - 1);
        }
        change = change.substring(1, change.length());
        double round = (double) Math.round(Double.parseDouble(change) * 100) / 100;
        change = String.format("%.2f", round);
        StringBuffer changeBuffer = new StringBuffer(change);
        changeBuffer.insert(0, weight);
        changeBuffer.append(ampersand);
        change = changeBuffer.toString();
        return change;
    }

    public static ContentProviderOperation buildBatchOperation(Quote quote, Context context) {

        ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(
                QuoteProvider.Quotes.CONTENT_URI);

        String symbol = quote.getSymbol();
        String bid = quote.getBid();
        String percentChange = quote.getPercentChange();
        String change = quote.getChange();

        if (null != symbol &&
                !symbol.isEmpty() &&
                null != percentChange &&
                !percentChange.isEmpty() &&
                null != change &&
                !change.isEmpty()) {

            builder.withValue(QuoteColumns.SYMBOL, symbol);
            builder.withValue(QuoteColumns.BIDPRICE, truncateBidPrice(bid));
            builder.withValue(QuoteColumns.PERCENT_CHANGE,
                    truncateChange(percentChange, true));
            builder.withValue(QuoteColumns.CHANGE, truncateChange(change, false));
            builder.withValue(QuoteColumns.ISCURRENT, 1);

            if (change.charAt(0) == '-') {
                builder.withValue(QuoteColumns.ISUP, 0);
            } else {
                builder.withValue(QuoteColumns.ISUP, 1);
            }
        } else {
            launchToastMessageOnMainThread(context);
        }


        return builder.build();
    }

    /**
     * Method that allow to show a Toast Message on the Main thread.
     * <p>
     * Source: https://discussions.udacity.com/t/stock-not-existing-toast-doesnt-show/180386
     *
     * @param context
     */
    public static void launchToastMessageOnMainThread(final Context context) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            public void run() {
                Toast.makeText(context, "The Stock quote does not exist.",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
