
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

    @SerializedName("EPSEstimateCurrentYear")
    public String ePSEstimateCurrentYear;
    @SerializedName("EPSEstimateNextYear")
    public String ePSEstimateNextYear;
    @SerializedName("EPSEstimateNextQuarter")
    public String ePSEstimateNextQuarter;
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

    @SerializedName("EBITDA")
    public String eBITDA;
    @SerializedName("ChangeFromYearLow")
    public String changeFromYearLow;


    @SerializedName("PercebtChangeFromYearHigh")
    public String percebtChangeFromYearHigh;
    @SerializedName("LastTradeWithTime")
    public String lastTradeWithTime;
    @SerializedName("LastTradePriceOnly")
    public String lastTradePriceOnly;

    @SerializedName("DaysRange")
    public String daysRange;

    @SerializedName("FiftydayMovingAverage")
    public String fiftydayMovingAverage;
    @SerializedName("TwoHundreddayMovingAverage")
    public String twoHundreddayMovingAverage;
    @SerializedName("ChangeFromTwoHundreddayMovingAverage")
    public String changeFromTwoHundreddayMovingAverage;
    @SerializedName("PercentChangeFromTwoHundreddayMovingAverage")
    public String percentChangeFromTwoHundreddayMovingAverage;
    @SerializedName("ChangeFromFiftydayMovingAverage")
    public String changeFromFiftydayMovingAverage;
    @SerializedName("PercentChangeFromFiftydayMovingAverage")
    public String percentChangeFromFiftydayMovingAverage;
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

    @SerializedName("PERatio")
    public Object pERatio;


    @SerializedName("PEGRatio")
    public String pEGRatio;
    @SerializedName("PriceEPSEstimateCurrentYear")
    public String priceEPSEstimateCurrentYear;
    @SerializedName("PriceEPSEstimateNextYear")
    public String priceEPSEstimateNextYear;
    @SerializedName("Symbol")
    public String symbol;

    @SerializedName("ShortRatio")
    public String shortRatio;
    @SerializedName("LastTradeTime")
    public String lastTradeTime;
    @SerializedName("TickerTrend")
    public Object tickerTrend;
    @SerializedName("OneyrTargetPrice")
    public String oneyrTargetPrice;
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

    public String getePSEstimateCurrentYear() {
        return ePSEstimateCurrentYear;
    }

    public String getePSEstimateNextYear() {
        return ePSEstimateNextYear;
    }

    public String getePSEstimateNextQuarter() {
        return ePSEstimateNextQuarter;
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

    public String geteBITDA() {
        return eBITDA;
    }

    public String getChangeFromYearLow() {
        return changeFromYearLow;
    }

    public String getPercebtChangeFromYearHigh() {
        return percebtChangeFromYearHigh;
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

    public String getFiftydayMovingAverage() {
        return fiftydayMovingAverage;
    }

    public String getTwoHundreddayMovingAverage() {
        return twoHundreddayMovingAverage;
    }

    public String getChangeFromTwoHundreddayMovingAverage() {
        return changeFromTwoHundreddayMovingAverage;
    }

    public String getPercentChangeFromTwoHundreddayMovingAverage() {
        return percentChangeFromTwoHundreddayMovingAverage;
    }

    public String getChangeFromFiftydayMovingAverage() {
        return changeFromFiftydayMovingAverage;
    }

    public String getPercentChangeFromFiftydayMovingAverage() {
        return percentChangeFromFiftydayMovingAverage;
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

    public Object getpERatio() {
        return pERatio;
    }

    public String getpEGRatio() {
        return pEGRatio;
    }

    public String getPriceEPSEstimateCurrentYear() {
        return priceEPSEstimateCurrentYear;
    }

    public String getPriceEPSEstimateNextYear() {
        return priceEPSEstimateNextYear;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getShortRatio() {
        return shortRatio;
    }

    public String getLastTradeTime() {
        return lastTradeTime;
    }

    public Object getTickerTrend() {
        return tickerTrend;
    }

    public String getOneyrTargetPrice() {
        return oneyrTargetPrice;
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
