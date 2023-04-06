package com.sam.coin.dao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sam.coin.model.Coin;
import org.hibernate.validator.internal.constraintvalidators.bv.time.futureorpresent.FutureOrPresentValidatorForZonedDateTime;

public interface CoinDao {

	boolean insertCoin(UUID id, Coin coin);

	default boolean insertCoin(Coin coin) {
		UUID id = UUID.randomUUID();
		return insertCoin(id, coin);
	}

	List<Coin> selectAllCoins();

	Optional<Coin> selectCoinById(UUID id);

	Optional<Coin> selectCoinByTableNameAndId(String tableName, UUID id);
	List<Coin> selectCoinByTableNameAndDate(String tableName, String date);

	boolean deleteCoinByID(UUID id);

	boolean updateCoinByID(UUID id, Coin coin);

}
