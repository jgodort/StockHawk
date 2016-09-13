package com.sam_chordas.android.stockhawk.touch_helper;

import android.util.Log;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

/**
 * Created by Javier Godino on 11/09/2016.
 * Email: jgodort.software@gmail.com
 */

public class CustomOnChartValueSelectedListener implements OnChartValueSelectedListener {


    private static final String LOG_TAG = CustomOnChartValueSelectedListener.class.getName();

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.d("Entry selected", e.toString());

    }

    @Override
    public void onNothingSelected() {

        Log.d(LOG_TAG, "Nothing selected Nothing selected.");
    }
}
