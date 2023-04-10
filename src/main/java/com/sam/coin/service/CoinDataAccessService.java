package com.sam.coin.service;

import com.sam.coin.api.controller.CoinController;
import com.sam.coin.dao.CoinDao;
import com.sam.coin.model.Coin;
import com.sam.coin.model.CoinsEntries;
import com.sam.coin.model.TableInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Repository("postgrescoins")
public class CoinDataAccessService implements CoinDao {

    private static final Logger LOG = LoggerFactory.getLogger(CoinController.class);
    private static final String SELECT_ALL_COINS_FROM_TABLE = "SELECT * FROM {table_name} ORDER BY time_stamp DESC";
    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DATE_PATTERN);
    private static String SELECT_COIN_BY_TABLE_AND_ID = "SELECT * FROM {table_name} WHERE {table_name}.id = '{id}'";
    private static String SELECT_COIN_BY_TABLE_AND_DATE = "SELECT * FROM {table_name} WHERE DATE({table_name}.time_stamp) = '{date}'";

    private static String SELECT_ALL_DUPLICATES_WITH_SAME_DATE = "SELECT * FROM {table_name} " + "WHERE DATE(time_stamp) IN (" + "   SELECT DATE(time_stamp)" + "   FROM {table_name}" + "   GROUP BY DATE(time_stamp)" + "   HAVING COUNT(*) > 1)" + " ORDER BY time_stamp DESC;";

    // Danger, this deletes all duplicates from db
    private static String DELETE_ALL_DUPLICATES_WITH_SAME_DATE = "DELETE FROM {table_name} " + "WHERE ctid NOT IN (" + "  SELECT DISTINCT ON (DATE(time_stamp)) ctid" + "  FROM {table_name}" + "  ORDER BY DATE(time_stamp), ctid" + ")";


    //	private static final String INSERT_COIN = "INSERT INTO coins (id, symbol, timestmp) VALUES (?, ?, ?)";
