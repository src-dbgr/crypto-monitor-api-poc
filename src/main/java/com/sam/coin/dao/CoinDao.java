package com.sam.coin.dao;

import java.util.*;

import com.sam.coin.model.Coin;

public interface CoinDao {

    boolean insertCoin(UUID id, Coin coin);

    default boolean insertCoin(Coin coin) {
        UUID id = UUID.randomUUID();
        return insertCoin(id, coin);
    }

    List<Coin> selectAllCoins();

    Optional<Coin> selectCoinById(UUID id);

    Map<String, Integer> countAllEntries();

    Optional<Coin> selectCoinByTableNameAndId(String tableName, UUID id);

    List<Coin> selectCoinByTableNameAndDate(String tableName, String date);

    List<Coin> getAllDuplicatesWithSameDate(String tableName);

    int deleteDuplicatesWithSameDate(String tableName);

    boolean deleteCoinByID(UUID id);

    boolean updateCoinByID(UUID id, Coin coin);

}
