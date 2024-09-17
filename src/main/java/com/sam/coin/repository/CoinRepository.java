package com.sam.coin.repository;

import com.sam.coin.domain.model.Coin;
import org.springframework.dao.DataAccessException;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CoinRepository {

    /**
     * Saves a coin entity to the database.
     * If the coin's ID is null, a new UUID will be generated.
     *
     * @param coin the Coin entity to be saved
     * @return the saved Coin entity
     * @throws DataAccessException if there's an error during the database operation
     */
    Coin save(Coin coin);

    /**
     * Retrieves all coin entities from the specified table.
     *
     * @param tableName the name of the table to query
     * @return a List of all Coin entities in the table
     * @throws DataAccessException if there's an error accessing the database
     */
    List<Coin> findAll(String tableName);

    /**
     * Finds a coin entity by its ID in the specified table.
     *
     * @param tableName the name of the table to query
     * @param id the UUID of the coin to find
     * @return an Optional containing the found Coin entity, or empty if not found
     * @throws DataAccessException if there's an error accessing the database
     */
    Optional<Coin> findById(String tableName, UUID id);

    /**
     * Finds all coin entities within a specified date range in the given table.
     *
     * @param tableName the name of the table to query
     * @param startDate the start date of the range (inclusive)
     * @param endDate the end date of the range (inclusive)
     * @return a List of Coin entities within the specified date range
     * @throws DataAccessException if there's an error accessing the database
     */
    List<Coin> findByTimestampBetween(String tableName, Date startDate, Date endDate);

    /**
     * Finds all coin entities for a specific coin ID within a specified date range in the given table.
     *
     * @param tableName the name of the table to query
     * @param coinId the unique identifier of the coin
     * @param startDate the start date of the range (inclusive)
     * @param endDate the end date of the range (inclusive)
     * @return a List of Coin entities matching the coin ID and within the specified date range
     * @throws DataAccessException if there's an error accessing the database
     */
    List<Coin> findByCoinIdAndTimestampBetween(String tableName, String coinId, Date startDate, Date endDate);

    /**
     * Counts the number of entries for a specific coin ID in the given table.
     *
     * @param tableName the name of the table to query
     * @param coinId the unique identifier of the coin
     * @return the count of entries for the specified coin ID
     * @throws DataAccessException if there's an error accessing the database
     */
    long countByCoinId(String tableName, String coinId);

    /**
     * Deletes a coin entity by its ID from the specified table.
     *
     * @param tableName the name of the table to delete from
     * @param id the UUID of the coin to delete
     * @throws DataAccessException if there's an error during the database operation
     */
    void deleteById(String tableName, UUID id);

    /**
     * Finds a coin entity by its symbol and timestamp in the specified table.
     *
     * @param tableName the name of the table to query
     * @param symbol the symbol of the coin
     * @param timestamp the timestamp to match
     * @return an Optional containing the found Coin entity, or empty if not found
     * @throws DataAccessException if there's an error accessing the database
     */
    Optional<Coin> findBySymbolAndTimestamp(String tableName, String symbol, Date timestamp);

    /**
     * Checks if a coin entity with the given ID exists in the specified table.
     *
     * @param tableName the name of the table to query
     * @param id the UUID of the coin to check
     * @return true if a coin with the given ID exists, false otherwise
     * @throws DataAccessException if there's an error accessing the database
     */
    boolean existsById(String tableName, UUID id);

    /**
     * Retrieves all duplicate coin entities with the same date in the specified table.
     * Duplicates are defined as multiple entries with the same date (ignoring time).
     *
     * @param tableName the name of the table to query
     * @return a List of Coin entities that are duplicates based on date
     * @throws DataAccessException if there's an error accessing the database
     */
    List<Coin> getAllDuplicatesWithSameDate(String tableName);

    /**
     * Deletes duplicate coin entities with the same date in the specified table,
     * keeping only one entry per date.
     *
     * @param tableName the name of the table to delete from
     * @return the number of deleted duplicate entries
     * @throws DataAccessException if there's an error during the database operation
     */
    int deleteDuplicatesWithSameDate(String tableName);

    /**
     * Retrieves the last valid date for a specific coin in the given table.
     *
     * A date is considered valid if it is part of a continuous sequence of dates
     * of the specified length for which data exists in the table.
     * The method accounts for month and year transitions in determining consecutive days.
     *
     * For example, if the sequence length is 5, and data exists for:
     * 2024-09-02, 2024-09-01, 2024-08-31, 2024-08-30, 2024-08-29
     * Then 2024-09-02 would be returned as the last valid date.
     *
     * @param tableName the name of the table to query
     * @param coinId the unique identifier of the coin
     * @param sequenceLength the length of the consecutive day sequence to consider valid
     * @return the Date representing the last valid date for the specified coin,
     *         or null if no valid sequence is found
     * @throws IllegalArgumentException if tableName or coinId is null or empty, or if sequenceLength is less than 1
     * @throws DataAccessException if there's an error accessing the database
     */
    Date getLastValidDateForCoin(String tableName, String coinId, int sequenceLength);
}