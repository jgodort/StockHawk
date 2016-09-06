
package com.sam_chordas.android.stockhawk.rest.model;

import com.google.gson.annotations.SerializedName;

public class HistoricalStockQuoteModel {

    @SerializedName("query")
    public HistoricalQueryQuote query;

    public HistoricalQueryQuote getQuery() {
        return query;
    }
}
