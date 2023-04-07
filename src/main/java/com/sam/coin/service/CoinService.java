package com.sam.coin.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sam.coin.dao.CoinDao;
import com.sam.coin.model.Coin;

@Service
public class CoinService {
	private static final Logger LOG = LoggerFactory.getLogger(CoinService.class);
	private final CoinDao coinDao;

	@Autowired
	public CoinService(@Qualifier("postgrescoins") CoinDao coinDao) {
		this.coinDao = coinDao;
	}

	public boolean addCoin(Coin coin) {
		LOG.info("Adding Coin: " + coin.toString());
		return coinDao.insertCoin(coin);
	}

	public List<Coin> getAllCoins() {
		LOG.info("Getting all coins from Database");
		return coinDao.selectAllCoins();
	}

	public boolean deleteCoinByID(UUID id) {
		LOG.info("Deleting Coin with ID: " + id);
		return coinDao.deleteCoinByID(id);
	}

	public Optional<Coin> selectCoinByTableNameAndId(String tableName, UUID id) {
		LOG.info("Getting Coin with ID: " + id + " and Table: " + tableName);
		Optional<Coin> coin = coinDao.selectCoinByTableNameAndId(tableName, id);
		LOG.info(coin.get().toString());
		return coin;
	}

	public List<Coin> selectCoinByTableNameAndDate(String tableName, String date) {
		LOG.info("Getting Coin with date: " + date + " and Table: " + tableName);
		List<Coin> coins = coinDao.selectCoinByTableNameAndDate(tableName, date);
		LOG.info(coins.toString());
		return coins;
	}

	public List<Coin> getAllDuplicatesWithSameDate(String tableName) {
		LOG.info("Get duplicates with Table: " + tableName);
		return coinDao.getAllDuplicatesWithSameDate(tableName);
	}

	public int deleteDuplicatesWithSameDate(String tableName) {
		LOG.info("Delete duplicates with Table: " + tableName);
		return coinDao.deleteDuplicatesWithSameDate(tableName);
	}

	public boolean updateCoin(UUID id, Coin newCoin) {
		LOG.info("Updating Coin with ID: " + id);
		return coinDao.updateCoinByID(id, newCoin);
	}
}
