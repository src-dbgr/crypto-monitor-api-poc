package com.sam.coin.model.company.exchange;

import com.sam.coin.model.company.exchange.currency.TradeCurrency;
import com.sam.coin.model.company.exchange.currency.candle.Candle;
import com.sam.coin.model.util.Exchange;
import com.sam.coin.model.util.Currency;
import lombok.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Value
public class TradeExchange {
    private Map<Currency,TradeCurrency> tradeCurrencies;
    private Exchange exchange;

    public TradeExchange(Exchange exchange, Currency currency, Candle candle){
        this.exchange = exchange;
        this.tradeCurrencies = new HashMap<>();
    }
}
