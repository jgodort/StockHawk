package com.sam_chordas.android.stockhawk.rest;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.Utilities.Utils;
import com.sam_chordas.android.stockhawk.data.StockQuoteContract;
import com.sam_chordas.android.stockhawk.touch_helper.ItemTouchHelperAdapter;
import com.sam_chordas.android.stockhawk.touch_helper.ItemTouchHelperViewHolder;

/**
 * Created by sam_chordas on 10/6/15.
 * Credit to skyfishjy gist:
 * https://gist.github.com/skyfishjy/443b7448f59be978bc59
 * for the code structure
 */
public class QuoteCursorAdapter extends CursorRecyclerViewAdapter<QuoteCursorAdapter.ViewHolder>
        implements ItemTouchHelperAdapter {

    private static Context mContext;
    private static Typeface robotoLight;
    private boolean isPercent;


    public QuoteCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        robotoLight = Typeface.createFromAsset(mContext.getAssets(), "fonts/Roboto-Light.ttf");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_quote, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final Cursor cursor) {
        viewHolder.symbol.setText(cursor.getString(cursor.getColumnIndex(StockQuoteContract.StockQuoteEntry.COLUMN_SYMBOL)));
        viewHolder.bidPrice.setText(cursor.getString(cursor.getColumnIndex(StockQuoteContract.StockQuoteEntry.COLUMN_BID)));
        int sdk = Build.VERSION.SDK_INT;
        if (cursor.getInt(cursor.getColumnIndex(StockQuoteContract.StockQuoteEntry.COLUMN_ISUP)) == 1) {
            if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                viewHolder.change.setBackgroundDrawable(
                        mContext.getResources().getDrawable(R.drawable.percent_change_pill_green));
            } else {
                viewHolder.change.setBackground(
                        mContext.getResources().getDrawable(R.drawable.percent_change_pill_green));
            }
        } else {
            if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                viewHolder.change.setBackgroundDrawable(
                        mContext.getResources().getDrawable(R.drawable.percent_change_pill_red));
            } else {
                viewHolder.change.setBackground(
                        mContext.getResources().getDrawable(R.drawable.percent_change_pill_red));
            }
        }
        if (Utils.showPercent) {
            viewHolder.change.setText(
                    cursor.getString(
                            cursor.getColumnIndex(
                                    StockQuoteContract.StockQuoteEntry.COLUMN_PERCENT_CHANGE)));
        } else {
            viewHolder.change.setText(
                    cursor.getString(
                            cursor.getColumnIndex(
                                    StockQuoteContract.StockQuoteEntry.COLUMN_CHANGE)));
        }


    }

    public int getStockId(int position) {
        Cursor cursor = getCursor();
        cursor.moveToPosition(position);

        return cursor.getInt(cursor.getColumnIndex(StockQuoteContract.StockQuoteEntry._ID));

    }

    public String getStockName(int position) {
        Cursor cursor = getCursor();
        cursor.moveToPosition(position);

        return cursor.getString(cursor.getColumnIndex(StockQuoteContract.StockQuoteEntry.COLUMN_NAME));

    }

    public String getStockSymbol(int position) {
        Cursor cursor = getCursor();
        cursor.moveToPosition(position);

        return cursor.getString(cursor.getColumnIndex(StockQuoteContract.StockQuoteEntry.COLUMN_SYMBOL));

    }

    public String getStockChange(int position) {
        Cursor cursor = getCursor();
        cursor.moveToPosition(position);

        return cursor.getString(cursor.getColumnIndex(StockQuoteContract.StockQuoteEntry.COLUMN_CHANGE));

    }

    public String getStockBid(int position) {
        Cursor cursor = getCursor();
        cursor.moveToPosition(position);

        return cursor.getString(cursor.getColumnIndex(StockQuoteContract.StockQuoteEntry.COLUMN_BID));

    }

    public String getStockPercentChange(int position) {
        Cursor cursor = getCursor();
        cursor.moveToPosition(position);

        return cursor.getString(cursor.getColumnIndex(StockQuoteContract.StockQuoteEntry.COLUMN_PERCENT_CHANGE));

    }


    @Override
    public void onItemDismiss(int position) {
        Cursor c = getCursor();
        c.moveToPosition(position);
        String symbol = c.getString(c.getColumnIndex(StockQuoteContract.StockQuoteEntry.COLUMN_SYMBOL));
        mContext.getContentResolver().delete(StockQuoteContract.StockQuoteEntry.CONTENT_URI,
                StockQuoteContract.StockQuoteEntry.COLUMN_SYMBOL + " =" + symbol, null);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder
            implements ItemTouchHelperViewHolder, View.OnClickListener {
        public final TextView symbol;
        public final TextView bidPrice;
        public final TextView change;

        public ViewHolder(View itemView) {
            super(itemView);
            symbol = (TextView) itemView.findViewById(R.id.stock_symbol);
            symbol.setTypeface(robotoLight);
            bidPrice = (TextView) itemView.findViewById(R.id.bid_price);
            change = (TextView) itemView.findViewById(R.id.change);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
