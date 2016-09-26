package com.sam_chordas.android.stockhawk.ui;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.rest.ServiceGenerator;
import com.sam_chordas.android.stockhawk.rest.Utils;
import com.sam_chordas.android.stockhawk.rest.client.YahooFinanceAPIClient;
import com.sam_chordas.android.stockhawk.rest.model.HistoricalQuote;
import com.sam_chordas.android.stockhawk.rest.model.HistoricalStockQuoteModel;
import com.sam_chordas.android.stockhawk.rest.model.Quote;
import com.sam_chordas.android.stockhawk.rest.request.RequestCallback;
import com.sam_chordas.android.stockhawk.rest.request.listener.RequestListener;
import com.sam_chordas.android.stockhawk.touch_helper.CustomOnChartGestureListener;
import com.sam_chordas.android.stockhawk.touch_helper.CustomOnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

/**
 * Created by Javier Godino on 11/09/2016.
 * Email: jgodort.software@gmail.com
 */

public class TabFragment extends Fragment {

    private static final String LOG_TAG = TabFragment.class.getName();

    public static final String CRITERIA_TIME_KEY = "CTK";

    public static final String SELECTED_QUOTE = "SQ_ID";

    public static final String ONE_WEEK_CRITERIA = "OWC";
    public static final String ONE_MONTH_CRITERIA = "OMC";
    public static final String THREE_MONTH_CRITERIA = "TMC";
    public static final String SIX_MONTH_CRITERIA = "SMC";


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

    @BindView(R.id.volumeBarChart)
    BarChart volumeChart;

    @BindView(R.id.stock_rise_icon)
    ImageView stockRiseIcon;

    @BindView(R.id.stock_fall_icon)
    ImageView stockFallIcon;


    List<HistoricalQuote> historicalQuotes;

    private ProgressDialog mprogressDialog;

