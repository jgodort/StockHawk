package com.sam_chordas.android.stockhawk.rest;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.TaskParams;
import com.sam_chordas.android.stockhawk.data.StockQuoteContract;
import com.sam_chordas.android.stockhawk.rest.model.HistoricalQuote;
import com.sam_chordas.android.stockhawk.rest.model.Quote;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by sam_chordas on 10/8/15.
 */
public class Utils {

    private static String LOG_TAG = Utils.class.getSimpleName();

    private static final SimpleDateFormat FORMAT_DATE_YAHOO_SDF = new SimpleDateFormat("yyyy-MM-dd");
    public static final String FORMAT_DATE_YAHOO = "yyyy-MM-dd";

    private static final String HISTORICAL_BASE_QUERY = "select * from yahoo.finance.historicaldata";
    private static final String HISTORICAL_QUERY_CONDITION = " where symbol = ";
    private static final String HISTORICAL_QUERY_START_DATE = " and startDate = ";
    private static final String HISTORICAL_QUERY_END_DATE = " and endDate = ";

    public static final String BASE_QUERY = "select * from yahoo.finance.quotes" +
            " where symbol in (";

    private static final String SAMPLE_STOCK_QUOTES = "\"YHOO\",\"AAPL\",\"GOOG\",\"MSFT\"";

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

    public static ContentProviderOperation buildBatchOperation(HistoricalQuote historicalQuote, int quoteId, Context context) {

        ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(
                StockQuoteContract.HistoricalQuoteEntry.CONTENT_URI);

        if (null != historicalQuote.getSymbol() &&
                !historicalQuote.getSymbol().isEmpty()) {

            builder.withValue(StockQuoteContract.HistoricalQuoteEntry.COLUMN_SYMBOL, historicalQuote.getSymbol());
            builder.withValue(StockQuoteContract.HistoricalQuoteEntry.COLUMN_QUOTE_ID, quoteId);
            builder.withValue(StockQuoteContract.HistoricalQuoteEntry.COLUMN_DATE, normalizeDateToPersist(historicalQuote.getDate()));
            builder.withValue(StockQuoteContract.HistoricalQuoteEntry.COLUMN_OPEN, historicalQuote.getOpen());
            builder.withValue(StockQuoteContract.HistoricalQuoteEntry.COLUMN_HIGH, historicalQuote.getHigh());
            builder.withValue(StockQuoteContract.HistoricalQuoteEntry.COLUMN_LOW, historicalQuote.getLow());
            builder.withValue(StockQuoteContract.HistoricalQuoteEntry.COLUMN_CLOSE, historicalQuote.getClose());
            builder.withValue(StockQuoteContract.HistoricalQuoteEntry.COLUMN_VOLUME, historicalQuote.getVolume());


        } else {
            launchToastMessageOnMainThread(context);
        }


        return builder.build();
    }

    public static final int normalizeDateToPersist(String date) {
        int normalizedDate = 0;
        try {
            normalizedDate = (int) new SimpleDateFormat("yyyy-MM-dd").parse(date).getTime();
        } catch (ParseException e) {
            Log.e(LOG_TAG, "Error while normalize date");
        }

        return normalizedDate;
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


    /**
     * Method that generate a YQL query to fetch the historical information about the Stock Quote.
     *
     * @param quoteSymbol Quote symbol who represent the acronim of the company.
     * @return YQL Query to make a call to the Financial API.
     */
    public static String generateHistoricalYQLQuery(String quoteSymbol) {

        GregorianCalendar calendar = new GregorianCalendar();

        calendar.set(Calendar.DATE, 1);
        calendar.set(Calendar.MONTH, 1);

        new SimpleDateFormat("yyyy-MM-dd");

        StringBuffer buffer = new StringBuffer(HISTORICAL_BASE_QUERY).
                append(HISTORICAL_QUERY_CONDITION).
                append("\"").
                append(quoteSymbol).
                append("\"").
                append(HISTORICAL_QUERY_START_DATE).
                append("\"").
                append(convertDateToString(calendar.getTime(), Utils.FORMAT_DATE_YAHOO)).//Start Date Value
                append("\"").
                append(HISTORICAL_QUERY_END_DATE).
                append("\"").
                append(convertDateToString(new Date(), Utils.FORMAT_DATE_YAHOO)).//End Date Value.
                append("\"");


        return buffer.toString();
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
    public static String generateQuoteQuery(Context context, TaskParams params) {
        ContentResolver contentResolver = context.getContentResolver();
        StringBuffer query = new StringBuffer(BASE_QUERY);

        Cursor dataBaseCursor;
        if (params.getTag().equals("init") || params.getTag().equals("periodic") || params.getTag().equals("add")) {
            dataBaseCursor = contentResolver.query(
                    StockQuoteContract.StockQuoteEntry.CONTENT_URI,
                    new String[]{"Distinct " + StockQuoteContract.StockQuoteEntry.TABLE_NAME + "." + StockQuoteContract.StockQuoteEntry.COLUMN_SYMBOL},
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
                query.append(mStoredSymbols.toString());
            } else {
                query.append(SAMPLE_STOCK_QUOTES + ")");
            }
        }
        return query.toString();
    }


    /**
     * Method that convert a Date into a String in the format given by parameters.
     *
     * @param date   A date to convert
     * @param format Formato de fecha "MMM" para Mes Cadena, "MM" para Mes Numerico "dd" para Dia "yyyy" para Aï¿½o
     * @return Un String con la fecha en el formato indicado
     */
    public static String convertDateToString(Date date, String format) {
        if (date == null) {
            return null;
        }

        if (format.equals(FORMAT_DATE_YAHOO)) {
            return FORMAT_DATE_YAHOO_SDF.format(date);
        }
        Format formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }
}
