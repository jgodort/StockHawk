package com.sam_chordas.android.stockhawk.rest.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.sam_chordas.android.stockhawk.Utilities.Utils;

import java.util.Comparator;

/**
 * Created by Javier Godino on 05/09/2016.
 * Email: jgodort.software@gmail.com
 */

public class HistoricalQuote implements Parcelable {


    public int quoteId;

    public int id;
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

    public HistoricalQuote() {

    }

    public HistoricalQuote(Parcel source) {
        super();
        id = source.readInt();
        symbol = source.readString();
        date = source.readString();
        open = source.readString();
        high = source.readString();
        low = source.readString();
        close = source.readString();
        volume = source.readString();
        adjClose = source.readString();
    }


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

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(symbol);
        dest.writeString(date);
        dest.writeString(open);
        dest.writeString(high);
        dest.writeString(low);
        dest.writeString(close);
        dest.writeString(volume);
        dest.writeString(adjClose);


    }

    public static final Parcelable.Creator<HistoricalQuote> CREATOR = new Parcelable.Creator<HistoricalQuote>() {

        @Override
        public HistoricalQuote createFromParcel(Parcel source) {
            return new HistoricalQuote(source);
        }

        @Override
        public HistoricalQuote[] newArray(int size) {
            return new HistoricalQuote[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }


    public static  Comparator<HistoricalQuote> COMPARATOR_DATE = new Comparator<HistoricalQuote>() {
        public int compare(HistoricalQuote left, HistoricalQuote right) {
            Long f1 = Utils.normalizeDateToPersist(left.getDate());
            Long f2 = Utils.normalizeDateToPersist(right.getDate());
            if (f1 == 0 && f2 == 0) {
                return 0;
            } else if (f1 == 0) {
                return 1;
            } else if (f2 == 0) {
                return -1;
            }
            return f2.compareTo(f1);

        }
    };

}
