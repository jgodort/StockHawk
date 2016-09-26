package com.sam_chordas.android.stockhawk.ui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.sam_chordas.android.stockhawk.R;

import java.util.List;

/**
 * Created by Javier Godino on 11/09/2016.
 * Email: jgodort.software@gmail.com
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {


    public static final int ONE_WEEK_KEY = 0;
    public static final int ONE_MONTH_KEY = 1;
    public static final int THREE_MONTH_KEY = 2;
    public static final int SIX_MONTH_KEY = 3;


    private List<Fragment> mFragments;
    private Context mContext;

    public ViewPagerAdapter(FragmentManager fragmentManager, List<Fragment> fragments, Context context) {
        super(fragmentManager);
        mFragments = fragments;
        mContext = context;
    }


    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        if (mFragments == null) {
            return 0;
        } else {
            return mFragments.size();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        CharSequence code;
        switch (position) {

            case ONE_WEEK_KEY:
                code = mContext.getString(R.string.tab_label_one_week);
                break;
            case ONE_MONTH_KEY:
                code = mContext.getString(R.string.tab_label_one_month);
                break;
            case THREE_MONTH_KEY:
                code = mContext.getString(R.string.tab_label_three_months);
                break;
            case SIX_MONTH_KEY:
                code = mContext.getString(R.string.tab_label_six_months);
                break;

            default:
                throw new IllegalArgumentException("Requesting a non existing fragment");
        }
        return code;
    }

}
