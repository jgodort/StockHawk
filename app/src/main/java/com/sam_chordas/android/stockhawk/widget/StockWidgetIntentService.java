package com.sam_chordas.android.stockhawk.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.StockQuoteContract;
import com.sam_chordas.android.stockhawk.ui.MyStocksActivity;

/**
 * Created by Javier Godino on 29/09/2016.
 * Email: jgodort.software@gmail.com
 */

public class StockWidgetIntentService extends IntentService {


    private static final String[] STOCKQUOTE_COLUMNS = {
            StockQuoteContract.StockQuoteEntry.TABLE_NAME + "." + StockQuoteContract.StockQuoteEntry.COLUMN_SYMBOL,
            StockQuoteContract.StockQuoteEntry.TABLE_NAME + "." + StockQuoteContract.StockQuoteEntry.COLUMN_NAME,
            StockQuoteContract.StockQuoteEntry.TABLE_NAME + "." + StockQuoteContract.StockQuoteEntry.COLUMN_BID,
            StockQuoteContract.StockQuoteEntry.TABLE_NAME + "." + StockQuoteContract.StockQuoteEntry.COLUMN_CHANGE,
    };

    // these indices must match the projection
    private static final int INDEX_SYMBOL = 0;
    private static final int INDEX_NAME = 1;
    private static final int INDEX_BID = 2;
    private static final int INDEX_CHANGE = 3;

    public StockWidgetIntentService() {
        super("StockWidgetIntentService");

    }

    @Override
    protected void onHandleIntent(Intent intent) {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, StockWidgetProvider.class));

        Cursor cursorWidget = getContentResolver().query(
                StockQuoteContract.StockQuoteEntry.buildStockQuotesSymbolUri("YHOO"),
                STOCKQUOTE_COLUMNS,
                null,
                null,
                null);

        if (cursorWidget == null) {
            return;
        }
        if (!cursorWidget.moveToFirst()) {
            cursorWidget.close();
            return;
        }

        String widgetSymbol = cursorWidget.getString(INDEX_SYMBOL);
        String widgetName = cursorWidget.getString(INDEX_NAME);
        String widgetBid = cursorWidget.getString(INDEX_BID);
        String widgetChange = cursorWidget.getString(INDEX_CHANGE);

        cursorWidget.close();


        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_stock_small);
            views.setTextViewText(R.id.widget_stock_symbol, widgetSymbol);
            views.setTextViewText(R.id.widget_bid_price, widgetBid);
            views.setTextViewText(R.id.widget_change, widgetChange);
            views.setTextViewText(R.id.widget_stock_name, widgetName);

            Intent launchIntent = new Intent(this, MyStocksActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, 0);
            views.setOnClickPendingIntent(R.id.widget_Stock_small, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }


    }
}
