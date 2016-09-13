package com.sam_chordas.android.stockhawk.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.StockQuoteContract;
import com.sam_chordas.android.stockhawk.touch_helper.CustomOnChartGestureListener;
import com.sam_chordas.android.stockhawk.touch_helper.CustomOnChartValueSelectedListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Javier Godino on 11/09/2016.
 * Email: jgodort.software@gmail.com
 */

public class TabFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    public static final String CRITERIA_TIME_KEY = "CTK";

    public static final String SELECTED_QUOTE="SQ_ID";

    public static final String ONE_MONTH_CRITERIA = "OMC";
    public static final String THREE_MONTH_CRITERIA = "TMC";
    public static final String SIX_MONTH_CRITERIA = "SMC";
    public static final String ONE_YEAR_CRITERIA = "OYC";

    private static final String[] DETAIL_COLUMNS = {
            StockQuoteContract.StockQuoteEntry.TABLE_NAME + "." + StockQuoteContract.StockQuoteEntry._ID,
            StockQuoteContract.StockQuoteEntry.COLUMN_SYMBOL,
            StockQuoteContract.StockQuoteEntry.COLUMN_NAME,
            StockQuoteContract.StockQuoteEntry.COLUMN_DAYS_HIGH,
            StockQuoteContract.StockQuoteEntry.COLUMN_DAYS_LOW,
            StockQuoteContract.StockQuoteEntry.COLUMN_DAYS_RANGE,
            StockQuoteContract.StockQuoteEntry.COLUMN_YEAR_HIGH,
            StockQuoteContract.StockQuoteEntry.COLUMN_YEAR_LOW,
            StockQuoteContract.StockQuoteEntry.COLUMN_YEAR_RANGE,
    };



    @BindView(R.id.dailyHigh)
    TextView dailyHigh;

    @BindView(R.id.dailyLow)
    TextView dailyLow;

    @BindView(R.id.stockQuoteChart)
    LineChart chartStock;

    @BindView(R.id.stock_rise_icon)
    ImageView stockRiseIcon;

    @BindView(R.id.stock_fall_icon)
    ImageView stockFallIcon;


    int selectedQuoteId;

    String criteriaTime;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(1, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.from(getContext()).inflate(R.layout.quote_detail, container, false);
        ButterKnife.bind(this, rootView);

       Bundle arguments= getArguments();

        if (null!=arguments){
            criteriaTime =arguments.getString(CRITERIA_TIME_KEY);
            selectedQuoteId=arguments.getInt(SELECTED_QUOTE);
        }

        chartStock.setNoDataTextDescription("You need to provide data for the chart.");
        chartStock.setOnChartGestureListener(new CustomOnChartGestureListener());
        chartStock.setOnChartValueSelectedListener(new CustomOnChartValueSelectedListener());

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String selection = StockQuoteContract.StockQuoteEntry._ID + "= ?";
        String[] selectionArgs = new String[]{
                String.valueOf(selectedQuoteId)
        };
        if (selectedQuoteId > 0) {
            return new CursorLoader(getContext(),
                    StockQuoteContract.StockQuoteEntry.CONTENT_URI,
                    DETAIL_COLUMNS,
                    selection,
                    selectionArgs,
                    null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (null != data && data.moveToFirst()) {

            String nameValue = data.getString(
                    data.getColumnIndex(
                            StockQuoteContract.StockQuoteEntry.COLUMN_NAME));
            //quoteName.setText(nameValue);
            //quoteName.setContentDescription(getString(R.string.a11y_company_name, nameValue));

            final String symbolValue = data.getString(
                    data.getColumnIndex(
                            StockQuoteContract.StockQuoteEntry.COLUMN_SYMBOL));
            //quoteSymbol.setText(symbolValue);
            //quoteSymbol.setContentDescription(getString(R.string.a11y_stock_symbol, symbolValue));


            String dailyHighValue = data.getString(
                    data.getColumnIndex(
                            StockQuoteContract.StockQuoteEntry.COLUMN_DAYS_HIGH));
            dailyHigh.setText(dailyHighValue);
            dailyHigh.setContentDescription(getString(R.string.a11y_daily_high, dailyHighValue));

            String dailyLowValue = data.getString(
                    data.getColumnIndex(
                            StockQuoteContract.StockQuoteEntry.COLUMN_DAYS_LOW));
            dailyLow.setText(dailyLowValue);
            dailyLow.setContentDescription(getString(R.string.a11y_daily_low, dailyHighValue));


            stockFallIcon.setContentDescription(getString(R.string.a11y_decrease_icon));
            stockRiseIcon.setContentDescription(getString(R.string.a11y_increse_icon));

//            RequestListener listener = new RequestListener() {
//                @Override
//                public void onSuccess(Object response) {
//                    historicalStockData = new ArrayList<>(((HistoricalStockQuoteModel) response).getQuery().getResults().getQuote());
//                    fillLinechart(historicalStockData);
//                    mDialog.dismiss();
//                }
//
//                @Override
//                public void onFailure(Object error) {
//                    Log.e(LOG_TAG, "Error getting the historical information of " + symbolValue);
//                    mDialog.dismiss();
//                }
//            };
//
//            historialRequestCallback = new RequestCallback<>(listener);
//            YahooFinanceAPIClient client = ServiceGenerator.createService(YahooFinanceAPIClient.class);
//
//
//            Call<HistoricalStockQuoteModel> call = client.getHistorialStockData(Utils.generateHistoricalYQLQuery(symbolValue));
//
//            call.enqueue(historialRequestCallback);


        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
