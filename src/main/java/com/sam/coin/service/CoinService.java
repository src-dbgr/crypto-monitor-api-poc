package com.sam.coin.service;

import com.sam.coin.domain.model.Coin;
import com.sam.coin.exception.CoinNotFoundException;
import com.sam.coin.exception.DuplicateCoinException;
import com.sam.coin.exception.InvalidCoinDataException;
import com.sam.coin.repository.CoinRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CoinService {

	private static final Logger logger = LoggerFactory.getLogger(CoinService.class);

	private final CoinRepository coinRepository;

	@Autowired
	public CoinService(CoinRepository coinRepository) {
		this.coinRepository = coinRepository;
	}

	public Coin saveCoin(Coin coin) {
		logger.info("Saving new coin: {}", coin.getSymbol());
		if (coin.getSymbol() == null || coin.getSymbol().isEmpty()) {
			throw new InvalidCoinDataException("Coin symbol cannot be empty");
		}
		String tableName = getTableName(coin.getCoinId());
		if (coinRepository.findBySymbolAndTimestamp(tableName, coin.getSymbol(), coin.getTimestamp()).isPresent()) {
			throw new DuplicateCoinException("Coin with symbol " + coin.getSymbol() + " already exists for the given timestamp");
		}
		Coin savedCoin = coinRepository.save(coin);
		logger.debug("Coin saved successfully with ID: {}", savedCoin.getId());
		return savedCoin;
	}

	public List<Coin> getAllCoins(String coinId) {
		logger.info("Fetching all coins for coinId: {}", coinId);
		return coinRepository.findAll(getTableName(coinId));
	}

	public Coin getCoinById(String coinId, UUID id) {
		logger.info("Fetching coin with ID: {} for coinId: {}", id, coinId);
		return coinRepository.findById(getTableName(coinId), id)
				.orElseThrow(() -> new CoinNotFoundException("Coin not found with id: " + id));
	}

	public List<Coin> getCoinsByDateRange(String coinId, Date startDate, Date endDate) {
		logger.info("Fetching coins for coinId: {} between {} and {}", coinId, startDate, endDate);
		return coinRepository.findByTimestampBetween(getTableName(coinId), startDate, endDate);
	}

	public List<Coin> getCoinsByCoinIdAndDateRange(String coinId, Date startDate, Date endDate) {
		logger.info("Fetching coins for coinId: {} between {} and {}", coinId, startDate, endDate);
		return coinRepository.findByCoinIdAndTimestampBetween(getTableName(coinId), coinId, startDate, endDate);
	}

	public long getCountByCoinId(String coinId) {
		logger.info("Counting coins for coinId: {}", coinId);
		return coinRepository.countByCoinId(getTableName(coinId), coinId);
	}

	public void deleteCoin(String coinId, UUID id) {
		logger.info("Deleting coin with ID: {} for coinId: {}", id, coinId);
		String tableName = getTableName(coinId);
		if (!coinRepository.existsById(tableName, id)) {
			throw new CoinNotFoundException("Coin not found with id: " + id);
		}
		coinRepository.deleteById(tableName, id);
		logger.debug("Coin deleted successfully");
	}

	public List<Coin> getAllDuplicatesWithSameDate(String coinId) {
		logger.info("Getting all duplicates with same date for coinId: {}", coinId);
		return coinRepository.getAllDuplicatesWithSameDate(getTableName(coinId));
	}

	public int deleteDuplicatesWithSameDate(String coinId) {
		logger.info("Deleting duplicates with same date for coinId: {}", coinId);
		return coinRepository.deleteDuplicatesWithSameDate(getTableName(coinId));
	}

	public Date getLastValidDateForCoin(String coinId, int sequenceLength) {
		logger.info("Getting last valid date for coinId: {} with sequence length: {}", coinId, sequenceLength);
		return coinRepository.getLastValidDateForCoin(getTableName(coinId), coinId, sequenceLength);
	}

	private String getTableName(String coinId) {
		return coinId.equals("bitcoin") ? "coins" : coinId.replace("-", "_");
	}
}