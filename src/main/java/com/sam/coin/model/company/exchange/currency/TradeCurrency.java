package com.sam.coin.model.company.exchange.currency;

import com.sam.coin.model.company.exchange.currency.candle.Candle;
import com.sam.coin.model.util.Currency;
import lombok.*;
import org.springframework.data.annotation.PersistenceConstructor;

import java.util.HashSet;
import java.util.Set;
@NoArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class TradeCurrency {
     private Set<Candle> candles;
     private Currency currency;

    public TradeCurrency(Currency currency) {
        this.currency = currency;
        this.candles = new HashSet<>();
    }

    public TradeCurrency(Currency currency, Set<Candle> candles) {
        this.currency = currency;
        this.candles = candles;
    }
}
