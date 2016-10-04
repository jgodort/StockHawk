package com.sam_chordas.android.stockhawk.Utilities;

/**
 * Created by Javier Godino on 28/09/2016.
 * Email: jgodort.software@gmail.com
 */

public class Constants {

    public static final String CRITERIA_TIME_KEY = "CTK";

    public static final String SELECTED_QUOTE = "SQ_ID";

    public static final String ONE_WEEK_CRITERIA = "OWC";
    public static final String ONE_MONTH_CRITERIA = "OMC";
    public static final String THREE_MONTH_CRITERIA = "TMC";
    public static final String SIX_MONTH_CRITERIA = "SMC";


    public static final String SELECTED_STOCKQUOTE_KEY = "S_STQ";
    public static final String HISTORICAL_DATA_BUNDLE_KEY = "S_HD";

    public static final String TAG_KEY = "tag";
    public static final String SYMBOL_KEY = "symbol";

    /**
     * We need this quote to handle the stranglely behaviour of the API
     * when the query only have a Quote.
     */
    public static final String FIX_ADD_CALL_STOCK_QUOTE = "\"YHOO\"";
}
