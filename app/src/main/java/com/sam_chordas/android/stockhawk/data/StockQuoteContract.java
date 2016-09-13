package com.sam_chordas.android.stockhawk.data;

/**
 * Created by Javier Godino on 31/08/2016.
 * Email: jgodort.software@gmail.com
 */


import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines table and column names for the StockQuote database.
 */
public class StockQuoteContract {


    private static final String CONTENT_TAG = "content://";

    //The "Content Authority" is a name for the entire content provider.
    public static final String CONTENT_AUTHORITY = "com.sam_chordas.android.stockhawk.data";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse(CONTENT_TAG + CONTENT_AUTHORITY);

    public static final String PATH_STOCKQUOTE = "stock_quote";

    public static final String PATH_HISTORICAL_STOCKQUOTE = "historical_quote";


    public static final class HistoricalQuoteEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_HISTORICAL_STOCKQUOTE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE +
                        "/" + CONTENT_AUTHORITY + "/" + PATH_HISTORICAL_STOCKQUOTE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE +
                        "/" + CONTENT_AUTHORITY + "/" + PATH_HISTORICAL_STOCKQUOTE;


        public static final String TABLE_NAME = "historicalquote";

        public static final String COLUMN_QUOTE_ID = "quote_id";

        public static final String COLUMN_SYMBOL = "symbol";

        public static final String COLUMN_DATE = "date";

        public static final String COLUMN_OPEN = "open";

        public static final String COLUMN_HIGH = "high";

        public static final String COLUMN_LOW = "low";

        public static final String COLUMN_CLOSE = "close";

        public static final String COLUMN_VOLUME = "volume";


        /**
         * Method that generate a uri filtering by the stock id.
         *
         * @param id
         * @return
         */
        public static Uri buildHistoricalQuoteUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildHistoricalQuoteInRangeUri(String symbol, int startDate, int endDate) {
            return CONTENT_URI.buildUpon().
                    appendQueryParameter(COLUMN_SYMBOL, symbol).
                    appendQueryParameter(COLUMN_DATE, String.valueOf(startDate)).
                    appendQueryParameter(COLUMN_DATE, String.valueOf(endDate)).
                    build();
        }


        public static String getSymbolFromUri(Uri uri) {
            return uri.getQueryParameter(COLUMN_SYMBOL);
        }

        public static int getStartDateFromUri(Uri uri) {
            return Integer.parseInt(uri.getPathSegments().get(1));
        }

        public static int getEndDateFromUri(Uri uri) {
            return Integer.parseInt(uri.getPathSegments().get(2));
        }
    }

    public static final class StockQuoteEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_STOCKQUOTE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE +
                        "/" + CONTENT_AUTHORITY + "/" + PATH_STOCKQUOTE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE +
                        "/" + CONTENT_AUTHORITY + "/" + PATH_STOCKQUOTE;

        public static final String TABLE_NAME = "stockquote";


        public static final String COLUMN_ASK = "ask";

        public static final String COLUMN_AVERAGE_DAILY_VOLUME = "average_day_vol";

        public static final String COLUMN_BID = "bid";

        public static final String COLUMN_BOOK_VALUE = "book_value";

        public static final String COLUMN_CHANGE_PERCENTCHANGE = "change_percent_change";

        public static final String COLUMN_CHANGE = "change";

        public static final String COLUMN_CURRENCY = "currency";

        public static final String COLUMN_LASTTRADE_DATE = "last_tradedate";

        public static final String COLUMN_DAYS_LOW = "days_low";

        public static final String COLUMN_DAYS_HIGH = "days_high";

        public static final String COLUMN_YEAR_LOW = "year_low";

        public static final String COLUMN_YEAR_HIGH = "year_high";

        public static final String COLUMN_MARKET_CAPITALIZATION = "market_cap";

        public static final String COLUMN_LAST_TRADE_TIME = "last_trade_time";

        public static final String COLUMN_LAST_TRADE_PRICE = "last_trade_price";

        public static final String COLUMN_DAYS_RANGE = "days_range";

        public static final String COLUMN_NAME = "name";

        public static final String COLUMN_OPEN = "open";

        public static final String COLUMN_PREVIOUS_CLOSE = "previous_close";

        public static final String COLUMN_CHANGE_PERCENT = "change_percent";

        public static final String COLUMN_PRICE_SALES = "price_sales";

        public static final String COLUMN_PRICE_BOOK = "price_book";

        public static final String COLUMN_SYMBOL = "symbol";


        public static final String COLUMN_VOLUME = "volume";

        public static final String COLUMN_YEAR_RANGE = "year_range";

        public static final String COLUMN_STOCK_EXCHANGE = "stock_exchange";

        public static final String COLUMN_PERCENT_CHANGE = "percent_change";

        public static final String COLUMN_ISCURRENT = "is_current";

        public static final String COLUMN_ISUP = "is_up";


        /**
         * Method that generate a uri filtering by the stock id.
         *
         * @param id
         * @return
         */
        public static Uri buildStockQuoteUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildStockQuoteSymbolUri(String symbol) {
            return CONTENT_URI.buildUpon().
                    appendQueryParameter(COLUMN_SYMBOL, symbol).build();
        }

        public static Uri buildStockQuotesSymbolUri(String symbols) {
            return CONTENT_URI.buildUpon().
                    appendQueryParameter(COLUMN_SYMBOL, symbols).build();
        }


    }


}
