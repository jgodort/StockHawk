package com.sam_chordas.android.stockhawk.rest.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Javier Godino on 05/09/2016.
 * Email: jgodort.software@gmail.com
 */

public class HistoricalQuote {


    public int quoteId;

    @SerializedName("Symbol")
    public String symbol;
    @SerializedName("Date")
    public String date;
    @SerializedName("Open")
    public String open;
    @SerializedName("High")
    public String high;
    @SerializedName("Low")
    public String low;
    @SerializedName("Close")
    public String close;
    @SerializedName("Volume")
    public String volume;
    @SerializedName("Adj_Close")
    public String adjClose;


    public String getSymbol() {
        return symbol;
    }

    public String getDate() {
        return date;
    }

    public String getOpen() {
        return open;
    }

    public String getHigh() {
        return high;
    }

    public String getLow() {
        return low;
    }

    public String getClose() {
        return close;
    }

    public String getVolume() {
        return volume;
    }

    public String getAdjClose() {
        return adjClose;
    }
}
