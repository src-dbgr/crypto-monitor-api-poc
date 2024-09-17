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

@Repository
public class JdbcCoinRepository implements CoinRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcCoinRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String INSERT_COIN = "INSERT INTO %s (id, time_stamp, symbol, coin_id, coin_name, price_eur, price_usd, price_btc, price_eth, market_cap_eur, market_cap_usd, market_cap_btc, market_cap_eth, total_volume_eur, total_volume_usd, total_volume_btc, total_volume_eth, twitter_followers, reddit_avg_posts_48_hours, reddit_avg_comments_48_hours, reddit_accounts_active_48_hours, reddit_subscribers, dev_forks, dev_stars, dev_total_issues, dev_closed_issues, dev_pull_requests_merged, dev_pull_request_contributors, dev_commit_count_4_weeks, dev_code_additions_4_weeks, dev_code_deletions_4_weeks, public_alexa_rank) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_COINS = "SELECT * FROM %s";
    private static final String SELECT_COIN_BY_ID = "SELECT * FROM %s WHERE id = ?";
    private static final String SELECT_COINS_BY_DATE_RANGE = "SELECT * FROM %s WHERE time_stamp BETWEEN ? AND ?";
    private static final String SELECT_COINS_BY_COIN_ID_AND_DATE_RANGE = "SELECT * FROM %s WHERE coin_id = ? AND time_stamp BETWEEN ? AND ?";
    private static final String COUNT_BY_COIN_ID = "SELECT COUNT(*) FROM %s WHERE coin_id = ?";
    private static final String DELETE_COIN_BY_ID = "DELETE FROM %s WHERE id = ?";
    private static final String SELECT_ALL_DUPLICATES_WITH_SAME_DATE =
            "SELECT * FROM %s WHERE DATE(time_stamp) IN (" +
                    "SELECT DATE(time_stamp) FROM %s GROUP BY DATE(time_stamp) HAVING COUNT(*) > 1) " +
                    "ORDER BY time_stamp DESC";

    private static final String DELETE_DUPLICATES_WITH_SAME_DATE =
            "DELETE FROM %s WHERE ctid NOT IN (" +
                    "SELECT DISTINCT ON (DATE(time_stamp)) ctid FROM %s ORDER BY DATE(time_stamp), ctid)";

    private static final String SELECT_LAST_VALID_DATE_FOR_COIN =
            "WITH RECURSIVE date_series AS (" +
                    "SELECT DISTINCT DATE(time_stamp) AS entry_date " +
                    "FROM %s " +
                    "ORDER BY entry_date DESC " +
                    "LIMIT 100" +  // Adjust this limit if needed
                    "), " +
                    "numbered_dates AS (" +
                    "SELECT " +
                    "entry_date, " +
                    "ROW_NUMBER() OVER (ORDER BY entry_date DESC) AS row_num " +
                    "FROM date_series" +
                    "), " +
                    "sequences AS (" +
                    "SELECT " +
                    "d1.entry_date AS start_date, " +
                    "d1.row_num AS start_row, " +
                    "1 AS length " +
                    "FROM numbered_dates d1 " +
                    "UNION ALL " +
                    "SELECT " +
                    "s.start_date, " +
                    "s.start_row, " +
                    "s.length + 1 " +
                    "FROM sequences s " +
                    "JOIN numbered_dates d ON d.row_num = s.start_row + s.length " +
                    "WHERE d.entry_date = s.start_date - INTERVAL '1 day' * s.length " +
                    "AND s.length < %d" +
                    ") " +
                    "SELECT start_date AS last_valid_date " +
                    "FROM sequences " +
                    "WHERE length = %d " +
                    "ORDER BY start_date DESC " +
                    "LIMIT 1";

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
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
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
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
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
                tableName,         // Tabellenname
                sequenceLength,    // Mindestgröße der Gruppe
                sequenceLength,    // Maximale Sequenznummer
                sequenceLength     // Erforderliche Anzahl von Tagen in der Sequenz
        );

        return jdbcTemplate.queryForObject(sqlQuery, Date.class);
    }

    private String getTableName(String coinId) {
        return coinId.equals("bitcoin") ? "coins" : coinId.replace("-", "_");
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