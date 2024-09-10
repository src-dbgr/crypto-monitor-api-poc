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

    private static final String INSERT_COIN = "INSERT INTO coins (id, time_stamp, symbol, coin_id, coin_name, price_eur, price_usd, price_btc, price_eth, market_cap_eur, market_cap_usd, market_cap_btc, market_cap_eth, total_volume_eur, total_volume_usd, total_volume_btc, total_volume_eth, twitter_followers, reddit_avg_posts_48_hours, reddit_avg_comments_48_hours, reddit_accounts_active_48_hours, reddit_subscribers, dev_forks, dev_stars, dev_total_issues, dev_closed_issues, dev_pull_requests_merged, dev_pull_request_contributors, dev_commit_count_4_weeks, dev_code_additions_4_weeks, dev_code_deletions_4_weeks, public_alexa_rank) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_COINS = "SELECT * FROM coins";
    private static final String SELECT_COIN_BY_ID = "SELECT * FROM coins WHERE id = ?";
    private static final String SELECT_COINS_BY_DATE_RANGE = "SELECT * FROM coins WHERE time_stamp BETWEEN ? AND ?";
    private static final String SELECT_COINS_BY_COIN_ID_AND_DATE_RANGE = "SELECT * FROM coins WHERE coin_id = ? AND time_stamp BETWEEN ? AND ?";
    private static final String COUNT_BY_COIN_ID = "SELECT COUNT(*) FROM coins WHERE coin_id = ?";
    private static final String DELETE_COIN_BY_ID = "DELETE FROM coins WHERE id = ?";

    @Override
    public Coin save(Coin coin) {
        if (coin.getId() == null) {
            coin.setId(UUID.randomUUID());
        }
        jdbcTemplate.update(INSERT_COIN,
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
    public List<Coin> findAll() {
        return jdbcTemplate.query(SELECT_ALL_COINS, new CoinRowMapper());
    }

    @Override
    public Optional<Coin> findById(UUID id) {
        List<Coin> results = jdbcTemplate.query(SELECT_COIN_BY_ID, new CoinRowMapper(), id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public List<Coin> findByTimestampBetween(Date startDate, Date endDate) {
        return jdbcTemplate.query(SELECT_COINS_BY_DATE_RANGE, new CoinRowMapper(), startDate, endDate);
    }

    @Override
    public List<Coin> findByCoinIdAndTimestampBetween(String coinId, Date startDate, Date endDate) {
        return jdbcTemplate.query(SELECT_COINS_BY_COIN_ID_AND_DATE_RANGE, new CoinRowMapper(), coinId, startDate, endDate);
    }

    @Override
    public long countByCoinId(String coinId) {
        return jdbcTemplate.queryForObject(COUNT_BY_COIN_ID, Long.class, coinId);
    }

    @Override
    public void deleteById(UUID id) {
        jdbcTemplate.update(DELETE_COIN_BY_ID, id);
    }

    @Override
    public Optional<Coin> findBySymbolAndTimestamp(String symbol, Date timestamp) {
        String sql = "SELECT * FROM coins WHERE symbol = ? AND time_stamp = ?";
        List<Coin> results = jdbcTemplate.query(sql, new CoinRowMapper(), symbol, timestamp);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public boolean existsById(UUID id) {
        String sql = "SELECT COUNT(*) FROM coins WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
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