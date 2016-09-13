package com.sam_chordas.android.stockhawk.touch_helper;

import android.util.Log;
import android.view.MotionEvent;

import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;

/**
 * Created by Javier Godino on 11/09/2016.
 * Email: jgodort.software@gmail.com
 */

public class CustomOnChartGestureListener implements OnChartGestureListener {

    private static final String LOG_TAG = CustomOnChartGestureListener.class.getName();

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.d(LOG_TAG, "START, x: " + me.getX() + ", y: " + me.getY());
    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.d(LOG_TAG, "END, lastGesture: " + lastPerformedGesture);
    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

        Log.d(LOG_TAG, "LongPress");
    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
        Log.d(LOG_TAG, "DoubleTapped");
    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {

        Log.d(LOG_TAG, "SingleTapped");
    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        Log.d(LOG_TAG, "Chart flinged. VeloX: " + velocityX + ", VeloY: " + velocityY);
    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
        Log.d(LOG_TAG, "Scale / Zoom, ScaleX: " + scaleX + ", ScaleY: " + scaleY);
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        Log.d(LOG_TAG, "Translate / Move, dX: " + dX + ", dY: " + dY);
    }
}
