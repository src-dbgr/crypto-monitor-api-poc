package com.sam.coin.model.company.exchange.currency.candle;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.lang.Nullable;

import java.util.List;

public class Candle {
    @Nullable
    @JsonProperty("i") // i = index
    private Long index;
    // expects the data in order:
    // timestamp, open, low, high, close, volume
    @JsonProperty("v") // v = value
    private List<Number> candleValues;

}
