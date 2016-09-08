package com.sam_chordas.android.stockhawk.ui;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.StockQuoteContract;
import com.sam_chordas.android.stockhawk.rest.ServiceGenerator;
import com.sam_chordas.android.stockhawk.rest.Utils;
import com.sam_chordas.android.stockhawk.rest.client.YahooFinanceAPIClient;
import com.sam_chordas.android.stockhawk.rest.model.HistoricalQuote;
import com.sam_chordas.android.stockhawk.rest.model.HistoricalStockQuoteModel;
import com.sam_chordas.android.stockhawk.rest.request.RequestCallback;
import com.sam_chordas.android.stockhawk.rest.request.listener.RequestListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

/**
 * Created by Javier Godino on 30/08/2016.
 * Email: jgodort.software@gmail.com
 */

public class DetailStockQuoteFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, OnChartGestureListener, OnChartValueSelectedListener {

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

    private ProgressDialog mDialog;


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

        mDialog = new ProgressDialog(getContext());
        mDialog.setTitle(getString(R.string.loading_message));


        chartStock.setNoDataTextDescription("You need to provide data for the chart.");
        chartStock.setOnChartGestureListener(this);
        chartStock.setOnChartValueSelectedListener(this);

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

        //Retrive the X/Y Axis dataset values.
        List<String> xAxis = generateXAxisValues(processedData);
        List<List<Entry>> yAxis = generateYAxisValues(processedData);

        if (null != xAxis && null != yAxis) {
            LineDataSet datasetHigh;
            LineDataSet datasetLow;

            //Create a dataset  and give it a type.
            datasetHigh = new LineDataSet(yAxis.get(0), "High Value");
            datasetHigh.setColor(Color.GREEN);
            datasetHigh.setCircleColor(Color.BLUE);
            datasetHigh.setFillColor(Color.GREEN);
            datasetHigh.setDrawFilled(true);
            datasetHigh.setValueTextColor(Color.WHITE);
            datasetLow = new LineDataSet(yAxis.get(1), "Low Value");
            datasetLow.setColor(Color.RED);
            datasetLow.setCircleColor(Color.BLUE);
            datasetLow.setFillColor(Color.RED);
            datasetLow.setDrawFilled(true);
            datasetLow.setValueTextColor(Color.WHITE);


            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(datasetHigh);
            dataSets.add(datasetLow);

            //Create a data object with the datasets
            LineData data = new LineData(dataSets);
            chartStock.setData(data);


            //customize contentDescription and chart information
            chartStock.setContentDescription(getString(R.string.a11y_historical_chart, quoteName.getText()));
            // no description text
            chartStock.setDescription("");

            //Refresh the chart
            chartStock.invalidate();


        }

    }

    private List<String> generateXAxisValues(List<HistoricalQuote> data) {

        List<String> labelsXAxis = null;
        if (null != data && !data.isEmpty()) {
            labelsXAxis = new ArrayList<>();
            for (HistoricalQuote hqIterator : data) {
                try {
                    //Obtain the date instance of the string value.
                    GregorianCalendar stockDate = new GregorianCalendar();
                    stockDate.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(hqIterator.getDate()));
                    labelsXAxis.add(String.valueOf(stockDate.get(GregorianCalendar.DATE)) + "/" + String.valueOf(stockDate.get(GregorianCalendar.MONTH)));

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }
        return labelsXAxis;
    }

    private List<List<Entry>> generateYAxisValues(List<HistoricalQuote> data) {

        List<List<Entry>> obtainedDataSets = null;
        List<Entry> valuesYAxisHigh = null;
        List<Entry> valuesYAxisLow = null;

        if (null != data && !data.isEmpty()) {
            int counter = 0;
            valuesYAxisHigh = new ArrayList<>();
            valuesYAxisLow = new ArrayList<>();
            for (HistoricalQuote hqIterator : data) {
                valuesYAxisHigh.add(new Entry(counter, Float.valueOf(hqIterator.getHigh())));
                valuesYAxisLow.add(new Entry(counter, Float.valueOf(hqIterator.getLow())));
                counter++;
            }
            obtainedDataSets = new ArrayList<>();
            obtainedDataSets.add(valuesYAxisHigh);
            obtainedDataSets.add(valuesYAxisLow);
        }

        return obtainedDataSets;
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
        mDialog.show();
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
                    mDialog.dismiss();
                }

                @Override
                public void onFailure(Object error) {
                    Log.e(LOG_TAG, "Error getting the historical information of " + symbolValue);
                    mDialog.dismiss();
                }
            };

            historialRequestCallback = new RequestCallback<>(listener);
            YahooFinanceAPIClient client = ServiceGenerator.createService(YahooFinanceAPIClient.class);


            Call<HistoricalStockQuoteModel> call = client.getHistorialStockData(Utils.generateHistoricalYQLQuery(symbolValue));

            call.enqueue(historialRequestCallback);


        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.d(LOG_TAG, "START, x: " + me.getX() + ", y: " + me.getY());
    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.d(LOG_TAG, "END, lastGesture: " + lastPerformedGesture);
    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

        Log.d(LOG_TAG, "LongPress");
    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
        Log.d(LOG_TAG, "DoubleTapped");
    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {

        Log.d(LOG_TAG, "SingleTapped");
    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        Log.d(LOG_TAG, "Chart flinged. VeloX: " + velocityX + ", VeloY: " + velocityY);
    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
        Log.d(LOG_TAG, "Scale / Zoom, ScaleX: " + scaleX + ", ScaleY: " + scaleY);
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        Log.d(LOG_TAG, "Translate / Move, dX: " + dX + ", dY: " + dY);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.d("Entry selected", e.toString());
        Log.d("LOWHIGH", "low: " + chartStock.getLowestVisibleX()
                + ", high: " + chartStock.getHighestVisibleX());

        Log.d("MIN MAX", "xmin: " + chartStock.getXChartMin()
                + ", xmax: " + chartStock.getXChartMax()
                + ", ymin: " + chartStock.getYChartMin()
                + ", ymax: " + chartStock.getYChartMax());
    }

    @Override
    public void onNothingSelected() {

        Log.d(LOG_TAG, "Nothing selected Nothing selected.");
    }
}
