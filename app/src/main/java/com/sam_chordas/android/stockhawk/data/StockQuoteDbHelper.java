package com.sam_chordas.android.stockhawk.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Javier Godino on 01/09/2016.
 * Email: jgodort.software@gmail.com
 */

public class StockQuoteDbHelper extends SQLiteOpenHelper {


    private static final String NOT_NULL = " NOT NULL ";
    private static final String CREATE_TABLE = " CREATE TABLE ";
    private static final String PRIMARY = " PRIMARY ";
    private static final String KEY = " KEY ";
    private static final String AUTOINCREMENT = " AUTOINCREMENT ";
    private static final String TEXT = " TEXT ";
    private static final String INTEGER = " INTEGER ";
    private static final String COMMA = ", ";
    private static final String LEFT_PARENTHESIS = " ( ";
    private static final String RIGHT_PARENTHESIS = " ) ";


    private static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "stockquote.db";


    public StockQuoteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


        final String SQL_CREATE_STOCKQUOTE_TABLE = CREATE_TABLE + StockQuoteContract.StockQuoteEntry.TABLE_NAME +
                LEFT_PARENTHESIS +
                StockQuoteContract.StockQuoteEntry._ID + INTEGER + PRIMARY + KEY + AUTOINCREMENT + COMMA +
                StockQuoteContract.StockQuoteEntry.COLUMN_SYMBOL + TEXT + NOT_NULL + COMMA +
                StockQuoteContract.StockQuoteEntry.COLUMN_ASK + TEXT + NOT_NULL + COMMA +
                StockQuoteContract.StockQuoteEntry.COLUMN_AVERAGE_DAILY_VOLUME + TEXT + NOT_NULL + COMMA +
                StockQuoteContract.StockQuoteEntry.COLUMN_BID + TEXT + NOT_NULL + COMMA +
                StockQuoteContract.StockQuoteEntry.COLUMN_BOOK_VALUE + TEXT + NOT_NULL + COMMA +
                StockQuoteContract.StockQuoteEntry.COLUMN_CHANGE + TEXT + NOT_NULL + COMMA +
                StockQuoteContract.StockQuoteEntry.COLUMN_CHANGE_PERCENT + TEXT + NOT_NULL + COMMA +
                StockQuoteContract.StockQuoteEntry.COLUMN_CHANGE_PERCENTCHANGE + TEXT + NOT_NULL + COMMA +
                StockQuoteContract.StockQuoteEntry.COLUMN_CURRENCY + TEXT + NOT_NULL + COMMA +
                StockQuoteContract.StockQuoteEntry.COLUMN_DAYS_HIGH + TEXT + NOT_NULL + COMMA +
                StockQuoteContract.StockQuoteEntry.COLUMN_DAYS_LOW + TEXT + NOT_NULL + COMMA +
                StockQuoteContract.StockQuoteEntry.COLUMN_DAYS_RANGE + TEXT + NOT_NULL + COMMA +
                StockQuoteContract.StockQuoteEntry.COLUMN_LAST_TRADE_PRICE + TEXT + NOT_NULL + COMMA +
                StockQuoteContract.StockQuoteEntry.COLUMN_LAST_TRADE_TIME + TEXT + NOT_NULL + COMMA +
                StockQuoteContract.StockQuoteEntry.COLUMN_LASTTRADE_DATE + TEXT + NOT_NULL + COMMA +
                StockQuoteContract.StockQuoteEntry.COLUMN_MARKET_CAPITALIZATION + TEXT + NOT_NULL + COMMA +
                StockQuoteContract.StockQuoteEntry.COLUMN_NAME + TEXT + NOT_NULL + COMMA +
                StockQuoteContract.StockQuoteEntry.COLUMN_OPEN + TEXT + NOT_NULL + COMMA +
                StockQuoteContract.StockQuoteEntry.COLUMN_PERCENT_CHANGE + TEXT + NOT_NULL + COMMA +
                StockQuoteContract.StockQuoteEntry.COLUMN_PREVIOUS_CLOSE + TEXT + NOT_NULL + COMMA +
                StockQuoteContract.StockQuoteEntry.COLUMN_PRICE_BOOK + TEXT + COMMA +
                StockQuoteContract.StockQuoteEntry.COLUMN_PRICE_SALES + TEXT + NOT_NULL + COMMA +
                StockQuoteContract.StockQuoteEntry.COLUMN_STOCK_EXCHANGE + TEXT + NOT_NULL + COMMA +
                StockQuoteContract.StockQuoteEntry.COLUMN_VOLUME + TEXT + NOT_NULL + COMMA +
                StockQuoteContract.StockQuoteEntry.COLUMN_YEAR_HIGH + TEXT + NOT_NULL + COMMA +
                StockQuoteContract.StockQuoteEntry.COLUMN_YEAR_LOW + TEXT + NOT_NULL + COMMA +
                StockQuoteContract.StockQuoteEntry.COLUMN_YEAR_RANGE + TEXT + NOT_NULL + COMMA +
                StockQuoteContract.StockQuoteEntry.COLUMN_ISCURRENT + INTEGER + NOT_NULL + COMMA +
                StockQuoteContract.StockQuoteEntry.COLUMN_ISUP + INTEGER + NOT_NULL +
                RIGHT_PARENTHESIS + ";";


        final String SQL_CREATE_HISTORICALQUOTE_TABLE = CREATE_TABLE + StockQuoteContract.HistoricalQuoteEntry.TABLE_NAME +
                LEFT_PARENTHESIS +
                StockQuoteContract.HistoricalQuoteEntry._ID + INTEGER + PRIMARY + KEY + AUTOINCREMENT + COMMA +
                StockQuoteContract.HistoricalQuoteEntry.COLUMN_QUOTE_ID + INTEGER + NOT_NULL + COMMA +
                StockQuoteContract.HistoricalQuoteEntry.COLUMN_SYMBOL + TEXT + NOT_NULL + COMMA +
                StockQuoteContract.HistoricalQuoteEntry.COLUMN_DATE + INTEGER + NOT_NULL + COMMA +
                StockQuoteContract.HistoricalQuoteEntry.COLUMN_OPEN + TEXT + NOT_NULL + COMMA +
                StockQuoteContract.HistoricalQuoteEntry.COLUMN_HIGH + TEXT + NOT_NULL + COMMA +
                StockQuoteContract.HistoricalQuoteEntry.COLUMN_LOW + TEXT + NOT_NULL + COMMA +
                StockQuoteContract.HistoricalQuoteEntry.COLUMN_CLOSE + TEXT + NOT_NULL + COMMA +
                StockQuoteContract.HistoricalQuoteEntry.COLUMN_VOLUME + TEXT + NOT_NULL + COMMA +

                // Set up the location column as a foreign key to location table.
                " FOREIGN KEY (" + StockQuoteContract.HistoricalQuoteEntry.COLUMN_QUOTE_ID + ") REFERENCES " +
                StockQuoteContract.StockQuoteEntry.TABLE_NAME + " (" + StockQuoteContract.StockQuoteEntry._ID + "), " +
                RIGHT_PARENTHESIS + ";";


        sqLiteDatabase.execSQL(SQL_CREATE_STOCKQUOTE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_HISTORICALQUOTE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + StockQuoteContract.HistoricalQuoteEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + StockQuoteContract.StockQuoteEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
