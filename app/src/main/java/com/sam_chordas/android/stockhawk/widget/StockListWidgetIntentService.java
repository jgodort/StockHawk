package com.sam_chordas.android.stockhawk.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Javier Godino on 03/10/2016.
 * Email: jgodort.software@gmail.com
 */

public class StockListWidgetIntentService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListWidgetDataProvider(this);
    }
}
