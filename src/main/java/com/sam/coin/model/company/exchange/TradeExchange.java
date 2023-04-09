package com.sam.coin.model.company.exchange;

import com.sam.coin.model.company.exchange.currency.TradeCurrency;
import com.sam.coin.model.company.exchange.currency.candle.Candle;
import com.sam.coin.model.util.Currency;
import com.sam.coin.model.util.Exchange;
import lombok.*;
import org.springframework.data.annotation.PersistenceConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@NoArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class TradeExchange {
    private Map<Currency,TradeCurrency> tradeCurrencies;
    private Exchange exchange;

    public TradeExchange(Exchange exchange){
        this.exchange = exchange;
        this.tradeCurrencies = new HashMap<>();
    }
    public TradeExchange(Exchange exchange, Map<Currency,TradeCurrency> tradeCurrencies) {
        this.exchange = exchange;
        this.tradeCurrencies = tradeCurrencies;
    }

}
