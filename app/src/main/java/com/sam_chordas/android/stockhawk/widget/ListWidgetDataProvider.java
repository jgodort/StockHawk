package com.sam_chordas.android.stockhawk.widget;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.StockQuoteContract;
import com.sam_chordas.android.stockhawk.rest.model.Quote;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Javier Godino on 03/10/2016.
 * Email: jgodort.software@gmail.com
 */

public class ListWidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    private static final String[] projection = new String[]{
            "DISTINCT " + StockQuoteContract.StockQuoteEntry.TABLE_NAME + "." + StockQuoteContract.StockQuoteEntry.COLUMN_SYMBOL,
            StockQuoteContract.StockQuoteEntry.TABLE_NAME + "." + StockQuoteContract.StockQuoteEntry._ID,
            StockQuoteContract.StockQuoteEntry.TABLE_NAME + "." + StockQuoteContract.StockQuoteEntry.COLUMN_NAME,
            StockQuoteContract.StockQuoteEntry.TABLE_NAME + "." + StockQuoteContract.StockQuoteEntry.COLUMN_BID,
            StockQuoteContract.StockQuoteEntry.TABLE_NAME + "." + StockQuoteContract.StockQuoteEntry.COLUMN_PERCENT_CHANGE,
            StockQuoteContract.StockQuoteEntry.TABLE_NAME + "." + StockQuoteContract.StockQuoteEntry.COLUMN_CHANGE,
            StockQuoteContract.StockQuoteEntry.TABLE_NAME + "." + StockQuoteContract.StockQuoteEntry.COLUMN_ISUP};

    private Cursor mCursor;
    private List<Quote> widgetListItems;
    private Context mContext;

    public ListWidgetDataProvider(Context context) {
        this.mContext = context;
        widgetListItems = new ArrayList<>();
    }


    private void loadWidgetData() {
        if (!widgetListItems.isEmpty()) {
            widgetListItems.clear();
        }

        mCursor = mContext.getContentResolver().query(StockQuoteContract.StockQuoteEntry.CONTENT_URI,
                projection,
                StockQuoteContract.StockQuoteEntry.TABLE_NAME + "." + StockQuoteContract.StockQuoteEntry.COLUMN_ISCURRENT + " = ?",
                new String[]{"1"},
                null);
        DatabaseUtils.dumpCursor(mCursor);

        while (mCursor.moveToNext()) {

            Quote quote = new Quote(
                    mCursor.getInt(mCursor.getColumnIndex(StockQuoteContract.StockQuoteEntry._ID)),
                    mCursor.getString(mCursor.getColumnIndex(StockQuoteContract.StockQuoteEntry.COLUMN_SYMBOL)),
                    mCursor.getString(mCursor.getColumnIndex(StockQuoteContract.StockQuoteEntry.COLUMN_NAME)),
                    mCursor.getString(mCursor.getColumnIndex(StockQuoteContract.StockQuoteEntry.COLUMN_BID)),
                    mCursor.getString(mCursor.getColumnIndex(StockQuoteContract.StockQuoteEntry.COLUMN_PERCENT_CHANGE)),
                    mCursor.getString(mCursor.getColumnIndex(StockQuoteContract.StockQuoteEntry.COLUMN_CHANGE)));
            widgetListItems.add(quote);
        }

        mCursor.close();


    }

    @Override
    public void onCreate() {
        loadWidgetData();
    }

    @Override
    public void onDataSetChanged() {
        loadWidgetData();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return widgetListItems.size() > 0 ? widgetListItems.size() : 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {

        Quote quote = widgetListItems.get(position);

        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.list_item_widget_quote);

        remoteViews.setTextViewText(R.id.widget_stock_symbol, quote.getSymbol());
        remoteViews.setTextViewText(R.id.widget_bid_price, quote.getBid());
        remoteViews.setTextViewText(R.id.widget_change, quote.getChange());
        remoteViews.setTextViewText(R.id.widget_stock_name, quote.getName());

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
