package com.sam.coin.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import com.sam.coin.api.CoinController;
import com.sam.coin.model.Coin;

@Repository("postgrescoins")
public class CoinDataAccessService implements CoinDao {

    private static final Logger LOG = LoggerFactory.getLogger(CoinController.class);
    private static final String SELECT_ALL_COINS = "SELECT * FROM coins";
    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static String SELECT_COIN_BY_TABLE_AND_ID = "SELECT * FROM {table_name} WHERE {table_name}.id = '{id}'";
    private static String SELECT_COIN_BY_TABLE_AND_DATE = "SELECT * FROM {table_name} WHERE DATE({table_name}.time_stamp) = '{date}'";

    private static String SELECT_ALL_DUPLICATES_WITH_SAME_DATE =
            "SELECT * FROM {table_name} " +
                    "WHERE DATE(time_stamp) IN (" +
                    "   SELECT DATE(time_stamp)" +
                    "   FROM {table_name}" +
                    "   GROUP BY DATE(time_stamp)" +
                    "   HAVING COUNT(*) > 1)" +
                    " ORDER BY time_stamp DESC;";

    // Danger, this deletes all duplicates from db
    private static String DELETE_ALL_DUPLICATES_WITH_SAME_DATE =
            "DELETE FROM {table_name} " +
                    "WHERE ctid NOT IN (" +
                    "  SELECT DISTINCT ON (DATE(time_stamp)) ctid" +
                    "  FROM {table_name}" +
                    "  ORDER BY DATE(time_stamp), ctid" +
                    ")";


