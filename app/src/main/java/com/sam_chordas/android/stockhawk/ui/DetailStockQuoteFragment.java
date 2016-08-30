package com.sam_chordas.android.stockhawk.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.text.BidiFormatter;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.sam_chordas.android.stockhawk.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Javier Godino on 30/08/2016.
 * Email: jgodort.software@gmail.com
 */

public class DetailStockQuoteFragment extends Fragment {

    private static final String LOG_TAG = DetailStockQuoteFragment.class.getSimpleName();


    @BindView(R.id.dailyHigh)
    TextView dailyHigh;

    @BindView(R.id.dailyLow)
    TextView dailyLow;

    @BindView(R.id.yearlyHigh)
    TextView yearlyHigh;

    @BindView(R.id.yearlyLow)
    TextView yearlyLow;

    @BindView(R.id.stockQuoteChart)
    LineChart chartStock;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

    }
}
