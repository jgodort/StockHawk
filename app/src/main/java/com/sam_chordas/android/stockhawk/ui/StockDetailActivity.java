package com.sam_chordas.android.stockhawk.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.sam_chordas.android.stockhawk.R;

/**
 * Created by Javier Godino on 30/08/2016.
 * Email: jgodort.software@gmail.com
 */

public class StockDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        DetailStockQuoteFragment fragment=new DetailStockQuoteFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.stockquote_detail_container,fragment).commit();

    }
}