    //	private static final String INSERT_COIN = "INSERT INTO coins (id, symbol, timestmp) VALUES (?, ?, ?)";
//	private static final String INSERT_COIN = "INSERT INTO coins (id, symbol, timestmp, datetime, high, low, bid, ask, vwap, close, last, basevolume, quotevolume, isymbol, ipricechange, ipricechangeprcnt, iweightedavgprice, iprevcloseprice, ilastprice, ilastqty, ibidprice, iaskprice, iaskqty, iopenprice, ihighprice, ilowprice, ivolume, iquotevolume, iopentime, iclosetime, ifirstid, ilastid, icount) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String INSERT_COIN = "INSERT INTO %s (id, time_stamp, symbol, coin_id, coin_name, price_eur, price_usd, price_btc, price_eth, market_cap_eur, market_cap_usd, market_cap_btc, market_cap_eth, total_volume_eur, total_volume_usd, total_volume_btc, total_volume_eth, twitter_followers, reddit_avg_posts_48_hours, reddit_avg_comments_48_hours, reddit_accounts_active_48_hours, reddit_subscribers, dev_forks, dev_stars, dev_total_issues, dev_closed_issues, dev_pull_requests_merged, dev_pull_request_contributors, dev_commit_count_4_weeks, dev_code_additions_4_weeks, dev_code_deletions_4_weeks, public_alexa_rank) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//	private static final String INSERT_COIN = "INSERT INTO coins (id, symbol, timestmp, datetime, high, low, bid, ask, vwap, close, last, basevolume, quotevolume) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    /*
     * In order to Autowire the correct implementation the following dependency
     * needs to be present in in the pom file
     *
     * <dependency> <groupId>org.springframework</groupId>
     * <artifactId>spring-jdbc</artifactId> </dependency>
     *
     */
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CoinDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

    }

    @Override
    public boolean insertCoin(UUID id, Coin coin) {
        // TODO Auto-generated method stub
        int update = -1;
        update = jdbcTemplate.update(
                INSERT_COIN.replace("%s",
                        coin.getCoinId().contentEquals("bitcoin") ? "coins" : coin.getCoinId().replace("-", "_")),
                id, coin.getTimestamp(), coin.getSymbol(), coin.getCoinId(), coin.getCoinName(), coin.getPriceEur(),
                coin.getPriceUsd(), coin.getPriceBtc(), coin.getPriceEth(), coin.getMarketCapEur(),
                coin.getMarketCapUsd(), coin.getMarketCapBtc(), coin.getMarketCapEth(), coin.getTotalVolumeEur(),
                coin.getTotalVolumeUsd(), coin.getTotalVolumeBtc(), coin.getTotalVolumeEth(),
                coin.getTwitterFollowers(), coin.getRedditAvgPosts48Hours(), coin.getRedditAvgComments48Hours(),
                coin.getRedditAccountsActive48Hours(), coin.getRedditSubscribers(), coin.getDevForks(),
                coin.getDevStars(), coin.getDevTotalIssues(), coin.getDevClosedIssues(),
                coin.getDevPullRequestsMerged(), coin.getDevPullRequestContributors(), coin.getDevCommitCount4Weeks(),
                coin.getDevCodeAdditions4Weeks(), coin.getDevCodeDeletions4Weeks(), coin.getPublicAlexaRank());
        if (update < 0) {
            LOG.error("Failed to insert Coin: {} with ID: {}", id, coin.getSymbol(), coin.getTimestamp());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to insert Coin: " + coin.getSymbol() + " with ID:" + id);
        }
        LOG.info("Successfully inserted #of Rows: " + update + ". Added Coin: " + coin.getSymbol() + " with ID: " + id
                + " and timestamp: " + coin.getTimestamp());
        return true;
    }

    @Override
    public List<Coin> selectAllCoins() {
        List<Coin> coins = null;
        // Lambda
        coins = jdbcTemplate.query(SELECT_ALL_COINS, (resultSet, i) -> {
            UUID id = UUID.fromString(resultSet.getString("id"));
            Coin coin = new Coin(id);
            coin.setSymbol(resultSet.getString(""));
            coin.setCoinId(resultSet.getString(""));
            coin.setCoinName(resultSet.getString(""));
            coin.setTimestamp(resultSet.getTimestamp(""));
            coin.setPriceEur(resultSet.getBigDecimal(""));
            coin.setPriceUsd(resultSet.getBigDecimal(""));
            coin.setPriceBtc(resultSet.getBigDecimal(""));
            coin.setPriceEth(resultSet.getBigDecimal(""));
            coin.setMarketCapEur(resultSet.getBigDecimal(""));
            coin.setMarketCapUsd(resultSet.getBigDecimal(""));
            coin.setMarketCapBtc(resultSet.getBigDecimal(""));
            coin.setMarketCapEth(resultSet.getBigDecimal(""));
            coin.setTotalVolumeEur(resultSet.getBigDecimal(""));
            coin.setTotalVolumeUsd(resultSet.getBigDecimal(""));
            coin.setTotalVolumeBtc(resultSet.getBigDecimal(""));
            coin.setTotalVolumeEth(resultSet.getBigDecimal(""));
            coin.setTwitterFollowers(resultSet.getLong(""));
            coin.setRedditAvgPosts48Hours(resultSet.getBigDecimal(""));
            coin.setRedditAvgComments48Hours(resultSet.getBigDecimal(""));
            coin.setRedditAccountsActive48Hours(resultSet.getBigDecimal(""));
            coin.setRedditSubscribers(resultSet.getLong(""));
            coin.setDevForks(resultSet.getLong(""));
            coin.setDevStars(resultSet.getLong(""));
            coin.setDevTotalIssues(resultSet.getLong(""));
            coin.setDevClosedIssues(resultSet.getLong(""));
            coin.setDevPullRequestsMerged(resultSet.getLong(""));
            coin.setDevPullRequestContributors(resultSet.getLong(""));
            coin.setDevCommitCount4Weeks(resultSet.getLong(""));
            coin.setDevCodeAdditions4Weeks(resultSet.getLong(""));
            coin.setDevCodeDeletions4Weeks(resultSet.getLong(""));
            coin.setPublicAlexaRank(resultSet.getLong(""));
            return coin;
        });
        if (coins == null) {
            LOG.error("Could not retrieve Coins from Database - Result null");
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No Content in Database");
        }
        return coins;
    }

    @Override
    public Optional<Coin> selectCoinById(UUID id) {
        return Optional.empty();
    }

    @Override
    public Optional<Coin> selectCoinByTableNameAndId(String tableName, UUID id) {
        String SELECT_COIN_BY_TABLE_AND_ID_QUERY = SELECT_COIN_BY_TABLE_AND_ID;
        SELECT_COIN_BY_TABLE_AND_ID_QUERY = SELECT_COIN_BY_TABLE_AND_ID_QUERY.replace("{table_name}", tableName);
        System.out.println("SELECT_COIN_BY_ID 1st Replace :" + SELECT_COIN_BY_TABLE_AND_ID_QUERY);
        SELECT_COIN_BY_TABLE_AND_ID_QUERY = SELECT_COIN_BY_TABLE_AND_ID_QUERY.replace("{id}", id.toString());
        System.out.println("SELECT_COIN_BY_ID 2st Replace :" + SELECT_COIN_BY_TABLE_AND_ID_QUERY);
        List<Coin> queryResult = queryCoin(SELECT_COIN_BY_TABLE_AND_ID_QUERY);
        return Optional.of(queryResult.get(0));
    }

    @Override
    public List<Coin> selectCoinByTableNameAndDate(String tableName, String date) {
        String SELECT_COIN_BY_TABLE_AND_DATE_QUERY = SELECT_COIN_BY_TABLE_AND_DATE;
        SELECT_COIN_BY_TABLE_AND_DATE_QUERY = SELECT_COIN_BY_TABLE_AND_DATE_QUERY.replace("{table_name}", tableName);
        System.out.println("SELECT_COIN_BY_ID 1st Replace :" + SELECT_COIN_BY_TABLE_AND_DATE_QUERY);
        SELECT_COIN_BY_TABLE_AND_DATE_QUERY = SELECT_COIN_BY_TABLE_AND_DATE_QUERY.replace("{date}", date);
        System.out.println("SELECT_COIN_BY_ID 2st Replace :" + SELECT_COIN_BY_TABLE_AND_DATE_QUERY);
        return queryCoin(SELECT_COIN_BY_TABLE_AND_DATE_QUERY);
    }

    @Override
    public int deleteDuplicatesWithSameDate(String tableName) {
        String DELETE_ALL_DUPLICATES_WITH_SAME_DATE_QUERY = DELETE_ALL_DUPLICATES_WITH_SAME_DATE;
        DELETE_ALL_DUPLICATES_WITH_SAME_DATE_QUERY = DELETE_ALL_DUPLICATES_WITH_SAME_DATE_QUERY.replace("{table_name}", tableName);
        System.out.println("SELECT_COIN_BY_ID 1st Replace :" + DELETE_ALL_DUPLICATES_WITH_SAME_DATE_QUERY);
        int updatedRows = jdbcTemplate.update(DELETE_ALL_DUPLICATES_WITH_SAME_DATE_QUERY);
        LOG.info("Updated {} rows.", updatedRows);
        return updatedRows;
    }

    @Override
    public List<Coin> getAllDuplicatesWithSameDate(String tableName) {
        String SELECT_ALL_DUPLICATES_WITH_SAME_DATE_QUERY = SELECT_ALL_DUPLICATES_WITH_SAME_DATE;
        SELECT_ALL_DUPLICATES_WITH_SAME_DATE_QUERY = SELECT_ALL_DUPLICATES_WITH_SAME_DATE_QUERY.replace("{table_name}", tableName);
        System.out.println("SELECT_COIN_BY_ID 1st Replace :" + SELECT_ALL_DUPLICATES_WITH_SAME_DATE_QUERY);
        List<Coin> duplicateCoins = queryCoin(SELECT_ALL_DUPLICATES_WITH_SAME_DATE_QUERY);
        LOG.info("Contains {} Entries with same date.", duplicateCoins.size());
        return duplicateCoins;
    }

    private List<Coin> queryCoin(String query) {
        List<Coin> queryResult = jdbcTemplate.query(query, (result, i) -> {
            UUID fetchedUuid = UUID.fromString(result.getString("id"));
            Coin coin = new Coin(fetchedUuid);
            coin.setSymbol(result.getString("symbol"));
            coin.setCoinId(result.getString("coin_id"));
            coin.setCoinName(result.getString("coin_name"));
            coin.setTimestamp(getTimestampFromQueryResult(result));
            coin.setPriceEur(result.getBigDecimal("price_eur"));
            coin.setPriceUsd(result.getBigDecimal("price_usd"));
            coin.setPriceBtc(result.getBigDecimal("price_btc"));
            coin.setPriceEth(result.getBigDecimal("price_eth"));
            coin.setMarketCapEur(result.getBigDecimal("market_cap_eur"));
            coin.setMarketCapUsd(result.getBigDecimal("market_cap_usd"));
            coin.setMarketCapBtc(result.getBigDecimal("market_cap_btc"));
            coin.setMarketCapEth(result.getBigDecimal("market_cap_eth"));
            coin.setTotalVolumeEur(result.getBigDecimal("total_volume_eur"));
            coin.setTotalVolumeUsd(result.getBigDecimal("total_volume_usd"));
            coin.setTotalVolumeBtc(result.getBigDecimal("total_volume_btc"));
            coin.setTotalVolumeEth(result.getBigDecimal("total_volume_eth"));
            coin.setTwitterFollowers(result.getLong("twitter_followers"));
            coin.setRedditAvgPosts48Hours(result.getBigDecimal("reddit_avg_posts_48_hours"));
            coin.setRedditAvgComments48Hours(result.getBigDecimal("reddit_avg_comments_48_hours"));
            coin.setRedditAccountsActive48Hours(result.getBigDecimal("reddit_accounts_active_48_hours"));
            coin.setRedditSubscribers(result.getLong("reddit_subscribers"));
            coin.setDevForks(result.getLong("dev_forks"));
            coin.setDevStars(result.getLong("dev_stars"));
            coin.setDevTotalIssues(result.getLong("dev_total_issues"));
            coin.setDevClosedIssues(result.getLong("dev_closed_issues"));
            coin.setDevPullRequestsMerged(result.getLong("dev_pull_requests_merged"));
            coin.setDevPullRequestContributors(result.getLong("dev_pull_request_contributors"));
            coin.setDevCommitCount4Weeks(result.getLong("dev_commit_count_4_weeks"));
            coin.setDevCodeAdditions4Weeks(result.getLong("dev_code_additions_4_weeks"));
            coin.setDevCodeDeletions4Weeks(result.getLong("dev_code_deletions_4_weeks"));
            coin.setPublicAlexaRank(result.getLong("public_alexa_rank"));
            return coin;
        });
        return queryResult;
    }

    private static Timestamp getTimestampFromQueryResult(ResultSet result) throws SQLException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
        Date parsedDate = null;
        try {
            String time_stamp = result.getString("time_stamp");
            parsedDate = dateFormat.parse(time_stamp);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Timestamp timestamp = new Timestamp(parsedDate.getTime());
        return timestamp;
    }

    @Override
    public boolean deleteCoinByID(UUID id) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean updateCoinByID(UUID id, Coin coin) {
        // TODO Auto-generated method stub
        return false;
    }

}
