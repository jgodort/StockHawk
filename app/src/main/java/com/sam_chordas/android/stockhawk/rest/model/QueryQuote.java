
package com.sam_chordas.android.stockhawk.rest.model;

import com.google.gson.annotations.SerializedName;

public class QueryQuote {

    @SerializedName("count")
    public Integer count;

    @SerializedName("results")
    public ResultsQuote results;

    public Integer getCount() {
        return count;
    }


    public ResultsQuote getResults() {
        return results;
    }
}
