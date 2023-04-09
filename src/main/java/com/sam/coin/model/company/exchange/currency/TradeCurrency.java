package com.sam.coin.model.company.exchange.currency;

import com.sam.coin.model.company.exchange.currency.candle.Candle;
import lombok.Value;

import java.util.*;

@Value
public class TradeCurrency {
     private Set<Candle> candles;
     private Currency currency;

    public TradeCurrency(Currency currency) {
        this.currency = currency;
        this.candles = new HashSet<>();
    }
}
