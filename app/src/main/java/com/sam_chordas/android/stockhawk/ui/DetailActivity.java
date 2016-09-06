package com.sam_chordas.android.stockhawk.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sam_chordas.android.stockhawk.R;

/**
 * Created by Javier Godino on 31/08/2016.
 * Email: jgodort.software@gmail.com
 */

public class DetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.

            Bundle arguments = new Bundle();
            arguments.putInt(MyStocksActivity.SELECTED_STOCKQUOTE, getIntent().getIntExtra(MyStocksActivity.SELECTED_STOCKQUOTE, 0));


            DetailStockQuoteFragment fragment = new DetailStockQuoteFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.stockquote_detail_container, fragment)
                    .commit();

        }
    }
}
