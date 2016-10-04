package com.sam_chordas.android.stockhawk.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Javier Godino on 01/09/2016.
 * Email: jgodort.software@gmail.com
 */

public class StockQuoteProvider extends ContentProvider {

    private static final String LOG_TAG = StockQuoteProvider.class.getName();


    private static final UriMatcher mUriMatcher = buildUriMatcher();
    private StockQuoteDbHelper mStockQuoteDbHelper;

    static final int STOCKQUOTE = 10;
    static final int STOCKQUOTE_WITH_SYMBOL = 11;
    static final int HISTORICAL_QUOTE = 12;
    static final int HISTORICAL_QUOTE_IN_RANGE = 13;


    private static final SQLiteQueryBuilder mQueryBuilder;

    static {
        mQueryBuilder = new SQLiteQueryBuilder();
        mQueryBuilder.setTables(StockQuoteContract.StockQuoteEntry.TABLE_NAME + " LEFT JOIN " +
                StockQuoteContract.HistoricalQuoteEntry.TABLE_NAME +
                " ON " + StockQuoteContract.StockQuoteEntry.TABLE_NAME +
                "." + StockQuoteContract.StockQuoteEntry._ID +
                " = " + StockQuoteContract.HistoricalQuoteEntry.TABLE_NAME +
                "." + StockQuoteContract.HistoricalQuoteEntry.COLUMN_QUOTE_ID);
    }

    public static final String sStockQuoteSelection =
            StockQuoteContract.StockQuoteEntry.TABLE_NAME +
                    "." +
                    StockQuoteContract.StockQuoteEntry.COLUMN_SYMBOL +
                    " =? ";

    private static final String sStockQuotesSelection = StockQuoteContract.StockQuoteEntry.TABLE_NAME +
            "." +
            StockQuoteContract.StockQuoteEntry.COLUMN_SYMBOL +
            " IN (?)";


    private static final String sHistoricalQuoteSelection =
            StockQuoteContract.HistoricalQuoteEntry.TABLE_NAME +
                    "." +
                    StockQuoteContract.HistoricalQuoteEntry.COLUMN_SYMBOL +
                    " =? ";

    private static final String sHistoricalQuotesSelection = StockQuoteContract.HistoricalQuoteEntry.TABLE_NAME +
            "." +
            StockQuoteContract.HistoricalQuoteEntry.COLUMN_SYMBOL +
            " IN (?)";

    public static final String sHistoricalQuoteBetweenDatesSelection = StockQuoteContract.HistoricalQuoteEntry.TABLE_NAME +
            "." +
            StockQuoteContract.HistoricalQuoteEntry.COLUMN_SYMBOL +
            " =? " +
            StockQuoteContract.HistoricalQuoteEntry.TABLE_NAME +
            "." +
            StockQuoteContract.HistoricalQuoteEntry.COLUMN_DATE +
            " >= ? " +
            " AND " +
            StockQuoteContract.HistoricalQuoteEntry.TABLE_NAME +
            "." +
            StockQuoteContract.HistoricalQuoteEntry.COLUMN_DATE +
            "<= ? ";