    private Quote quote;
    String criteriaTime;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.quote_detail, container, false);
        ButterKnife.bind(this, rootView);

        mprogressDialog = new ProgressDialog(getContext());

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

            if (ONE_WEEK_CRITERIA.equals(criteriaTime) &&
                    arguments.containsKey(MyStocksActivity.HISTORICAL_DATA_BUNDLE_KEY)) {
                historicalQuotes = arguments.getParcelableArrayList(MyStocksActivity.HISTORICAL_DATA_BUNDLE_KEY);
            }


            RequestListener listener = new RequestListener() {
                @Override
                public void onSuccess(Object response) {
                    historicalQuotes = ((HistoricalStockQuoteModel) response).getQuery().getResults().getQuote();
                    fillLinechart(historicalQuotes);
                    fillBarchart(historicalQuotes);
                    mprogressDialog.dismiss();
                }

                @Override
                public void onFailure(Object error) {
                    Log.e(LOG_TAG, "The response of the API return an error.");
                    mprogressDialog.dismiss();
                }
            };

            retrieveInformationForChart(criteriaTime, quote, historicalQuotes, listener);
        }


        return rootView;
    }

    private void retrieveInformationForChart(String criteriaTime, Quote quote, List<HistoricalQuote> historicalQuotes, RequestListener listener) {

        mprogressDialog.show();
        switch (criteriaTime) {
            case TabFragment.ONE_WEEK_CRITERIA:
                prepareDataOneWeek(historicalQuotes);
                break;
            case TabFragment.ONE_MONTH_CRITERIA:
                prepareDataCallingAPI(ONE_MONTH_CRITERIA, listener);
                break;
            case TabFragment.THREE_MONTH_CRITERIA:
                prepareDataCallingAPI(THREE_MONTH_CRITERIA, listener);
                break;
            case TabFragment.SIX_MONTH_CRITERIA:
                prepareDataCallingAPI(SIX_MONTH_CRITERIA, listener);
                break;
            default:
                throw new UnsupportedOperationException("Unknown criteria time key");

        }
        mprogressDialog.dismiss();
    }

    private void prepareDataOneWeek(List<HistoricalQuote> historicalQuotes) {
        fillLinechart(historicalQuotes);
        fillBarchart(historicalQuotes);
    }

    private void prepareDataCallingAPI(String criteriaTime, RequestListener<HistoricalStockQuoteModel> listener) {
        if (historicalQuotes == null || historicalQuotes.isEmpty()) {
            //RequestCallback to retrieve the data from the response.
            RequestCallback<HistoricalStockQuoteModel> requestCallback = new RequestCallback<>(listener);
            YahooFinanceAPIClient client = ServiceGenerator.createService(YahooFinanceAPIClient.class);

            Call<HistoricalStockQuoteModel> call = client.getHistorialStockData(Utils.generateHistoricalYQLQuery(quote.getSymbol(), criteriaTime));

            call.enqueue(requestCallback);
        } else {
            fillLinechart(historicalQuotes);
            fillBarchart(historicalQuotes);
        }

    }


    private void fillBarchart(List<HistoricalQuote> processedData) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        float contador = 0;
        for (HistoricalQuote historicalQuote : processedData) {
            entries.add(new BarEntry(contador++,
                    Float.valueOf(historicalQuote.getVolume())));

            labels.add(generateLabelDate(historicalQuote));
        }

        BarDataSet dataset = new BarDataSet(entries, "Volume value Chart");
        dataset.setColor(Color.BLUE);
        List<IBarDataSet> listBarDatasets = new ArrayList<>();
        listBarDatasets.add(dataset);
        BarData data = new BarData(listBarDatasets);
        volumeChart.setData(data); // set the data and list of lables into chart<br />
        // volumeChart.getXAxis().setValueFormatter(new XValueFormatter(labels));
        volumeChart.animateXY(6000, 6000);
        volumeChart.invalidate();


    }


    private void fillLinechart(List<HistoricalQuote> processedData) {

        //Retrive the X/Y Axis dataset values.
        List<String> xAxis = generateXAxisValues(processedData);
        List<List<Entry>> yAxis = generateYAxisValues(processedData);

        if (null != xAxis && null != yAxis) {
            LineDataSet datasetHigh;
            LineDataSet datasetLow;

            //Configuring High values Dataset
            datasetHigh = new LineDataSet(yAxis.get(0), "High Value");
            datasetHigh.setColor(Color.GREEN);
            datasetHigh.setCircleColor(Color.BLUE);
            datasetHigh.setFillColor(Color.GREEN);
            datasetHigh.setDrawFilled(true);
            datasetHigh.setValueTextColor(Color.WHITE);
            datasetHigh.setValueTextSize(12);

            //Configuring Low Values Dataset.
            datasetLow = new LineDataSet(yAxis.get(1), "Low Value");
            datasetLow.setColor(Color.RED);
            datasetLow.setCircleColor(Color.BLUE);
            datasetLow.setFillColor(Color.RED);
            datasetLow.setDrawFilled(true);
            datasetLow.setValueTextColor(Color.WHITE);
            datasetLow.setValueTextSize(12);


            //Create a list to group datasets.
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(datasetHigh);
            dataSets.add(datasetLow);

            //Create a data object with the datasets
            LineData data = new LineData(dataSets);
            chartStock.setData(data);

            //Configure Legend
            Legend legend = chartStock.getLegend();
            legend.setTextColor(Color.WHITE);
            legend.setTextSize(15);

            //Configure axys
            chartStock.getXAxis().setTextColor(Color.WHITE);
            chartStock.getAxisLeft().setTextColor(Color.WHITE);
            chartStock.getAxisRight().setTextColor(Color.WHITE);


            chartStock.animateXY(3000, 3000);


            chartStock.getXAxis().setValueFormatter(new XValueFormatter(xAxis));

            //customize contentDescription and chart information
            chartStock.setContentDescription(getString(R.string.a11y_historical_chart, quote.getName()));

            //Refresh the chart
            chartStock.invalidate();


        }

    }

    private List<String> generateXAxisValues(List<HistoricalQuote> data) {

        LinkedList<String> labelsXAxis = null;
        if (null != data && !data.isEmpty()) {
            labelsXAxis = new LinkedList<>();
            for (HistoricalQuote hqIterator : data) {
                labelsXAxis.add(generateLabelDate(hqIterator));
            }
        }


        return labelsXAxis;
    }

    @NonNull
    private String generateLabelDate(HistoricalQuote hqIterator) {
        // normalize the start date to the beginning of the (UTC) day
        Time time = new Time();
        long normalizedDate = 0;
        if (hqIterator.getDate().contains("-")) {
            normalizedDate = Utils.normalizeDateToPersist(hqIterator.getDate());
        } else {
            normalizedDate = Long.valueOf(hqIterator.getDate());
        }
        time.set(normalizedDate);
        int julianDay = Time.getJulianDay(normalizedDate, time.gmtoff);
        //Obtain the date instance of the string value.
        Date date = new Date(time.setJulianDay(julianDay));
        return String.valueOf(date.getDate()) + "/" + String.valueOf(date.getMonth() + 1);
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

    class XValueFormatter implements com.github.mikephil.charting.formatter.AxisValueFormatter {

        private final String LOG_TAG = XValueFormatter.class.getName();

        private Map<Integer, String> positions = new HashMap<>();
        private List<String> xAxysLabels;

        public XValueFormatter(List<String> xLabelValues) {
            xAxysLabels = xLabelValues;
        }


        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            if (!positions.containsKey((int) value)) {
                String text = xAxysLabels.get((int) value);
                positions.put((int) value, text);
                //  Log.d(LOG_TAG, "returning value: " + text);
                return text;
            } else {
                //   Log.d(LOG_TAG, "returning cached value: " + positions.get((int) value));
                return positions.get((int) value);
            }

        }

        @Override
        public int getDecimalDigits() {
            return 0;
        }
    }
}
