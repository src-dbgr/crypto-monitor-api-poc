package com.sam.coin.dao;

import java.util.*;

import com.sam.coin.model.Coin;
import com.sam.coin.service.CoinDataAccessService.OrderBy;
import com.sam.coin.model.TableInfo;

public interface CoinDao {

    boolean insertCoin(UUID id, Coin coin);

    default boolean insertCoin(Coin coin) {
        UUID id = UUID.randomUUID();
        return insertCoin(id, coin);
    }

    List<Coin> selectAllCoins(String tableName);

    Optional<Coin> selectCoinById(UUID id);

    Map<String, TableInfo>  countAllEntries(OrderBy orderBy);

    Optional<Coin> selectCoinByTableNameAndId(String tableName, UUID id);

    List<Coin> selectCoinByTableNameAndDate(String tableName, String date);

    List<Coin> getAllDuplicatesWithSameDate(String tableName);

    int deleteDuplicatesWithSameDate(String tableName);

    boolean deleteCoinByID(UUID id);

    boolean updateCoinByID(UUID id, Coin coin);
    String exportTableToCsv(String tableName);

}
