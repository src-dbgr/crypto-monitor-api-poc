package com.sam.coin.api;

import com.sam.coin.model.company.Company;
import com.sam.coin.model.company.exchange.currency.candle.Candle;
import com.sam.coin.model.util.CompanyName;
import com.sam.coin.model.util.Currency;
import com.sam.coin.model.util.Exchange;
import com.sam.coin.service.CoinCandleService;
import com.sam.coin.service.exceptions.DataValidationException;
import com.sam.coin.service.exceptions.DatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

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

    // Example /api/v1/coincandle/theta/candle?curreny=USD&exchange=BINANCE
    @PutMapping("/{companyName}/candle")
    public ResponseEntity<ApiResponse<Company>> addCandleData(@PathVariable CompanyName companyName, @RequestParam Currency currency, @RequestParam Exchange exchange, @RequestBody Candle candle) {
        Company updatedCompany = coinCandleService.addCandleData(companyName, currency, exchange, candle);
        ApiResponse<Company> response = new ApiResponse<>(true, updatedCompany, "Candle saved successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{companyName}/candleset")
    public ResponseEntity<ApiResponse<Company>> addCandleData(@PathVariable CompanyName companyName, @RequestParam Currency currency, @RequestParam Exchange exchange, @RequestBody Set<Candle> candles) {
        Company updatedCompany = coinCandleService.addCandlesetData(companyName, currency, exchange, candles);
        ApiResponse<Company> response = new ApiResponse<>(true, updatedCompany, "Candles saved successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{companyName}/candles")
    public Optional<Company> getCandleData(@PathVariable CompanyName companyName) {
        return coinCandleService.findByCompanyName(companyName);
    }

    @ExceptionHandler(DataValidationException.class)
    public ResponseEntity<ApiResponse<Object>> handleDataValidationException(DataValidationException ex) {
        ApiResponse<Object> response = new ApiResponse<>(false, null, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<ApiResponse<Object>> handleDatabaseException(DatabaseException ex) {
        ApiResponse<Object> response = new ApiResponse<>(false, null, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
