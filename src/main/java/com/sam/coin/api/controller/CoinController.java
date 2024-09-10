package com.sam.coin.api.controller;

import com.sam.coin.domain.model.Coin;
import com.sam.coin.exception.CoinNotFoundException;
import com.sam.coin.service.CoinService;
import com.sam.coin.api.SamApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/coins")
@Validated
@Tag(name = "Coin", description = "The Coin API")
public class CoinController {

    private static final Logger logger = LoggerFactory.getLogger(CoinController.class);

    private final CoinService coinService;

    @Autowired
    public CoinController(CoinService coinService) {
        this.coinService = coinService;
    }

    @Operation(summary = "Add a new coin", description = "Adds a new coin to the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Coin created",
                    content = @Content(schema = @Schema(implementation = Coin.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<SamApiResponse<Coin>> addCoin(@Valid @RequestBody Coin coin) {
        logger.info("Received request to add new coin: {}", coin.getSymbol());
        Coin savedCoin = coinService.saveCoin(coin);
        logger.debug("Coin added successfully with ID: {}", savedCoin.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new SamApiResponse<>(true, savedCoin, "Coin added successfully"));
    }

    @Operation(summary = "Get all coins", description = "Retrieves a list of all coins")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = Coin.class)))
    })
    @GetMapping
    public ResponseEntity<SamApiResponse<List<Coin>>> getAllCoins() {
        logger.info("Received request to get all coins");
        List<Coin> coins = coinService.getAllCoins();
        logger.debug("Retrieved {} coins", coins.size());
        return ResponseEntity.ok(new SamApiResponse<>(true, coins, "Coins retrieved successfully"));
    }

    @Operation(summary = "Get a coin by ID", description = "Retrieves a coin by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = Coin.class))),
            @ApiResponse(responseCode = "404", description = "Coin not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SamApiResponse<Coin>> getCoinById(
            @Parameter(description = "ID of the coin to retrieve") @PathVariable("id") @NotNull UUID id) {
        logger.info("Received request to get coin with ID: {}", id);
        try {
            Coin coin = coinService.getCoinById(id);
            logger.debug("Retrieved coin: {}", coin.getSymbol());
            return ResponseEntity.ok(new SamApiResponse<>(true, coin, "Coin retrieved successfully"));
        } catch (CoinNotFoundException e) {
            logger.warn("Coin not found with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new SamApiResponse<>(false, null, e.getMessage()));
        }
    }

    @Operation(summary = "Get coins by date range", description = "Retrieves a list of coins within a specified date range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = Coin.class)))
    })
    @GetMapping("/date-range")
    public ResponseEntity<SamApiResponse<List<Coin>>> getCoinsByDateRange(
            @Parameter(description = "Start date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @Parameter(description = "End date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        logger.info("Received request to get coins between {} and {}", startDate, endDate);
        List<Coin> coins = coinService.getCoinsByDateRange(startDate, endDate);
        logger.debug("Retrieved {} coins for the specified date range", coins.size());
        return ResponseEntity.ok(new SamApiResponse<>(true, coins, "Coins retrieved successfully"));
    }

    @Operation(summary = "Get coins by coin ID and date range", description = "Retrieves a list of coins for a specific coin ID within a date range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = Coin.class)))
    })
    @GetMapping("/coin-id/{coinId}/date-range")
    public ResponseEntity<SamApiResponse<List<Coin>>> getCoinsByCoinIdAndDateRange(
            @Parameter(description = "ID of the coin") @PathVariable @NotBlank String coinId,
            @Parameter(description = "Start date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @Parameter(description = "End date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        logger.info("Received request to get coins for coinId: {} between {} and {}", coinId, startDate, endDate);
        List<Coin> coins = coinService.getCoinsByCoinIdAndDateRange(coinId, startDate, endDate);
        logger.debug("Retrieved {} coins for coinId: {} and specified date range", coins.size(), coinId);
        return ResponseEntity.ok(new SamApiResponse<>(true, coins, "Coins retrieved successfully"));
    }

    @Operation(summary = "Get count of coins by coin ID", description = "Retrieves the count of coins for a specific coin ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = Long.class)))
    })
    @GetMapping("/count/{coinId}")
    public ResponseEntity<SamApiResponse<Long>> getCountByCoinId(
            @Parameter(description = "ID of the coin") @PathVariable @NotBlank String coinId) {
        logger.info("Received request to get count for coinId: {}", coinId);
        long count = coinService.getCountByCoinId(coinId);
        logger.debug("Count for coinId {}: {}", coinId, count);
        return ResponseEntity.ok(new SamApiResponse<>(true, count, "Count retrieved successfully"));
    }

    @Operation(summary = "Delete a coin", description = "Deletes a coin by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Coin not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<SamApiResponse<Void>> deleteCoin(
            @Parameter(description = "ID of the coin to delete") @PathVariable("id") @NotNull UUID id) {
        logger.info("Received request to delete coin with ID: {}", id);
        coinService.deleteCoin(id);
        logger.debug("Coin with ID: {} deleted successfully", id);
        return ResponseEntity.ok(new SamApiResponse<>(true, null, "Coin deleted successfully"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<SamApiResponse<Void>> handleException(Exception e) {
        logger.error("An unexpected error occurred", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new SamApiResponse<>(false, null, "An error occurred: " + e.getMessage()));
    }
}