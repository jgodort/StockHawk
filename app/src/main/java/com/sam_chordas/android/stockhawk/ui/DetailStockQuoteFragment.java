package com.sam_chordas.android.stockhawk.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.StockQuoteContract;
import com.sam_chordas.android.stockhawk.rest.ServiceGenerator;
import com.sam_chordas.android.stockhawk.rest.client.YahooFinanceAPIClient;
import com.sam_chordas.android.stockhawk.rest.model.HistoricalQuote;
import com.sam_chordas.android.stockhawk.rest.model.HistoricalStockQuoteModel;
import com.sam_chordas.android.stockhawk.rest.request.RequestCallback;
import com.sam_chordas.android.stockhawk.rest.request.listener.RequestListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

/**
 * Created by Javier Godino on 30/08/2016.
 * Email: jgodort.software@gmail.com
 */

public class DetailStockQuoteFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = DetailStockQuoteFragment.class.getSimpleName();

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

    private static final int DETAIL_LOADER = 0;


    @BindView(R.id.quoteName)
    TextView quoteName;

    @BindView(R.id.quoteSymbol)
    TextView quoteSymbol;

    @BindView(R.id.rangeDaily)
    TextView rangeDaily;

    @BindView(R.id.dailyHigh)
    TextView dailyHigh;

    @BindView(R.id.dailyLow)
    TextView dailyLow;

    @BindView(R.id.rangeYearly)
    TextView rangeYearly;

    @BindView(R.id.yearlyHigh)
    TextView yearlyHigh;

    @BindView(R.id.yearlyLow)
    TextView yearlyLow;

    @BindView(R.id.stockQuoteChart)
    LineChart chartStock;

    @BindView(R.id.stock_rise_icon)
    ImageView stockRiseIcon;

    @BindView(R.id.stock_fall_icon)
    ImageView stockFallIcon;


    private int selectedStockQuoteId;

    private List<HistoricalQuote> historicalStockData;

    private RequestCallback<HistoricalStockQuoteModel> historialRequestCallback;


    public DetailStockQuoteFragment() {
        setHasOptionsMenu(true);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.detail_stockquote, container, false);
        ButterKnife.bind(this, rootView);


        chartStock.setContentDescription("");
        // no description text
        chartStock.setDescription("");
        chartStock.setNoDataTextDescription("You need to provide data for the chart.");

        //Retrive the Bundle arguments
        Bundle bundle = getArguments();

        if (null != bundle) {
            selectedStockQuoteId = bundle.getInt(MyStocksActivity.SELECTED_STOCKQUOTE);
        }
        return rootView;
    }

    private void configureLinechar() {

    }

    private void fillLinechart(List<HistoricalQuote> processedData) {

        LineDataSet lineDataSet;
        List<String> labelsXAxis;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        if (null != processedData && !processedData.isEmpty()) {
            labelsXAxis = new ArrayList<>();
            List<Entry> entryData = new ArrayList<>();
            for (HistoricalQuote hqIterator : processedData) {
                try {
                    //Obtain the date instance of the string value.
                    Date stockDate = dateFormat.parse(hqIterator.getDate());
                    labelsXAxis.add(hqIterator.getDate());

                    entryData.add(new Entry(stockDate.getTime(), Float.valueOf(hqIterator.getHigh())));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
            lineDataSet=new LineDataSet(entryData,"Prueba");
            LineData lineData=new LineData(lineDataSet);
            chartStock.setData(lineData);

        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String selection = StockQuoteContract.StockQuoteEntry._ID + "= ?";
        String[] selectionArgs = new String[]{
                String.valueOf(selectedStockQuoteId)
        };
        if (selectedStockQuoteId > 0) {
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
            quoteName.setText(nameValue);
            quoteName.setContentDescription(getString(R.string.a11y_company_name, nameValue));

            final String symbolValue = data.getString(
                    data.getColumnIndex(
                            StockQuoteContract.StockQuoteEntry.COLUMN_SYMBOL));
            quoteSymbol.setText(symbolValue);
            quoteSymbol.setContentDescription(getString(R.string.a11y_stock_symbol, symbolValue));

            String rangeDailyValue = data.getString(
                    data.getColumnIndex(
                            StockQuoteContract.StockQuoteEntry.COLUMN_DAYS_RANGE));
            rangeDaily.setText(rangeDailyValue);
            rangeDaily.setContentDescription(getString(R.string.a11y_dayly_range, rangeDailyValue));

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

            String rangeYearlyValue = data.getString(
                    data.getColumnIndex(
                            StockQuoteContract.StockQuoteEntry.COLUMN_YEAR_RANGE));
            rangeYearly.setText(rangeYearlyValue);
            rangeYearly.setContentDescription(getString(R.string.a11y_yearly_range, rangeYearlyValue));

            String yearlyHighValue =
                    data.getString(
                            data.getColumnIndex(
                                    StockQuoteContract.StockQuoteEntry.COLUMN_YEAR_HIGH));
            yearlyHigh.setText(yearlyHighValue);
            yearlyHigh.setContentDescription(getString(R.string.a11y_yearly_high, yearlyHighValue));

            String yearlyLowValue =
                    data.getString(
                            data.getColumnIndex(
                                    StockQuoteContract.StockQuoteEntry.COLUMN_YEAR_LOW));
            yearlyLow.setText(yearlyLowValue);
            yearlyLow.setContentDescription(getString(R.string.a11y_yearly_low, yearlyLowValue));

            stockFallIcon.setContentDescription(getString(R.string.a11y_decrease_icon));
            stockRiseIcon.setContentDescription(getString(R.string.a11y_increse_icon));

            RequestListener listener = new RequestListener() {
                @Override
                public void onSuccess(Object response) {
                    historicalStockData = new ArrayList<>(((HistoricalStockQuoteModel) response).getQuery().getResults().getQuote());
                    fillLinechart(historicalStockData);
                }

                @Override
                public void onFailure(Object error) {
                    Log.e(LOG_TAG, "Error getting the historical information of " + symbolValue);
                }
            };

            historialRequestCallback = new RequestCallback<>(listener);
            YahooFinanceAPIClient client = ServiceGenerator.createService(YahooFinanceAPIClient.class);

            Call<HistoricalStockQuoteModel> call = client.getHistorialStockData("select * from yahoo.finance.historicaldata where symbol = \"YHOO\" and startDate = \"2009-09-11\" and endDate = \"2010-03-10\"");

            call.enqueue(historialRequestCallback);


        }
    }


    public void prepareDataChart() {

        if (historicalStockData != null) {


            for (HistoricalQuote quote : historicalStockData) {
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
