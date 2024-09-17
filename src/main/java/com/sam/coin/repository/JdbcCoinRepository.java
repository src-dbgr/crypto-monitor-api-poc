package com.sam.coin.repository;

import com.sam.coin.domain.model.Coin;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.sam.coin.repository.CoinQueries.*;

@Repository
public class JdbcCoinRepository implements CoinRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcCoinRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Coin save(Coin coin) {
        if (coin.getId() == null) {
            coin.setId(UUID.randomUUID());
        }
        String tableName = getTableName(coin.getCoinId());
        jdbcTemplate.update(String.format(INSERT_COIN, tableName),
                coin.getId(), coin.getTimestamp(), coin.getSymbol(), coin.getCoinId(), coin.getCoinName(),
                coin.getPriceEur(), coin.getPriceUsd(), coin.getPriceBtc(), coin.getPriceEth(),
                coin.getMarketCapEur(), coin.getMarketCapUsd(), coin.getMarketCapBtc(), coin.getMarketCapEth(),
                coin.getTotalVolumeEur(), coin.getTotalVolumeUsd(), coin.getTotalVolumeBtc(), coin.getTotalVolumeEth(),
                coin.getTwitterFollowers(), coin.getRedditAvgPosts48Hours(), coin.getRedditAvgComments48Hours(),
                coin.getRedditAccountsActive48Hours(), coin.getRedditSubscribers(),
                coin.getDevForks(), coin.getDevStars(), coin.getDevTotalIssues(), coin.getDevClosedIssues(),
                coin.getDevPullRequestsMerged(), coin.getDevPullRequestContributors(), coin.getDevCommitCount4Weeks(),
                coin.getDevCodeAdditions4Weeks(), coin.getDevCodeDeletions4Weeks(), coin.getPublicAlexaRank());
        return coin;
    }

    @Override
    public List<Coin> findAll(String tableName) {
        return jdbcTemplate.query(String.format(SELECT_ALL_COINS, tableName), new CoinRowMapper());
    }

    @Override
    public Optional<Coin> findById(String tableName, UUID id) {
        List<Coin> results = jdbcTemplate.query(String.format(SELECT_COIN_BY_ID, tableName), new CoinRowMapper(), id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.getFirst());
    }

    @Override
    public List<Coin> findByTimestampBetween(String tableName, Date startDate, Date endDate) {
        return jdbcTemplate.query(String.format(SELECT_COINS_BY_DATE_RANGE, tableName), new CoinRowMapper(), startDate, endDate);
    }

    @Override
    public List<Coin> findByCoinIdAndTimestampBetween(String tableName, String coinId, Date startDate, Date endDate) {
        return jdbcTemplate.query(String.format(SELECT_COINS_BY_COIN_ID_AND_DATE_RANGE, tableName), new CoinRowMapper(), coinId, startDate, endDate);
    }

    @Override
    public long countByCoinId(String tableName, String coinId) {
        return jdbcTemplate.queryForObject(String.format(COUNT_BY_COIN_ID, tableName), Long.class, coinId);
    }

    @Override
    public void deleteById(String tableName, UUID id) {
        jdbcTemplate.update(String.format(DELETE_COIN_BY_ID, tableName), id);
    }

    @Override
    public Optional<Coin> findBySymbolAndTimestamp(String tableName, String symbol, Date timestamp) {
        String sql = String.format("SELECT * FROM %s WHERE symbol = ? AND time_stamp = ?", tableName);
        List<Coin> results = jdbcTemplate.query(sql, new CoinRowMapper(), symbol, timestamp);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.getFirst());
    }

    @Override
    public boolean existsById(String tableName, UUID id) {
        String sql = String.format("SELECT COUNT(*) FROM %s WHERE id = ?", tableName);
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public List<Coin> getAllDuplicatesWithSameDate(String tableName) {
        String sql = String.format(SELECT_ALL_DUPLICATES_WITH_SAME_DATE, tableName, tableName);
        return jdbcTemplate.query(sql, new CoinRowMapper());
    }

    @Override
    public int deleteDuplicatesWithSameDate(String tableName) {
        String sql = String.format(DELETE_DUPLICATES_WITH_SAME_DATE, tableName, tableName);
        return jdbcTemplate.update(sql);
    }

    @Override
    public Date getLastValidDateForCoin(String tableName, String coinId, int sequenceLength) {
        String sqlQuery = String.format(SELECT_LAST_VALID_DATE_FOR_COIN,
                tableName,         // Table name
                sequenceLength,    // Minimum group size and maximum sequence number
                sequenceLength     // Required number of days in the sequence
        );

        return jdbcTemplate.queryForObject(sqlQuery, Date.class);
    }

    private static class CoinRowMapper implements RowMapper<Coin> {
        @Override
        public Coin mapRow(ResultSet rs, int rowNum) throws SQLException {
            Coin coin = new Coin();
            coin.setId(UUID.fromString(rs.getString("id")));
            coin.setTimestamp(rs.getTimestamp("time_stamp"));
            coin.setSymbol(rs.getString("symbol"));
            coin.setCoinId(rs.getString("coin_id"));
            coin.setCoinName(rs.getString("coin_name"));
            coin.setPriceEur(rs.getBigDecimal("price_eur"));
            coin.setPriceUsd(rs.getBigDecimal("price_usd"));
            coin.setPriceBtc(rs.getBigDecimal("price_btc"));
            coin.setPriceEth(rs.getBigDecimal("price_eth"));
            coin.setMarketCapEur(rs.getBigDecimal("market_cap_eur"));
            coin.setMarketCapUsd(rs.getBigDecimal("market_cap_usd"));
            coin.setMarketCapBtc(rs.getBigDecimal("market_cap_btc"));
            coin.setMarketCapEth(rs.getBigDecimal("market_cap_eth"));
            coin.setTotalVolumeEur(rs.getBigDecimal("total_volume_eur"));
            coin.setTotalVolumeUsd(rs.getBigDecimal("total_volume_usd"));
            coin.setTotalVolumeBtc(rs.getBigDecimal("total_volume_btc"));
            coin.setTotalVolumeEth(rs.getBigDecimal("total_volume_eth"));
            coin.setTwitterFollowers(rs.getLong("twitter_followers"));
            coin.setRedditAvgPosts48Hours(rs.getBigDecimal("reddit_avg_posts_48_hours"));
            coin.setRedditAvgComments48Hours(rs.getBigDecimal("reddit_avg_comments_48_hours"));
            coin.setRedditAccountsActive48Hours(rs.getBigDecimal("reddit_accounts_active_48_hours"));
            coin.setRedditSubscribers(rs.getLong("reddit_subscribers"));
            coin.setDevForks(rs.getLong("dev_forks"));
            coin.setDevStars(rs.getLong("dev_stars"));
            coin.setDevTotalIssues(rs.getLong("dev_total_issues"));
            coin.setDevClosedIssues(rs.getLong("dev_closed_issues"));
            coin.setDevPullRequestsMerged(rs.getLong("dev_pull_requests_merged"));
            coin.setDevPullRequestContributors(rs.getLong("dev_pull_request_contributors"));
            coin.setDevCommitCount4Weeks(rs.getLong("dev_commit_count_4_weeks"));
            coin.setDevCodeAdditions4Weeks(rs.getLong("dev_code_additions_4_weeks"));
            coin.setDevCodeDeletions4Weeks(rs.getLong("dev_code_deletions_4_weeks"));
            coin.setPublicAlexaRank(rs.getLong("public_alexa_rank"));
            return coin;
        }
    }
}