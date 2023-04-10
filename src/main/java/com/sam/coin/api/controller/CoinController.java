package com.sam.coin.api.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.sam.coin.service.CoinDataAccessService.OrderBy;
import com.sam.coin.model.TableInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import com.sam.coin.model.Coin;
import com.sam.coin.service.CoinService;

@RestController
@RequestMapping("/api/v1/coin")
public class CoinController {
    private static final Logger LOG = LoggerFactory.getLogger(CoinController.class);
    private final CoinService coinService;

    @Autowired
    public CoinController(CoinService coinService) {
        this.coinService = coinService;
    }

    @PostMapping
    public void addCoin(@Valid @NonNull @RequestBody Coin coin) {
        coinService.addCoin(coin);
        LOG.info("Added new coin: " + coin.getSymbol());
    }

    @GetMapping(path = "{table}/all")
    public List<Coin> getAllCoins(String tableName) {
        //TODO
        return null;
//		return coinService.getAllCoins();
    }

    @DeleteMapping(path = "{id}")
    public void deleteCoin(@PathVariable("id") UUID id) {
        coinService.deleteCoinByID(id);
    }

    @GetMapping(path = "count")
    public Map<String, TableInfo> countAllEntries(@RequestParam(defaultValue = "alphabetical") String orderBy) {
        if (orderBy.equals("numeric")) {
            return coinService.countAllEntries(OrderBy.COUNT);
        } else if (orderBy.equals("date")) {
            return coinService.countAllEntries(OrderBy.DATE);
        }
        return coinService.countAllEntries(OrderBy.ALPHABETICAL);
    }

    @GetMapping(path = "{table}/count")
    public Map<String, TableInfo> selectCoinByTableNameAndId(@PathVariable("table") String tableName) {
        Map<String, TableInfo> result = new HashMap<String, TableInfo>();
        try {
            result.put(tableName, coinService.countAllEntries(OrderBy.ALPHABETICAL).get(tableName));
            return result;
        } catch (Exception e) {
            LOG.error("Failed to find info for Table Name {}", tableName, e);
        }
        return result;
    }

    @GetMapping(path = "{table}/id/{id}")
    public Optional<Coin> selectCoinByTableNameAndId(@PathVariable("table") String tableName, @PathVariable("id") UUID id) {
        return coinService.selectCoinByTableNameAndId(tableName, id);
    }

    // Date Pattern yyyy-mm-dd
    // 2020-09-04 <- 4th September of 2020
    @GetMapping(path = "{table}/date/{date}")
    public List<Coin> selectCoinByTableNameAndDate(@PathVariable("table") String tableName, @PathVariable("date") String date) {
        List<Coin> coins = coinService.selectCoinByTableNameAndDate(tableName, date);
        return coins;
    }

    @GetMapping(path = "{table}/duplicates")
    public Map<Integer, List<Coin>> getAllDuplicatesWithSameDate(@PathVariable("table") String tableName) {
        List<Coin> coins = coinService.getAllDuplicatesWithSameDate(tableName);
        HashMap<Integer, List<Coin>> result = new HashMap<>();
        result.put(coins.size(), coins);
        return result;
    }

    @DeleteMapping(path = "{table}/duplicates")
    public int deleteDuplicatesWithSameDate(@PathVariable("table") String tableName) {
        return coinService.deleteDuplicatesWithSameDate(tableName);
    }

    @PutMapping(path = "{id}")
    public void updateCoin(@PathVariable("id") UUID id, @Valid @NonNull @RequestBody Coin coinToUpdate) {
        LOG.debug("PUT ID: " + id);
        coinService.updateCoin(id, coinToUpdate);
    }

    @GetMapping(value = "{tableName}/export", produces = "text/csv")
    public ResponseEntity<String> exportTableToCsv(@PathVariable("tableName") String tableName, HttpServletResponse response) throws IOException {
        String csvData = coinService.exportTableToCsv(tableName);
        // Set up the response headers for CSV file download
        String fileName = tableName + "_export.csv";
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

        // Write the CSV data to the response body
        try (PrintWriter writer = response.getWriter()) {
            writer.write(csvData);
        }

        // Return an empty response
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "{tableName}/exporttext", produces = MediaType.TEXT_PLAIN_VALUE)
    public String exportTableToCsv(@PathVariable("tableName") String tableName) throws IOException {
        return coinService.exportTableToCsv(tableName);
    }

}
