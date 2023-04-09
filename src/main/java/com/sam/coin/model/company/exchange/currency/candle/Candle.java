package com.sam.coin.model.company.exchange.currency.candle;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@EqualsAndHashCode
@ToString
public class Candle {
    @JsonProperty("i") // i = index
    private Long index;
    // expects the data in order:
    // timestamp, open, low, high, close, volume
    @JsonProperty("v") // v = value
    private List<Number> candleValues;

}
