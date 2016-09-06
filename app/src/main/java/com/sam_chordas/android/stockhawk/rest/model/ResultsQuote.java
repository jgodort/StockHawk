
package com.sam_chordas.android.stockhawk.rest.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ResultsQuote {

    @SerializedName("quote")
    public List<Quote> quote = new ArrayList<Quote>();

    public List<Quote> getQuote() {
        return quote;
    }
}
