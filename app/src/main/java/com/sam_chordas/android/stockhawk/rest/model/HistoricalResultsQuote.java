
package com.sam_chordas.android.stockhawk.rest.model;

        import com.google.gson.annotations.SerializedName;

        import java.util.ArrayList;
        import java.util.List;

public class HistoricalResultsQuote {

    @SerializedName("quote")
    public List<HistoricalQuote> quote = new ArrayList<HistoricalQuote>();


    public List<HistoricalQuote> getQuote() {
        return quote;
    }
}
