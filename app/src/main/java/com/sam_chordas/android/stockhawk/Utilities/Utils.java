package com.sam_chordas.android.stockhawk.Utilities;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.gcm.TaskParams;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.StockQuoteContract;
import com.sam_chordas.android.stockhawk.rest.model.HistoricalQuote;
import com.sam_chordas.android.stockhawk.rest.model.Quote;
import com.sam_chordas.android.stockhawk.service.StockTaskService;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.sam_chordas.android.stockhawk.Utilities.Constants.FIX_ADD_CALL_STOCK_QUOTE;
import static com.sam_chordas.android.stockhawk.Utilities.Constants.ONE_MONTH_CRITERIA;
import static com.sam_chordas.android.stockhawk.Utilities.Constants.ONE_WEEK_CRITERIA;
import static com.sam_chordas.android.stockhawk.Utilities.Constants.SIX_MONTH_CRITERIA;
import static com.sam_chordas.android.stockhawk.Utilities.Constants.THREE_MONTH_CRITERIA;

/**
 * Created by sam_chordas on 10/8/15.
 */
public class Utils {

    private static String LOG_TAG = Utils.class.getSimpleName();

    private static final SimpleDateFormat FORMAT_DATE_YAHOO_SDF = new SimpleDateFormat("yyyy-MM-dd");
    public static final String FORMAT_DATE_YAHOO = "yyyy-MM-dd";

    private static final int LAST_WEEK_VALUE = 7;
    private static final int LAST_MONTH_VALUE = 30;
    private static final int LAST_THREE_MONTH_VALUE = 90;
    private static final int LAST_SIX_MONTH_VALUE = 180;

    private static final String HISTORICAL_BASE_QUERY = "select * from yahoo.finance.historicaldata";
    private static final String HISTORICAL_QUERY_CONDITION = " where symbol = ";
    private static final String HISTORICAL_QUERY_START_DATE = " and startDate = ";
    private static final String HISTORICAL_QUERY_END_DATE = " and endDate = ";

    private static final String BASE_QUERY = "select * from yahoo.finance.quotes" +
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


