package com.sam.coin.repository;

import com.sam.coin.domain.model.Coin;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CoinRepository {
    Coin save(Coin coin);
    List<Coin> findAll();
    Optional<Coin> findById(UUID id);
    List<Coin> findByTimestampBetween(Date startDate, Date endDate);
    List<Coin> findByCoinIdAndTimestampBetween(String coinId, Date startDate, Date endDate);
    long countByCoinId(String coinId);
    void deleteById(UUID id);
    Optional<Coin> findBySymbolAndTimestamp(String symbol, Date timestamp);
    boolean existsById(UUID id);
}