//	private static final String INSERT_COIN = "INSERT INTO coins (id, symbol, timestmp, datetime, high, low, bid, ask, vwap, close, last, basevolume, quotevolume, isymbol, ipricechange, ipricechangeprcnt, iweightedavgprice, iprevcloseprice, ilastprice, ilastqty, ibidprice, iaskprice, iaskqty, iopenprice, ihighprice, ilowprice, ivolume, iquotevolume, iopentime, iclosetime, ifirstid, ilastid, icount) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String INSERT_COIN = "INSERT INTO %s (id, time_stamp, symbol, coin_id, coin_name, price_eur, price_usd, price_btc, price_eth, market_cap_eur, market_cap_usd, market_cap_btc, market_cap_eth, total_volume_eur, total_volume_usd, total_volume_btc, total_volume_eth, twitter_followers, reddit_avg_posts_48_hours, reddit_avg_comments_48_hours, reddit_accounts_active_48_hours, reddit_subscribers, dev_forks, dev_stars, dev_total_issues, dev_closed_issues, dev_pull_requests_merged, dev_pull_request_contributors, dev_commit_count_4_weeks, dev_code_additions_4_weeks, dev_code_deletions_4_weeks, public_alexa_rank) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//	private static final String INSERT_COIN = "INSERT INTO coins (id, symbol, timestmp, datetime, high, low, bid, ask, vwap, close, last, basevolume, quotevolume) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    Set<String> tableNames = Set.of("ampleforth", "zilliqa", "vechain", "bitcoin_diamond", "litecoin", "compound_ether", "compound_coin", "waves", "uma", "bzx_protocol", "band_protocol", "ocean_protocol", "theta_token", "singularitynet", "thorchain", "kava", "ethereum", "algorand", "cardano", "chainlink", "polkadot", "stellar", "zcash", "coins");

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
        update = jdbcTemplate.update(INSERT_COIN.replace("%s", coin.getCoinId().contentEquals("bitcoin") ? "coins" : coin.getCoinId().replace("-", "_")), id, coin.getTimestamp(), coin.getSymbol(), coin.getCoinId(), coin.getCoinName(), coin.getPriceEur(), coin.getPriceUsd(), coin.getPriceBtc(), coin.getPriceEth(), coin.getMarketCapEur(), coin.getMarketCapUsd(), coin.getMarketCapBtc(), coin.getMarketCapEth(), coin.getTotalVolumeEur(), coin.getTotalVolumeUsd(), coin.getTotalVolumeBtc(), coin.getTotalVolumeEth(), coin.getTwitterFollowers(), coin.getRedditAvgPosts48Hours(), coin.getRedditAvgComments48Hours(), coin.getRedditAccountsActive48Hours(), coin.getRedditSubscribers(), coin.getDevForks(), coin.getDevStars(), coin.getDevTotalIssues(), coin.getDevClosedIssues(), coin.getDevPullRequestsMerged(), coin.getDevPullRequestContributors(), coin.getDevCommitCount4Weeks(), coin.getDevCodeAdditions4Weeks(), coin.getDevCodeDeletions4Weeks(), coin.getPublicAlexaRank());
        if (update < 0) {
            LOG.error("Failed to insert Coin: {} with ID: {}", id, coin.getSymbol(), coin.getTimestamp());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to insert Coin: " + coin.getSymbol() + " with ID:" + id);
        }
        LOG.info("Successfully inserted #of Rows: " + update + ". Added Coin: " + coin.getSymbol() + " with ID: " + id + " and timestamp: " + coin.getTimestamp());
        return true;
    }

    @Override
    public List<Coin> selectAllCoins(String tableName) {
        List<Coin> coins = null;
        // Lambda
        String SELECT_ALL_COINS_FROM_TABLE_QUERY = SELECT_ALL_COINS_FROM_TABLE.replace("{table_name}", tableName);
        coins = jdbcTemplate.query(SELECT_ALL_COINS_FROM_TABLE_QUERY, (resultSet, i) -> {
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
    public Map<String, TableInfo> countAllEntries(OrderBy orderBy) {
        List<CoinsEntries> coinsEntries = this.queryCoinsCount(this.getCountQueryForAllToken(tableNames));
        Map<String, Integer> sortedCoinEntries = sortHashMapByValuesDescending(coinEntriesToHashMap(coinsEntries.get(0)));

        Map<String, TableInfo> tableInfo = getTableInfo(this.getCountQueryForAllToken(tableNames), orderBy);
        LOG.info(tableInfo.toString());

        return tableInfo;
//        return sortedCoinEntries;
    }

    @Override
    public Optional<Coin> selectCoinByTableNameAndId(String tableName, UUID id) {
        String SELECT_COIN_BY_TABLE_AND_ID_QUERY = SELECT_COIN_BY_TABLE_AND_ID;
        SELECT_COIN_BY_TABLE_AND_ID_QUERY = SELECT_COIN_BY_TABLE_AND_ID_QUERY.replace("{table_name}", tableName);
        SELECT_COIN_BY_TABLE_AND_ID_QUERY = SELECT_COIN_BY_TABLE_AND_ID_QUERY.replace("{id}", id.toString());
        List<Coin> queryResult = queryCoin(SELECT_COIN_BY_TABLE_AND_ID_QUERY);
        return Optional.of(queryResult.get(0));
    }

    @Override
    public List<Coin> selectCoinByTableNameAndDate(String tableName, String date) {
        String SELECT_COIN_BY_TABLE_AND_DATE_QUERY = SELECT_COIN_BY_TABLE_AND_DATE;
        SELECT_COIN_BY_TABLE_AND_DATE_QUERY = SELECT_COIN_BY_TABLE_AND_DATE_QUERY.replace("{table_name}", tableName);
        SELECT_COIN_BY_TABLE_AND_DATE_QUERY = SELECT_COIN_BY_TABLE_AND_DATE_QUERY.replace("{date}", date);
        return queryCoin(SELECT_COIN_BY_TABLE_AND_DATE_QUERY);
    }

    @Override
    public int deleteDuplicatesWithSameDate(String tableName) {
        String DELETE_ALL_DUPLICATES_WITH_SAME_DATE_QUERY = DELETE_ALL_DUPLICATES_WITH_SAME_DATE;
        DELETE_ALL_DUPLICATES_WITH_SAME_DATE_QUERY = DELETE_ALL_DUPLICATES_WITH_SAME_DATE_QUERY.replace("{table_name}", tableName);
        int updatedRows = jdbcTemplate.update(DELETE_ALL_DUPLICATES_WITH_SAME_DATE_QUERY);
        LOG.info("Updated {} rows.", updatedRows);
        return updatedRows;
    }

    @Override
    public List<Coin> getAllDuplicatesWithSameDate(String tableName) {
        String SELECT_ALL_DUPLICATES_WITH_SAME_DATE_QUERY = SELECT_ALL_DUPLICATES_WITH_SAME_DATE;
        SELECT_ALL_DUPLICATES_WITH_SAME_DATE_QUERY = SELECT_ALL_DUPLICATES_WITH_SAME_DATE_QUERY.replace("{table_name}", tableName);
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

    private List<CoinsEntries> queryCoinsCount(String query) {
        List<CoinsEntries> queryResult = jdbcTemplate.query(query, (result, i) -> {
            CoinsEntries coinsEntries = new CoinsEntries();
            coinsEntries.setAmpleforthCount(result.getInt("ampleforth_count"));
            coinsEntries.setZilliqaCount(result.getInt("zilliqa_count"));
            coinsEntries.setVechainCount(result.getInt("vechain_count"));
            coinsEntries.setBitcoinDiamondCount(result.getInt("bitcoin_diamond_count"));
            coinsEntries.setLitecoinCount(result.getInt("litecoin_count"));
            coinsEntries.setCompoundEtherCount(result.getInt("compound_ether_count"));
            coinsEntries.setCompoundCoinCount(result.getInt("compound_coin_count"));
            coinsEntries.setWavesCount(result.getInt("waves_count"));
            coinsEntries.setUmaCount(result.getInt("uma_count"));
            coinsEntries.setBzxProtocolCount(result.getInt("bzx_protocol_count"));
            coinsEntries.setBandProtocolCount(result.getInt("band_protocol_count"));
            coinsEntries.setOceanProtocolCount(result.getInt("ocean_protocol_count"));
            coinsEntries.setThetaTokenCount(result.getInt("theta_token_count"));
            coinsEntries.setSingularitynetCount(result.getInt("singularitynet_count"));
            coinsEntries.setThorchainCount(result.getInt("thorchain_count"));
            coinsEntries.setKavaCount(result.getInt("kava_count"));
            coinsEntries.setEthereumCount(result.getInt("ethereum_count"));
            coinsEntries.setAlgorandCount(result.getInt("algorand_count"));
            coinsEntries.setCardanoCount(result.getInt("cardano_count"));
            coinsEntries.setChainlinkCount(result.getInt("chainlink_count"));
            coinsEntries.setPolkadotCount(result.getInt("polkadot_count"));
            coinsEntries.setStellarCount(result.getInt("stellar_count"));
            coinsEntries.setZcashCount(result.getInt("zcash_count"));
            coinsEntries.setBitcoinCount(result.getInt("bitcoin_count"));
            return coinsEntries;
        });
        return queryResult;
    }

    private static Timestamp getTimestampFromQueryResult(ResultSet result) throws SQLException {
        Date parsedDate = null;
        try {
            String time_stamp = result.getString("time_stamp");
            parsedDate = DATE_FORMAT.parse(time_stamp);
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

    @Override
    public String exportTableToCsv(String tableName) {
        String SELECT_ALL_COINS_FROM_TABLE_QUERY = SELECT_ALL_COINS_FROM_TABLE.replace("{table_name}", tableName);
        return fetchDataAndGenerateCsv(SELECT_ALL_COINS_FROM_TABLE_QUERY);
    }


    private String fetchDataAndGenerateCsv(String query) {
        return jdbcTemplate.query(query, new ResultSetExtractor<String>() {
            @Override
            public String extractData(ResultSet rs) throws SQLException {
                StringBuilder sb = new StringBuilder();
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                // Write the header line
                for (int i = 1; i <= columnCount; i++) {
                    sb.append(metaData.getColumnName(i) == null ? "" : metaData.getColumnName(i));
                    if (i < columnCount) {
                        sb.append(",");
                    }
                }
                sb.append("\n");

                // Write the data lines
                while (rs.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        sb.append(rs.getString(i) == null ? "" : rs.getString(i));
                        if (i < columnCount) {
                            sb.append(",");
                        }
                    }
                    sb.append("\n");
                }

                return sb.toString();
            }
        });
    }

    private String getCountQueryForAllToken(Set<String> tableNames) {
        String allCountQuery = "SELECT * from " + tableNames.stream().map(this::replaceTableNameInQuery).collect(Collectors.joining(", "));
        return allCountQuery;
    }

    private String replaceTableNameInQuery(String tableName) {
        String replacedQuery = "(SELECT COUNT(*) as {table_name_short}_count, MAX(time_stamp) as {table_name_short}_most_recent from {table_name}) as {table_name_short}".replace("{table_name}", tableName).replace("{table_name_short}", tableName.equals("coins") ? "bitcoin" : tableName);
        return replacedQuery;
    }

    // (tableName.substring(0, tableName.contains("_") && isNoTableException(tableName) ? tableName.indexOf("_") : tableName.length()))

    private boolean isNoTableException(String tableName) {
        return !(tableName.equals("compound_coin") || tableName.equals("compound_ether") || tableName.equals("bitcoin_diamond"));
    }

    private Map<String, Integer> coinEntriesToHashMap(CoinsEntries coinEntries) {
        Map<String, Integer> resultMap = new HashMap<>();
        // Iterate over the fields of the CoinEntries class
        for (Field field : coinEntries.getClass().getDeclaredFields()) {
            try {
                // Make the field accessible
                field.setAccessible(true);
                // Get the field's value and name
                Integer fieldValue = (Integer) field.get(coinEntries);
                String fieldName = field.getName();
                // Add the field name and value to the resultMap
                resultMap.put(fieldName, fieldValue);
            } catch (IllegalAccessException e) {
                // Handle any exceptions that might occur
                e.printStackTrace();
            }
        }
        return resultMap;
    }

    private Map<String, Integer> sortHashMapByValuesDescending(Map<String, Integer> unsortedMap) {
        return unsortedMap.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    public enum OrderBy {
        COUNT, DATE, ALPHABETICAL
    }

    public Map<String, TableInfo> getTableInfo(String sqlQuery, OrderBy orderBy) {
        // Execute the query
        Map<String, Object> resultSet = jdbcTemplate.queryForMap(sqlQuery);

        // Convert the result set to a map of TableInfo objects
//        Map<String, TableInfo> tableInfoMap = new HashMap<>();
        Map<String, TableInfo> tableInfoMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : resultSet.entrySet()) {
            String key = entry.getKey();
            if (key.endsWith("_count")) {
                String tableName = key.substring(0, key.length() - 6);
                int count = ((Long) entry.getValue()).intValue();
                Timestamp mostRecent = (Timestamp) resultSet.get(tableName + "_most_recent");
                tableInfoMap.put(tableName, new TableInfo(count, mostRecent));
            }
        }

        Comparator<String> comparator = getComparator(orderBy, tableInfoMap);

        SortedMap<String, TableInfo> sortedTableInfoMap = new TreeMap<>(comparator);
        sortedTableInfoMap.putAll(tableInfoMap);

        return sortedTableInfoMap;
    }

    private static Comparator<String> getComparator(OrderBy orderBy, Map<String, TableInfo> tableInfoMap) {
        // Custom comparator for ordering
        Comparator<String> comparator = (key1, key2) -> {
            if (orderBy == OrderBy.ALPHABETICAL) {
                return key1.compareTo(key2);
            } else {
                TableInfo info1 = tableInfoMap.get(key1);
                TableInfo info2 = tableInfoMap.get(key2);

                int comparisonResult;
                if (orderBy == OrderBy.COUNT) {
                    comparisonResult = Integer.compare(info2.getCount(), info1.getCount());
                } else {
                    comparisonResult = info2.getMostRecent().compareTo(info1.getMostRecent());
                }
                // If the comparator returns 0, the elements are considered equal, and the TreeMap implementation assumes that the keys are equal, so it only keeps one of the elements.
                // When two TableInfo objects have the same date, the comparator returns 0.
                // Make the comparator return a non-zero value for distinct keys even if the main sorting criteria (for instance date) are the same.
                // If the comparison result is 0, fall back to comparing the keys alphabetically
                return comparisonResult != 0 ? comparisonResult : key1.compareTo(key2);
            }
        };
        return comparator;
    }

    class TableInfoRowMapper implements RowMapper<TableInfo> {
        @Override
        public TableInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
            int count = rs.getInt(1);
            Timestamp mostRecent = rs.getTimestamp(2);
            return new TableInfo(count, mostRecent);
        }
    }

}