    public static long normalizeDateToPersist(String date) {
        // normalize the start date to the beginning of the (UTC) day
        try {
            Time time = new Time();
            Date convertedStringDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            time.set(convertedStringDate.getTime());
            int julianDay = Time.getJulianDay(convertedStringDate.getTime(), time.gmtoff);
            return time.setJulianDay(julianDay);
        } catch (ParseException e) {
            Log.e(LOG_TAG, "normalizeDateToPersist: ", e);
        }
        return 0;
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
    public static String generateHistoricalYQLQuery(String quoteSymbol, String criteriaTime) {

        int queryValue;

        switch (criteriaTime) {
            case ONE_WEEK_CRITERIA:
                queryValue = LAST_WEEK_VALUE;
                break;
            case ONE_MONTH_CRITERIA:
                queryValue = LAST_MONTH_VALUE;
                break;
            case THREE_MONTH_CRITERIA:
                queryValue = LAST_THREE_MONTH_VALUE;
                break;
            case SIX_MONTH_CRITERIA:
                queryValue = LAST_SIX_MONTH_VALUE;
                break;
            default:
                throw new UnsupportedOperationException("Unknow Critera Value.");

        }

        return HISTORICAL_BASE_QUERY +
                HISTORICAL_QUERY_CONDITION +
                "\"" +
                quoteSymbol +
                "\"" +
                HISTORICAL_QUERY_START_DATE +
                "\"" +
                convertDateToString(decreaseDaysToDate(new Date(), queryValue), Utils.FORMAT_DATE_YAHOO) +//Start Date Value
                "\"" +
                HISTORICAL_QUERY_END_DATE +
                "\"" +
                convertDateToString(new Date(), Utils.FORMAT_DATE_YAHOO) +//End Date Value.
                "\"";
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
        StringBuilder query = new StringBuilder(BASE_QUERY);

        Cursor dataBaseCursor = contentResolver.query(
                StockQuoteContract.StockQuoteEntry.CONTENT_URI,
                new String[]{"Distinct " + StockQuoteContract.StockQuoteEntry.TABLE_NAME + "." + StockQuoteContract.StockQuoteEntry.COLUMN_SYMBOL},
                null,
                null,
                null);

        if (params.getTag().equals(StockTaskService.INIT_PARAM) ||
                params.getTag().equals(StockTaskService.PERIODIC_PARAM)) {


            if (dataBaseCursor != null && dataBaseCursor.getCount() > 0) {
                StringBuilder mStoredSymbols = obtainQuoteSymbolsFromCursor(dataBaseCursor);
                mStoredSymbols.replace(mStoredSymbols.length() - 1, mStoredSymbols.length(), ")");
                query.append(mStoredSymbols.toString());
            } else {
                Log.d(LOG_TAG, "generateQuoteQuery: Generating sample query");
                query.append(SAMPLE_STOCK_QUOTES + ")");
                Log.d(LOG_TAG, "generateQuoteQuery: " + query.toString());

            }
        } else if (params.getTag().equals(StockTaskService.ADD_PARAM)) {
            // To dealing with the the behaviour of the API when you try to
            // retrieve only one quote (to retrive one quote you need to
            // call another endpoint) we fetch from the database the stored
            // quotes and update all.
            query.append(FIX_ADD_CALL_STOCK_QUOTE).append(",");
            Log.d(LOG_TAG, "generateQuoteQuery: Generating query to add quote:");
            query.append("\"").append(params.getExtras().getString(Constants.SYMBOL_KEY)).append("\"").append(")");
            Log.d(LOG_TAG, "generateQuoteQuery: " + query.toString());
        }

        if (!dataBaseCursor.isClosed()) {
            dataBaseCursor.close();
        }
        return query.toString();
    }

    @NonNull
    private static StringBuilder obtainQuoteSymbolsFromCursor(Cursor dataBaseCursor) {
        DatabaseUtils.dumpCursor(dataBaseCursor);
        dataBaseCursor.moveToFirst();
        StringBuilder mStoredSymbols = new StringBuilder();
        for (int i = 0; i < dataBaseCursor.getCount(); i++) {

            mStoredSymbols.append("\"").append(dataBaseCursor.getString(
                    dataBaseCursor.getColumnIndex(StockQuoteContract.StockQuoteEntry.COLUMN_SYMBOL)))
                    .append("\",");

            dataBaseCursor.moveToNext();
        }
        dataBaseCursor.close();
        return mStoredSymbols;
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

    /**
     *
     */
    public static Date decreaseDaysToDate(Date date, int numberOfDays) {
        return addDaysToDate(date, numberOfDays * (-1));
    }

    /**
     *
     */
    public static Date addDaysToDate(Date date, int numberOfDays) {
        GregorianCalendar grCal = new GregorianCalendar();
        grCal.setTime(date);
        grCal.add(GregorianCalendar.DAY_OF_MONTH, numberOfDays);
        return grCal.getTime();
    }


    /**
     * Method that given a view and a integer who represent the up/down of the stock price, change the
     * color of the pill.
     *
     * @param view
     * @param isUpValue
     * @param context
     */
    public static void defineQuotePillColor(View view, int isUpValue, Context context) {
        int sdk = Build.VERSION.SDK_INT;
        if (isUpValue == 1) {
            if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                view.setBackgroundDrawable(
                        context.getResources().getDrawable(R.drawable.percent_change_pill_green));
            } else {
                view.setBackground(
                        context.getResources().getDrawable(R.drawable.percent_change_pill_green));
            }
        } else {
            if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                view.setBackgroundDrawable(
                        context.getResources().getDrawable(R.drawable.percent_change_pill_red));
            } else {
                view.setBackground(
                        context.getResources().getDrawable(R.drawable.percent_change_pill_red));
            }
        }
    }

}
