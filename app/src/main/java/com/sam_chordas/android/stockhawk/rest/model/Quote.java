
package com.sam_chordas.android.stockhawk.rest.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Quote implements Parcelable {


    public int id;

    @SerializedName("Ask")
    public String ask;

    @SerializedName("AverageDailyVolume")
    public String averageDailyVolume;

    @SerializedName("Bid")
    public String bid;

    @SerializedName("BookValue")
    public String bookValue;

    @SerializedName("Change_PercentChange")
    public String changePercentChange;

    @SerializedName("Change")
    public String change;

    @SerializedName("Currency")
    public String currency;


    @SerializedName("LastTradeDate")
    public String lastTradeDate;

    @SerializedName("EarningsShare")
    public String earningsShare;

    @SerializedName("DaysLow")
    public String daysLow;

    @SerializedName("DaysHigh")
    public String daysHigh;

    @SerializedName("YearLow")
    public String yearLow;

    @SerializedName("YearHigh")
    public String yearHigh;

    @SerializedName("MarketCapitalization")
    public String marketCapitalization;

    @SerializedName("LastTradeWithTime")
    public String lastTradeWithTime;

    @SerializedName("LastTradePriceOnly")
    public String lastTradePriceOnly;

    @SerializedName("DaysRange")
    public String daysRange;

    @SerializedName("Name")
    public String name;
    @SerializedName("Open")
    public String open;
    @SerializedName("PreviousClose")
    public String previousClose;
    @SerializedName("ChangeinPercent")
    public String changeinPercent;
    @SerializedName("PriceSales")
    public String priceSales;
    @SerializedName("PriceBook")
    public String priceBook;

    @SerializedName("Symbol")
    public String symbol;
    @SerializedName("LastTradeTime")
    public String lastTradeTime;
    @SerializedName("Volume")
    public String volume;
    @SerializedName("YearRange")
    public String yearRange;
    @SerializedName("StockExchange")
    public String stockExchange;
    @SerializedName("PercentChange")
    public String percentChange;

    public Quote(int _id, String _symbol, String _name, String _bid, String _percentChange, String _change) {
        id = _id;
        symbol = _symbol;
        name = _name;
        bid = _bid;
        percentChange = _percentChange;
        change = _change;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(ask);
        dest.writeString(averageDailyVolume);
        dest.writeString(bid);
        dest.writeString(bookValue);
        dest.writeString(change);
        dest.writeString(changeinPercent);
        dest.writeString(changePercentChange);
        dest.writeString(currency);
        dest.writeString(daysHigh);
        dest.writeString(daysLow);
        dest.writeString(daysRange);
        dest.writeString(earningsShare);
        dest.writeString(lastTradeDate);
        dest.writeString(lastTradePriceOnly);
        dest.writeString(lastTradeTime);
        dest.writeString(lastTradeWithTime);
        dest.writeString(marketCapitalization);
        dest.writeString(name);
        dest.writeString(open);
        dest.writeString(percentChange);
        dest.writeString(previousClose);
        dest.writeString(priceBook);
        dest.writeString(priceSales);
        dest.writeString(stockExchange);
        dest.writeString(symbol);
        dest.writeString(volume);
        dest.writeString(yearHigh);
        dest.writeString(yearLow);
        dest.writeString(yearRange);

    }

    public static final Parcelable.Creator<Quote> CREATOR = new Parcelable.Creator<Quote>() {

        @Override
        public Quote createFromParcel(Parcel source) {
            return new Quote(source);
        }

        @Override
        public Quote[] newArray(int size) {
            return new Quote[size];
        }
    };

    private Quote(Parcel in) {
        id = in.readInt();
        ask = in.readString();
        averageDailyVolume = in.readString();
        bid = in.readString();
        bookValue = in.readString();
        change = in.readString();
        changeinPercent = in.readString();
        changePercentChange = in.readString();
        currency = in.readString();
        daysHigh = in.readString();
        daysLow = in.readString();
        daysRange = in.readString();
        earningsShare = in.readString();
        lastTradeDate = in.readString();
        lastTradePriceOnly = in.readString();
        lastTradeTime = in.readString();
        lastTradeWithTime = in.readString();
        marketCapitalization = in.readString();
        name = in.readString();
        open = in.readString();
        percentChange = in.readString();
        previousClose = in.readString();
        priceBook = in.readString();
        priceSales = in.readString();
        stockExchange = in.readString();
        symbol = in.readString();
        volume = in.readString();
        yearHigh = in.readString();
        yearLow = in.readString();
        yearRange = in.readString();
    }


    public String getAsk() {
        return ask;
    }

    public String getAverageDailyVolume() {
        return averageDailyVolume;
    }

    public String getBid() {
        return bid;
    }

    public String getBookValue() {
        return bookValue;
    }

    public String getChangePercentChange() {
        return changePercentChange;
    }

    public String getChange() {
        return change;
    }

    public String getCurrency() {
        return currency;
    }

    public String getLastTradeDate() {
        return lastTradeDate;
    }

    public String getEarningsShare() {
        return earningsShare;
    }


    public String getDaysLow() {
        return daysLow;
    }

    public String getDaysHigh() {
        return daysHigh;
    }

    public String getYearLow() {
        return yearLow;
    }

    public String getYearHigh() {
        return yearHigh;
    }

    public String getMarketCapitalization() {
        return marketCapitalization;
    }

    public String getLastTradeWithTime() {
        return lastTradeWithTime;
    }

    public String getLastTradePriceOnly() {
        return lastTradePriceOnly;
    }

    public String getDaysRange() {
        return daysRange;
    }

    public String getName() {
        return name;
    }

    public String getOpen() {
        return open;
    }

    public String getPreviousClose() {
        return previousClose;
    }

    public String getChangeinPercent() {
        return changeinPercent;
    }

    public String getPriceSales() {
        return priceSales;
    }

    public String getPriceBook() {
        return priceBook;
    }


    public String getSymbol() {
        return symbol;
    }


    public String getLastTradeTime() {
        return lastTradeTime;
    }


    public String getVolume() {
        return volume;
    }

    public String getYearRange() {
        return yearRange;
    }

    public String getStockExchange() {
        return stockExchange;
    }

    public String getPercentChange() {
        return percentChange;
    }


}
