package com.sam.coin.api.controller;

import com.sam.coin.domain.model.Coin;
import com.sam.coin.exception.CoinNotFoundException;
import com.sam.coin.service.CoinService;
import com.sam.coin.api.SamApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/coins")
@Tag(name = "Coin", description = "The Coin API")
public class CoinController {

    private static final Logger logger = LoggerFactory.getLogger(CoinController.class);
    private final CoinService coinService;

    public CoinController(CoinService coinService) {
        this.coinService = coinService;
    }

    @Operation(summary = "Add a new coin")
    @ApiResponse(responseCode = "201", description = "Coin created")
    @PostMapping
    public ResponseEntity<SamApiResponse<Coin>> addCoin(@Valid @RequestBody Coin coin) {
        logger.info("Received request to add new coin: {}", coin.getSymbol());
        Coin savedCoin = coinService.saveCoin(coin);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new SamApiResponse<>(true, savedCoin, "Coin added successfully"));
    }

    @Operation(summary = "Get all coins for a specific coin ID")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @GetMapping("/{coinId}")
    public ResponseEntity<SamApiResponse<List<Coin>>> getAllCoins(
            @Parameter(name = "coinId", description = "ID of the coin", required = true, example = "bitcoin")
            @PathVariable String coinId) {
        logger.info("Received request to get all coins for coinId: {}", coinId);
        List<Coin> coins = coinService.getAllCoins(coinId);
        return ResponseEntity.ok(new SamApiResponse<>(true, coins, "Coins retrieved successfully"));
    }

    @Operation(summary = "Get a coin by ID")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "404", description = "Coin not found")
    @GetMapping("/{coinId}/{id}")
    public ResponseEntity<SamApiResponse<Coin>> getCoinById(
            @Parameter(name = "coinId", description = "ID of the coin type", required = true, example = "bitcoin")
            @PathVariable String coinId,
            @Parameter(name = "id", description = "ID of the coin to retrieve", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id) {
        logger.info("Received request to get coin with ID: {} for coinId: {}", id, coinId);
        Coin coin = coinService.getCoinById(coinId, id);
        return ResponseEntity.ok(new SamApiResponse<>(true, coin, "Coin retrieved successfully"));
    }

    @Operation(summary = "Get coins by date range")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @GetMapping("/{coinId}/date-range")
    public ResponseEntity<SamApiResponse<List<Coin>>> getCoinsByDateRange(
            @Parameter(name = "coinId", description = "ID of the coin", required = true, example = "bitcoin")
            @PathVariable String coinId,
            @Parameter(name = "startDate", description = "Start date", required = true, example = "2023-01-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @Parameter(name = "endDate", description = "End date", required = true, example = "2023-12-31")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        logger.info("Received request to get coins for coinId: {} between {} and {}", coinId, startDate, endDate);
        List<Coin> coins = coinService.getCoinsByDateRange(coinId, startDate, endDate);
        return ResponseEntity.ok(new SamApiResponse<>(true, coins, "Coins retrieved successfully"));
    }

    @Operation(summary = "Get count of coins by coin ID")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @GetMapping("/count/{coinId}")
    public ResponseEntity<SamApiResponse<Long>> getCountByCoinId(
            @Parameter(name = "coinId", description = "ID of the coin", required = true, example = "bitcoin")
            @PathVariable String coinId) {
        logger.info("Received request to get count for coinId: {}", coinId);
        long count = coinService.getCountByCoinId(coinId);
        return ResponseEntity.ok(new SamApiResponse<>(true, count, "Count retrieved successfully"));
    }

    @Operation(summary = "Delete a coin")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "404", description = "Coin not found")
    @DeleteMapping("/{coinId}/{id}")
    public ResponseEntity<SamApiResponse<Void>> deleteCoin(
            @Parameter(name = "coinId", description = "ID of the coin type", required = true, example = "bitcoin")
            @PathVariable String coinId,
            @Parameter(name = "id", description = "ID of the coin to delete", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id) {
        logger.info("Received request to delete coin with ID: {} for coinId: {}", id, coinId);
        coinService.deleteCoin(coinId, id);
        return ResponseEntity.ok(new SamApiResponse<>(true, null, "Coin deleted successfully"));
    }

    @Operation(summary = "Get all duplicates with same date")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @GetMapping("/{coinId}/duplicates")
    public ResponseEntity<SamApiResponse<List<Coin>>> getAllDuplicatesWithSameDate(
            @Parameter(name = "coinId", description = "ID of the coin", required = true, example = "bitcoin")
            @PathVariable String coinId) {
        logger.info("Received request to get all duplicates with same date for coinId: {}", coinId);
        List<Coin> duplicates = coinService.getAllDuplicatesWithSameDate(coinId);
        return ResponseEntity.ok(new SamApiResponse<>(true, duplicates, "Duplicates retrieved successfully"));
    }

    @Operation(summary = "Delete duplicates with same date")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @DeleteMapping("/{coinId}/duplicates")
    public ResponseEntity<SamApiResponse<Integer>> deleteDuplicatesWithSameDate(
            @Parameter(name = "coinId", description = "ID of the coin", required = true, example = "bitcoin")
            @PathVariable String coinId) {
        logger.info("Received request to delete duplicates with same date for coinId: {}", coinId);
        int deletedCount = coinService.deleteDuplicatesWithSameDate(coinId);
        return ResponseEntity.ok(new SamApiResponse<>(true, deletedCount, "Duplicates deleted successfully"));
    }

    @Operation(summary = "Get last valid date for coin")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @GetMapping("/{coinId}/lastValidDate")
    public ResponseEntity<SamApiResponse<Date>> getLastValidDateForCoin(
            @Parameter(name = "coinId", description = "ID of the coin", required = true, example = "bitcoin")
            @PathVariable String coinId,
            @Parameter(name = "sequenceLength", description = "Length of the consecutive day sequence (default is 10)", required = false, example = "5")
            @RequestParam(defaultValue = "10") int sequenceLength) {
        logger.info("Received request to get last valid date for coinId: {} with sequence length: {}", coinId, sequenceLength);
        Date lastValidDate = coinService.getLastValidDateForCoin(coinId, sequenceLength);
        return ResponseEntity.ok(new SamApiResponse<>(true, lastValidDate, "Last valid date retrieved successfully"));
    }

    // Exception Handler
    @ExceptionHandler(CoinNotFoundException.class)
    public ResponseEntity<SamApiResponse<Void>> handleCoinNotFoundException(CoinNotFoundException e) {
        logger.error("Coin not found exception: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new SamApiResponse<>(false, null, e.getMessage()));
    }
}
