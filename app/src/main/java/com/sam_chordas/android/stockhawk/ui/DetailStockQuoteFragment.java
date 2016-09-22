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









}