    /**
     * This method match the Uri with the constants defined above.
     *
     * @return
     */
    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = StockQuoteContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, StockQuoteContract.PATH_STOCKQUOTE, STOCKQUOTE);
        matcher.addURI(authority, StockQuoteContract.PATH_STOCKQUOTE + "/*", STOCKQUOTE_WITH_SYMBOL);
        matcher.addURI(authority, StockQuoteContract.PATH_HISTORICAL_STOCKQUOTE, HISTORICAL_QUOTE);
        matcher.addURI(authority, StockQuoteContract.PATH_HISTORICAL_STOCKQUOTE + "/*", HISTORICAL_QUOTE_IN_RANGE);

        return matcher;

    }

    private Cursor getStockQuoteCursor(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        return mQueryBuilder.query(mStockQuoteDbHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
    }

    private Cursor getHistoricalQuoteCursor(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        return mQueryBuilder.query(mStockQuoteDbHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
    }

    private Cursor getHistoricalQuoteInRangeCursor(Uri uri, String[] projection, String selection, String sortOrder) {

        String symbol = StockQuoteContract.HistoricalQuoteEntry.getSymbolFromUri(uri);
        int startDate = StockQuoteContract.HistoricalQuoteEntry.getStartDateFromUri(uri);
        int endDate = StockQuoteContract.HistoricalQuoteEntry.getEndDateFromUri(uri);

        return mQueryBuilder.query(mStockQuoteDbHelper.getReadableDatabase(),
                projection,
                selection,
                new String[]{symbol, String.valueOf(startDate), String.valueOf(endDate)},
                null,
                null,
                sortOrder);
    }


    @Override
    public boolean onCreate() {
        mStockQuoteDbHelper = new StockQuoteDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor retrievedCursor;

        switch (mUriMatcher.match(uri)) {
            case STOCKQUOTE:
                retrievedCursor = getStockQuoteCursor(uri, projection, selection, selectionArgs, sortOrder);
                break;
            case STOCKQUOTE_WITH_SYMBOL:
                retrievedCursor = null;
                break;
            case HISTORICAL_QUOTE:
                retrievedCursor = getHistoricalQuoteCursor(uri, projection, selection, selectionArgs, sortOrder);
                break;
            case HISTORICAL_QUOTE_IN_RANGE:
                retrievedCursor = getHistoricalQuoteInRangeCursor(uri, projection, selection, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        retrievedCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retrievedCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        final int match = mUriMatcher.match(uri);

        switch (match) {
            case STOCKQUOTE:
                return StockQuoteContract.StockQuoteEntry.CONTENT_TYPE;
            case STOCKQUOTE_WITH_SYMBOL:
                return StockQuoteContract.StockQuoteEntry.CONTENT_ITEM_TYPE;
            case HISTORICAL_QUOTE:
                return StockQuoteContract.HistoricalQuoteEntry.CONTENT_TYPE;
            case HISTORICAL_QUOTE_IN_RANGE:
                return StockQuoteContract.HistoricalQuoteEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {

        final SQLiteDatabase db = mStockQuoteDbHelper.getWritableDatabase();
        final int match = mUriMatcher.match(uri);

        Uri returnUri;

        switch (match) {
            case STOCKQUOTE:
                long _id = db.insert(StockQuoteContract.StockQuoteEntry.TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    returnUri = StockQuoteContract.StockQuoteEntry.buildStockQuoteUri(_id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;

            case HISTORICAL_QUOTE:

                long _idH = db.insert(StockQuoteContract.HistoricalQuoteEntry.TABLE_NAME, null, contentValues);
                if (_idH > 0) {
                    returnUri = StockQuoteContract.HistoricalQuoteEntry.buildHistoricalQuoteUri(_idH);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }


    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mStockQuoteDbHelper.getWritableDatabase();
        final int match = mUriMatcher.match(uri);
        int rowsDeleted;

        if (null == selection) {
            selection = "1";
        }

        switch (match) {
            case STOCKQUOTE:
                rowsDeleted = db.delete(StockQuoteContract.StockQuoteEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case HISTORICAL_QUOTE:
                rowsDeleted = db.delete(StockQuoteContract.HistoricalQuoteEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override

    public int update(@NonNull Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mStockQuoteDbHelper.getWritableDatabase();
        final int match = mUriMatcher.match(uri);
        int rowsUpdated;
        switch (match) {
            case STOCKQUOTE:
                rowsUpdated = db.update(StockQuoteContract.StockQuoteEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            case HISTORICAL_QUOTE:
                rowsUpdated = db.update(StockQuoteContract.HistoricalQuoteEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);

        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {

        Log.d(LOG_TAG, "Performing a bulk insert");
        Log.d(LOG_TAG, "The asociated uri: " + uri.toString());
        final SQLiteDatabase db = mStockQuoteDbHelper.getWritableDatabase();
        final int match = mUriMatcher.match(uri);

        switch (match) {
            case STOCKQUOTE:
                Log.d(LOG_TAG, "Beginning the database transaction");
                db.beginTransaction();
                int returnedCount = 0;
                try {
                    for (ContentValues iterator : values) {
                        Long _id = db.insert(StockQuoteContract.StockQuoteEntry.TABLE_NAME, null, iterator);
                        if (_id != -1) {
                            returnedCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    Log.d(LOG_TAG, "Closing the database transaction");
                    db.endTransaction();
                }


                getContext().getContentResolver().notifyChange(uri, null);
                return returnedCount;

            case HISTORICAL_QUOTE:
                Log.d(LOG_TAG, "Beginning the database transaction");
                db.beginTransaction();
                int returnedCountHistorical = 0;
                try {
                    for (ContentValues iterator : values) {
                        Long _id = db.insert(StockQuoteContract.HistoricalQuoteEntry.TABLE_NAME, null, iterator);
                        if (_id != -1) {
                            returnedCountHistorical++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    Log.d(LOG_TAG, "Closing the database transaction");
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnedCountHistorical;

            default:
                return super.bulkInsert(uri, values);
        }
    }
}
