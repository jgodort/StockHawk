
package com.sam_chordas.android.stockhawk.rest.model;

import com.google.gson.annotations.SerializedName;

public class HistoricalQueryQuote {

    @SerializedName("count")
    public Integer count;

    @SerializedName("results")
    public HistoricalResultsQuote results;


    public Integer getCount() {
        return count;
    }

    public HistoricalResultsQuote getResults() {
        return results;
    }
}
