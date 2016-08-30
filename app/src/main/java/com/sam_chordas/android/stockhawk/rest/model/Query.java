
package com.sam_chordas.android.stockhawk.rest.model;

import com.google.gson.annotations.SerializedName;

public class Query {

    @SerializedName("count")
    public Integer count;

    @SerializedName("results")
    public Results results;

    public Integer getCount() {
        return count;
    }


    public Results getResults() {
        return results;
    }
}
