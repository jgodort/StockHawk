package com.sam_chordas.android.stockhawk.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.rest.model.Quote;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Javier Godino on 31/08/2016.
 * Email: jgodort.software@gmail.com
 */

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.quoteName)
    TextView quoteName;

    @BindView(R.id.bid_price)
    TextView quoteBidPrice;

    @BindView(R.id.quoteSymbol)
    TextView quoteSymbol;

    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Bundle arguments = getIntent().getBundleExtra(MyStocksActivity.SELECTED_STOCKQUOTE);

        Quote quote = (Quote) arguments.get(MyStocksActivity.SELECTED_STOCKQUOTE);

        quoteName.setText(quote.getName());
        quoteBidPrice.setText(quote.getBid());
        quoteSymbol.setText(quote.getSymbol());


        List<Fragment> tabsFragments = new ArrayList<>();

        Bundle bundleOneMonth = new Bundle();
        bundleOneMonth.putString(TabFragment.CRITERIA_TIME_KEY, TabFragment.ONE_MONTH_CRITERIA);
        bundleOneMonth.putParcelable(TabFragment.SELECTED_QUOTE, quote);
        bundleOneMonth.putParcelableArrayList(MyStocksActivity.HISTORICAL_DATA_BUNDLE_KEY,
                arguments.getParcelableArrayList(MyStocksActivity.HISTORICAL_DATA_BUNDLE_KEY));
        tabsFragments.add(TabFragment.instantiate(this, TabFragment.class.getName(), bundleOneMonth));

        Bundle bundleThreeMonth = new Bundle();
        bundleThreeMonth.putString(TabFragment.CRITERIA_TIME_KEY, TabFragment.THREE_MONTH_CRITERIA);
        bundleThreeMonth.putParcelable(TabFragment.SELECTED_QUOTE, quote);
        tabsFragments.add(TabFragment.instantiate(this, TabFragment.class.getName(), bundleThreeMonth));

        Bundle bundleSixMonth = new Bundle();
        bundleSixMonth.putString(TabFragment.CRITERIA_TIME_KEY, TabFragment.SIX_MONTH_CRITERIA);
        bundleSixMonth.putParcelable(TabFragment.SELECTED_QUOTE, quote);
        tabsFragments.add(TabFragment.instantiate(this, TabFragment.class.getName(), bundleSixMonth));

        Bundle bundleOneYear = new Bundle();
        bundleOneYear.putString(TabFragment.CRITERIA_TIME_KEY, TabFragment.ONE_YEAR_CRITERIA);
        bundleOneYear.putParcelable(TabFragment.SELECTED_QUOTE, quote);
        tabsFragments.add(TabFragment.instantiate(this, TabFragment.class.getName(), bundleOneYear));


        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), tabsFragments, this);
        viewPager.setAdapter(viewPagerAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setupWithViewPager(viewPager);



    }
}
