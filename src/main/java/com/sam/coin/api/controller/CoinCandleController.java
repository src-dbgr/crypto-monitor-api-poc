package com.sam.coin.api.controller;

import com.sam.coin.api.ApiResponse;
import com.sam.coin.exception.UnknownEnumException;
import com.sam.coin.model.company.Company;
import com.sam.coin.model.company.exchange.currency.candle.Candle;
import com.sam.coin.model.util.CompanyName;
import com.sam.coin.model.util.Currency;
import com.sam.coin.model.util.Exchange;
import com.sam.coin.service.CoinCandleService;
import com.sam.coin.service.CsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.beans.PropertyEditorSupport;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/coincandle")
public class CoinCandleController {
    private final CoinCandleService coinCandleService;
    private final CsvService csvService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(CompanyName.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String company) {
                try{
                    company = company.replace("-","_");
                    setValue(CompanyName.valueOf(company.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    throw new UnknownEnumException("Unknown CompanyName value: " + company);
                }
            }
        });

        binder.registerCustomEditor(Currency.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String currency) {
                try{
                    setValue(Currency.valueOf(currency.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    throw new UnknownEnumException("Unknown Currency value: " + currency);
                }
            }
        });

        binder.registerCustomEditor(Exchange.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String exchange) {
                try{
                    setValue(Exchange.valueOf(exchange.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    throw new UnknownEnumException("Unknown Exchange value: " + exchange);
                }
            }
        });
    }

    @Autowired
    public CoinCandleController(CoinCandleService coinCandleService, CsvService csvService) {
        this.coinCandleService = coinCandleService;
        this.csvService = csvService;
    }

    @PostMapping("/{companyName}")
    public ResponseEntity<ApiResponse<Company>> create(@PathVariable CompanyName companyName) {
        Company updatedCompany = coinCandleService.createNewCompany(companyName);
        ApiResponse<Company> response = new ApiResponse<>(true, updatedCompany, "Candle saved successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
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

    @PutMapping("/{companyName}/removeduplicates")
    public ResponseEntity<ApiResponse<Company>> addCandleData(@PathVariable CompanyName companyName, @RequestParam Currency currency, @RequestParam Exchange exchange) {
        Company updatedCompany = coinCandleService.removeDuplicateCandles(companyName, currency, exchange);
        ApiResponse<Company> response = new ApiResponse<>(true, updatedCompany, "Candle duplicates updated successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{companyName}/candles")
    public Optional<Company> getCandleData(@PathVariable CompanyName companyName) {
        return coinCandleService.findByCompanyName(companyName);
    }

    @GetMapping("{companyName}/candle-csv")
    public ResponseEntity<String> getCandleDataAsCSV(@PathVariable CompanyName companyName,
                                                     @RequestParam Exchange exchange,
                                                     @RequestParam Currency currency) {
        Set<Candle> candles = coinCandleService.getCandleData(companyName, exchange, currency);
        String csvData = csvService.convertCandleDataToCSV(candles);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "text/csv");
        headers.add("Content-Disposition", "attachment; filename=candle-data.csv");
        return new ResponseEntity<>(csvData, headers, HttpStatus.OK);
    }

}
