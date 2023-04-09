package com.sam.coin.service;

import com.sam.coin.model.company.Company;
import com.sam.coin.model.company.exchange.currency.candle.Candle;
import com.sam.coin.model.util.Currency;
import com.sam.coin.repository.CandleBasedRepository;
import com.sam.coin.model.util.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

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

    public Optional<Company> findByCompanyName(String companyName) {
        return candleBasedRepository.findById(companyName);
    }

    public Company addCandleData(String companyName, Currency currency, Exchange exchange, Candle candle) {
        Optional<Company> companyOpt = candleBasedRepository.findByCompanyName(companyName);

        Company company;
        if (companyOpt.isPresent()) {
            company = companyOpt.get();
        } else {
            company = new Company(companyName, new HashMap<>());
        }
        if (company.getTradeExchanges().containsKey(exchange)) { // TODO
            if (company.getTradeExchanges().get(exchange).getTradeCurrencies().containsKey(currency)) {
                company.getTradeExchanges().get(exchange).getTradeCurrencies().get(currency).getCandles().add(candle);
            }
        }
        return candleBasedRepository.save(company);
    }

}
