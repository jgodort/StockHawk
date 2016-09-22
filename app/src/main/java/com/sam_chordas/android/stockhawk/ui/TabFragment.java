package com.sam_chordas.android.stockhawk.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.rest.model.HistoricalQuote;
import com.sam_chordas.android.stockhawk.rest.model.Quote;
import com.sam_chordas.android.stockhawk.touch_helper.CustomOnChartGestureListener;
import com.sam_chordas.android.stockhawk.touch_helper.CustomOnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Javier Godino on 11/09/2016.
 * Email: jgodort.software@gmail.com
 */

public class TabFragment extends Fragment {


    public static final String CRITERIA_TIME_KEY = "CTK";

    public static final String SELECTED_QUOTE = "SQ_ID";

    public static final String ONE_MONTH_CRITERIA = "OMC";
    public static final String THREE_MONTH_CRITERIA = "TMC";
    public static final String SIX_MONTH_CRITERIA = "SMC";
    public static final String ONE_YEAR_CRITERIA = "OYC";


    @BindView(R.id.dailyHigh)
    TextView dailyHigh;

    @BindView(R.id.dailyLow)
    TextView dailyLow;

    @BindView(R.id.yearlyLow)
    TextView yearlyLow;

    @BindView(R.id.yearlyHigh)
    TextView yearlyHigh;


    @BindView(R.id.stockQuoteChart)
    LineChart chartStock;

    @BindView(R.id.stock_rise_icon)
    ImageView stockRiseIcon;

    @BindView(R.id.stock_fall_icon)
    ImageView stockFallIcon;

    private Quote quote;
    String criteriaTime;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.from(getContext()).inflate(R.layout.quote_detail, container, false);
        ButterKnife.bind(this, rootView);


        chartStock.setNoDataTextDescription("You need to provide data for the chart.");
        chartStock.setOnChartGestureListener(new CustomOnChartGestureListener());
        chartStock.setOnChartValueSelectedListener(new CustomOnChartValueSelectedListener());

        Bundle arguments = getArguments();
        if (null != arguments) {
            criteriaTime = arguments.getString(CRITERIA_TIME_KEY);
            quote = arguments.getParcelable(SELECTED_QUOTE);

            dailyHigh.setText(quote.getDaysHigh());
            dailyHigh.setContentDescription(getString(R.string.a11y_daily_high, quote.getDaysHigh()));
            dailyLow.setText(quote.getDaysLow());
            dailyLow.setContentDescription(getString(R.string.a11y_daily_low, quote.getDaysLow()));

            yearlyHigh.setText(quote.getYearHigh());
            yearlyHigh.setContentDescription(getString(R.string.a11y_yearly_high, quote.getYearHigh()));
            yearlyLow.setText(quote.getYearLow());
            yearlyLow.setContentDescription(getString(R.string.a11y_daily_low, quote.getYearLow()));

            stockFallIcon.setContentDescription(getString(R.string.a11y_decrease_icon));
            stockRiseIcon.setContentDescription(getString(R.string.a11y_increse_icon));


            //List<HistoricalQuote> historicalQuotes = arguments.getParcelableArrayList(MyStocksActivity.HISTORICAL_DATA_BUNDLE_KEY);
            //retrieveInformationForChart(criteriaTime, quote, historicalQuotes);
        }


        return rootView;
    }

    private void retrieveInformationForChart(String criteriaTime, Quote quote, List<HistoricalQuote> historicalQuotes) {

        switch (criteriaTime) {
            case TabFragment.ONE_MONTH_CRITERIA:
                prepareDataOneWeek(historicalQuotes);
                break;
            case TabFragment.THREE_MONTH_CRITERIA:
                break;
            case TabFragment.SIX_MONTH_CRITERIA:
                break;
            case TabFragment.ONE_YEAR_CRITERIA:
                break;
            default:
                throw new UnsupportedOperationException("Unknown criteria time key");

        }
    }

    private void prepareDataOneWeek(List<HistoricalQuote> historicalQuotes) {
        fillLinechart(historicalQuotes);
    }

    private void prepareDataCallingAPI(String criteriaTime, Date date) {

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
//
//
//            //customize contentDescription and chart information
            chartStock.setContentDescription(getString(R.string.a11y_historical_chart, quote.getName()));
//            // no description text
//            chartStock.setDescription("");
//
//            //Refresh the chart
            chartStock.invalidate();


        }

    }

    private List<String> generateXAxisValues(List<HistoricalQuote> data) {

        List<String> labelsXAxis = null;
        if (null != data && !data.isEmpty()) {
            labelsXAxis = new ArrayList<>();
            for (HistoricalQuote hqIterator : data) {
                //Obtain the date instance of the string value.
                GregorianCalendar stockDate = new GregorianCalendar();
                Date date = new Date();
                date.setTime(Long.valueOf(hqIterator.getDate()));
                stockDate.setTime(date);

                labelsXAxis.add(String.valueOf(stockDate.get(GregorianCalendar.DATE)) + "/" + String.valueOf(stockDate.get(GregorianCalendar.MONTH)));

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
