package com.sam_chordas.android.stockhawk.ui;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.rest.model.HistoricalQuote;
import com.sam_chordas.android.stockhawk.rest.model.HistoricalStockQuoteModel;
import com.sam_chordas.android.stockhawk.rest.request.RequestCallback;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Javier Godino on 30/08/2016.
 * Email: jgodort.software@gmail.com
 */

public class DetailStockQuoteFragment extends Fragment  {

    private static final String LOG_TAG = DetailStockQuoteFragment.class.getSimpleName();



    private static final int DETAIL_LOADER = 0;





    private int selectedStockQuoteId;

    private List<HistoricalQuote> historicalStockData;

    private RequestCallback<HistoricalStockQuoteModel> historialRequestCallback;

    private ProgressDialog mDialog;


    public DetailStockQuoteFragment() {
        setHasOptionsMenu(true);
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.detail_stockquote, container, false);
        ButterKnife.bind(this, rootView);

        mDialog = new ProgressDialog(getContext());
        mDialog.setTitle(getString(R.string.loading_message));


        //Retrive the Bundle arguments
        Bundle bundle = getArguments();

        if (null != bundle) {
            selectedStockQuoteId = bundle.getInt(MyStocksActivity.SELECTED_STOCKQUOTE);
        }
        return rootView;
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
//            chartStock.setData(data);
//
//
//            //customize contentDescription and chart information
//            chartStock.setContentDescription(getString(R.string.a11y_historical_chart, quoteName.getText()));
//            // no description text
//            chartStock.setDescription("");
//
//            //Refresh the chart
//            chartStock.invalidate();


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






}
