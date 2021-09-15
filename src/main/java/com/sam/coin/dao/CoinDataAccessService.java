package com.sam.coin.dao;

import java.math.BigDecimal;
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
//		update = jdbcTemplate.update(INSERT_COIN, 
//				id,
//				coin.getSymbol(),
//				coin.getTimestamp(),
//				coin.getDatetime(),
//				coin.getHigh(),
//				coin.getLow(),
//				coin.getBid(),
//				coin.getAsk(),
//				coin.getVwap(),
//				coin.getClose(),
//				coin.getLast(),
//				coin.getBaseVolume(),
//				coin.getQuoteVolume(),
//				coin.getSymbol(),
//				coin.getInfo().getPriceChange(),
//				coin.getInfo().getPriceChangePercent(),
//				coin.getInfo().getWeightedAvgPrice(),
//				coin.getInfo().getPrevClosePrice(),
//				coin.getInfo().getLastPrice(),
//				coin.getInfo().getLastQty(),
//				coin.getInfo().getBidPrice(),
//				coin.getInfo().getAskPrice(),
//				coin.getInfo().getAskQty(),
//				coin.getInfo().getOpenPrice(),
//				coin.getInfo().getHighPrice(),
//				coin.getInfo().getLowPrice(),
//				coin.getInfo().getVolume(),
//				coin.getInfo().getQuoteVolume(),
//				coin.getInfo().getOpenTime(),
//				coin.getInfo().getCloseTime(),
//				coin.getInfo().getFirstID(),
//				coin.getInfo().getLastID(),
//				coin.getInfo().getCount()
//				);
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
//		coins = jdbcTemplate.query(SELECT_ALL_COINS, (resultSet, i) -> {
//			UUID id = UUID.fromString(resultSet.getString("id"));
//			Coin coin = new Coin(id);
//			Info info = coin.new Info();
//			coin.setSymbol(resultSet.getString("symbol"));
//			coin.setTimestamp(resultSet.getLong("timestmp"));
//			coin.setDatetime(resultSet.getString("datetime"));
//			coin.setHigh(resultSet.getBigDecimal("high"));
//			coin.setLow(resultSet.getBigDecimal("low"));
//			coin.setBid(resultSet.getBigDecimal("bid"));
//			coin.setAsk(resultSet.getBigDecimal("ask"));
//			coin.setVwap(resultSet.getBigDecimal("vwap"));
//			coin.setClose(resultSet.getBigDecimal("close"));
//			coin.setLast(resultSet.getBigDecimal("last"));
//			coin.setBaseVolume(resultSet.getBigDecimal("basevolume"));
//			coin.setQuoteVolume(resultSet.getBigDecimal("quotevolume"));
//			info.setSymbol(resultSet.getString("isymbol"));
//			info.setPriceChange(resultSet.getBigDecimal("ipricechange"));
//			info.setPriceChangePercent(resultSet.getBigDecimal("ipricechangeprcnt"));
//			info.setWeightedAvgPrice(resultSet.getBigDecimal("iweightedavgprice"));
//			info.setPrevClosePrice(resultSet.getBigDecimal("iprevcloseprice"));
//			info.setLastPrice(resultSet.getBigDecimal("ilastprice"));
//			info.setLastQty(resultSet.getBigDecimal("ilastqty"));
//			info.setBidPrice(resultSet.getBigDecimal("ibidprice"));
//			info.setAskPrice(resultSet.getBigDecimal("iaskprice"));
//			info.setAskQty(resultSet.getBigDecimal("iaskqty"));
//			info.setOpenPrice(resultSet.getBigDecimal("iopenprice"));
//			info.setHighPrice(resultSet.getBigDecimal("ihighprice"));
//			info.setLowPrice(resultSet.getBigDecimal("ilowprice"));
//			info.setVolume(resultSet.getBigDecimal("ivolume"));
//			info.setQuoteVolume(resultSet.getBigDecimal("iquotevolume"));
//			info.setOpenTime(resultSet.getLong("iopentime"));
//			info.setCloseTime(resultSet.getLong("iclosetime"));
//			info.setFirstID(resultSet.getLong("ifirstid"));
//			info.setLastID(resultSet.getLong("ilastid"));
//			info.setCount(resultSet.getLong("icount"));
//			coin.setInfo(info);
//			LOG.debug("Found Coin with Symbol: {} and timestamp: {}", coin.getSymbol(), coin.getTimestamp());
//			return coin;
//		});
		if (coins == null) {
			LOG.error("Could not retrieve Coins from Database - Result null");
			throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No Content in Database");
		}
		return coins;
	}

	@Override
	public Optional<Coin> selectCoinById(UUID id) {
		// TODO Auto-generated method stub
		return null;
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
