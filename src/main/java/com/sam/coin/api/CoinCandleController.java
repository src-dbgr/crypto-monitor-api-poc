package com.sam.coin.api;

import com.sam.coin.model.company.exchange.currency.candle.Candle;
import com.sam.coin.model.company.Company;
import com.sam.coin.model.util.Currency;
import com.sam.coin.model.util.Exchange;
import com.sam.coin.service.CoinCandleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/coincandle")
public class CoinCandleController {
    private final CoinCandleService coinCandleService;

    @Autowired
    public CoinCandleController(CoinCandleService coinCandleService) {
        this.coinCandleService = coinCandleService;
    }

    @PostMapping
    public Company create(@RequestBody Company company) {
        return coinCandleService.save(company);
    }

    // Example /api/v1/coincandle/theta/candles?curreny=USD&exchange=BINANCE
    @PutMapping("/{companyName}/candles")
    public Company addCandleData(@PathVariable String companyName, @RequestParam Currency currency, @RequestParam Exchange exchange, @RequestBody Candle candle) {
        return coinCandleService.addCandleData(companyName, currency, exchange, candle);
    }

    @GetMapping("/{companyName}/candles")
    public Optional<Company> getCandleData(@PathVariable String companyName) {
        return coinCandleService.findByCompanyName(companyName);
    }
}
