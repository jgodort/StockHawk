package com.sam_chordas.android.stockhawk.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.Utilities.Constants;
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

    @BindView(R.id.dailyHigh)
    TextView dailyHigh;

    @BindView(R.id.dailyLow)
    TextView dailyLow;

    @BindView(R.id.yearlyLow)
    TextView yearlyLow;

    @BindView(R.id.yearlyHigh)
    TextView yearlyHigh;

    @BindView(R.id.stock_rise_icon)
    ImageView stockRiseIcon;

    @BindView(R.id.stock_fall_icon)
    ImageView stockFallIcon;


    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Bundle arguments = getIntent().getBundleExtra(Constants.SELECTED_STOCKQUOTE_KEY);

        Quote quote = (Quote) arguments.get(Constants.SELECTED_STOCKQUOTE_KEY);

        if (null != quote) {
            quoteName.setText(quote.getName());
            quoteBidPrice.setText(quote.getBid());
            quoteSymbol.setText(quote.getSymbol());
            dailyHigh.setText(quote.getDaysHigh());
            dailyHigh.setContentDescription(getString(R.string.a11y_daily_high, quote.getDaysHigh()));
            dailyLow.setText(quote.getDaysLow());
            dailyLow.setContentDescription(getString(R.string.a11y_daily_low, quote.getDaysLow()));

            yearlyHigh.setText(quote.getYearHigh());
            yearlyHigh.setContentDescription(getString(R.string.a11y_yearly_high, quote.getYearHigh()));
            yearlyLow.setText(quote.getYearLow());
            yearlyLow.setContentDescription(getString(R.string.a11y_daily_low, quote.getYearLow()));

        }

        stockFallIcon.setContentDescription(getString(R.string.a11y_decrease_icon));
        stockRiseIcon.setContentDescription(getString(R.string.a11y_increse_icon));
        List<Fragment> tabsFragments = new ArrayList<>();

        Bundle bundleOneWeek = new Bundle();
        bundleOneWeek.putString(Constants.CRITERIA_TIME_KEY, Constants.ONE_WEEK_CRITERIA);
        bundleOneWeek.putParcelable(Constants.SELECTED_QUOTE, quote);
        bundleOneWeek.putParcelableArrayList(Constants.HISTORICAL_DATA_BUNDLE_KEY,
                arguments.getParcelableArrayList(Constants.HISTORICAL_DATA_BUNDLE_KEY));
        tabsFragments.add(TabFragment.instantiate(this, TabFragment.class.getName(), bundleOneWeek));

        Bundle bundleOneMonth = new Bundle();
        bundleOneMonth.putString(Constants.CRITERIA_TIME_KEY, Constants.ONE_MONTH_CRITERIA);
        bundleOneMonth.putParcelable(Constants.SELECTED_QUOTE, quote);
        tabsFragments.add(TabFragment.instantiate(this, TabFragment.class.getName(), bundleOneMonth));

        Bundle bundleThreeMonth = new Bundle();
        bundleThreeMonth.putString(Constants.CRITERIA_TIME_KEY, Constants.THREE_MONTH_CRITERIA);
        bundleThreeMonth.putParcelable(Constants.SELECTED_QUOTE, quote);
        tabsFragments.add(TabFragment.instantiate(this, TabFragment.class.getName(), bundleThreeMonth));

        Bundle bundleSixMonth = new Bundle();
        bundleSixMonth.putString(Constants.CRITERIA_TIME_KEY, Constants.SIX_MONTH_CRITERIA);
        bundleSixMonth.putParcelable(Constants.SELECTED_QUOTE, quote);
        tabsFragments.add(TabFragment.instantiate(this, TabFragment.class.getName(), bundleSixMonth));


        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), tabsFragments, this);
        viewPager.setAdapter(viewPagerAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setupWithViewPager(viewPager);


    }
}
