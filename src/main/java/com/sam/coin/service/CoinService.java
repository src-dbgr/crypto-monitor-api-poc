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
import java.util.Optional;
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
		if (coinRepository.findBySymbolAndTimestamp(coin.getSymbol(), coin.getTimestamp()).isPresent()) {
			throw new DuplicateCoinException("Coin with symbol " + coin.getSymbol() + " already exists for the given timestamp");
		}
		Coin savedCoin = coinRepository.save(coin);
		logger.debug("Coin saved successfully with ID: {}", savedCoin.getId());
		return savedCoin;
	}

	public List<Coin> getAllCoins() {
		logger.info("Fetching all coins");
		return coinRepository.findAll();
	}

	public Coin getCoinById(UUID id) {
		logger.info("Fetching coin with ID: {}", id);
		return coinRepository.findById(id)
				.orElseThrow(() -> new CoinNotFoundException("Coin not found with id: " + id));
	}

	public List<Coin> getCoinsByDateRange(Date startDate, Date endDate) {
		logger.info("Fetching coins between {} and {}", startDate, endDate);
		return coinRepository.findByTimestampBetween(startDate, endDate);
	}

	public List<Coin> getCoinsByCoinIdAndDateRange(String coinId, Date startDate, Date endDate) {
		logger.info("Fetching coins for coinId: {} between {} and {}", coinId, startDate, endDate);
		return coinRepository.findByCoinIdAndTimestampBetween(coinId, startDate, endDate);
	}

	public long getCountByCoinId(String coinId) {
		logger.info("Counting coins for coinId: {}", coinId);
		return coinRepository.countByCoinId(coinId);
	}

	public void deleteCoin(UUID id) {
		logger.info("Deleting coin with ID: {}", id);
		if (!coinRepository.existsById(id)) {
			throw new CoinNotFoundException("Coin not found with id: " + id);
		}
		coinRepository.deleteById(id);
		logger.debug("Coin deleted successfully");
	}
}