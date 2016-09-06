package com.sam_chordas.android.stockhawk.rest;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.sam_chordas.android.stockhawk.data.StockQuoteContract;
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
                StockQuoteContract.StockQuoteEntry.CONTENT_URI);

        if (null != quote.getSymbol() &&
                !quote.getSymbol().isEmpty() &&
                null != quote.getPercentChange() &&
                !quote.getPercentChange().isEmpty() &&
                null != quote.getChange() &&
                !quote.getChange().isEmpty()) {


            builder.withValue(StockQuoteContract.StockQuoteEntry.COLUMN_ASK, quote.getAsk());

            builder.withValue(StockQuoteContract.StockQuoteEntry.COLUMN_AVERAGE_DAILY_VOLUME, quote.averageDailyVolume);

            builder.withValue(StockQuoteContract.StockQuoteEntry.COLUMN_BID,
                    truncateBidPrice(quote.getBid()));
            builder.withValue(StockQuoteContract.StockQuoteEntry.COLUMN_BOOK_VALUE, quote.bookValue);

            builder.withValue(StockQuoteContract.StockQuoteEntry.COLUMN_CHANGE,
                    truncateChange(quote.getChange(), false));

            builder.withValue(StockQuoteContract.StockQuoteEntry.COLUMN_CHANGE_PERCENT,
                    truncateChange(quote.getPercentChange(), true));

            builder.withValue(StockQuoteContract.StockQuoteEntry.COLUMN_CHANGE_PERCENTCHANGE,
                    quote.getChangePercentChange());

            builder.withValue(StockQuoteContract.StockQuoteEntry.COLUMN_CURRENCY, quote.getCurrency());

            builder.withValue(StockQuoteContract.StockQuoteEntry.COLUMN_DAYS_HIGH, quote.getDaysHigh());
            builder.withValue(StockQuoteContract.StockQuoteEntry.COLUMN_DAYS_LOW, quote.getDaysLow());
            builder.withValue(StockQuoteContract.StockQuoteEntry.COLUMN_DAYS_RANGE, quote.getDaysRange());

            builder.withValue(StockQuoteContract.StockQuoteEntry.COLUMN_ISCURRENT,
                    1);

            if (quote.getChange().charAt(0) == '-') {
                builder.withValue(StockQuoteContract.StockQuoteEntry.COLUMN_ISUP, 0);
            } else {
                builder.withValue(StockQuoteContract.StockQuoteEntry.COLUMN_ISUP, 1);
            }

            builder.withValue(StockQuoteContract.StockQuoteEntry.COLUMN_LAST_TRADE_PRICE, quote.getLastTradeDate());

            builder.withValue(StockQuoteContract.StockQuoteEntry.COLUMN_LAST_TRADE_TIME, quote.getLastTradeTime());

            builder.withValue(StockQuoteContract.StockQuoteEntry.COLUMN_LASTTRADE_DATE, quote.getLastTradeDate());

            builder.withValue(StockQuoteContract.StockQuoteEntry.COLUMN_MARKET_CAPITALIZATION, quote.getMarketCapitalization());
            builder.withValue(StockQuoteContract.StockQuoteEntry.COLUMN_NAME, quote.getName());

            builder.withValue(StockQuoteContract.StockQuoteEntry.COLUMN_OPEN, quote.getOpen());

            builder.withValue(StockQuoteContract.StockQuoteEntry.COLUMN_PERCENT_CHANGE, quote.getPercentChange());
            builder.withValue(StockQuoteContract.StockQuoteEntry.COLUMN_PREVIOUS_CLOSE, quote.getPreviousClose());
            builder.withValue(StockQuoteContract.StockQuoteEntry.COLUMN_PRICE_BOOK, quote.getPriceBook());
            builder.withValue(StockQuoteContract.StockQuoteEntry.COLUMN_PRICE_SALES, quote.getPriceSales());
            builder.withValue(StockQuoteContract.StockQuoteEntry.COLUMN_STOCK_EXCHANGE, quote.stockExchange);
            builder.withValue(StockQuoteContract.StockQuoteEntry.COLUMN_SYMBOL, quote.getSymbol());
            builder.withValue(StockQuoteContract.StockQuoteEntry.COLUMN_VOLUME, quote.getVolume());
            builder.withValue(StockQuoteContract.StockQuoteEntry.COLUMN_YEAR_HIGH, quote.getYearHigh());
            builder.withValue(StockQuoteContract.StockQuoteEntry.COLUMN_YEAR_LOW, quote.getYearLow());
            builder.withValue(StockQuoteContract.StockQuoteEntry.COLUMN_YEAR_RANGE, quote.getYearRange());

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
