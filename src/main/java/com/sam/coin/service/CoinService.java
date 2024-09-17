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

/**
 * Service class for managing cryptocurrency-related operations.
 */
@Service
@Transactional
public class CoinService {

	private static final Logger LOG = LoggerFactory.getLogger(CoinService.class);

	private final CoinRepository coinRepository;

	@Autowired
	public CoinService(CoinRepository coinRepository) {
		this.coinRepository = coinRepository;
	}

	/**
	 * Saves a new cryptocurrency coin to the database.
	 *
	 * @param coin The cryptocurrency coin to be saved.
	 * @return The saved cryptocurrency coin.
	 * @throws InvalidCoinDataException If the cryptocurrency coin data is invalid.
	 * @throws DuplicateCoinException If a cryptocurrency coin with the same symbol and timestamp already exists.
	 */
	public Coin saveCoin(Coin coin) {
		LOG.info("Saving new cryptocurrency coin: {}", coin.getSymbol());
		if (coin.getSymbol() == null || coin.getSymbol().isEmpty()) {
			throw new InvalidCoinDataException("Coin symbol cannot be empty");
		}
		String tableName = getTableName(coin.getCoinId());
		if (coinRepository.findBySymbolAndTimestamp(tableName, coin.getSymbol(), coin.getTimestamp()).isPresent()) {
			throw new DuplicateCoinException("Cryptocurrency coin with symbol " + coin.getSymbol() + " already exists for the given timestamp");
		}
		Coin savedCoin = coinRepository.save(coin);
		LOG.debug("Cryptocurrency coin saved successfully with ID: {}", savedCoin.getId());
		return savedCoin;
	}

	/**
	 * Retrieves all coins for a given cryptocurrency coin ID.
	 *
	 * @param coinId The ID of the cryptocurrency.
	 * @return A list of all cryptocurrency coin entries for the given cryptocurrency coin ID.
	 */
	public List<Coin> getAllCoins(String coinId) {
		LOG.info("Fetching all coins for coinId: {}", coinId);
		return coinRepository.findAll(getTableName(coinId));
	}

	/**
	 * Retrieves a specific cryptocurrency coin by its ID and coin entry UUID.
	 *
	 * @param coinId The ID of the cryptocurrency coin type.
	 * @param id The UUID of the specific cryptocurrency coin.
	 * @return The requested cryptocurrency coin that matches with the UUID.
	 * @throws CoinNotFoundException If the cryptocurrency coin is not found.
	 */
	public Coin getCoinById(String coinId, UUID id) {
		LOG.info("Fetching cryptocurrency coin with UUID: {} for coinId: {}", id, coinId);
		return coinRepository.findById(getTableName(coinId), id)
				.orElseThrow(() -> new CoinNotFoundException("Coin not found with id: " + id));
	}

	/**
	 * Retrieves coins within a specified date range for a given cryptocurrency coin ID.
	 *
	 * @param coinId The ID of the cryptocurrency coin.
	 * @param startDate The start date of the range.
	 * @param endDate The end date of the range.
	 * @return A list of coins within the specified date range.
	 */
	public List<Coin> getCoinsByDateRange(String coinId, Date startDate, Date endDate) {
		LOG.info("Fetching coins for coinId: {} between {} and {}", coinId, startDate, endDate);
		return coinRepository.findByTimestampBetween(getTableName(coinId), startDate, endDate);
	}

	/**
	 * Retrieves coins for a specific cryptocurrency coin ID within a specified date range.
	 *
	 * @param coinId The ID of the cryptocurrency coin.
	 * @param startDate The start date of the range.
	 * @param endDate The end date of the range.
	 * @return A list of coins for the specific cryptocurrency coin ID within the specified date range.
	 */
	public List<Coin> getCoinsByCoinIdAndDateRange(String coinId, Date startDate, Date endDate) {
		LOG.info("Fetching coins for coinId: {} between {} and {}", coinId, startDate, endDate);
		return coinRepository.findByCoinIdAndTimestampBetween(getTableName(coinId), coinId, startDate, endDate);
	}

	/**
	 * Counts the number of coins for a given cryptocurrency coin ID.
	 *
	 * @param coinId The ID of the cryptocurrency coin.
	 * @return The count of coins for the given cryptocurrency coin ID.
	 */
	public long getCountByCoinId(String coinId) {
		LOG.info("Counting coins for coinId: {}", coinId);
		return coinRepository.countByCoinId(getTableName(coinId), coinId);
	}

	/**
	 * Deletes a specific cryptocurrency coin entry by its UUID and cryptocurrency coin ID.
	 *
	 * @param coinId The ID of the cryptocurrency coin type.
	 * @param id The UUID of the specific cryptocurrency coin to delete.
	 * @throws CoinNotFoundException If the cryptocurrency coin is not found.
	 */
	public void deleteCoin(String coinId, UUID id) {
		LOG.info("Deleting cryptocurrency coin with UUID: {} for coinId: {}", id, coinId);
		String tableName = getTableName(coinId);
		if (!coinRepository.existsById(tableName, id)) {
			throw new CoinNotFoundException("Cryptocurrency coin " + coinId + " not found with UUID: " + id);
		}
		coinRepository.deleteById(tableName, id);
		LOG.debug("Coin deleted successfully");
	}

	/**
	 * Retrieves all duplicate coins with the same date for a given cryptocurrency coin ID.
	 *
	 * @param coinId The ID of the cryptocurrency coin.
	 * @return A list of duplicate coins with the same date.
	 */
	public List<Coin> getAllDuplicatesWithSameDate(String coinId) {
		LOG.info("Getting all duplicates with same date for coinId: {}", coinId);
		return coinRepository.getAllDuplicatesWithSameDate(getTableName(coinId));
	}

	/**
	 * Deletes duplicate coins with the same date for a given cryptocurrency coin ID.
	 *
	 * @param coinId The ID of the cryptocurrency coin.
	 * @return The number of deleted duplicate entries.
	 */
	public int deleteDuplicatesWithSameDate(String coinId) {
		LOG.info("Deleting duplicates with same date for coinId: {}", coinId);
		return coinRepository.deleteDuplicatesWithSameDate(getTableName(coinId));
	}

	/**
	 * Retrieves the last valid date for a cryptocurrency coin with a specified sequence length.
	 *
	 * @param coinId The ID of the cryptocurrency coin.
	 * @param sequenceLength The length of the consecutive day sequence to consider valid.
	 * @return The last valid date for the specified cryptocurrency coin.
	 */
	public Date getLastValidDateForCoin(String coinId, int sequenceLength) {
		LOG.info("Getting last valid date for coinId: {} with sequence length: {}", coinId, sequenceLength);
		return coinRepository.getLastValidDateForCoin(getTableName(coinId), coinId, sequenceLength);
	}

	/**
	 * Returns the valid table name for a given cryptocurrency coin ID.
	 *
	 * @param coinId The ID of the cryptocurrency coin.
	 * @return The corresponding table name.
	 */
	private String getTableName(String coinId) {
		return coinId.equals("bitcoin") ? "coins" : coinId.replace("-", "_");
	}
}