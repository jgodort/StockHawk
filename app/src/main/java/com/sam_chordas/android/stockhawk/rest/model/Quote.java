
package com.sam_chordas.android.stockhawk.rest.model;

import com.google.gson.annotations.SerializedName;

public class Quote {

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
