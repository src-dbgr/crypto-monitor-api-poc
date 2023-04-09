package com.sam.coin.service;

import com.sam.coin.api.ApiResponse;
import com.sam.coin.model.company.Company;
import com.sam.coin.model.company.exchange.TradeExchange;
import com.sam.coin.model.company.exchange.currency.TradeCurrency;
import com.sam.coin.model.company.exchange.currency.candle.Candle;
import com.sam.coin.model.util.CompanyName;
import com.sam.coin.model.util.Currency;
import com.sam.coin.model.util.Exchange;
import com.sam.coin.repository.CandleBasedRepository;
import com.sam.coin.service.exceptions.DataValidationException;
import com.sam.coin.service.exceptions.DatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CoinCandleService {

    private final CandleBasedRepository candleBasedRepository;

    @Autowired
    public CoinCandleService(CandleBasedRepository candleBasedRepository) {
        this.candleBasedRepository = candleBasedRepository;
    }

    public Company save(Company company) {
        return candleBasedRepository.save(company);
    }

    public Optional<Company> findByCompanyName(CompanyName companyName) {
        return candleBasedRepository.findByCompanyName(companyName);
    }

    public Company addCandlesetData(CompanyName companyName, Currency currency, Exchange exchange, Set<Candle> candles) {
        try {
            if (!isValidCandleSet(candles)) {
                throw new DataValidationException("Invalid candle data");
            }
            Optional<Company> companyOptional = candleBasedRepository.findByCompanyName(companyName);
            Company company = companyOptional.orElseGet(() -> createNewCompanyWithCandleSet(companyName, currency, exchange, candles));
            Map<Exchange, TradeExchange> tradeExchangesAffected = company.getTradeExchanges();
            if (tradeExchangesAffected.containsKey(exchange)) {
                Map<Currency, TradeCurrency> tradeCurrenciesAffected = tradeExchangesAffected.get(exchange).getTradeCurrencies();
                if (tradeCurrenciesAffected.containsKey(currency)) {
                    tradeCurrenciesAffected.get(currency).getCandles().addAll(candles); // adds all candles here if exchange and currency already exists
                } else {
                    tradeCurrenciesAffected.put(currency, new TradeCurrency(currency, candles)); // exchange exists but not the currency, add the currency and add all candles info
                }
            } else {
                tradeExchangesAffected.put(exchange, new TradeExchange(exchange, createNewTradeCurrenciesCandleSet(currency, candles))); // company exists but the exchange does not yet exist, create a new one and add it to the list with the affected currency and all candle info
            }
            return candleBasedRepository.save(company);
        } catch (DataValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Failed to save candle data", e);
        }
    }

    public Company addCandleData(CompanyName companyName, Currency currency, Exchange exchange, Candle candle) {
        try {
            if (!isValidCandle(candle)) {
                throw new DataValidationException("Invalid candle data");
            }
            Optional<Company> companyOptional = candleBasedRepository.findByCompanyName(companyName);
            // company does not yet exist, create a new one with the affected exchange and currency and add the candle info
            Company company = companyOptional.orElseGet(() -> createNewCompanyWithCandle(companyName, currency, exchange, candle));
            Map<Exchange, TradeExchange> tradeExchangesAffected = company.getTradeExchanges();
            if (tradeExchangesAffected.containsKey(exchange)) {
                Map<Currency, TradeCurrency> tradeCurrenciesAffected = tradeExchangesAffected.get(exchange).getTradeCurrencies();
                if (tradeCurrenciesAffected.containsKey(currency)) {
                    tradeCurrenciesAffected.get(currency).getCandles().add(candle); // adds Candle here if exchange and currency already exists
                } else {
                    tradeCurrenciesAffected.put(currency, new TradeCurrency(currency, createNewCandleSet(candle))); // exchange exists but not the currency, add the currency and add the candle info
                }
            } else {
                tradeExchangesAffected.put(exchange, new TradeExchange(exchange, createNewTradeCurrencies(currency, candle))); // company exists but the exchange does not yet exist, create a new one and add it to the list with the affected currency and candle info
            }
            return candleBasedRepository.save(company);
        } catch (DataValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Failed to save candle data", e);
        }
    }

    private boolean isValidCandleSet(Set<Candle> candles) {
        if (candles == null || candles.isEmpty()) {
            return false;
        }
        return candles.stream().allMatch(this::isValidCandle);
    }

    private boolean isValidCandle(Candle candle) {
        return candle != null && candle.getCandleValues().size() == 5;
    }


    private static Company createNewCompanyWithCandle(CompanyName companyName, Currency currency, Exchange exchange, Candle candle) {
        return new Company(companyName, createNewTradeExchanges(currency, exchange, candle));
    }

    private static Map<Exchange, TradeExchange> createNewTradeExchanges(Currency currency, Exchange exchange, Candle candle) {
        Map<Exchange, TradeExchange> tradeExchanges = new HashMap<>();
        tradeExchanges.put(exchange, new TradeExchange(exchange, createNewTradeCurrencies(currency, candle)));
        return tradeExchanges;
    }

    private static Map<Currency, TradeCurrency> createNewTradeCurrencies(Currency currency, Candle candle) {
        Map<Currency, TradeCurrency> tradeCurrencies = new HashMap<>();
        tradeCurrencies.put(currency, new TradeCurrency(currency, createNewCandleSet(candle)));
        return tradeCurrencies;
    }

    private static Set<Candle> createNewCandleSet(Candle candle) {
        Set<Candle> candleSet = new HashSet<>();
        candleSet.add(candle);
        return candleSet;
    }

    private static Company createNewCompanyWithCandleSet(CompanyName companyName, Currency currency, Exchange exchange, Set<Candle> candles) {
        return new Company(companyName, createNewTradeExchangesCandleSet(currency, exchange, candles));
    }

    private static Map<Exchange, TradeExchange> createNewTradeExchangesCandleSet(Currency currency, Exchange exchange, Set<Candle> candles) {
        Map<Exchange, TradeExchange> tradeExchanges = new HashMap<>();
        tradeExchanges.put(exchange, new TradeExchange(exchange, createNewTradeCurrenciesCandleSet(currency, candles)));
        return tradeExchanges;
    }

    private static Map<Currency, TradeCurrency> createNewTradeCurrenciesCandleSet(Currency currency, Set<Candle> candles) {
        Map<Currency, TradeCurrency> tradeCurrencies = new HashMap<>();
        tradeCurrencies.put(currency, new TradeCurrency(currency, candles));
        return tradeCurrencies;
    }

}
